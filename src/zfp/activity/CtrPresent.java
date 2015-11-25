package zfp.activity;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by 7YHong on 2015/11/25.
 */
public class CtrPresent {
    static CtrPresent present;
    static Context context;
    static CtrView view;
    static HandlerThread handlerThread;
    static Handler handler;
    static boolean isUpdateInfo;

    public static CtrPresent get(Context c, CtrView v) {
        if (present == null)
            present = new CtrPresent(context, view);
        context = c;
        view = v;
        initBackThread();
        System.out.println("get");
        return present;
    }

    CtrPresent(Context context, CtrView view) {
    }

    private static void initBackThread() {
        handlerThread = new HandlerThread("ReadThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = "<font color='red'>%d</font>";
                result = String.format(result, (int) (Math.random() * 3000 + 1000));
                view.updateMessage("00", result);
                if (isUpdateInfo)
                    handler.sendEmptyMessageDelayed(0, 1000);
            }
        };
    }

    public void start() {
        isUpdateInfo = true;
//        开始线程的运转
        handler.sendEmptyMessage(0);
    }

    public void pause() {
        isUpdateInfo = false;
        handler.removeMessages(0);
    }

    public void stop() {
        handlerThread.quit();
    }


}
