package zfp.config;

import android.content.Context;
import android.widget.Toast;

/**常用工具包
 * @author zfp
 *
 */
public class Tools {
	/**界面的tips
	 * @param mContext
	 * @param content
	 */
	public static void tips(Context mContext ,String content){
		 Toast t = Toast.makeText(mContext, content+"", Toast.LENGTH_SHORT);
		 t.show();
	}
}
