package zfp.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.app.Activity;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import zfp.mycar.R;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * 小车控制类
 *
 * @author zfp
 */
public class Ctr extends Activity {

    private Socket socket = null;
    // 指令发出 数据缓存
    private static PrintWriter printWriter = null;
    private static BufferedReader bufferedReader = null;//把内容放进来后可以用来判断连接是否异常,不必要就给注释了

    Handler mHandler;

    private boolean isConnect = false;//是否连接小车.用于销毁时处理事件的判断条件
    private Thread thread = null;//控制小车线程

    private RadioButton radioConn;//连接控制按钮的变量,只是为了以后方便
    private Button btnForward;//前
    private Button btnBack;//后
    private Button btnLeft;//左
    private Button btnRight;//右

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ctr);
        mHandler = new Handler();
        // =========================================
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏代码
        // SharedPreferences sp = this.getSharedPreferences("set",
        // MODE_PRIVATE);
        setBtnAffairs();
    }


    // ======================================================

    /**
     * 配置按钮功能
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setBtnAffairs() {
        // 主界面按钮
        radioConn = (RadioButton) this.findViewById(R.id.radioConn);//点击后单选按钮后才可以控制小车
        btnForward = (Button) this.findViewById(R.id.btnForward);
        btnBack = (Button) this.findViewById(R.id.btnBack);
        btnLeft = (Button) this.findViewById(R.id.btnLeft);
        btnRight = (Button) this.findViewById(R.id.btnRight);
        radioConn.setOnClickListener(new View.OnClickListener() {// 控制启动
            @Override
            public void onClick(View v) {
                isConnect = true;
                // 开启控制线程(注意视频连接会在进入控制界面时直接开启,这里只响应控制小车的线程)
                thread = new Thread(runnable);
                thread.start();
            }
        });

        findViewById(R.id.BtnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctrOrder("r", "读取数据测试");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int msg;
                        try {
                            msg = bufferedReader.read();
                            System.out.println("收到" + String.valueOf(msg));
                        } catch (Exception e) {
                            System.out.println("没有收到东西");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        //运动控制部分
        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "a", "前");
                return true;
            }
        });
        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "b", "后");
                return true;
            }
        });
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "c", "左");
                return true;
            }
        });
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "d", "右");
                return true;
            }
        });

        //云台控制部分
        findViewById(R.id.servoUp).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "k", "上");
                return true;
            }
        });
        findViewById(R.id.servoDown).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "i", "下");
                return true;
            }
        });
        findViewById(R.id.servoLeft).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "j", "左");
                return true;
            }
        });
        findViewById(R.id.servoRight).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchHandle(event, "l", "右");
                return true;
            }
        });
        findViewById(R.id.servo_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servoReset();
            }
        });

        //功能复选框部分
        ((CheckBox) findViewById(R.id.check_speed)).setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.leftbar).setVisibility(View.VISIBLE);
                    findViewById(R.id.rightbar).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.leftbar).setVisibility(View.INVISIBLE);
                    findViewById(R.id.rightbar).setVisibility(View.INVISIBLE);
                }
            }
        });
        ((CheckBox) findViewById(R.id.check_light)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ctrOrder("n", "开灯");
                } else {
                    ctrOrder("m", "关灯");
                }
            }
        });
        ((CheckBox) findViewById(R.id.check_servo)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.servoCtr).setVisibility(View.VISIBLE);
                    findViewById(R.id.servo_reset).setVisibility(View.VISIBLE);
                } else {
                    servoReset();
                    findViewById(R.id.servoCtr).setVisibility(View.INVISIBLE);
                    findViewById(R.id.servo_reset).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void servoReset() {
        ctrOrder("q", "云台归位");
    }

    /**
     * @param event
     */
    private void touchHandle(MotionEvent event, String orderStr, String tips) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ctrOrder(orderStr, tips);
                break;
            case MotionEvent.ACTION_UP:
                ctrOrder("e", "停");
                break;
            default:
                break;
        }
    }

    /**
     * 给小车发送指令,
     *
     * @param orderStr 发"a",则单片机接收到"a"
     * @param tips     提示
     */
    private void ctrOrder(String orderStr, String tips) {
        try {
            printWriter.print(orderStr);
            printWriter.flush();
            System.out.println(tips + ":" + orderStr);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "请先确保已经正确连接到小车！", Toast.LENGTH_SHORT).show();
        }
        // if(tips != null && !"".equals(tips)){
        // Tools.tips(Ctr.this, tips);
        // }

    }

    // ============================================
    // 线程mRunnable启动
    private Runnable runnable = new Runnable() {
        public void run() {
            try {
                // 连接服务器
                socket = new Socket("192.168.8.1", 2001); //小车ip,端口
                // //取得输入、输出流
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);// 根据新建s的sock建立
            } catch (Exception e) {
//				Tools.tips(Ctr.this, "连接错误,请检查网络");
                return;
            }
        }
    };

    public void onDestroy() {//视频占用的资源会在退出此类时自动销毁,配置在surfaceview的子类PaintVedio中,这里只端口小车的控制连接
        super.onDestroy();
        if (isConnect) {
            isConnect = false;
            try {
                if (socket != null) {
                    socket.close();
                    socket = null;
                    printWriter.close();
                    printWriter = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            thread.interrupt();
        }

    }
}