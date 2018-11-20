package com.hanvon.face;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.hanvon.faceRec.Consts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HWFaceClient {
    private static final String TAG = "HWFaceClient";
    /**
     * 执行成功
     */
    public final static int HW_OK = 0;
    /**
     * 失败
     */
    public final static int HW_FAIL = -1;
    /**
     * 人脸信息数据长度
     */
    //public final static int HW_FACEINFO_LEN = 224;
    /**
     * 人脸坐标数据长度
     */
    public final static int HW_FACEPOS_LEN = 4;

    public final static int HW_EYEPOS_LEN = 4;

    public final static String strDataFile2 = FaceVerificationApplication.getmApplication().getFilesDir().getAbsolutePath();
    //    	public final static String strDataFile2 = "/data/data/com.hanvon.facepos/files";
    //public final static String strDataFile2 = getSdCardPath(MyApplication.getContext()) + "/核心测试";
    public final static String strDataFile = "HanvonFeature";

    private static Socket client;
    private static String strServerAddressIP;
    private static Boolean ServerState = true;
    public static byte[] bpKeyCode = null;
    public static int iKeyCodeSize = 0;
    private static int nPort;

    //    public static byte[] pHandle = new byte[4]; // v7a x86
    public static byte[] pHandle = new byte[4]; // V8a
    public static byte[] pTrackerHandle = new byte[4]; // V8a

    private static Context mContext = null;

    private static int count_frames = 0;  // 帧数 计数
    private static int detect_rate = 5;  //间隔 detect_rate 帧做一次检测
    private static Map<Integer, TrackerRect> map_rect = new HashMap<Integer, TrackerRect>();  // 保存跟踪框id和位置
    private static TrackerParam trackerParam;


    /**
     * 将int类型的数据转换为byte数组
     * 原理：将int数据中的四个byte取出，分别存储
     *
     * @param n int数据
     * @return 生成的byte数组
     */
    public static byte[] intToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    /**
     * 将byte数组转换为int数据
     *
     * @param b 字节数组
     * @return 生成的int数据
     */
    public static int byteToInt2(byte[] b) {
        return (((int) b[0]) << 24) + (((int) b[1]) << 16) + (((int) b[2]) << 8) + b[3];
    }

    /**
     * 获取客户端人脸核心秘钥
     *
     * @param ServerAddressIP [input] 服务器IP
     * @param Port            [input] 服务器端口
     * @return 见接口返回值定义说明
     */
    public static int InitFaceClient(String ServerAddressIP, int Port, Context contex, final Handler handler) {
        Log.e(TAG, "InitFaceClient: 进入");
        int Result = -1;
        try {
            byte[] bpSend = new byte[256];
            int[] nSendLen = new int[1];
            nSendLen[0] = bpSend.length;

            int result = -1;
            result = FaceCoreHelper.HWGetKeyCode(bpSend, nSendLen, contex);

            Log.e(TAG, "InitFaceClient: HWGetKeyCode" + result);
            if (nSendLen[0] <= 0)
                return -1;

            strServerAddressIP = ServerAddressIP;
            nPort = Port;
            client = new Socket(strServerAddressIP, nPort);

            byte[] bpHeadData = intToBytes2(nSendLen[0]);
            byte[] bpKey = new byte[nSendLen[0]];
            System.arraycopy(bpSend, 0, bpKey, 0, bpKey.length);
            DataOutputStream out;
            out = new DataOutputStream(client.getOutputStream());

            out.write(bpHeadData);
            out.write(bpKey);

            Thread ServerConnect = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Log.e(TAG, "onserver: in-----111");
                    while (ServerState) {
                        if (!client.isClosed()) {
                            Log.e(TAG, "onserver: in-----2222");
                            //接收来自 server的响应数据
                            try {
                                DataInputStream in;

                                in = new DataInputStream(client.getInputStream());
                                byte[] bpHead = new byte[4];

                                int result = in.read(bpHead, 0, bpHead.length);

                                if (result <= 0)
                                    continue;

                                int KeyLen = byteToInt2(bpHead);
                                Log.e(TAG, "onserver: in-----333333");
                                if (KeyLen > 0) {
                                    byte[] bpData = new byte[KeyLen];
                                    Log.e(TAG, "onserver: in-----44444");
                                    int iReadLen = in.read(bpData, 0, bpData.length);

                                    if (iReadLen <= 0) {
                                        ServerState = false;
                                        return;
                                    } else {
                                        Log.e(TAG, "onserver: in-----555555");
                                        int funResult = CreatInitKeyCodeFile(bpData, bpData.length);
                                        if (funResult == 0) {
                                            Log.e(TAG, "onserver: in-----666666");
                                            handler.sendMessage(handler.obtainMessage(ConstsUtils.INIT_SUCCESS, "初始化成功"));
                                        }
                                    }
                                    ServerState = false;
                                }
                                in.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });

            ServerConnect.start();
            out.flush();
            Result = 0;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Result;
    }

    /**
     * 客户端Socket关闭
     *
     * @return 见接口返回值定义说明
     */
    public static int ReleaseFaceClient() {
        int Result = HW_FAIL;
        try {
            if (client != null) {
                if (client.isConnected()) {
                    client.close();
                    client = null;
                }
            }
            Result = HW_OK;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result;
    }

    /**
     * 获取秘钥信息
     *
     * @return 见接口返回值定义说明
     */
    public static int GetKeyCode() {
        int nResult = HW_OK;

        try {
            FileInputStream in = null;
            String strKeyCode = strDataFile2 + "/" + strDataFile + "/hwKeyCode.dat";
            File dstFile = new File(strKeyCode);

            if (dstFile.exists()) {
                dstFile.createNewFile();
                Log.e(TAG, "GetKeyCode: onCreate");
                in = new FileInputStream(strKeyCode);
                iKeyCodeSize = in.available();
                if (iKeyCodeSize > 0) {
                    bpKeyCode = new byte[iKeyCodeSize];
                    in.read(bpKeyCode);
                }
                in.close();
            } else {
                Log.e(TAG, "GetKeyCode: onFailure" + HW_FAIL);
                nResult = HW_FAIL;
            }
        } catch (Exception ex) {
            nResult = HW_FAIL;
            ex.printStackTrace();
        }
        return nResult;
    }

    /**
     * 创建秘钥
     *
     * @param pbKeyCode   秘钥
     * @param iKeyCodeLen 秘钥长度
     * @return 见接口返回值定义说明
     */
    public static int CreatInitKeyCodeFile(byte[] pbKeyCode, int iKeyCodeLen) {
        int nResult = 0;

        FileOutputStream out = null;

        try {
            File file = new File(strDataFile2);
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(strDataFile2 + "/" + strDataFile);
            if (!file.exists()) {
                file.mkdirs();
            }

            String strFilePath = strDataFile2 + "/" + strDataFile + "/hwKeyCode.dat";

            File srcFile = new File(strFilePath);

            if (!srcFile.exists()) {
                srcFile.createNewFile();
            }

            if (iKeyCodeLen > 0) {
                try {
                    out = new FileOutputStream(strFilePath);
                    out.write(pbKeyCode);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            nResult = HW_FAIL;
            ex.printStackTrace();
        }

        return nResult;
    }

    /**
     * 获取SD卡路径
     *
     * @param context
     * @return
     */
    public static String getSdCardPath(Context context) {
        boolean isSdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!isSdCardExist) {
            Toast.makeText(context, "SD卡目录获取失败", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            return sdCardPath;
        }
    }

//    public static int HwDetectMultiFaceTracker(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFacePos, float[] pEyePos, int[] pnDetectFaceNum) {
//        int result = -1;
//        if (count_frames % detect_rate == 0) {
//            int Result = -1;
//
//            /*
//            if(map_rect.size() > 0 ) {
//                ++count_frames;
//                return result;
//            }
//            */
//
//            result = FaceCoreHelper.HWFaceDetectFaces(pHandle, pbImg, width, height, pFacePos, pEyePos, pnDetectFaceNum);
//            if (result == HW_OK && pnDetectFaceNum[0] > 0) {
//                boolean[] need_insert = new boolean[pnDetectFaceNum[0]];
//                boolean[] flag_save = null;
//                if(map_rect.size() > 0) {
//                    int num = map_rect.size();
//                    flag_save = new boolean[num];
//                }
//                for (int i = 0; i < pnDetectFaceNum[0]; i++) {
//                    need_insert[i] = true;
//                    TrackerRect rect = new TrackerRect(pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN)],
//                            pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 1],
//                            pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 2],
//                            pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 3]);
//
//                    Iterator<Entry<Integer, TrackerRect>> iter = map_rect.entrySet().iterator();
//                    int count_save = 0;
//                    while (iter.hasNext()) {//是否有元素
//                        flag_save[count_save] = false;
//                        Entry<Integer, TrackerRect> entry = iter.next();//返回元素
//                        TrackerRect rectindex = entry.getValue();
//                        TrackerRect coincideRect = getOverLapping(rectindex, rect);
//                        if (coincideRect.width != 0 && coincideRect.height != 0 && ((float) coincideRect.width / (float) rectindex.width) > trackerParam.min_bBoxMergeRate) {
//                            need_insert[i] = false;  // 不需要新增跟踪目标
//                            flag_save[count_save] = true; // 不需要删框
//                            // 修改跟踪器，确定这个检测框就是当前跟踪框后，可以使用检测框去修正跟踪框的错位（跟踪结果永远不如检测结果）
//                            int trackerCount = 1;
//                            int[] pnInfo = new int[5 * trackerCount];
//                            pnInfo[0] = rect.x;
//                            pnInfo[1] = rect.y;
//                            pnInfo[2] = rect.width;
//                            pnInfo[3] = rect.height;
//                            pnInfo[4] = entry.getKey();
//                            float[] pfInfoConfidence = new float[trackerCount];
//                            Result = FaceCoreHelper.HwModifyTracker(pTrackerHandle, pbImg, width, height, trackerCount, pnInfo, pfInfoConfidence);
//                            if (Result != HW_OK) {
//                                Log.e("Tracker", "HwModifyTracker: " + Result);
//                            }
//                            else {
//                                Log.e("Tracker", "HwModifyTracker pnInfo: " + pnInfo[0] + "," + pnInfo[1] + "," + pnInfo[2] + "," + pnInfo[3] + ",id:" + pnInfo[4] + " Confidence:" + pfInfoConfidence[0]);
//                            }
//                            break;
//                        }
//                        ++count_save;
//                    }
//                }
//
//                // 删框操作，在上边2个遍历都结束后，奉行检测结果高于跟踪结果的原则，需要删除跟踪器内置信度过低的框（可能是跟丢等情况，但不排除检测漏检。）
//                int count_delete = 0;
//                ArrayList idVec = new ArrayList();
//                Iterator<Entry<Integer, TrackerRect>> iter = map_rect.entrySet().iterator();
//                while (iter.hasNext()) {//是否有元素
//                    Entry<Integer, TrackerRect> entry = iter.next();//返回元素
//                    if (!flag_save[count_delete]) {
//                        idVec.add(entry.getKey());  // 获取待删除框的id
//                    }
//                    ++count_delete;
//                }
//
//                if (idVec.size() > 0) {
//                    for (int j = 0; j < idVec.size(); ++j) {
//                        map_rect.remove(idVec.get(j));  // 跟踪队列内删除对应id的框
//                        int[] pnID = new int[1];
//                        pnID[0] = (int) idVec.get(j);
//                        Result = FaceCoreHelper.HwDropTracker(pTrackerHandle, 1, pnID);  // 跟踪器内删除对应id的框
//                        if (Result != HW_OK) {
//                           Log.e("Tracker", "HwDropTracker: " + Result);
//                        }
//                    }
//                }
//
//                // 新增操作，与跟踪队列进行融合率比对后，发现的新增人脸框，需要添加新的跟踪器用来跟踪
//                for (int i = 0; i < pnDetectFaceNum[0]; i++) {
//                    if(need_insert[i]) {
//                        TrackerRect rect = new TrackerRect(pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN)],
//                                pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 1],
//                                pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 2],
//                                pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 3]);
//                        int trackerCount = 1;
//                        int[] pnInfo = new int[5 * trackerCount];
//                        pnInfo[0] = rect.x;
//                        pnInfo[1] = rect.y;
//                        pnInfo[2] = rect.width;
//                        pnInfo[3] = rect.height;
//                        pnInfo[4] = 0;
//                        float[] pfInfoConfidence = new float[trackerCount];
//                        // 新增跟踪器
//                        Result = FaceCoreHelper.HwInsertTracker(pTrackerHandle, pbImg, width, height, trackerCount, pnInfo, pfInfoConfidence);
//                        if(Result == HW_OK)
//                        {
//                            // 跟踪队列新增框
//                            map_rect.put(pnInfo[4], rect);
//                            Log.e("Tracker", "HwInsertTracker pnInfo: " +  pnInfo[0] + "," + pnInfo[1] + "," + pnInfo[2] + "," + pnInfo[3] + ",id:" + pnInfo[4] + " Confidence:" + pfInfoConfidence[0]);
//                        }
//                    }
//                }
//            } else {
//                // 未检测到人脸，执行清框操作
//                Iterator<Entry<Integer, TrackerRect>> iter = map_rect.entrySet().iterator();
//                while (iter.hasNext()) {//是否有元素
//                    Entry<Integer, TrackerRect> entry = iter.next();//返回元素
//                    int[] pnID = {entry.getKey()};
//                    Result = FaceCoreHelper.HwDropTracker(pTrackerHandle, 1, pnID);
//                }
//                map_rect.clear();
//            }
//        } else {
//            // 当前帧跟踪
//            // 人脸跟踪部分
//            if(map_rect.size() > 0)// 如果跟踪队列中有框
//            {
//                int res = -1;
//                int[] pnTrack_num = {map_rect.size()};// 获得框的数量，这个数量必须和跟踪器内的目标物体数相同，不确定的话可以调用HW_GetTrackerSize获取
//                int trackerCount = 1;
//                int[] pnInfo = new int[5 * pnTrack_num[0]];
//                float[] pfInfoConfidence = new float[pnTrack_num[0]];
//                result = FaceCoreHelper.HwUpdateTracker(pTrackerHandle, pbImg, width, height, pnTrack_num, pnInfo, pfInfoConfidence);
//                if(result == HW_OK) // 如果跟踪执行成功
//                {
//                    map_rect.clear();
//                    pnDetectFaceNum[0] = pnTrack_num[0];
//                    for(int i = 0; i < pnTrack_num[0]; i++)
//                    {
//                        TrackerRect rect = new TrackerRect();
//                        rect.x = pnInfo[5 * i];
//                        rect.y = pnInfo[5 * i + 1];
//                        rect.width = pnInfo[5 * i + 2];
//                        rect.height = pnInfo[5 * i + 3];
//                        map_rect.put(pnInfo[5 * i + 4], rect);
//                        Log.e("Tracker", "HwUpdateTracker pnInfo: " +  pnInfo[5 * i] + "," + pnInfo[5 * i + 1] + "," + pnInfo[5 * i + 2] + "," + pnInfo[5 * i + 3] + ",id:" + pnInfo[5 * i + 4] + " Confidence:" + pfInfoConfidence[i] + "=========== " + pnTrack_num[0]);
//
//                        int[] pFaceRect = new int[4];
//                        pFaceRect[0] = rect.y;
//                        pFaceRect[1] = rect.y + rect.height;
//                        pFaceRect[2] = rect.x;
//                        pFaceRect[3] = rect.x + rect.width;
//                        int[] pOneFacePose = new int[HWFaceClient.HW_FACEPOS_LEN];
//                        float[] pOneEyePose = new float[HWFaceClient.HW_EYEPOS_LEN];
//
//                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN)] = rect.y;
//                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN) + 1] = rect.y + rect.height;
//                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN) + 2] = rect.x;
//                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN) + 3] = rect.x + rect.width;
//                        result = FaceCoreHelper.HwGetFaceByRect(pHandle, pbImg, width, height, pFaceRect, pOneFacePose, pOneEyePose);
//
//                        System.arraycopy(pOneFacePose, 0, pFacePos, i * (HWFaceClient.HW_FACEPOS_LEN), (HWFaceClient.HW_FACEPOS_LEN));
//                        System.arraycopy(pOneEyePose, 0, pEyePos, i * (HWFaceClient.HW_EYEPOS_LEN), (HWFaceClient.HW_EYEPOS_LEN));
//                    }
//                }
//            }
//        }
//        ++count_frames;
//        return result;
//    }

    /**
     * 人脸跟踪定位
     *
     * @param pHandle
     * @param pbImg
     * @param width
     * @param height
     * @param pFacePos
     * @param pnDetectFaceNum
     * @param iTrackerID
     * @param fInfoConfidence 稳定性阈值
     * @return
     */
    public static int HwDetectMultiFaceTracker(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFacePos, float[] pEyePos, int[] pnDetectFaceNum, int[] iTrackerID, float[] fInfoConfidence) {
        int result = -1;
        if (count_frames % detect_rate == 0) {
            int Result = -1;
            count_frames = 0;
            /*
            if(map_rect.size() > 0 ) {
                ++count_frames;
                return result;
            }
            */

            result = FaceCoreHelper.HWFaceDetectFaces(pHandle, pbImg, width, height, pFacePos, pEyePos, pnDetectFaceNum);
            if (result == HW_OK && pnDetectFaceNum[0] > 0) {
                boolean[] need_insert = new boolean[pnDetectFaceNum[0]];
                boolean[] flag_save = null;
                if (map_rect.size() > 0) {
                    int num = map_rect.size();
                    flag_save = new boolean[num];
                    for (int i = 0; i < num; i++) {
                        flag_save[i] = false;
                    }
                }
                for (int i = 0; i < pnDetectFaceNum[0]; i++) {
                    need_insert[i] = true;
                    TrackerRect rect = new TrackerRect(pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN)],
                            pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 1],
                            pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 2],
                            pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 3]);

                    Iterator<Entry<Integer, TrackerRect>> iter = map_rect.entrySet().iterator();
                    int count_save = 0;
                    while (iter.hasNext()) {//是否有元素
                        Entry<Integer, TrackerRect> entry = iter.next();//返回元素
                        TrackerRect rectindex = entry.getValue();
                        TrackerRect coincideRect = getOverLapping(rectindex, rect);
                        if (coincideRect.width != 0 && coincideRect.height != 0 && ((float) coincideRect.width / (float) rectindex.width) > trackerParam.min_bBoxMergeRate) {
                            need_insert[i] = false;  // 不需要新增跟踪目标
                            flag_save[count_save] = true; // 不需要删框
                            // 修改跟踪器，确定这个检测框就是当前跟踪框后，可以使用检测框去修正跟踪框的错位（跟踪结果永远不如检测结果）
                            int trackerCount = 1;
                            int[] pnInfo = new int[5 * trackerCount];
                            pnInfo[0] = rect.x;
                            pnInfo[1] = rect.y;
                            pnInfo[2] = rect.width;
                            pnInfo[3] = rect.height;
                            pnInfo[4] = entry.getKey();
                            float[] pfInfoConfidence = new float[trackerCount];
                            Result = FaceCoreHelper.HwModifyTracker(pTrackerHandle, pbImg, width, height, trackerCount, pnInfo, pfInfoConfidence);

							/*
							if (Result != HWConsts.HW_OK) {
								LogUtil.e("Tracker", "HwModifyTracker: " + Result);
							}
							else {
								LogUtil.e("Tracker", "HwModifyTracker pnInfo: " + pnInfo[0] + "," + pnInfo[1] + "," + pnInfo[2] + "," + pnInfo[3] + ",id:" + pnInfo[4] + " Confidence:" + pfInfoConfidence[0]);
							}
							*/

                            break;
                        }
                        ++count_save;
                    }
                }

                // 删框操作，在上边2个遍历都结束后，奉行检测结果高于跟踪结果的原则，需要删除跟踪器内置信度过低的框（可能是跟丢等情况，但不排除检测漏检。）
                int count_delete = 0;
                ArrayList idVec = new ArrayList();
                Iterator<Entry<Integer, TrackerRect>> iter = map_rect.entrySet().iterator();
                while (iter.hasNext()) {//是否有元素
                    Entry<Integer, TrackerRect> entry = iter.next();//返回元素
                    if (!flag_save[count_delete]) {
                        idVec.add(entry.getKey());  // 获取待删除框的id
                    }
                    ++count_delete;
                }

                if (idVec.size() > 0) {
                    for (int j = 0; j < idVec.size(); ++j) {
                        map_rect.remove(idVec.get(j));  // 跟踪队列内删除对应id的框
                        int[] pnID = new int[1];
                        //pnID[0] = (int)idVec.get(j);
                        pnID[0] = Integer.parseInt(idVec.get(j).toString());
                        Result = FaceCoreHelper.HwDropTracker(pTrackerHandle, 1, pnID);  // 跟踪器内删除对应id的框

						/*
						if (Result != HWConsts.HW_OK) {
							LogUtil.e("Tracker", "HwDropTracker: " + Result);
						}
						*/
                    }
                }

                // 新增操作，与跟踪队列进行融合率比对后，发现的新增人脸框，需要添加新的跟踪器用来跟踪
                for (int i = 0; i < pnDetectFaceNum[0]; i++) {
                    if (need_insert[i]) {
                        TrackerRect rect = new TrackerRect(pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN)],
                                pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 1],
                                pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 2],
                                pFacePos[i * (HWFaceClient.HW_FACEPOS_LEN) + 3]);
                        int trackerCount = 1;
                        int[] pnInfo = new int[5 * trackerCount];
                        pnInfo[0] = rect.x;
                        pnInfo[1] = rect.y;
                        pnInfo[2] = rect.width;
                        pnInfo[3] = rect.height;
                        pnInfo[4] = 0;
                        float[] pfInfoConfidence = new float[trackerCount];
                        // 新增跟踪器
                        Result = FaceCoreHelper.HwInsertTracker(pTrackerHandle, pbImg, width, height, trackerCount, pnInfo, pfInfoConfidence);
                        if (Result == HW_OK) {
                            rect.pfInfoConfidence = pfInfoConfidence[0];
                            // 跟踪队列新增框
                            map_rect.put(pnInfo[4], rect);
                            //LogUtil.e("Tracker", "HwInsertTracker pnInfo: " +  pnInfo[0] + "," + pnInfo[1] + "," + pnInfo[2] + "," + pnInfo[3] + ",id:" + pnInfo[4] + " Confidence:" + pfInfoConfidence[0]);
                        }
                    }
                }
            } else {
                // 未检测到人脸，执行清框操作
                if (map_rect.size() > 0) {
                    Iterator<Entry<Integer, TrackerRect>> iter = map_rect.entrySet().iterator();
                    while (iter.hasNext()) {//是否有元素
                        Entry<Integer, TrackerRect> entry = iter.next();//返回元素
                        int[] pnID = {entry.getKey()};
                        Result = FaceCoreHelper.HwDropTracker(pTrackerHandle, 1, pnID);
                    }
                }
                map_rect.clear();
                pnDetectFaceNum[0] = 0;
            }
        } else {
            // 当前帧跟踪
            // 人脸跟踪部分
            if (map_rect.size() > 0)// 如果跟踪队列中有框
            {
                int res = -1;
                int[] pnTrack_num = {map_rect.size()};// 获得框的数量，这个数量必须和跟踪器内的目标物体数相同，不确定的话可以调用HW_GetTrackerSize获取
                int trackerCount = 1;
                int[] pnInfo = new int[5 * pnTrack_num[0]];
                float[] pfInfoConfidence = new float[pnTrack_num[0]];
                result = FaceCoreHelper.HwUpdateTracker(pTrackerHandle, pbImg, width, height, pnTrack_num, pnInfo, pfInfoConfidence);
                if (result == HW_OK) // 如果跟踪执行成功
                {
                    map_rect.clear();
                    pnDetectFaceNum[0] = pnTrack_num[0];
                    for (int i = 0; i < pnTrack_num[0]; i++) {
                        TrackerRect rect = new TrackerRect();
                        rect.x = pnInfo[5 * i];
                        rect.y = pnInfo[5 * i + 1];
                        rect.width = pnInfo[5 * i + 2];
                        rect.height = pnInfo[5 * i + 3];
                        rect.pfInfoConfidence = pfInfoConfidence[i];
                        map_rect.put(pnInfo[5 * i + 4], rect);
                        //LogUtil.e("Tracker", "HwUpdateTracker pnInfo: " +  pnInfo[5 * i] + "," + pnInfo[5 * i + 1] + "," + pnInfo[5 * i + 2] + "," + pnInfo[5 * i + 3] + ",id:" + pnInfo[5 * i + 4] + " Confidence:" + pfInfoConfidence[i] + "=========== " + pnTrack_num[0]);

                        int[] pFaceRect = new int[4];
                        pFaceRect[0] = rect.y;
                        pFaceRect[1] = rect.y + rect.height;
                        pFaceRect[2] = rect.x;
                        pFaceRect[3] = rect.x + rect.width;
                        int[] pOneFacePose = new int[HWFaceClient.HW_FACEPOS_LEN];
                        float[] pOneEyePose = new float[HWFaceClient.HW_EYEPOS_LEN];

                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN)] = rect.y;
                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN) + 1] = rect.y + rect.height;
                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN) + 2] = rect.x;
                        //pFacePos[i * (HWFaceClient.HW_FACEINFO_LEN + HWFaceClient.HW_FACEPOS_LEN) + 3] = rect.x + rect.width;
                        result = FaceCoreHelper.HwGetFaceByRect(pHandle, pbImg, width, height, pFaceRect, pOneFacePose, pOneEyePose);

                        System.arraycopy(pOneFacePose, 0, pFacePos, i * (HWFaceClient.HW_FACEPOS_LEN), (HWFaceClient.HW_FACEPOS_LEN));
                        System.arraycopy(pOneEyePose, 0, pEyePos, i * (HWFaceClient.HW_EYEPOS_LEN), (HWFaceClient.HW_EYEPOS_LEN));
                    }
                }
            }
        }

        if (map_rect.size() > 0) {
            for (Entry<Integer, TrackerRect> entry : map_rect.entrySet()) {
                iTrackerID[0] = entry.getKey();
                fInfoConfidence[0] = entry.getValue().pfInfoConfidence;
            }
        } else {
            pnDetectFaceNum[0] = 0;
        }

        ++count_frames;
        return result;
    }

    /**
     * 根据两个矩形坐标求交叉矩形面积
     *
     * @param a 矩形 a
     * @param b 矩形 b
     * @return 交叉矩形面积
     */
    public static int getOverLappingArea(TrackerRect a, TrackerRect b) {
        int overLappingArea = 0;

        int startX = Math.min(a.x, b.x);
        int endX = Math.max(a.x + a.width, b.x + b.width);
        int overLappingWidth = a.width + b.width - (endX - startX);

        int startY = Math.min(a.y, b.y);
        int endY = Math.max(a.y + a.height, b.y + b.height);
        int overLappingHeight = a.height + b.height - (endY - startY);

        if (overLappingWidth <= 0 || overLappingHeight <= 0) {
            overLappingArea = 0;
        } else {
            overLappingArea = overLappingWidth * overLappingHeight;
        }
        return overLappingArea;
    }

    /**
     * 根据两个矩形坐标求交叉矩形坐标
     *
     * @param a 矩形 a
     * @param b 矩形 b
     * @return 交叉矩形
     */
    public static TrackerRect getOverLapping(TrackerRect a, TrackerRect b) {
        TrackerRect rect = new TrackerRect();

        int startX = Math.min(a.x, b.x);
        int endX = Math.max(a.x + a.width, b.x + b.width);
        rect.width = a.width + b.width - (endX - startX);

        int startY = Math.min(a.y, b.y);
        int endY = Math.max(a.y + a.height, b.y + b.height);
        rect.height = a.height + b.height - (endY - startY);

        rect.x = Math.max(a.x, b.x);
        rect.y = Math.max(a.y, b.y);

        return rect;
    }
}
