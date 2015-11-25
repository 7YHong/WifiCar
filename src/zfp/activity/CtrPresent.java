package zfp.activity;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by 7YHong on 2015/11/25.
 */
public class CtrPresent {
    final static int CONNECT = 0;
    final static int RECEIVE = 1;
    static CtrPresent present;
    static Context context;
    static CtrView view;
    static HandlerThread handlerThread;
    static Handler handler;
    static Socket socket;
    static PrintWriter printWriter;
    static BufferedReader bufferedReader;
    boolean isConnected;
    boolean isUpdateInfo;


    public CtrPresent(Context context, CtrView view) {
        this.context = context;
        this.view = view;
        isUpdateInfo = false;
        isConnected = false;
        initBackThread();
    }

    private void initBackThread() {
        handlerThread = new HandlerThread("ReadThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECT:
                        conn();
                        break;
                    case RECEIVE:
                        receive();
                        break;
                }

            }
        };
    }

    void conn() {
        try {   //若连接成功，则给Looper发送消息，类型为接收
            socket = new Socket("192.168.8.1", 2001);
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            isConnected = true;
            view.dispToast("连接成功！");
            handler.sendEmptyMessage(RECEIVE);
        } catch (Exception e) {      //若连接失败则隔1.5秒再次尝试
            e.printStackTrace();
            view.dispToast("连接失败，正在重连。。。");
            handler.sendEmptyMessageDelayed(CONNECT, 1500);
        }
    }

    void receive() {
//        String result = "<font color='red'>%d</font>";
//        result = String.format(result, (int) (Math.random() * 3000 + 1000));
//        view.updateMessage("00", result);
        try {
            String s=bufferedReader.readLine();
            System.out.println("收到消息"+s);
            view.updateMessage("00",s);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("接收出错！");
        }
        handler.sendEmptyMessageDelayed(RECEIVE, 50);//1秒刷新20次
    }

    public void resume() {//若未连接则发送连接消息，否则发送接收消息
        if (!isConnected)
            handler.sendEmptyMessage(CONNECT);
        else handler.sendEmptyMessage(RECEIVE);
//        handler.sendEmptyMessage(RECEIVE);
    }

    public void pause() {//目的是使得接收消息停下来
        if (isUpdateInfo)
            handler.removeMessages(RECEIVE);
        isUpdateInfo = false;
    }

    public void stop() {
        isConnected = false;
        isUpdateInfo = false;
        closeAllStream();
        handlerThread.quit();
    }

    private void closeAllStream() {
        try {
            if (printWriter != null) {
                printWriter.close();
                printWriter = null;
            }
            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOrder(String order, String hint) {
        char ord = order.charAt(0);
        try {
            printWriter.print(ord);
            printWriter.flush();
            System.out.println("Send:" + hint + ":" + ord);
        } catch (Exception e) {
            e.printStackTrace();
            view.dispToast("发送失败！");
        }
    }


}
