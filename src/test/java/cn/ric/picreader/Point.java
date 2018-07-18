package cn.ric.picreader;

public class Point {
	private int count;
	private int value;
 private RGB rgb;

	@Override
 public String toString() {
	 return "Point [count=" + count + ", value=" + value + ", rgb=" + rgb + "]";
 }

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public RGB getRgb() {
		return rgb;
	}

	public void setRgb(RGB rgb) {
		this.rgb = rgb;
	}

}
