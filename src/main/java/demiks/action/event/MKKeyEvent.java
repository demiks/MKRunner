package demiks.action.event;

import demiks.action.Window;

public class MKKeyEvent extends MKEvent {
	private int keyValue;

	@Override
	public String toString() {
		return MKEvent.Type.Key.name() + "\\*/" + getReleaseType() + "\\*/" + getSleepTime() + "\\*/" + getKeyValue();
	}

	@Override
	public void doEvent(Window win) throws Exception {
		super.doEvent(win);
		if (getReleaseType() == 1)
			win.keyUp(getKeyValue());
		else if (getReleaseType() == 2)
			win.keyDown(getKeyValue());
		else
		 win.typeKey(getKeyValue());
	}

	public int getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

}
