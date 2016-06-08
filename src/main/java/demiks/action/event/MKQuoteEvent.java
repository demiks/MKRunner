package demiks.action.event;

import demiks.action.ActionLabel;
import demiks.action.Window;
import demiks.action.check.MKCheck;

public class MKQuoteEvent extends MKEvent {

	private String mkPath;
	private ActionLabel label;

	@Override
	public void doEvent(Window win) throws Exception {
		super.doEvent(win);
		boolean pass = true;
		for (MKCheck check : label.getChecks()) {
			pass = pass && check.doCheck(win);
		}
		if (pass)
			for (MKEvent event : label.getEvents()) {
				event.doEvent(win);
			}
	}

	@Override
	public String toString() {
		return "Quote\\*/0\\*/" + getSleepTime() + "\\*/" + getMkPath();
	}
	
	public void reload() throws Exception {
		if (label != null)
			label.loadFromFile();
	}

	public String getMkPath() {
		return mkPath;
	}

	public void setMkPath(String mkPath) {
		this.mkPath = mkPath;
		label = new ActionLabel();
		label.setFileName(mkPath);
	}

}
