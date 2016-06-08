package demiks.user32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

/** Sample implementation of a low-level keyboard hook on W32. */
public class KeyHook {
	private HHOOK hhk;
	private LowLevelKeyboardProc keyboardHook;
	private Thread t;

	public static interface KeyListener {
		void callback(int type, int code);
	}

	public KeyHook(KeyListener listener) {
  t = new Thread(new Runnable() {
			@Override
			public void run() {
				HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
				keyboardHook = new LowLevelKeyboardProc() {
					@Override
					public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
						if (nCode >= 0) {
							switch (wParam.intValue()) {
							case WinUser.WM_KEYUP:
							case WinUser.WM_KEYDOWN:
							case WinUser.WM_SYSKEYUP:
							case WinUser.WM_SYSKEYDOWN:
								listener.callback(wParam.intValue(), info.vkCode);
							}
						}
						Pointer ptr = info.getPointer();
						long peer = Pointer.nativeValue(ptr);
						return User32Ex.INSTANCE.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
					}
				};
				hhk = User32Ex.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
				
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
