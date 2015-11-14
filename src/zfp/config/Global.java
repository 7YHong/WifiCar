package zfp.config;

import android.os.Environment;

public class Global {
	//SD卡路径.比如访问log.txt 那就是GLOBAL_SDCARDPATH+"/log.txt"
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
	public static final String SERVER_IP_Test = "192.168.0.162"; //测试用
	public static final int SERVER_PORT_Test = 7777; //测试用
	public static final String SERVER_IP = "192.168.8.1"; 
	public static final int SERVER_PORT = 2001; 
	
	
	
//	[VideoUrl]
//			videourl=http://192.168.8.1:8083/?action=snapshot
//			[ControlUrl]
//			controlUrl=192.168.8.1
//			[ControlPort]
//			controlPort=2001
//			[ControlCommand]
//			CMD_Forward=a
//			CMD_Backward=b
//			CMD_TurnLeft=c
//			CMD_TurnRight=d
//			CMD_Stop=e
//			CMD_EngineUp=5
//			CMD_EngineDown=6
//			CMD_EngineLeft=1
//			CMD_EngineCenter=2
//			CMD_EngineRight=3
//			CMD_Xunxian=4
//			CMD_Hongwai=5
//			CMD_Zhuiguang=6
//			CMD_Bizhang=7
//			CMD_Wudao=8
//			CMD_Wangluo=9

	
}
