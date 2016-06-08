package demiks.util;

public class StringUtils {
 public static int[] getInts(String str) {
 	String[] strings = str.split(",");
 	int[] result = new int[strings.length];
 	for (int i = 0; i < strings.length; i++) {
			result[i] = Integer.parseInt(strings[i]);
		}
 	return result;
 }

 /**
  * 判断是否以指定前缀开始且不等于该前缀。
  * @param prefix
  * @param str
  * @return
  */
 public static boolean startsWithAndNotEquals(String prefix, String str) {
 	return str != null && str.startsWith(prefix) && str.length() > prefix.length();
 }

 /**
  * 判断字符串是否为空
  * @param str
  * @return
  */
	public static boolean isEmpty(Object str) {
		return str == null || "".equals(str);
	}
}
