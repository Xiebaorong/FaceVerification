package com.example.zd_x.faceverification.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.hanvon.faceRec.Camera2Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件辅助工具，提供注视点标定数据的保存、读取和BMP文件流的封装。
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();


    /**
     * 无外部存储卡访问权限
     */
    public static class NoExternalStoragePermissionException extends Exception {
        public NoExternalStoragePermissionException(String message) {
            super(message);
        }
    }

    /**
     * 无存储卡挂载异常
     */
    public static class NoExternalStorageMountedException extends Exception {
        public NoExternalStorageMountedException(String message) {
            super(message);
        }
    }

    /**
     * 文件夹无足够空闲空间异常
     */
    public static class DirHasNoFreeSpaceException extends Exception {
        public DirHasNoFreeSpaceException(String message) {
            super(message);
        }
    }

    /**
     * 创建内部私有目录
     *
     * @param context
     * @param dirPathName
     * @return
     */
    public static File createPrivateDir(Context context, String dirPathName) {
        File dir = context.getFilesDir();
        File privateDir = new File(dir, dirPathName);
        if (privateDir.exists()) {
            if (!privateDir.isDirectory()) {
                privateDir.delete();
            }
        }
        privateDir.mkdirs();
        return privateDir;
    }

    /**
     * 在外部存储空间或内部私有空间创建文件夹
     *
     * @param context
     * @param dirName 目录名
     * @return 创建的文件夹
     * @throws NoExternalStoragePermissionException 未能获取外部存储卡读写权限异常
     * @throws NoExternalStorageMountedException    外部存储卡未挂载异常
     */
    public static File createExternalDir(Context context, String dirName) throws NoExternalStoragePermissionException, NoExternalStorageMountedException {
        if (((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED))) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File parentDir = Environment.getExternalStorageDirectory();
                File dir = new File(parentDir, dirName);
                if (dir.exists()) {
                    if (!dir.isDirectory()) {
                        dir.delete();
                    }
                }
                dir.mkdirs();
                return dir;
            } else {

                throw new NoExternalStorageMountedException("外部存储卡未挂载");
            }
        } else {
            throw new NoExternalStoragePermissionException("无法获取对外部存储空间的读写权限，请在Activity中加入获取权限的代码并在AndroidManifest中声明权限");
        }

    }


    /**
     * 在外部存储空间或内部私有空间新建指定文件夹中的指定文件
     *
     * @param context
     * @param dirPath       目录路径名称
     * @param fileName      文件名
     * @param needFreeSpace 需要的空间空间，以字节计
     * @return 创建的文件
     * @throws NoExternalStoragePermissionException 未能获取外部存储卡读写权限异常
     * @throws NoExternalStorageMountedException    外部存储卡未挂载异常
     * @throws DirHasNoFreeSpaceException           目录缺少足够的存储空间异常
     * @throws IOException                          文件读写IO异常
     */
    public static File createFile(Context context, boolean isPrivate, String dirPath, String fileName, long needFreeSpace) throws NoExternalStoragePermissionException, NoExternalStorageMountedException, DirHasNoFreeSpaceException, IOException {
        File dir = null;
        if (isPrivate) {
            dir = createPrivateDir(context, dirPath);
        } else {
            dir = createExternalDir(context, dirPath);
        }
        long freeSpace = dir.getFreeSpace();
        //需要保留50MB空间
        if (freeSpace > needFreeSpace) {
            File targetFile = new File(dir, fileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            targetFile.createNewFile();
            Log.e(TAG, "createFile:" + targetFile.getAbsolutePath());
            return targetFile;
        } else {
            throw new DirHasNoFreeSpaceException("目录" + dir.getAbsolutePath() + "缺少足够的空间以保存文件");
        }
    }

    /**
     * 将输入流写入到指定文件
     *
     * @param context
     * @param isPrivate      是否私有
     * @param dirPath        目录路径
     * @param targetFileName 目标文件名
     * @param inputStream    输入流
     * @return 写入完成的文件
     * @throws IOException                          文件读写IO异常
     * @throws NoExternalStoragePermissionException 未能获取外部存储卡读写权限异常
     * @throws NoExternalStorageMountedException    外部存储卡未挂载异常
     * @throws DirHasNoFreeSpaceException           目录缺少足够的存储空间异常
     */
    private static File writeInputStreamToFile(Context context, boolean isPrivate, String dirPath,
                                               String targetFileName, InputStream inputStream) throws IOException, NoExternalStoragePermissionException, DirHasNoFreeSpaceException, NoExternalStorageMountedException {
        File targetFile = createFile(context, isPrivate, dirPath, targetFileName, inputStream.available());
        byte[] buffer = new byte[2048];
        int byteCount = 0;
        FileOutputStream out = new FileOutputStream(targetFile);
        while ((byteCount = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, byteCount);
        }
        out.flush();
        out.close();
        inputStream.close();
        return targetFile;
    }


    /**
     * 写入字节流到指定目录的指定文件
     *
     * @param context
     * @param isPrivate      是否私有
     * @param dirPath        目录相对当前类型的根目录路径
     * @param targetFileName 目标文件名
     * @return 写入完成的文件
     * @throws NoExternalStoragePermissionException 未能获取外部存储卡读写权限异常
     * @throws NoExternalStorageMountedException    外部存储卡未挂载异常
     * @throws DirHasNoFreeSpaceException           目录缺少足够的存储空间异常
     * @throws IOException                          文件读写IO异常
     */
    public static File writeByteDataToTargetFile(Context context, byte[] data, boolean isPrivate, String dirPath,
                                                 String targetFileName) throws NoExternalStoragePermissionException, DirHasNoFreeSpaceException, NoExternalStorageMountedException, IOException {

        InputStream inputStream = new ByteArrayInputStream(data);
        return writeInputStreamToFile(context, isPrivate, dirPath, targetFileName, inputStream);
    }

    /**
     * 读取指定目录的指定文件到字节数组
     *
     * @param context
     * @param isPrivate      是否私有
     * @param dirPath        目录相对当前类型的根目录路径
     * @param targetFileName 目标文件名
     * @return 读取的字节数组
     * @throws IOException           IO异常
     * @throws FileNotFoundException 目标文件未找到
     */
    public static byte[] readByteDateFromTargetFile(Context context, boolean isPrivate, String dirPath,
                                                    String targetFileName) throws IOException, NoExternalStoragePermissionException, NoExternalStorageMountedException {
        File dir = null;
        if (isPrivate) {
            dir = createPrivateDir(context, dirPath);
        } else {
            dir = createExternalDir(context, dirPath);
        }
        File file = new File(dir, targetFileName);
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int byteCount = 0;
            while ((byteCount = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, byteCount);
            }
            out.flush();
            out.close();
            inputStream.close();
            byte[] result = out.toByteArray();
            return result;
        } else {
            throw new FileNotFoundException("未找到指定文件");
        }
    }

    public static void saveBitmap(Context context, Bitmap mBitmap, String dirPath, String fileName) {
        try {
            File file = createFile(context, false, dirPath, fileName, 1074000000);
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "1111");
        } catch (NoExternalStoragePermissionException e) {
            e.printStackTrace();
            Log.e(TAG, "saveBitmap: " + e.getMessage());
        } catch (NoExternalStorageMountedException e) {
            e.printStackTrace();
            Log.e(TAG, "saveBitmap: " + e.getMessage());
        } catch (DirHasNoFreeSpaceException e) {
            e.printStackTrace();
            Log.e(TAG, "saveBitmap: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public static Bitmap imageSaver(final Image mImage, String imageID) {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap != null) {
            mImage.close();
        }
        //
        Bitmap bitmap1 = newShearBitmap(bitmap);

        FileUtils.saveBitmap(FaceVerificationApplication.getmApplication(), bitmap1, "camera/", imageID + ".jpg");
        return bitmap1;

    }

    private static Bitmap newShearBitmap(Bitmap bitmap) {
        int startX = ConstsUtils.iStartX;
        int startY = ConstsUtils.iStartY;
        int width = ConstsUtils.iWidth;
        int height = ConstsUtils.iHeight;
        Log.e(TAG, "newShearBitmap: "+bitmap.getWidth()+"----"+bitmap.getHeight()+"-----"+startX+"---:"+startY+"---:"+width+"---:"+height );
//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, 1080, 1440);

        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, startX, startY, 500, 500);

        return bitmap1;
    }

    private static Bitmap shearBitmap(Bitmap bitmap) {
        int startX = ConstsUtils.iStartX;
        int startY = ConstsUtils.iStartY;
        int width = ConstsUtils.iWidth;
        int height = ConstsUtils.iHeight;

        Bitmap newBitmap = null;
        try {
            if (ConstsUtils.iStartX != 0) {
                if (startX + width * (1 + 0.2) <= bitmap.getWidth() && startY + height * (1 + 0.5) <= bitmap.getHeight()) {
                    startX = (int) (startX * (0.9));
                    startY = (int) (startY * (0.8));
                    width = (int) (width * (1 + 0.2));
                    height = (int) (height * (1 + 0.5));
                } else {
                    LogUtil.e("剪裁区域过大");
                    startX = 0;
                    startY = 0;
                    width = bitmap.getWidth();
                    height = bitmap.getHeight();
                }
            } else {
                LogUtil.e("shearBitmap 未检测到人脸");
                startX = 0;
                startY = 0;
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }
            newBitmap = Bitmap.createBitmap(bitmap, startX, startY, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("shearBitmap: " + e);
        }

        return newBitmap;
    }

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
            } else {
                return null;
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

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
