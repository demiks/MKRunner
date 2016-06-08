/**
 * 
 */
package demiks.user32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

/**
 * @author Ric-office
 *
 */
public interface User32Ex extends User32 {
 static final User32Ex INSTANCE = (User32Ex) Native.loadLibrary(User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);

	/**
	 * 设置窗口的标题
	 * @param hwnd
	 * @param title
	 */
 void SetWindowText(HWND hwnd, String title);

	/**
	 * 检查窗口是否有效
	 * @param hWnd
	 * @return
	 */
 boolean IsWindow(HWND hWnd);

	/**
	 * 判断窗口当前是否最小化
	 * @param hWnd
	 * @return
	 */
 boolean IsIconic(HWND hWnd);

	/**
	 * 寻找一个窗口的控件
	 * @param hwndParent 顶层窗口句柄
	 * @param hwndChildAfter 前一个控件的句柄，从该控件索引处开始搜索
	 * @param lpszClass 要查找的类名
	 * @param lpszWindow 文字
	 * @return
	 */
 HWND FindWindowEx(HWND hwndParent, HWND hwndChildAfter, String lpszClass, String lpszWindow);

 /**
  * 虚拟按键转换为扫描码，详情参考MSN官方
  * @param VirtualKey
  * @param flag
  * @return
  */
 LPARAM MapVirtualKey(long VirtualKey, long flag);
 
 /**
  * 将相对于屏幕的坐标转换为相对于指定窗口的坐标
  * @param hWnd
  * @param lpPoint
  * @return
  */
 boolean ScreenToClient(HWND hWnd, WinDef.POINT lpPoint);

 /**
  * 获取客户区窗口的大小
  * @param hwnd
  * @param rect
  * @return
  */
 boolean GetClientRect(WinDef.HWND hwnd, WinDef.RECT rect);

 /**
  * 调用声卡发出声音
  * @param dwFreq
  * @param dwDuration
  * @return
  */
 Long Beep(WinDef.DWORD dwFreq,WinDef.DWORD dwDuration);
}
