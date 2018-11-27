package com.hanvon.faceRec;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Camera2Helper {
    private static final String TAG = "Camera2Helper";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    // 相机预览分辨率
    public static int PIXEL_WIDTH = 640;// 1920,1280,(1280),(960),(640),576,480,384,352
    public static int PIXEL_HEIGHT = 480;// 1080,720,(960),(720),(480),432,320,288,288
    public static Camera2Helper camera2Helper = new Camera2Helper();
    public static Size mPreviewSize, mCaptureSize;
    private CameraManager manager;
    private CameraDevice cameraDevice;
    private StreamConfigurationMap map;
    private CaptureRequest.Builder previewCaptureRequestBuilder, pictureCaptureRequestBuilder;//拍照使用  captureRequestBuilder
    private HandlerThread mCameraThread;
    private CameraCaptureSession captureSession;
    private ImageReader previewImageReader, pictureImageReader;
    private SurfaceTexture texture;
    // 定义用于预览照片的捕获请求
    private CaptureRequest previewRequest;
    public volatile  boolean isCanDetectFace = true;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Handler mCameraHandler;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int initCamera(Activity mContext) {
        try {
            manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
                map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                //获取相机支持的最大拍照尺寸
                mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                    @Override
                    public int compare(Size lhs, Size rhs) {
                        return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                    }
                });
                startCameraThread();
                break;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return ConstsUtils.FAIL;
        }
        return ConstsUtils.OK;
    }

    private void startCameraThread() {
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void openCamera(Activity context, final TextureView textureView) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                initImageReader();
                manager.openCamera(ConstsUtils.CAMERA_ID, new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice camera) {
                        cameraDevice = camera;
                        takePreview(textureView);
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice camera) {
                        cameraDevice.close();
                        cameraDevice = null;
                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {
                        cameraDevice.close();
                        cameraDevice = null;
                    }
                }, mCameraHandler);
            }


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initImageReader() {
        previewImageReader = ImageReader.newInstance(Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, ImageFormat.YUV_420_888, 1);
        pictureImageReader = ImageReader.newInstance(Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, ImageFormat.JPEG, 2);
        previewImageReader.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler);

    }

    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            synchronized (this) {
                Image image = null;
                try {
                    image = reader.acquireNextImage();
                    if (image == null) {
                        return;
                    }
                    if (isCanDetectFace) {
                        HanvonfaceCamera2ShowView.hanvonfaceShowView.cameraPreview(image);
                    }
                } catch (Exception e) {
                    LogUtil.e("---" + e);
                } finally {
                    if (image != null) {
                        image.close();
                    }
                }
            }
        }
    };


    /**
     * 开始预览
     *
     * @param textureView
     */
    public void takePreview(TextureView textureView) {
        if (cameraDevice == null) {
            Log.e(TAG, "takePreview: null");
            return;
        }
        try {
            previewCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            texture = textureView.getSurfaceTexture();
            //根据TextureView的尺寸设置预览尺寸
            mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), textureView.getWidth(), textureView.getHeight());
            texture.setDefaultBufferSize(Camera2Helper.mPreviewSize.getWidth(), Camera2Helper.camera2Helper.mPreviewSize.getHeight());
            Surface surface = new Surface(texture);

            previewCaptureRequestBuilder.addTarget(surface);
            previewCaptureRequestBuilder.addTarget(previewImageReader.getSurface());
            cameraDevice.createCaptureSession(Arrays.asList(surface, previewImageReader.getSurface(), pictureImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (null == cameraDevice) {
                        return;
                    }
                    // 当摄像头已经准备好时，开始显示预览
                    captureSession = session;
                    try {
                        // 设置自动对焦模式
                        previewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 设置自动曝光模式
                        previewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 开始显示相机预览
                        previewRequest = previewCaptureRequestBuilder.build();
                        // 设置预览时连续捕获图像数据
                        captureSession.setRepeatingRequest(previewRequest,
                                null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    LogUtil.e("配置失败");
                }
            }, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * 拍照
     */
    public void takePicture(Activity context, ImageReader.OnImageAvailableListener pictureOnImageAvailableListener) {
        if (cameraDevice == null) {
            return;
        }
        // 创建拍照请求
        try {
            pictureCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            pictureCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            pictureCaptureRequestBuilder.addTarget(pictureImageReader.getSurface());
            if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                pictureCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION
                        , 270);
            } else {
                pictureCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION
                        , 90);
            }
            ;
            pictureImageReader.setOnImageAvailableListener(pictureOnImageAvailableListener, mCameraHandler);
            // 停止连续取景
            captureSession.stopRepeating();
//            //拍照
            CaptureRequest capture = pictureCaptureRequestBuilder.build();
//            //设置拍照监听
            captureSession.capture(capture, captureCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
        }


        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    public void restartPreview(Context context) {
        // 重设自动对焦模式
        pictureCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        // 设置自动曝光模式
        pictureCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        try {
            captureSession.setRepeatingRequest(previewRequest, null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sizeMap
     * @param width
     * @param height
     * @return
     */
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    /**
     * 切換相关方法
     *
     * @param context
     */
    public void cameraSwitch(Activity context) {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (ConstsUtils.CAMERA_ID.equals(String.valueOf(CameraCharacteristics.LENS_FACING_BACK)) && characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    //前置转后置
                    ConstsUtils.CAMERA_ID = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
                    stopCamera();
                    openCamera(context, HanvonfaceCamera2ShowView.hanvonfaceShowView.getTextureView());
                    break;
                } else if (ConstsUtils.CAMERA_ID.equals(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT)) && characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    //后置转前置
                    ConstsUtils.CAMERA_ID = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);
                    stopCamera();
                    openCamera(context, HanvonfaceCamera2ShowView.hanvonfaceShowView.getTextureView());
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拍照释放资源
     */
    public void stopCamera() {
        if (captureSession != null) {
            try {
                captureSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            } finally {
                captureSession.close();
                captureSession = null;
            }
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }


        if (previewImageReader != null) {
            previewImageReader.close();
            previewImageReader = null;
        }

        if (pictureImageReader != null) {
            pictureImageReader.close();
            pictureImageReader = null;
        }
    }
}
