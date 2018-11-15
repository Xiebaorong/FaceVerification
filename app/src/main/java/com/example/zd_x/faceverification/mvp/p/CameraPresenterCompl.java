package com.example.zd_x.faceverification.mvp.p;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.util.Base64;
import android.util.Log;

import com.example.zd_x.faceverification.callBack.LoadCallBack;
import com.example.zd_x.faceverification.callBack.PictureCallBack;
import com.example.zd_x.faceverification.mvp.model.DetectionModel;
import com.example.zd_x.faceverification.mvp.model.VerificationModel;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.utils.APPUrl;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.FileUtils;
import com.example.zd_x.faceverification.utils.LogUtils;
import com.example.zd_x.faceverification.http.OkHttpManager;
import com.example.zd_x.faceverification.utils.PictureMsgUtils;
import com.google.gson.Gson;
import com.hanvon.faceRec.Camera2Helper;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public void requestContrast(Context context) {


        Log.e(TAG, "requestContrast: 发送成功");
        Map<String, String> params = new HashMap<>();
        String json = gson.toJson(detectionModel);
            e(TAG,json);
        params.put("",json);
        OkHttpManager.getInstance().postRequest(APPUrl.SEND, params, new LoadCallBack<String>(context) {
            @Override
            public void onSuccess(Call call, Response response, String result) {
                iCameraView.showDialog(ConstsUtils.DIS_DIALOG);
                Log.e(TAG, "onSuccess: " + result);
                VerificationModel verificationModel = gson.fromJson(result, VerificationModel.class);
                verificationResult(verificationModel);

            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void verificationResult(VerificationModel verificationModel) {
        if (verificationModel.getStatus()==1){
            Log.e(TAG, "对比回调:  "+verificationModel.getMessage() );

        }else{

        }
    }

    public static void e(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = 2000;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + 2000;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        iCameraView.showDialog(ConstsUtils.SHOW_DIALOG);
        String base64 = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
