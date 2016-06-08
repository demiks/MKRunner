package demiks.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class INI {
	private String filePath;
	private Properties ps;
	
	public static INI INSTANCE = new INI(new File("").getAbsolutePath() + File.separatorChar + "MK.INI");

	public INI(String filePath) {
		this.filePath = filePath;
		ps = new Properties();
		InputStream fis = null;
		File file = new File(filePath);
		if (file.exists())
			try {
				fis = new FileInputStream(file);
				ps.load(fis);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
	}

	public boolean save() {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			ps.store(fos, "");
		} catch (Exception e) {
			return false;
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true;
	}

	public void set(String key, Object value) {
		ps.setProperty(key, value == null ? "" : value.toString());
	}
	
	public void set(String key, Collection<?> value) {
		set(key, value.toArray());
	}
	
	public void set(String key, Object[] value) {
		ps.remove(key);
		if (value == null)
			return;
		for (int i = 0; i < value.length; i++) {
			if (StringUtils.isEmpty(value[i]))
				continue;
			String property = ps.getProperty(key);
			if (StringUtils.isEmpty(property))
				ps.setProperty(key, value[i].toString());
			else
				ps.setProperty(key, property + "\\*/" + value[i].toString());
		}
	}

	public void setInt(String key, int value) {
		ps.setProperty(key, String.valueOf(value));
	}

	public void setLong(String key, long value) {
		ps.setProperty(key, String.valueOf(value));
	}

	public void setDouble(String key, double value) {
		ps.setProperty(key, String.valueOf(value));
	}

	public void setBoolean(String key, boolean value) {
		ps.setProperty(key, String.valueOf(value));
	}

	/**
	 * 获取一个配置项
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return ps.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		String _get = get(key);
		return _get == null ? defaultValue : _get;
	}

	public List<String> getStringList(String key) {
		String string = get(key, "");
		if (StringUtils.isEmpty(string))
			return null;
		ArrayList<String> list = new ArrayList<String>();
		for (String str : string.split("\\\\\\*/")) {
			if (!StringUtils.isEmpty(str))
				list.add(str);
		}
		return list;
	}

	/**
	 * 强转为 整数
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return Integer.parseInt(StringUtils.isEmpty(ps.getProperty(key)) ? "0" : ps.getProperty(key));
	}

	public int getInt(String key, int defaultValue) {
		String _get = get(key);
		if (_get == null) {
			return defaultValue;
		}
		return Integer.parseInt(_get);
	}
	public short getShort(String key) {
		return Short.parseShort(StringUtils.isEmpty(ps.getProperty(key)) ? "0" : ps.getProperty(key));
	}
	
	public short getShort(String key, short defaultValue) {
		String _get = get(key);
		if (_get == null) {
			return defaultValue;
		}
		return Short.parseShort(_get);
	}

	/**
	 * 强转为 长整数
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return Long.parseLong(StringUtils.isEmpty(ps.getProperty(key)) ? "0" : ps.getProperty(key));
	}

	public long getLong(String key, long defaultValue) {
		String _get = get(key);
		if (_get == null) {
			return defaultValue;
		}
		return Long.parseLong(_get);
	}

	/**
	 * 强转为 长整数
	 * 
	 * @param key
	 * @return
	 */
	public double getDouble(String key) {
		return Double.parseDouble(StringUtils.isEmpty(ps.getProperty(key)) ? "0" : ps.getProperty(key));
	}

	public double getDouble(String key, double defaultValue) {
		String _get = get(key);
		if (_get == null) {
			return defaultValue;
		}
		return Double.parseDouble(_get);
	}

	public boolean getBoolean(String key) {
		return StringUtils.isEmpty(ps.getProperty(key)) ? false : Boolean.parseBoolean(ps.getProperty(key));
	}
}
