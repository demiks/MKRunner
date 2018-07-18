package cn.ric.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import cn.ric.action.RGB;
import cn.ric.action.XY;

public class ImageUtils {

 public static void main(String[] args) {
  RGB[][] parent = readRGBFromFile("C:/Users/zhangming/Documents/mk/鬼/parent.png");
  RGB[][] child = readRGBFromFile("C:/Users/zhangming/Documents/mk/鬼/child.png");
  System.out.println(compare(child, parent));
  // child = new RGB[10][10];
//  getMainRgb(child);
  System.out.println(findPosition(child, parent)); // 260,180
 }

 /**
  * 去掉杂质,只保留主要的数个颜色值,比较时将更容易被匹配上
  * 
  * @param rgbs
  * @return
  */
 public static void getMainRgb(RGB[][] rgbs) {
  int first = 0, second = 0, thrid = 0;
  RGB firstRGB = null, secondRGB = null, thridRGB = null;
  HashMap<RGB, Integer> map = new HashMap<RGB, Integer>();
  // 统计颜色出现的情况
  for (int i = 0; i < rgbs.length; i++) {
   for (int j = 0; j < rgbs[i].length; j++) {
    if (map.containsKey(rgbs[i][j]))
     map.put(rgbs[i][j], map.get(rgbs[i][j]) + 1);
    else
     map.put(rgbs[i][j], 1);
   }
  }
  // 颜色个数少于11,不进行过滤
  if (map.size() < 11)
   return;
  // 过滤掉某些颜色(只保留出现率居第二第三的颜色)
  for (Entry<RGB, Integer> ent : map.entrySet()) {
   if (ent.getValue() > first) {
    thrid = second;
    thridRGB = secondRGB;
    second = first;
    secondRGB = firstRGB;
    first = ent.getValue();
    firstRGB = ent.getKey();
   } else if (ent.getValue() > second) {
    thrid = second;
    thridRGB = secondRGB;
    second = ent.getValue();
    secondRGB = ent.getKey();
   } else if (ent.getValue() > thrid) {
    thrid = ent.getValue();
    thridRGB = ent.getKey();
   }
  }
  
  for (int i = 0; i < rgbs.length; i++) {
   for (int j = 0; j < rgbs[i].length; j++) {
    if (rgbs[i][j] != null && (!rgbs[i][j].equals(thridRGB) || !rgbs[i][j].equals(secondRGB))) {
     rgbs[i][j] = null;
    }
   }
  }
 }

 /**
  * 从文件读取RGB数组
  * 
  * @param filePath
  * @return
  */
 public static RGB[][] readRGBFromFile(String filePath) {
  BufferedImage bi = null;
  try {
   bi = ImageIO.read(new File(filePath));
  } catch (IOException e) {
   throw new RuntimeException(e.getMessage());
  }
  RGB[][] result = readRGBFromImage(bi);
  return result;
 }

 /**
  * 从图片中读取RGB数组
  * 
  * @param bi
  * @return
  */
 public static RGB[][] readRGBFromImage(BufferedImage bi) {
  RGB[][] result = new RGB[bi.getWidth()][bi.getHeight()];
  for (int i = 0; i < result.length; i++) {
   for (int j = 0; j < result[i].length; j++) {
    result[i][j] = new RGB(bi.getRGB(i, j));
   }
  }
  return result;
 }

 /**
  * 找到 child 在 parent 中的定位
  * 
  * @param child
  * @param parent
  * @return 坐标
  */
 public static XY findPosition(RGB[][] child, RGB[][] parent) {
  if (child == null || child.length == 0)
   return null;
  if (parent == null || parent.length < child.length)
   return null;
  XY xy = new XY(0, 0);
  for (int i = 0; i < parent.length - child.length; i++) {
   RGB[] childLine = child[i - xy.getX()];
   xy.setX(i);
   if (childLine == null) {
    continue;
   }
   for (int j = 0; j < parent[i].length - childLine.length; j++) {
    xy.setY(j);
    if (compare(child, parent, xy))
     return xy;
   }
  }
  return null;
 }

 public static boolean compare(RGB[][] child, RGB[][] parent) {
  return compare(child, parent, new XY(0, 0));
 }

 /**
  * 比较两个区域的 RGB 是否匹配
  * 
  * @param child
  * @param parent
  * @return
  */
 public static boolean compare(RGB[][] child, RGB[][] parent, XY start) {
  if (child == parent)
   return true;
  if (child == null || parent == null)
   return false;
  if (child.length > parent.length - start.getX())
   return false;
  // 模糊性: 允许不一致的点的个数
  int runs = child.length * child[0].length / 20;
  for (int i = 0; i < child.length; i++) {
   if (child[i] == null)
    continue;
   if (child[i] == parent[i + start.getX()])
    continue;
   for (int j = 0; j < child[i].length; j++) {
    RGB childRGB = child[i][j];
    RGB parntRGB = parent[i + start.getX()][j + start.getY()];
    if (childRGB == null)
     continue;
    if (childRGB == parntRGB)
     continue;
    if (!childRGB.equals(parntRGB) && runs-- < 0)
     return false;
   }
  }
  return true;
 }
}
