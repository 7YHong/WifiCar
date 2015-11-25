package zfp.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.app.Activity;
import android.os.Handler;
import android.text.Html;
import android.view.*;
import android.widget.*;
import zfp.mycar.R;
import android.os.Bundle;

/**
 * 小车控制类
 *
 * @author zfp
 */
public class Ctr extends Activity implements CtrView{
    Handler mHandler;
    private Button btnForward;//前
    private Button btnBack;//后
    private Button btnLeft;//左
    private Button btnRight;//右
    private TextView speed,distance,tempreture,wet;
    CtrPresent present;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ctr);
        mHandler = new Handler();
        present=new CtrPresent(getApplicationContext(),this);
        setBtnAffairs();
    }


    // ======================================================

    /**
     * 配置按钮功能
     */
//    @SuppressLint("ClickableViewAccessibility")
    private void setBtnAffairs() {
        // 主界面按钮
        btnForward = (Button) this.findViewById(R.id.moveForward);
        btnBack = (Button) this.findViewById(R.id.moveBack);
        btnLeft = (Button) this.findViewById(R.id.moveLeft);
        btnRight = (Button) this.findViewById(R.id.moveRight);
        //显示部分
        speed= (TextView) findViewById(R.id.state_speed);
        distance= (TextView) findViewById(R.id.state_distance);
        tempreture= (TextView) findViewById(R.id.state_tempreture);
        wet= (TextView) findViewById(R.id.state_wet);
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
//        findViewById(R.id.servo_reset).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                servoReset();
//            }
//        });

        //功能复选框部分
        ((CheckBox) findViewById(R.id.check_servo)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.servoCtr_left).setVisibility(View.VISIBLE);
                    findViewById(R.id.servoCtr_right).setVisibility(View.VISIBLE);
                } else {
                    servoReset();
                    findViewById(R.id.servoCtr_left).setVisibility(View.INVISIBLE);
                    findViewById(R.id.servoCtr_right).setVisibility(View.INVISIBLE);
                }
            }
        });
        ((CheckBox) findViewById(R.id.check_speed)).setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.speedBar).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.speedBar).setVisibility(View.INVISIBLE);
                }
            }
        });
        ((CheckBox) findViewById(R.id.check_light)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    present.sendOrder("n", "开灯");
                } else {
                    present.sendOrder("m", "关灯");
                }
            }
        });
        ((CheckBox) findViewById(R.id.check_alertLine)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.alertline).setVisibility(View.VISIBLE);
                else findViewById(R.id.alertline).setVisibility(View.INVISIBLE);

            }
        });
    }

    private void servoReset() {
        present.sendOrder("q", "云台归位");
    }

    /**
     * @param event
     */
    private void touchHandle(MotionEvent event, String orderStr, String tips) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                present.sendOrder(orderStr, tips);
                break;
            case MotionEvent.ACTION_UP:
                present.sendOrder("e", "停");
                break;
            default:
                break;
        }
    }


    public void onDestroy() {//视频占用的资源会在退出此类时自动销毁,配置在surfaceview的子类PaintVedio中,这里只端口小车的控制连接
        super.onDestroy();
        present.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        present.resume();
    }

    @Override
    protected void onPause() {
        present.pause();
        super.onPause();
    }

    @Override
    public void dispToast(final String tip) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),tip,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void updateMessage(final String type, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                speed.setText(type);
                wet.setText(Html.fromHtml(msg));
            }
        });
    }
}