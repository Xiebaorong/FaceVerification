package com.example.zd_x.faceverification.mvp.p;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.example.zd_x.faceverification.callBack.LoadCallBack;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.utils.APPUrl;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.FileUtils;
import com.example.zd_x.faceverification.utils.LogUtils;
import com.example.zd_x.faceverification.utils.OkHttpManager;
import com.example.zd_x.faceverification.utils.PictureMsgUtils;
import com.hanvon.faceRec.Camera2Helper;
import com.hanvon.faceRec.CameraHelper;
import com.hanvon.faceRec.HWFaceIDCardCompareLib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class CameraPresenterCompl implements ICameraPresenter {
    private static final String TAG = "CameraPresenterCompl";
    private ICameraView iCameraView;

    public CameraPresenterCompl(ICameraView view) {
        iCameraView = view;
    }

    @Override
    public void takePicture(Context context) {
//        if (HWFaceIDCardCompareLib.getInstance().objRect.iTop > 0) {
//            CameraHelper.cameraHelper.takePicture(context, this);
//            iCameraView.getPhotoResults(ConstsUtils.OK);
//        }else {
//            iCameraView.getPhotoResults(ConstsUtils.FAIL);
//        }

        Camera2Helper.camera2Helper.takePicture((Activity) context);
    }

    @Override
    public void cameraSwitch() {
        Camera camera = CameraHelper.cameraHelper.cameraSwitch();
        if (camera != null) {

        }
    }

    @Override
    public void requestContrast(Context context) {
        String pictureImageId = PictureMsgUtils.getInstance().getPictureImageId();
        LogUtils.d(TAG, pictureImageId);
        Map<String, String> params = new HashMap<>();

        OkHttpManager.getInstance().postRequest(APPUrl.SEND, params, new LoadCallBack<String>(context) {
            @Override
            public void onSuccess(Call call, Response response, String result) {
                Log.e(TAG, "onSuccess: " + result);
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }


        });
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

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
