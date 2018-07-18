package cn.ric.user32;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.platform.win32.WinUser.MSG;

/** Sample implementation of a low-level mouse hook on W32. */
public class MouseHook {
	private HHOOK hhk;
	private LowLevelMouseProc mouseHook;
	private Thread t;

	public static final int WM_MOUSEMOVE = 512;
	public static final int WM_LBUTTONDOWN = 513;
	public static final int WM_LBUTTONUP = 514;
	public static final int WM_RBUTTONDOWN = 516;
	public static final int WM_RBUTTONUP = 517;
	public static final int WM_MBUTTONDOWN = 519;
	public static final int WM_MBUTTONUP = 520;

	public interface LowLevelMouseProc extends HOOKPROC {
		LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
	}

	public static class MOUSEHOOKSTRUCT extends Structure {
		public static class ByReference extends MOUSEHOOKSTRUCT implements Structure.ByReference {
		};

		public User32.POINT pt;
		public User32.ULONG_PTR dwExtraInfo;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "pt", "dwExtraInfo" });
		}
	}

	public static interface MouseListener {
		void callback(int type, int x, int y);
	}

	public MouseHook(MouseListener listener) {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
				mouseHook = new LowLevelMouseProc() {
					@Override
					public LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT info) {
						if (nCode >= 0) {
							switch (wParam.intValue()) {
							case WM_LBUTTONDOWN:
							case WM_LBUTTONUP:
							case WM_MBUTTONDOWN:
							case WM_MBUTTONUP:
							case WM_RBUTTONDOWN:
							case WM_RBUTTONUP:
								listener.callback(wParam.intValue(), info.pt.x, info.pt.y);
							}
						}
						Pointer ptr = info.getPointer();
						long peer = Pointer.nativeValue(ptr);
						return User32Ex.INSTANCE.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
					}
				};
				hhk = User32Ex.INSTANCE.SetWindowsHookEx(WinUser.WH_MOUSE_LL, mouseHook, hMod, 0);

				// This bit never returns from GetMessage
				int result;
				MSG msg = new MSG();
				while ((result = User32Ex.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
					if (result == -1) {
						System.err.println("error in get message");
						break;
					} else {
						System.err.println("got message");
						User32Ex.INSTANCE.TranslateMessage(msg);
						User32Ex.INSTANCE.DispatchMessage(msg);
					}
				}
			} // end run
		});
		t.start();
	}

	public void unHook() {
		t.interrupt();
		User32Ex.INSTANCE.UnhookWindowsHookEx(hhk);
	}
}
