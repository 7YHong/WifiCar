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

/**��������һ�����ʵ���,����ѡ��.����,���Ժ�����
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
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//��������		
		setMainBtnAffairs();//���ð�ť����
//		loadActivity(Main.this, Set.class);//����set��Activity-������
//		loadActivity(Main.this, Ctr.class);//����ctr��Activity-������
//		loadActivity(Main.this, TestAct.class);//�����µ�Activity-������
	}

	//================================================================================


	/**��������Activity��ͨ�÷���
	 * @param context ��ǰ������
	 * @param c Ҫ��ת���ĸ�Activity��
	 */
	private void loadActivity(Context context,Class<?> c){
		Intent intent=new Intent();
		intent.setClass(context,c);
		startActivity(intent);
		overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out); //���ӵ��л�Ч��.��������
	}

	
	/**
	 * ���ð�ť����
	 */
	private void setMainBtnAffairs() {
		// �����水ť
		final Button btnConn = (Button) this.findViewById(R.id.btnConn);//�������С������İ�ť
		final Button btnSet = (Button) this.findViewById(R.id.btnSet);//��������
		//final ImageButton imageButtonTest = (ImageButton) this.findViewById(R.id.imageButtonTest);//�������
		btnConn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, Ctr.class);//�������С������
			}
		});
		btnSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, Set.class);//�������ý���,ע�����ý���û��д��.
				
			}
		});
		
		/*imageButtonTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, TestAct.class);//������Խ���
			}
		});*/
		
		
		
	}
	
	
}
