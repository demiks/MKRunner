package demiks.action.event;

import demiks.action.Window;
import demiks.action.XY;
import demiks.util.StringUtils;

public abstract class MKEvent {
	public static enum Type {
		/**
		 * 鼠标
		 */
		Mouse,
		/**
		 * 按键
		 */
		Key,
		Beep,
		Quote,
		Focus
	}

	public static MKEvent getFromStr(String str) {
		String[] split = str.split("\\\\\\*/");
		MKEvent event = null;
		switch (split[0]) {
		case "Mouse":
			MKMouseEvent mouseEvent = new MKMouseEvent();
			switch (split[3]) {
			case "RightButton":
				mouseEvent.setType(MKMouseEvent.Type.RightButton);
				break;
			case "LeftButton":
				mouseEvent.setType(MKMouseEvent.Type.LeftButton);
				break;
			default:
				mouseEvent.setType(MKMouseEvent.Type.LeftButton);
				break;
			}
			XY xy = new XY(Integer.parseInt(split[4]), Integer.parseInt(split[5]));
			mouseEvent.setXY(xy);
			if (split.length > 6 && !StringUtils.isEmpty(split[6]))
			 mouseEvent.setPicPath(split[6]);
			event = mouseEvent;
			break;
		case "Key":
			MKKeyEvent keyEvent = new MKKeyEvent();
			keyEvent.setKeyValue(Integer.parseInt(split[3]));
			event = keyEvent;
			break;
		case "Beep":
			MKBeepEvent mkBeepEvent = new MKBeepEvent();
			mkBeepEvent.setTime(Long.parseLong(split[3]));
   event = mkBeepEvent;
			break;
		case "Focus":
		 MKFocusEvent mkFocusEvent = new MKFocusEvent();
		 mkFocusEvent.setTime(Long.parseLong(split[3]));
		 event = mkFocusEvent;
		 break;
		case "Quote":
			MKQuoteEvent quoteEvent = new MKQuoteEvent();
			quoteEvent.setMkPath(split[3]);
			event = quoteEvent;
			break;
		default:
			System.err.println("错误的事件类型：" + str);
			break;
		}
		event.setReleaseType(Short.parseShort(split[1]));
		event.setSleepTime(Long.parseLong(split[2]));
		return event;
	}

	/**
	 * 执行动作，包括之前睡眠
	 * 
	 * @param win
	 * @throws Exception 
	 */
	public void doEvent(Window win) throws Exception {
		if (getSleepTime() > 0)
		 Thread.sleep(getSleepTime());
	}

	/**
	 * 执行前的休眠时间
	 */
	protected long sleepTime;
	/**
	 * 弹起（释放）
	 */
	private short releaseType;

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

 public short getReleaseType() {
  return releaseType;
 }

 public void setReleaseType(int i) {
  this.releaseType = (short) i;
 }

}
