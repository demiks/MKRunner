package demiks.action.check;

import demiks.action.RGB;
import demiks.action.Window;
import demiks.action.XY;

public class PointCheck extends MKCheck {
	private RGB rgb = new RGB(-1);
	private XY xy = new XY(0, 0);

	@Override
	public boolean doCheck(Window win) {
		int colorAt = win.getColorAt(xy.getX(), xy.getY());
		if (colorAt == -1)
			return false;
		switch (getCheckType()) {
		case 0:
			return rgb.equals(new RGB(colorAt));
		case 1:
			return !rgb.equals(new RGB(colorAt));
		default:
			return false;
		}
	}

	@Override
	public String toString() {
		return MKCheck.Type.PointCheck.name() + "\\*/" + getCheckType() + "\\*/" + rgb + "\\*/" + xy;
	}

	public RGB getRGB() {
		return rgb;
	}

	public void setRGB(RGB rgb) {
		this.rgb = rgb;
	}

	public XY getXy() {
		return xy;
	}

	public void setXy(XY xy) {
		this.xy = xy;
	}

}
