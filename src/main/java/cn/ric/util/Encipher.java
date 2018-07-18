package cn.ric.util;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class Encipher {

	public static void main(String[] args) {
		String she = "QvjvVaRKPcPa" + "-" + (System.currentTimeMillis() / 1000000000 + 5);
		System.out.println("原始：" + she);
		String mi = Encipher.encode(she);
		System.out.println("密文：" + mi);
		System.out.println("解密：" + Encipher.decode(mi));
	}

	/**
	 * 密钥
	 */
	private static String sc = "PxJcKvLaMsNfdBqHwUeYrVpCiXuGyTtRgEhWbQnAmFjSkD";

	/**
	 * 加密字符串
	 * 
	 * @param value
	 *         需加密的字符
	 * @return
	 */
	public static String encode(String value) {
		if (value.length() < 6) {
			value = "ZMmk" + value;
		}
		value = toMesy(value);
		double d = Math.min(value.length() * 0.5, value.length() - 1);
		int s = (int) (Math.ceil(Math.random() * d));
		int l = (int) (Math.ceil(Math.random() * d));
		return (value.substring(s + l) + s + value.substring(0, s) + l + value.substring(s, s + l));
	}

	private static String toMesy(String value) {
		StringBuilder sb = new StringBuilder();
		for (char c : value.toCharArray()) {
			sb.append(enc(c + 10));
		}
		return sb.toString();
	}

	public static String getMacCode() {
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if (mac == null)
					continue;
				StringBuilder builder = new StringBuilder();
				for (byte b : mac) {
					builder.append(Encipher.enc(b + 221));
				}
				return builder.toString();

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	private static String enc(int i) {
		String k;
		if (i < sc.length()) {
			k = String.valueOf(sc.charAt(i));
		} else {
			k = sc.charAt(i % sc.length()) + enc((int) Math.floor(i / sc.length()));
		}
		return k;
	}

	private static int dec(String k) {
		if (k == null) {
			return 0;
		}
		char value = 0;
		for (int i = 0; i < k.length(); i++) {
			String _k = String.valueOf(k.charAt(i));
			int l = sc.indexOf(_k);
			value += Math.pow(sc.length(), i) * l;
		}
		return value;
	}

	public static String decode(String miy) {
		if (miy == null || miy.isEmpty())
			return "";
		int s = Integer.parseInt(miy.replaceAll("^.*?(\\d+).*$", "$1"));
		miy = miy.replaceFirst("\\d+", "");
		int l = Integer.parseInt(miy.replaceAll("^.*?(\\d+).*$", "$1"));
		String str = miy.replaceFirst("\\d+", "");
		int i = str.length() - s - l;
		String result = unMesy(str.substring(i, i + s) + str.substring(str.length() - l) + str.substring(0, i));
		return result.length() < 6 ? result.substring(9) : result;
	}

	private static String unMesy(String miy) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < miy.length(); i += 2) {
			String s1 = miy.substring(i, i + 2);
			char s2 = (char) (dec(s1) - 10);
			sb.append(s2);
		}
		return sb.toString();
	}

}
