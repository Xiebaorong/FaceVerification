package com.hanvon.faceRec;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.example.zd_x.faceverification.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HanvonfaceCamera2ShowView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "HanvonfaceCamera2ShowVi";
    public static HanvonfaceCamera2ShowView hanvonfaceShowView = null;
    private Handler mHandler;
    private SurfaceView mSurfaceView02;//mSurfaceHolder02 显示框 对应SurfaceView

    public HanvonfaceCamera2ShowView(Context context) {
        super(context);
        hanvonfaceShowView = this;

    }

    public HanvonfaceCamera2ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        hanvonfaceShowView = this;
        setSurfaceTextureListener(this);
    }

    public HanvonfaceCamera2ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hanvonfaceShowView = this;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Camera2Helper.camera2Helper.takePreview(this, onImageAvailableListener);
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 设置显示和绘制SurfaceView
     *
     * @param view02 绘制 Surface
     */
    public void setSurfaceView(SurfaceView view02, Handler handler) {
        this.mHandler = handler;
        mSurfaceView02 = view02;
        mSurfaceView02.setZOrderOnTop(true);

    }


    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            if (Camera2Helper.isTakePicture) {
                imageSaver(reader.acquireNextImage());
            }else {
                Image image = reader.acquireNextImage();
                image.close();
            }


        }
    };

    public void imageSaver(final Image mImage) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                Log.e(TAG, "onImageAvailable: 21");
                ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                Log.e(TAG, "FileOutputStream: " + data.length);
                buffer.get(data);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "IMG_" + timeStamp + ".jpg";

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, 600, 600);
                FileUtils.saveBitmap(FaceVerificationApplication.getmApplication(), bitmap1, "camera/", fileName );
                mImage.close();
//                FileOutputStream fos = null;
//                try {
//                    File file = FileUtils.createFile(FaceVerificationApplication.getmApplication(), false, "camera", fileName, 1074000000);
//                    fos = new FileOutputStream(file);
//                    fos.write(data, 0, data.length);
//                } catch (FileUtils.NoExternalStoragePermissionException e) {
//                    e.printStackTrace();
//                } catch (FileUtils.NoExternalStorageMountedException e) {
//                    e.printStackTrace();
//                } catch (FileUtils.DirHasNoFreeSpaceException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    mImage.close();
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        }).start();

    }

}
