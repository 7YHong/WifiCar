package zfp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import zfp.mycar.R;

/**进程序后第一个访问的类,三个选项.配置,测试和连接
 * @author zfp
 *
 */
public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		//=========================================
		setMainBtnAffairs();//配置按钮功能
//		loadActivity(Main.this, Set.class);//载入set的Activity-仅测试
//		loadActivity(Main.this, Ctr.class);//载入ctr的Activity-仅测试
//		loadActivity(Main.this, TestAct.class);//载入新的Activity-仅测试
	}

	//================================================================================


	/**载入其他Activity的通用方法
	 * @param context 当前上下文
	 * @param c 要跳转到哪个Activity类
	 */
	private void loadActivity(Context context,Class<?> c){
		Intent intent=new Intent();
		intent.setClass(context,c);
		startActivity(intent);
		overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out); //随便加的切换效果.请无视它
	}


	/**
	 * 配置按钮功能
	 */
	private void setMainBtnAffairs() {
		// 主界面按钮
		findViewById(R.id.btnConn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, Ctr.class);//载入控制小车界面
			}
		});
		findViewById(R.id.btnSet).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, Set.class);//载入配置界面,注意配置界面没有写好.

			}
		});
	}


}
