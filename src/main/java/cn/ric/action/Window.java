package cn.ric.action;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFOHEADER;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.FLASHWINFO;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

import cn.ric.user32.GDI32;
import cn.ric.user32.KeyHook;
import cn.ric.user32.MouseHook;
import cn.ric.user32.User32Ex;
import cn.ric.user32.KeyHook.KeyListener;
import cn.ric.user32.MouseHook.MouseListener;
import cn.ric.util.INI;
import cn.ric.util.StringUtils;

public class Window {
 private static final GDI32 gdi32 = GDI32.INSTANCE;
 private static final User32Ex user32 = User32Ex.INSTANCE;
 private HWND hwnd;
 /**
  * 切换时间
  */
 // public static long swichTime = INI.INSTANCE.getLong("swich-time", 80);
 public static long pressTime = 90;
 public static short keyMode = INI.INSTANCE.getShort("key-mode");
 public static boolean keyFocuse = INI.INSTANCE.getBoolean("key-focuse");
 public static boolean mouseFocuse = INI.INSTANCE.getBoolean("mouse-focuse");
 public static boolean shootFocuse = INI.INSTANCE.getBoolean("shoot-focuse");
 /**
  * 滞留时间
  */
 public static long stayTime = INI.INSTANCE.getLong("stay-time", 800);

 private Window() {
 }

 private Window(com.sun.jna.platform.win32.WinDef.HWND HWND) {
  this.hwnd = HWND;
 }

 public Window(String title, String className) {
  this();
  if (!StringUtils.isEmpty(title))
   this.hwnd = user32.FindWindow(className, title);
 }

 public boolean hasWindow() {
  return hwnd != null && user32.IsWindow(hwnd);
 }

 public void setTitle(String title) {
  user32.SetWindowText(hwnd, title);
 }

 private long recodeTime;
 private KeyHook keyHook;
 private MouseHook mouseHook;

 public static interface RecordingListener {
  public void onEnd(ArrayList<String> recordings);
 }

 /**
  * 开始录制
  * 
  * @param endRecordHotKeyCode
  *         0 表示鼠标左键弹起时结束
  * @param listener
  */
 public void startRecording(int endRecordHotKeyCode, RecordingListener listener) {
  if (keyHook != null || mouseHook != null) {
   stopRecording();
  }
  final ArrayList<String> recordings = new ArrayList<String>();
  // 监听键盘
  recodeTime = System.currentTimeMillis();
  keyHook = new KeyHook(new KeyListener() {
   @Override
   public void callback(int type, int code) {
    if ((type != WinUser.WM_KEYUP && type != WinUser.WM_KEYDOWN) || !hwnd.equals(user32.GetForegroundWindow())) {
     return;
    }
    long timeMillis = System.currentTimeMillis();
    int releaseType = type == WinUser.WM_KEYUP ? 1 : 2;
    String content = "Key\\*/" + releaseType + "\\*/" + (timeMillis - recodeTime) + "\\*/" + code;
    recordings.add(content);
    recodeTime = timeMillis;
    if (code == endRecordHotKeyCode && type == WinUser.WM_KEYUP) {
     stopRecording();
     if (listener != null)
      listener.onEnd(recordings);
    }
   }
  });
  mouseHook = new MouseHook(new MouseListener() {
   @Override
   public void callback(int type, int x, int y) {
    if ((type != MouseHook.WM_LBUTTONUP && type != MouseHook.WM_RBUTTONUP && type != MouseHook.WM_LBUTTONDOWN
      && type != MouseHook.WM_RBUTTONDOWN) || !hwnd.equals(user32.GetForegroundWindow())) {
     return;
    }
    long timeMillis = System.currentTimeMillis();
    WinDef.POINT point = new WinDef.POINT(x, y);
    user32.ScreenToClient(hwnd, point);
    int releaseType = (type == MouseHook.WM_LBUTTONUP || type == MouseHook.WM_RBUTTONUP) ? 1 : 2;
    String content = "Mouse\\*/" + releaseType + "\\*/" + (timeMillis - recodeTime) + "\\*/"
      + ((type == MouseHook.WM_LBUTTONUP || type == MouseHook.WM_LBUTTONDOWN) ? "LeftButton" : "RightButton") + "\\*/" + point.x
      + "\\*/" + point.y;
    recordings.add(content);
    recodeTime = timeMillis;
    if (0 == endRecordHotKeyCode && type == MouseHook.WM_LBUTTONUP) {
     stopRecording();
     if (listener != null)
      listener.onEnd(recordings);
    }
   }
  });
 }

 /**
  * 停止录制
  */
 public void stopRecording() {
  if (keyHook != null) {
   keyHook.unHook();
   keyHook = null;
  }
  if (mouseHook != null) {
   mouseHook.unHook();
   mouseHook = null;
  }
 }

 /**
  * 休眠被打断则返回 false
  * 
  * @param time
  * @return
  */
 private boolean sleep(long time) {
  if (time > 0)
   try {
    Thread.sleep(time);
    return true;
   } catch (InterruptedException e) {
    // 被打断
    return false;
   }
  else
   return true;
 }

 public String getTitle() {
  int getWindowTextLength = user32.GetWindowTextLength(hwnd);
  if (getWindowTextLength == 0) {
   return "";
  }
  char[] title = new char[getWindowTextLength];
  user32.GetWindowText(hwnd, title, getWindowTextLength + 1);
  return new String(title);
 }

 @Deprecated
 public static Window searchWindow(String reg) {
  final ArrayList<Window> list = new ArrayList<>(1);
  user32.EnumWindows(new WNDENUMPROC() {
   @Override
   public boolean callback(HWND arg0, Pointer arg1) {
    int getWindowTextLength = user32.GetWindowTextLength(arg0);
    if (getWindowTextLength == 0) {
     return true;
    }
    char[] title = new char[getWindowTextLength];
    user32.GetWindowText(arg0, title, getWindowTextLength + 1);
    if (new String(title).indexOf(reg) != -1) {
     list.add(new Window(arg0));
     return false;
    }
    return true;
   }
  }, null);
  if (list.isEmpty()) {
   return null;
  } else {
   Window result = list.get(0);
   return result;
  }
 }

 public Window[] getChildren() {
  final ArrayList<Window> list = new ArrayList<>();
  user32.EnumChildWindows(hwnd, new WinUser.WNDENUMPROC() {
   @Override
   public boolean callback(com.sun.jna.platform.win32.WinDef.HWND paramHWND, Pointer paramPointer) {
    Window w = new Window(paramHWND);
    list.add(w);
    return true;
   }
  }, null);
  return list.toArray(new Window[0]);
 }

 /**
  * 点击左键
  * 
  * @param x
  * @param y
  */
 public void click(long x, long y) {
  click(x, y, pressTime);
 }

 /**
  * 点击左键
  * 
  * @param x
  * @param y
  */
 public void click(long x, long y, long pressTime) {
  clickDown(x, y);
  sleep(pressTime);
  clickUp(x, y);
 }

 /**
  * 鼠标左键按下
  * 
  * @param x
  * @param y
  */
 public void clickDown(long x, long y) {
  if (mouseFocuse) {
   autoHide(30000000L);
   if (foregroundWindowMouse == null)
    foregroundWindowMouse = user32.GetForegroundWindow();
   if (!hwnd.equals(foregroundWindowMouse)) {
    focus();
    // sleep(swichTime);
   }
  }
  user32.PostMessage(hwnd, MouseHook.WM_LBUTTONDOWN, null, new LPARAM(y << 16 | x << 16 >> 16));
 }

 /**
  * 鼠标左键弹起
  * 
  * @param x
  * @param y
  */
 public void clickUp(long x, long y) {
  user32.PostMessage(hwnd, MouseHook.WM_LBUTTONUP, null, new LPARAM(y << 16 | x << 16 >> 16));
  if (mouseFocuse) {
   autoHide(stayTime);
   if (!hwnd.equals(foregroundWindowMouse))
    user32.SetForegroundWindow(foregroundWindowMouse);
  }
  foregroundWindowMouse = null;
 }

 /**
  * 点击右键
  * 
  * @param x
  * @param y
  */
 public void clickR(long x, long y) {
  clickDownR(x, y);
  sleep(200);
  clickUpR(x, y);
 }

 /**
  * 鼠标右键按下
  * 
  * @param x
  * @param y
  */
 public void clickDownR(long x, long y) {
  if (mouseFocuse) {
   autoHide(30000000L);
   if (foregroundWindowMouse == null)
    foregroundWindowMouse = user32.GetForegroundWindow();
   if (!hwnd.equals(foregroundWindowMouse)) {
    focus();
    // sleep(swichTime);
   }
  }
  user32.PostMessage(hwnd, MouseHook.WM_RBUTTONDOWN, null, new LPARAM(y << 16 | x << 16 >> 16));
 }

 /**
  * 鼠标右键弹起
  * 
  * @param x
  * @param y
  */
 public void clickUpR(long x, long y) {
  user32.PostMessage(hwnd, MouseHook.WM_RBUTTONUP, null, new LPARAM(y << 16 | x << 16 >> 16));
  if (mouseFocuse) {
   autoHide(stayTime);
   if (!hwnd.equals(foregroundWindowMouse))
    user32.SetForegroundWindow(foregroundWindowMouse);
  }
  foregroundWindowMouse = null;
 }

 /**
  * 按键
  * 
  * @param hWnd
  * @param c
  */
 public void typeKey(int c) {
  typeKey(c, pressTime);
 }

 private HWND foregroundWindowMouse = null;
 private HWND foregroundWindowKey = null;
 private HWND foregroundWindow = null;
 private boolean isIconic;
 private boolean isWindowVisible;

 public void typeKey(int c, long presTime) {
  keyDown(c);
  sleep(presTime);
  keyUp(c);
 }

 public void flashWindow(long time) {
  FLASHWINFO flashwinfo = new FLASHWINFO();
  flashwinfo.hWnd = hwnd;
  flashwinfo.dwFlags = WinUser.FLASHW_ALL | WinUser.FLASHW_TIMERNOFG;
  user32.FlashWindowEx(flashwinfo);
  user32.Beep(new WinDef.DWORD(600L), new WinDef.DWORD(time));
 }

 /**
  * 获取指定坐标点的颜色值(客户区坐标)
  * 
  * @param x
  * @param y
  * @return
  */
 public int getColorAt(int x, int y) {
  autoHide(stayTime);
  HWND foregroundWindow = null;
  HDC dc;
  synchronized (user32) {
   if (shootFocuse) {
    foregroundWindow = user32.GetForegroundWindow();
    focus();
    if (!hwnd.equals(foregroundWindow))
     user32.SetForegroundWindow(foregroundWindow);
   } else {
    show();
   }
   dc = gdi32.GetDC(hwnd);
  }
  int getPixel = gdi32.GetPixel(dc, x, y);

  // 还原窗口
  if (shootFocuse && !hwnd.equals(foregroundWindow))
   user32.SetForegroundWindow(foregroundWindow);

  return getPixel;
 }

 private Thread hideWinThread;

 public BufferedImage getScreenshot(Rectangle bounds) {
  autoHide(stayTime);
  HWND foregroundWindow = null;
  HDC dc;
  synchronized (user32) {
   if (shootFocuse) {
    foregroundWindow = user32.GetForegroundWindow();
    focus();
   } else {
    show();
   }
   dc = gdi32.GetDC(hwnd);
  }

  BufferedImage bufferedImageFromBitmap = null;
  if (bounds == null) {
   RECT rect = new RECT();
   user32.GetClientRect(hwnd, rect);
   bounds = rect.toRectangle();
  }
  HBITMAP outputBitmap = gdi32.CreateCompatibleBitmap(dc, bounds.width, bounds.height);
  try {
   HDC blitDC = gdi32.CreateCompatibleDC(dc);
   try {
    HANDLE oldBitmap = gdi32.SelectObject(blitDC, outputBitmap);
    try {
     gdi32.BitBlt(blitDC, 0, 0, bounds.width, bounds.height, dc, bounds.x, bounds.y, GDI32.SRCCOPY);
    } finally {
     gdi32.SelectObject(blitDC, oldBitmap);
    }
    BITMAPINFO bi = new BITMAPINFO(40);
    bi.bmiHeader.biSize = 40;
    boolean ok = gdi32.GetDIBits(blitDC, outputBitmap, 0, bounds.height, (byte[]) null, bi, WinGDI.DIB_RGB_COLORS);

    // 还原窗口
    if (shootFocuse && !hwnd.equals(foregroundWindow))
     user32.SetForegroundWindow(foregroundWindow);

    if (ok) {
     BITMAPINFOHEADER bih = bi.bmiHeader;
     bih.biHeight = -Math.abs(bih.biHeight);
     bi.bmiHeader.biCompression = 0;
     bufferedImageFromBitmap = bufferedImageFromBitmap(blitDC, outputBitmap, bi);
    }
   } finally {
    gdi32.DeleteObject(blitDC);
   }
  } finally {
   gdi32.DeleteObject(outputBitmap);
  }
  return bufferedImageFromBitmap;
 }

 private long whenSwich;

 /**
  * 在指定时间后将窗口恢复成调用时的状态
  * 
  * @author Ric-office
  * @param delay
  * @time 2016年5月26日
  */
 public void autoHide(long delay) {
  whenSwich = System.currentTimeMillis();
  if (hideWinThread == null || !hideWinThread.isAlive()) {
   if (foregroundWindow == null) {
    foregroundWindow = user32.GetForegroundWindow();
    isIconic = isIconic();
    isWindowVisible = isVisible();
   }
   hideWinThread = new Thread(() -> {
    long l = System.currentTimeMillis() - whenSwich;
    while (l < delay) {
     if (!sleep(l)) {
      return;
     }
     l = System.currentTimeMillis() - whenSwich;
    }
    if (isIconic) {
     min();
    }
    if (!isWindowVisible) {
     hide();
    }
    if (!hwnd.equals(foregroundWindow)) {
     user32.SetForegroundWindow(foregroundWindow);
    }
    foregroundWindow = null;
    hideWinThread = null;
   });
   hideWinThread.start();
  }
 }

 private boolean isVisible() {
  return user32.IsWindowVisible(hwnd);
 }

 private boolean isIconic() {
  return user32.IsIconic(hwnd);
 }

 private static BufferedImage bufferedImageFromBitmap(HDC blitDC, HBITMAP outputBitmap, BITMAPINFO bi) {
  BITMAPINFOHEADER bih = bi.bmiHeader;
  int height = Math.abs(bih.biHeight);
  final ColorModel cm;
  final DataBuffer buffer;
  final WritableRaster raster;
  int strideBits = (bih.biWidth * bih.biBitCount);
  int strideBytesAligned = (((strideBits - 1) | 0x1F) + 1) >> 3;
  final int strideElementsAligned;
  switch (bih.biBitCount) {
  case 16:
   strideElementsAligned = strideBytesAligned / 2;
   cm = new DirectColorModel(16, 0x7C00, 0x3E0, 0x1F);
   buffer = new DataBufferUShort(strideElementsAligned * height);
   raster = Raster.createPackedRaster(buffer, bih.biWidth, height, strideElementsAligned, ((DirectColorModel) cm).getMasks(),
     null);
   break;
  case 32:
   strideElementsAligned = strideBytesAligned / 4;
   cm = new DirectColorModel(32, 0xFF0000, 0xFF00, 0xFF);
   buffer = new DataBufferInt(strideElementsAligned * height);
   raster = Raster.createPackedRaster(buffer, bih.biWidth, height, strideElementsAligned, ((DirectColorModel) cm).getMasks(),
     null);
   break;
  default:
   throw new IllegalArgumentException("Unsupported bit count: " + bih.biBitCount);
  }
  final boolean ok;
  switch (buffer.getDataType()) {
  case DataBuffer.TYPE_INT: {
   int[] pixels = ((DataBufferInt) buffer).getData();
   ok = gdi32.GetDIBits(blitDC, outputBitmap, 0, raster.getHeight(), pixels, bi, 0);
  }
   break;
  case DataBuffer.TYPE_USHORT: {
   short[] pixels = ((DataBufferUShort) buffer).getData();
   ok = gdi32.GetDIBits(blitDC, outputBitmap, 0, raster.getHeight(), pixels, bi, 0);
  }
   break;
  default:
   throw new AssertionError("Unexpected buffer element type: " + buffer.getDataType());
  }
  if (ok) {
   return new BufferedImage(cm, raster, false, null);
  } else {
   return null;
  }
 }

 /**
  * 键按下
  * 
  * @param c
  */
 public void keyDown(int c) {
  if (keyFocuse) {
   autoHide(30000000L);
   if (foregroundWindowKey == null)
    foregroundWindowKey = user32.GetForegroundWindow();
   if (!hwnd.equals(foregroundWindowKey)) {
    focus();
    // sleep(swichTime);
   }
  }
  long scan = user32.MapVirtualKey(c, 0).longValue();
  long lparam = 0x00000001 | (scan << 16);
  // if (true) // 是否扩展键
  // lparam = lparam | 0x01000000;
  if (keyMode == 0 || keyMode == 1)
   user32.PostMessage(hwnd, WinUser.WM_SYSKEYDOWN, new WinDef.WPARAM(c), new LPARAM(lparam | 0x20000000));
  if (keyMode == 0 || keyMode == 2)
   user32.PostMessage(hwnd, WinUser.WM_KEYDOWN, new WinDef.WPARAM(c), new LPARAM(lparam));
 }

 /**
  * 键弹起
  * 
  * @param c
  */
 public void keyUp(int c) {
  long scan = user32.MapVirtualKey(c, 0).longValue();
  long lparam = 0x00000001 | (scan << 16);
  // if (true) // 是否扩展键
  // lparam = lparam | 0x01000000;
  if (keyMode == 0 || keyMode == 1)
   user32.PostMessage(hwnd, WinUser.WM_SYSKEYUP, new WinDef.WPARAM(c), new LPARAM(lparam | 0x20000000));
  if (keyMode == 0 || keyMode == 2)
   user32.PostMessage(hwnd, WinUser.WM_KEYUP, new WinDef.WPARAM(c), new LPARAM(lparam));
  if (keyFocuse) {
   autoHide(stayTime);
   if (!hwnd.equals(foregroundWindowKey))
    user32.SetForegroundWindow(foregroundWindowKey);
  }
  foregroundWindowKey = null;
 }

 /**
  * 获取窗口宽度
  * 
  * @return
  */
 public int getWidth() {
  WinDef.RECT rect = new WinDef.RECT();
  user32.GetWindowRect(hwnd, rect);
  return rect.toRectangle().width;
 }

 /**
  * 获取窗口高度
  * 
  * @return
  */
 public int getHeight() {
  WinDef.RECT rect = new WinDef.RECT();
  user32.GetWindowRect(hwnd, rect);
  return rect.toRectangle().height;
 }

 /**
  * 隐藏
  */
 public void hide() {
  min();
  user32.ShowWindow(hwnd, WinUser.SW_HIDE);
 }

 /**
  * 显示
  */
 public void show() {
  user32.ShowWindow(hwnd, WinUser.SW_SHOWNOACTIVATE);
 }

 public void focus() {
  show();
  user32.SetFocus(hwnd);
  user32.SetForegroundWindow(hwnd);
 }

 /**
  * 最大化
  */
 public void max() {
  user32.ShowWindow(hwnd, WinUser.SW_MAXIMIZE);
 }

 /**
  * 最小化
  */
 public void min() {
  user32.ShowWindow(hwnd, WinUser.SW_MINIMIZE);
 }

 @Override
 public String toString() {
  return "Window [HWND=" + hwnd + "]";
 }

}
