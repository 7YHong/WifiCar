package zfp.config;

import android.content.Context;
import android.widget.Toast;

/**���ù��߰�
 * @author zfp
 *
 */
public class Tools {
	/**�����tips
	 * @param mContext
	 * @param content
	 */
	public static void tips(Context mContext ,String content){
		 Toast t = Toast.makeText(mContext, content+"", Toast.LENGTH_SHORT);
		 t.show();
	}
}
