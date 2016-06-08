package demiks.user32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;

/**
 * 与显卡相关的一些函数
 * 
 * @author zhangming
 *
 */
public interface GDI32 extends com.sun.jna.platform.win32.GDI32 {
 GDI32 INSTANCE = (GDI32) Native.loadLibrary(GDI32.class);

 /**
  * 截图相关的函数
  */
 boolean BitBlt(HDC hdcDest, int nXDest, int nYDest, int nWidth, int nHeight, HDC hdcSrc, int nXSrc, int nYSrc,
   int dwRop);

 /**
  * 截图相关的函数
  */
 HDC GetDC(HWND hWnd);

 /**
  * 截图相关的函数
  */
 boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, byte[] pixels, BITMAPINFO bi, int usage);

 /**
  * 截图相关的函数
  */
 boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, short[] pixels, BITMAPINFO bi, int usage);

 /**
  * 截图相关的函数
  */
 boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, int[] pixels, BITMAPINFO bi, int usage);

 static final int SRCCOPY = 0xCC0020;

 /**
  * 获取指定点的象素RGB值，窗口最小化时无效
  * 
  * @param hdc
  * @param nXPos
  * @param nYPos
  * @return
  */
 int GetPixel(HDC hdc, int nXPos, int nYPos);
}
