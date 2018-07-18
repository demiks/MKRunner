package cn.ric.action.event;

import cn.ric.action.Window;

public class MKFocusEvent extends MKEvent {

	private long time;

	@Override
	public void doEvent(Window win) throws Exception {
		if (getTime() > 0) {
		 win.autoHide(time);
		 win.focus();
		}
	}

	@Override
	public String toString() {
		return "Focus\\*/0\\*/0\\*/" + getTime();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
