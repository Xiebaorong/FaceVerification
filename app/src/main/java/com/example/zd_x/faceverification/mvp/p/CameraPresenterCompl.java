package com.example.zd_x.faceverification.mvp.p;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.util.Log;

import com.example.zd_x.faceverification.callBack.LoadCallBack;
import com.example.zd_x.faceverification.database.DataManipulation;
import com.example.zd_x.faceverification.mvp.model.DetectionModel;
import com.example.zd_x.faceverification.mvp.model.VerificationModel;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.utils.APPUrl;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.FileUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.example.zd_x.faceverification.http.OkHttpManager;
import com.example.zd_x.faceverification.utils.PictureMsgUtils;
import com.google.gson.Gson;
import com.hanvon.faceRec.Camera2Helper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class CameraPresenterCompl implements ICameraPresenter, ImageReader.OnImageAvailableListener {
    private static final String TAG = "CameraPresenterCompl";
    private ICameraView iCameraView;
    private DetectionModel detectionModel;

    public CameraPresenterCompl(ICameraView view) {
        iCameraView = view;
    }

    public Gson gson = new Gson();

    @Override
    public void takePicture(Context context) {
        Camera2Helper.camera2Helper.takePicture((Activity) context, this);
    }

    /**
     * TODO 切換相关方法
     *
     * @param context
     */
    @Override
    public void cameraSwitch(Context context) {
        Camera2Helper.camera2Helper.cameraSwitch(context);
    }


    @Override
    public void requestContrast(final Context context) {
        String json = gson.toJson(detectionModel);
        LogUtil.allLog(json);
        OkHttpManager.getInstance().postRequest(APPUrl.SEND, ConstsUtils.MEDIA_TYPE_JSON, json, new LoadCallBack<String>(context) {
            @Override
            public void onSuccess(Call call, Response response, String result) {
                iCameraView.showUploadDialog(ConstsUtils.DIS_DIALOG);
                Log.e(TAG, "onSuccess: " + result);
                VerificationModel verificationModel = gson.fromJson(result, VerificationModel.class);
                verificationResult(context, verificationModel);

            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @Override
    public void restartPreview(Context context) {
        Camera2Helper.camera2Helper.restartPreview(context);
    }

    private void verificationResult(Context context, final VerificationModel verificationModel) {
        if (verificationModel.getStatus() == 1) {
            Log.e(TAG, "对比回调:  " + verificationModel.getMessage());
            //显示数据

            //放入数据库保存
            DataManipulation.getInstance().insertData(detectionModel, verificationModel);

        } else {
            final String message = verificationModel.getMessage();
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iCameraView.showVerificationMsgDialog(message);
                }
            });

        }
    }


    @Override
    public void onImageAvailable(ImageReader reader) {
        iCameraView.showUploadDialog(ConstsUtils.SHOW_DIALOG);
        String base64 = null;
        String imageID = PictureMsgUtils.getInstance().getPictureImageId();

        Bitmap bitmap = FileUtils.imageSaver(reader.acquireNextImage(), imageID);
        if (bitmap != null) {
            base64 = FileUtils.bitmapToBase64(bitmap);
        }
        setImageMsg(imageID, base64.replace("\n", ""));

    }

    private void setImageMsg(String imageID, String base64) {
        PictureMsgUtils.imageID = imageID;
        PictureMsgUtils.deviceName = ConstsUtils.CAMERA_ID;
        PictureMsgUtils.faceBase64 = base64;
        PictureMsgUtils.limit = 3;
        detectionModel = new DetectionModel(PictureMsgUtils.imageID, PictureMsgUtils.deviceID, PictureMsgUtils.deviceName, PictureMsgUtils.faceBase64, PictureMsgUtils.skip,
                PictureMsgUtils.limit, PictureMsgUtils.strategy, PictureMsgUtils.threshold, PictureMsgUtils.repoIDs);
        if (detectionModel.getImageID() != null) {
            iCameraView.getPhotoResults(ConstsUtils.SUCCEED);
        }
    }


    /**
     * Camera 拍照
     */
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
////        CameraHelper.cameraHelper.stopPreview();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 320, 100, 400, 400);
//        FileUtils.saveBitmap(FaceVerificationApplication.getmApplication(), bitmap1, "camera/", System.currentTimeMillis() + ".png");
//        String stringBase64 = bitmapToBase64(bitmap1);
//
//        if (stringBase64!=null){
//            iCameraView.getPhotoResults(ConstsUtils.SUCCEED);
//        }
//        try {
//            FileUtils.writeByteDataToTargetFile(FaceVerificationApplication.getmApplication(), data, false, "camera/", System.currentTimeMillis() + ".jpg");
//        } catch (FileUtils.NoExternalStoragePermissionException e) {
//            e.printStackTrace();
//        } catch (FileUtils.DirHasNoFreeSpaceException e) {
//            e.printStackTrace();
//        } catch (FileUtils.NoExternalStorageMountedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
