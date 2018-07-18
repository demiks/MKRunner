package cn.ric.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cn.ric.util.StringUtils;

public class ViewUtils {
	public static interface WhenSetNumberTextField {
		void setNumber(int i, String text);
	}

	public static String cutFilePath(String filePath) {
		if (StringUtils.isEmpty(filePath) || !filePath.endsWith(".mk"))
			return filePath;
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length() - 3);
	}

	public static void initNumberTextField(final JTextField textField, WhenSetNumberTextField when) {
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent focusevent) {
				String text = textField.getText();
				if (StringUtils.isEmpty(text))
					textField.setText("0");
				if (text.length() != 1)
 				text = text.replaceAll("^([-+]?)\\D*(\\d*)\\D*$", "$1$2");
				if (text.length() > textField.getColumns()) {
					text = text.substring(0, textField.getColumns());
				}
				textField.setText(text);
				if (text.matches("^[-+]?\\d+$"))
				 when.setNumber(Integer.parseInt(text, 10), text);
				else
					when.setNumber(0, text);
			}
		});
	}

	public static interface WhenSelectFile {
		void openFilePath(String path);
	}

	public static void initPicLabel(final JLabel btnNewButton, WhenSelectFile when) {
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseevent) {
				super.mouseClicked(mouseevent);
				openFile(btnNewButton, (path) -> {
			  ImageIcon image = new ImageIcon(path);
			  image.setImage(image.getImage().getScaledInstance(40, 25, Image.SCALE_DEFAULT));
			  btnNewButton.setIcon(image);
			  when.openFilePath(path);
		  });
			}
		});
	}

	/**
	 * 打开文件对话框
	 * 
	 * @param when
	 */
	public static void openFile(Component com, WhenSelectFile when) {
		final JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setCurrentDirectory(new File(""));
		jFileChooser.addActionListener((e) -> {
			if (jFileChooser.getSelectedFile() == null) {
				return;
			}
			String absolutePath = jFileChooser.getSelectedFile().getAbsolutePath();
			when.openFilePath(absolutePath);
		});
		Component c = com;
		while (c != null && !(c instanceof JFrame))
			c = c.getParent();
		jFileChooser.showDialog(c, "请选择文件");
	}
	
	/**
	 * 打开目录对话框
	 * 
	 * @param when
	 */
	public static void openPath(Component com, WhenSelectFile when) {
		final JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setCurrentDirectory(new File(""));
		jFileChooser.addActionListener((e) -> {
			if (jFileChooser.getSelectedFile() == null) {
				return;
			}
			String absolutePath = jFileChooser.getSelectedFile().getAbsolutePath();
			when.openFilePath(absolutePath);
		});
		Component c = com;
		while (c != null && !(c instanceof JFrame)) {
			c = c.getParent();
		}
		jFileChooser.showDialog(c, "请选择路径");
	}
}
