package demilks.test;

public class Test {
 public static void main(String[] args) throws Exception {
 	byte[] bytes = new String("…Ë".getBytes("utf-8")).getBytes("gbk");
 	for (byte b : bytes) {
			System.out.println(b);
		}
 }

}
