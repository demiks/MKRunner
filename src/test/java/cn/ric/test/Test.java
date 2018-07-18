package cn.ric.test;

public class Test {
 public static void main(String[] args) throws Exception {
 	byte[] bytes = new String("��".getBytes("utf-8")).getBytes("gbk");
 	for (byte b : bytes) {
			System.out.println(b);
		}
 }

}
