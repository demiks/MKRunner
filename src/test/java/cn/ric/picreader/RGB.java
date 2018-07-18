package cn.ric.picreader;

public class RGB {
	private int r;
	private int g;
	private int b;
	
	public static int floatR = 40;
	public static int floatG = 40;
	public static int floatB = 40;

	public RGB(int value) {
		r = (value & 0xff0000) >> 16;
		g = (value & 0xff00) >> 8;
		b = (value & 0xff);
	}

	@Override
	public String toString() {
		return "RGB [R=" + r + ", G=" + g + ", B=" + b + "]";
	}

	@Override
 public boolean equals(Object obj) {
	 if (this == obj)
		 return true;
	 if (obj == null)
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

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
}
