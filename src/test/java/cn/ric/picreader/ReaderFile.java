package cn.ric.picreader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ReaderFile {
	public static void main(String[] args) throws IOException {
		BufferedImage bi = ImageIO.read(new File("C:/Users/zhangming/Documents/mk/鬼/child2.png"));
		int width = bi.getWidth();
		int height = bi.getHeight();
		HashMap<RGB, Point> map = new HashMap<RGB, Point>();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				RGB rgb = new RGB(bi.getRGB(j, i));
				Point point;
				if (map.containsKey(rgb)) {
					point = map.get(rgb);
					point.setCount(point.getCount() + 1);
				} else {
					point = new Point();
					point.setCount(1);
					point.setRgb(rgb);
					map.put(rgb, point);
				}
			}
		}
		Point[] array = map.values().toArray(new Point[0]);
		Arrays.sort(array, (arg1, arg2) -> {
			return arg2.getCount() - arg1.getCount();
		});
		for (Point point : array) {
			System.out.println(point);
		}

		Pic pic = new Pic();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				RGB rgb = new RGB(bi.getRGB(j, i));
				if (array[1].equals(map.get(rgb))) {
					pic.draw(i, j);
				}
			}
		}
		pic.trim().print();
		System.out.println("\n\n----");
		pic.print();

	} // end of main

}
