package cn.ric.action.check;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import cn.ric.action.RGB;
import cn.ric.action.Window;
import cn.ric.action.XY;
import cn.ric.util.ImageUtils;
import cn.ric.util.StringUtils;

public class PhotoCheck extends MKCheck {
	private String picPath;
	private RGB[][] colors;
	private XY xy = new XY(0, 0);
	private XY position;

	@Override
	public String toString() {
		return MKCheck.Type.PhotoCheck.name() + "\\*/" + getCheckType() + "\\*/" + picPath + "\\*/" + xy;
	}

	@Override
	public boolean doCheck(Window win) throws Exception {
		Rectangle bounds = null;
		// 截图
		switch (getCheckType()) {
		case 1:
			bounds = new Rectangle(xy.getX(), xy.getY(), colors[0].length, colors.length);
			break;
		case 3:
			bounds = new Rectangle(xy.getX(), xy.getY(), colors[0].length, colors.length);
			break;
		}
		BufferedImage screenshot = win.getScreenshot(bounds);
		if (screenshot == null)
			throw new Exception("截屏失败");
		RGB[][] screen = ImageUtils.readRGBFromImage(screenshot);
		position = ImageUtils.findPosition(colors, screen);
		switch (getCheckType()) {
		case 0:
			return getPosition() != null;
		case 1:
			return getPosition() != null;
		case 2:
			return getPosition() == null;
		case 3:
			return getPosition() == null;
		default:
			return false;
		}
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
		if (!StringUtils.isEmpty(picPath))
			this.colors = ImageUtils.readRGBFromFile(picPath);
		else
			this.colors = null;
	}

	public XY getXY() {
		return xy;
	}

	public void setXY(XY xy) {
		this.xy = xy;
	}

	public XY getPosition() {
		return position;
	}

}
