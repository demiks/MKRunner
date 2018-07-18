package cn.ric.action;

public class RGB {
	private int r;
	private int g;
	private int b;
	private int value;

	public static int floatR = 80;
	public static int floatG = 80;
	public static int floatB = 80;

	public RGB(int value) {
		this.value = value;
		r = (value & 0xff0000) >> 16;
		g = (value & 0xff00) >> 8;
		b = (value & 0xff);
	}

	public RGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toHex() {
		return '#' + Integer.toHexString(value).toUpperCase();
	}

	@Override
	public String toString() {
		return r + "\\*/" + g + "\\*/" + b;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RGB other = (RGB) obj;
		if (Math.abs(b - other.b) > floatB)
			return false;
		if (Math.abs(g - other.g) > floatG)
			return false;
		if (Math.abs(r - other.r) > floatR)
			return false;
		return true;
	}
}
