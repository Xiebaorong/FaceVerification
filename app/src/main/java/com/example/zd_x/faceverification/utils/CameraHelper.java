package com.example.zd_x.faceverification.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;

import com.example.zd_x.faceverification.callBack.PictureCallBack;
import com.example.zd_x.faceverification.ui.activity.Camera2Activity;
import com.example.zd_x.faceverification.ui.widget.AutoFitTextureView;
import com.example.zd_x.faceverification.ui.widget.FaceFrameTextureView;
import com.hanvon.faceRec.Camera2Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CameraHelper {
    private static final String TAG = "CameraHelper";
    public static CameraHelper cameraHelper = new CameraHelper();
    private Activity mContext;
    private Handler mHandler;
    private AutoFitTextureView mTextureView;
    private FaceFrameTextureView mFaceview;

    public void setPictureCallBack(PictureCallBack pictureCallBack) {
        this.pictureCallBack = pictureCallBack;
    }

    public PictureCallBack pictureCallBack;

    /**
     * Camera state: 显示相机预览
     */
    private static final int STATE_PREVIEW = 0;
    /**
     * Camera state: 等待焦点锁定.
     */
    private static final int STATE_WAITING_LOCK = 1;
    /**
     * Camera state: 等待曝光处于预捕获状态.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;
    /**
     * Camera state: 等待曝光状态不是预捕获状态.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: 照片拍摄.
     */
    private static final int STATE_PICTURE_TAKEN = 4;
    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 640;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 480;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private CameraCaptureSession mCaptureSession;

    private CameraDevice mCameraDevice;

    private Size mPreviewSize, cPixelSize;
    /**
     * 一个额外的线程，用于运行不应该阻塞UI的任务.
     */
    private HandlerThread mBackgroundThread;

    /**
     * 用于在后台运行任务的处理程序
     */
    private Handler mBackgroundHandler;

    private ImageReader mImageReader;

    private CaptureRequest.Builder mPreviewRequestBuilder;

    private CaptureRequest mPreviewRequest;

    /**
     * 拍照时照相机的当前状态
     */
    private int mState = STATE_PREVIEW;

    /**
     * 一个 信号量 来防止应用程序在关闭相机之前退出
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * 当前的摄像设备是否支持Flash.
     */
    private boolean mFlashSupported;

    /**
     * 摄像机传感器的方向
     */
    private int mSensorOrientation;


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    public void initTextureView(Activity context, Handler handler, AutoFitTextureView textureView, FaceFrameTextureView faceview) {
        mHandler = handler;
        mContext = context;
        mTextureView = textureView;
        mFaceview = faceview;
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            mFaceview.setAlpha(0.9f);
            openCamera(width, height);

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    public void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);

        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(ConstsUtils.CAMERA_ID, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            mContext.finish();

        }

    };


    private void setUpCameraOutputs(int width, int height) {

        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {

            CameraCharacteristics characteristics
                    = manager.getCameraCharacteristics(ConstsUtils.CAMERA_ID);

            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                return;
            }

            //对于静态图像捕获，我们使用最大的可用大小.
            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
            mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.JPEG, /*maxImages*/2);

            //看看我们是否需要交换尺寸来获得相对于传感器坐标的预览尺寸。
            int displayRotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                        swappedDimensions = true;
                    }
                    break;
                default:
                    Log.e(TAG, "Display rotation is invalid: " + displayRotation);
            }

            Point displaySize = new Point();
            mContext.getWindowManager().getDefaultDisplay().getSize(displaySize);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = displaySize.x;
            int maxPreviewHeight = displaySize.y;

            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = displaySize.y;
                maxPreviewHeight = displaySize.x;
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }

            // 危险,得到!尝试使用太大的预览大小
            // 可能会超过摄像机总线的带宽限制，导致华丽的预览，但存储垃圾捕获数据。
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                    maxPreviewHeight, largest);
            cPixelSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);//获取成像尺寸
            // We fit the aspect ratio of TextureView to the size of preview we picked.
            int orientation = mContext.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {//1440---1080
                Log.e(TAG, "setUpCameraOutputs1: " + mPreviewSize.getWidth() + "---" + mPreviewSize.getHeight());
                mTextureView.setAspectRatio(
                        mPreviewSize.getWidth(), mPreviewSize.getHeight());
                mFaceview.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                Log.e(TAG, "setUpCameraOutputs2: " + mPreviewSize.getWidth() + "---" + mPreviewSize.getHeight());
                mTextureView.setAspectRatio(
                        mPreviewSize.getHeight(), mPreviewSize.getWidth());
                mFaceview.setAspectRatio(
                        mPreviewSize.getHeight(), mPreviewSize.getWidth());

            }
            //获取人脸检测参数
            characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
            // Check if the flash is supported.
            Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            mFlashSupported = available == null ? false : available;


        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
        }
    }

    /**
     * 为相机预览创建一个新的CameraCaptureSession。
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // 我们将默认缓冲区的大小配置为我们想要的相机预览的大小
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // 这是我们需要开始预览的输出表面。
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
            // 人脸识别
            mPreviewRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE,
                    CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL);
            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {

                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 启动静态图像捕获
     */
    public void takePicture(Activity context, ImageReader.OnImageAvailableListener mOnImageAvailableListener) {
        mImageReader.setOnImageAvailableListener(
                mOnImageAvailableListener, mBackgroundHandler);
        lockFocus();
    }

    /**
     * 锁定焦点作为静止图像捕获的第一步
     */
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    public void captureStillPicture() {
        try {
            if (null == mCameraDevice) {
                return;
            }
            // 这是捕获请求。我们用它来拍照.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));
            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private CameraCaptureSession.CaptureCallback CaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {

        }
    };

    /**
     * 从指定的屏幕旋转中检索JPEG方向
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * 解锁的焦点。当静态图像捕获序列完成时，应调用此方法
     */
    public void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }


    /**
     * 运行预捕获序列来捕获静止图像。当我们从lockFocus()获得mCaptureCallback中的响应时，应该调用这个方法
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {

                    Face[] faces = result.get(CaptureResult.STATISTICS_FACES);
                    // 当相机预览正常工作时，我们没有事情可做.
                    mBackgroundHandler.post(new ShowFaceView(mFaceview, result));
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
//                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };


    private void configureTransform(int viewWidth, int viewHeight) {

        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        Log.e(TAG, "chooseOptimalSize: textureViewWidth: " + textureViewWidth + "---textureViewHeight: " + textureViewHeight + "---maxWidth: " + maxWidth + "---maxHeight: " + maxHeight);
        // 收集至少与预览表面一样大的支持分辨率
        List<Size> bigEnough = new ArrayList<>();
        // 收集比预览表面小的支持分辨率
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    Log.e(TAG, "chooseOptimalSize: " + option.getWidth() + "---" + option.getHeight());
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    /**
     * 根据面积比较两种尺寸。
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }


    public void cameraSwitch() {
        if (ConstsUtils.CAMERA_ID.equals(String.valueOf(CameraCharacteristics.LENS_FACING_BACK))) {
            //前置转后置
            ConstsUtils.CAMERA_ID = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
            closeCamera();
            if (mTextureView.isAvailable()) {
                openCamera(mTextureView.getWidth(), mTextureView.getHeight());
            } else {
                mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
            }
        } else if (ConstsUtils.CAMERA_ID.equals(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT))) {
            ConstsUtils.CAMERA_ID = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);
            closeCamera();
            if (mTextureView.isAvailable()) {
                openCamera(mTextureView.getWidth(), mTextureView.getHeight());
            } else {
                mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
            }
        }
    }


    public void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    public static Paint getPaint() {
        Paint pb = new Paint();
        pb.setColor(Color.BLUE);
        pb.setStrokeWidth(5);
//        pb.setStyle(Paint.Style.STROKE);//使绘制的矩形中空
        return pb;
    }


    public static class ShowFaceView implements Runnable {
        private final CaptureResult mCaptureResult;
        private final FaceFrameTextureView faceview;

        ShowFaceView(FaceFrameTextureView tvFaceShowCamera2Fragment, CaptureResult result) {
            mCaptureResult = result;
            faceview = tvFaceShowCamera2Fragment;
        }

        @Override
        public void run() {
            synchronized (this) {
                Face[] faces = mCaptureResult.get(CaptureResult.STATISTICS_FACES);
                Canvas canvas = faceview.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//旧画面清理覆盖
                if (faces.length > 0) {
                    Rect rect = faces[0].getBounds();
                    canvasDarwLine(canvas, rect);
                } else {
                    ConstsUtils.iStartX = 0;
                }
                faceview.unlockCanvasAndPost(canvas);
            }
        }

        private void canvasDarwLine(Canvas canvas, Rect rect) {

            Paint p = getPaint();
            float scaleWidth = canvas.getHeight() * 1.0f / 3968;
            float scaleHeight = canvas.getWidth() * 1.0f / 2976;

            int startX = rect.top;//  t  3
            int startY = rect.left;// l  4
            int stopX = rect.bottom;//b  1
            int stopY = rect.right;// r  2
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
//                stopX = width - stopX;
//                stopY = height - stopY;
//                startX = width - startX;
//                startY = height - startY;

                ConstsUtils.iStartX =  2976- stopX;
                ConstsUtils.iStartY = 3968-stopY;
//                ConstsUtils.iWidth = ( startX) - ConstsUtils.iStartX;
//                ConstsUtils.iHeight = (3968 - startY) - ConstsUtils.iHeight;
                Log.e(TAG, "[T" + 0 + "]:[startX:" + startX + ",startY:" + startY + ",stopX:" + stopX + ",stopY:" + stopY + "]");
            } else {
//                stopX = width - stopX;
//                startX = width - startX;
                //剪裁
                ConstsUtils.iStartX = 2976 - stopX;
                ConstsUtils.iStartY = startY;
                ConstsUtils.iWidth = 2976 - startX - ConstsUtils.iStartX;
                ConstsUtils.iHeight = stopY - startY;
//                Log.e(TAG, canvas.getWidth() + "--" + canvas.getHeight() + "[T" + 0 + "]:[startX:" + startX + ",startY:" + startY + ",stopX:" + stopX + ",stopY:" + stopY + "]");

            }

            //添加 比例换算
            startX = (int) ((float) (startX) * scaleWidth);
            startY = (int) ((float) startY * scaleHeight);
            stopX = (int) ((float) (stopX * scaleWidth));
            stopY = (int) ((float) (stopY * scaleHeight));
            if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                stopX = width - stopX;
                stopY = height - stopY;
                startX = width - startX;
                startY = height - startY;
                int iPaintSize = (stopY - startY) / 6;

                //左上-
                canvas.drawLine(stopX - iPaintSize, stopY, stopX, stopY, p);
                canvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, p);

                //右上-
                canvas.drawLine(startX, stopY, startX + iPaintSize, stopY, p);
                canvas.drawLine(startX, stopY, startX, stopY - iPaintSize, p);

                //左下-
                canvas.drawLine(stopX - iPaintSize, startY, stopX, startY, p);
                canvas.drawLine(stopX, startY, stopX, startY + iPaintSize, p);

                //右下-
                canvas.drawLine(startX, startY, startX + iPaintSize, startY, p);
                canvas.drawLine(startX, startY, startX, startY + iPaintSize, p);
            } else {
                stopX = width - stopX;
                startX = width - startX;
                int iPaintSize = (stopY - startY) / 6;

                //左上-
                canvas.drawLine(stopX, startY, stopX + iPaintSize, startY, p);
                canvas.drawLine(stopX, startY, stopX, startY + iPaintSize, p);

                //左下-
                canvas.drawLine(stopX, stopY, stopX + iPaintSize, stopY, p);
                canvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, p);

                //右上-
                canvas.drawLine(startX - iPaintSize, startY, startX, startY, p);
                canvas.drawLine(startX, startY, startX, startY + iPaintSize, p);

                //右下-
                canvas.drawLine(startX - iPaintSize, stopY, startX, stopY, p);
                canvas.drawLine(startX, stopY, startX, stopY - iPaintSize, p);

            }

        }
    }
}
