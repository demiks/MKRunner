package cn.ric.action.event;

import java.awt.image.BufferedImage;

import cn.ric.action.RGB;
import cn.ric.action.Window;
import cn.ric.action.XY;
import cn.ric.util.ImageUtils;
import cn.ric.util.StringUtils;

public class MKMouseEvent extends MKEvent {
 public static enum Type {
  RightButton, LeftButton
 }

 private Type type = Type.LeftButton;
 private String picPath;
 private RGB[][] colors;
 private XY xy = new XY(0, 0);
 private XY position;

 @Override
 public String toString() {
  if (picPath == null)
   picPath = "";
  return MKEvent.Type.Mouse.name() + "\\*/" + getReleaseType() + "\\*/" + sleepTime + "\\*/" + type.name() + "\\*/" + xy + "\\*/"
    + picPath;
 }

 @Override
 public void doEvent(Window win) throws Exception {
  super.doEvent(win);
  // init xy
  int x = xy.getX();
  int y = xy.getY();
  if (colors != null) {
   if (position == null) {
    BufferedImage screenshot = win.getScreenshot(null);
    if (screenshot == null)
     throw new Exception("截屏失败，无法点击。");
    RGB[][] screen = ImageUtils.readRGBFromImage(screenshot);
    setPosition(ImageUtils.findPosition(colors, screen));
   }
   if (position == null)
    throw new Exception("截屏成功但没有找到图片，无法点击:\n  " + picPath);
   x += position.getX();
   y += position.getY();
   position = null;
  }
  switch (type.name()) {
  case "RightButton":
   if (getReleaseType() == 1)
    win.clickUpR(x, y);
   else if (getReleaseType() == 2)
    win.clickDownR(x, y);
   else
    win.clickR(x, y);
   break;
  case "LeftButton":
   if (getReleaseType() == 1)
    win.clickUp(x, y);
   else if (getReleaseType() == 2)
    win.clickDown(x, y);
   else
    win.click(x, y);
   break;
  default:
   System.err.println("不支持的鼠标事件类型：" + this.toString());
  }
 }

 public Type getType() {
  return type;
 }

 public void setType(Type type) {
  this.type = type;
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

 public void setPosition(XY position) {
  this.position = position;
 }
}
