package cn.ric.action.check;

import cn.ric.action.RGB;
import cn.ric.action.Window;
import cn.ric.action.XY;

public abstract class MKCheck {
	public static enum Type {
		/**
		 * 点色匹配
		 */
		PointCheck, PhotoCheck, CycleCheck
	}

	public static MKCheck getFromStr(String str) {
		String[] split = str.split("\\\\\\*/");
		MKCheck check = null;
		switch (split[0]) {
		case "PointCheck":
			PointCheck pointCheck = new PointCheck();
			pointCheck.setRGB(new RGB(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4])));
			pointCheck.setXy(new XY(Integer.parseInt(split[5]), Integer.parseInt(split[6])));
			check = pointCheck;
			break;
		case "PhotoCheck":
			PhotoCheck photoCheck = new PhotoCheck();
			photoCheck.setPicPath(split[2]);
			if (split.length > 4) {
				photoCheck.setXY(new XY(Integer.parseInt(split[3]), Integer.parseInt(split[4])));
			}
			check = photoCheck;
			break;
		case "CycleCheck":
			CycleCheck cycleCheck = new CycleCheck();
			cycleCheck.setTimes(Integer.parseInt(split[2]));
			check = cycleCheck;
			break;
		default:
			break;
		}
		check.setCheckType(Integer.parseInt(split[1]));
		return check;
	}

	/**
	 * 检查
	 * @param win
	 * @return
	 */
	public abstract boolean doCheck(Window win) throws Exception;

	protected int checkType;

	public int getCheckType() {
		return checkType;
	}

	public void setCheckType(int neither) {
		this.checkType = neither;
	}

}
