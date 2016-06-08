package demiks.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class CutJar {

	private static HashMap<String, Set<String>> classes = new HashMap<String, Set<String>>();

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("/mouse-key-helper/out.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("[Loaded "))
				continue;
			String[] split = line.substring(8, line.length() - 1).split(" from (file:)?");
			markClass(split[0] + ".class", split[1]);
		}
		reader.close();
		classes.remove("/D:/mouse-key-helper/MK.DLL");
		for (String jarPath : classes.keySet()) {
			copyClass(jarPath, "/mk/");
		}
		System.out.println("all done!");
	}

	private static void markClass(String fullClassName, String jarFilePath) {
		Set<String> set;
		if (classes.containsKey(jarFilePath))
			set = classes.get(jarFilePath);
		else {
			set = new HashSet<String>();
			classes.put(jarFilePath, set);
		}
		// classname 转为filename
		set.add(fullClassName.replaceAll("\\.(?![a-zA-Z]+$)", "/")); // 正则后行零宽断言，排除.class的匹配
	}

	private static void copyClass(String jarFilePath, String path) {
		if (!new File(jarFilePath).canRead()) {
			System.out.println("can not read : " + jarFilePath);
			return;
		}
		JarInputStream stream = null;
		InputStream input = null;
		OutputStream output = null;
		byte[] buffed = new byte[1024];
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarFilePath);
			stream = new JarInputStream(new FileInputStream(jarFilePath));
			JarEntry jarEntry;
			Set<String> set = classes.get(jarFilePath);
			while (set != null && (jarEntry = stream.getNextJarEntry()) != null) {
				String name = jarEntry.getName();
				if (name.endsWith(".class") && !set.contains(name)) {
					continue;
				}
				// cteateFilePath
				File file = new File(path + name);
				if (jarEntry.isDirectory()) {
					file.mkdirs();
					continue;
				}
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				input = jarFile.getInputStream(jarEntry);
				output = new FileOutputStream(file);
				int readLength;
				while ((readLength = input.read(buffed)) > 0) {
					output.write(buffed, 0, readLength);
				}
				output.flush();
			}
			jarFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(jarFilePath);
			e.printStackTrace();
		} finally {
			releaseResource(stream, input, output, jarFile);
		}
		System.out.println("copy done: " + jarFilePath);
	}

	private static void releaseResource(JarInputStream stream, InputStream input, OutputStream output, JarFile jarFile) {
		if (jarFile != null) {
			try {
				jarFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (input != null)
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (output != null)
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
