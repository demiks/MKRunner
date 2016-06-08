package demiks.action.event;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import demiks.action.Window;

public class MKBeepEvent extends MKEvent {

	private String savePath;
	private long time;

	@Override
	public void doEvent(Window win) throws Exception {
		if (getTime() > 0)
		 win.flashWindow(getTime());
		ImageIO.write(win.getScreenshot(null), "jpg", new File(savePath + new SimpleDateFormat("MM-dd HH.mm.ss'.jpg'").format(new Date())));
	}

	@Override
	public String toString() {
		return "Beep\\*/0\\*/0\\*/" + getTime();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
		File file = new File(savePath);
		if (!file.exists())
			file.mkdirs();
	}

}
