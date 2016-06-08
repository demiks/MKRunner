package demiks.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import demiks.action.Window;
import demiks.util.Encipher;
import demiks.util.INI;
import demiks.util.StringUtils;

public class MKLauncher extends JFrame {
	private static final long serialVersionUID = 6850437143225016692L;
	private static final INI SETING = INI.INSTANCE;
	private final TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("tray.png")));
	private final JTabbedPane main = new JTabbedPane();
	private SettingJFrame setPanel;
	private List<String> fileList;

	/**
	 * 用于与Launcher通信的接口
	 * 
	 * @author Ric-office
	 *
	 */
	static interface LauchStateChangeListner {
		void changeToCloseable(JComponent tab);

		void changeToUnCloseable(JComponent tab);

		void setFilePath(JComponent tab, String filePath);

		Window getWindow(String windowTitle);
	};

	private LauchStateChangeListner change = new LauchStateChangeListner() {
		@Override
		public void setFilePath(JComponent tab, String filePath) {
			if (!StringUtils.isEmpty(filePath)) {
				int index = main.indexOfComponent(tab);
				if ("*New".equals(main.getTitleAt(index))) {
					LabPanel _tab = new LabPanel();
					_tab.setLauchStateChangeListner(this);
					main.addTab("*New", _tab);
					_tab.initFromFilePath(null);
					fileList.add(filePath);
				} else {
					fileList.set(index, filePath);
				}
				main.setTitleAt(index, ViewUtils.cutFilePath(filePath));
				changeToCloseable(tab);
			} else {
				filePath = "*New";
				int index = main.indexOfComponent(tab);
				main.setTitleAt(index, filePath);
				changeToUnCloseable(tab);
			}
		}

		@Override
		public void changeToUnCloseable(JComponent tab) {
			JPanel titlePanel = new JPanel();
			titlePanel.setBackground(new Color(0, 0, 0, 0));
			int index = main.indexOfComponent(tab);
			titlePanel.add(new JLabel(main.getTitleAt(index)));
			main.setTabComponentAt(index, titlePanel);
		}

		@Override
		public void changeToCloseable(JComponent tab) {
			JPanel titlePanel = new JPanel();
			titlePanel.setBackground(new Color(0, 0, 0, 0));
			int index = main.indexOfComponent(tab);
			titlePanel.add(new JLabel(main.getTitleAt(index) + '　'));
			JLabel close = new JLabel("×");
			close.setToolTipText("点击关闭");
			close.setCursor(new Cursor(Cursor.HAND_CURSOR));
			close.addMouseListener(new MouseAdapter() {
		  @Override
		  public void mouseClicked(MouseEvent mouseevent) {
			  super.mouseClicked(mouseevent);
			  fileList.remove(main.indexOfComponent(tab));
			  main.remove(tab);
			  tab.disable();
		  }
	  });
			titlePanel.add(close);
			close.setForeground(new Color(0, 0, 0));
			main.setTabComponentAt(index, titlePanel);
		}

		@Override
		public Window getWindow(String windowTitle) {
			String title = windowTitle.substring(4);
			for (int i = 0; i < main.getTabCount(); i++) {
				if (title.equals(main.getTitleAt(i))) {
					LabPanel component = (LabPanel) main.getComponentAt(i);
					return component.win;
				}
			}
			return null;
		}
	};

	public static void main(String[] args) throws Exception {
		setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MKLauncher helper = new MKLauncher();
		helper.setMinimumSize(new Dimension(550, 500));
		helper.initWinAction();
		helper.initFromINI();
		helper.initMainTab();
		if (inv())
			helper.setContentPane(helper.main);
		else {
//			Browser brow = new Browser();
		}
		helper.setVisible(true);
	}

	private void initWinAction() throws Exception {
		WindowAdapter windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 位置、大小
				SETING.setInt("view-location-x", (int) getLocation().getX());
				SETING.setInt("view-location-y", (int) getLocation().getY());
				SETING.setInt("view-size-width", getWidth());
				SETING.setInt("view-size-height", getHeight());
				// 文件路径
				for (int i = 0; i < main.getTabCount() - 1; i++) {
					Component tab = main.getComponentAt(i);
					tab.disable();
				}
				SETING.set("view-files", fileList);

				// 保存
				SETING.save();
			}
		};
		addWindowListener(windowAdapter);
		if (!SystemTray.isSupported())
		 // 当前平台不支持系统托盘
		 return;
		PopupMenu popupMenu = new PopupMenu();
		MenuItem menuItemSet = new MenuItem("Setting");
		menuItemSet.addActionListener((e) -> {
			if (setPanel == null)
			 setPanel = new SettingJFrame();
			setPanel.setVisible(true);
		});
		popupMenu.add(menuItemSet);
		MenuItem menuItemHelp = new MenuItem("About Me");
		menuItemHelp.addActionListener((e) -> {
			try {
				String macCode = Encipher.getMacCode();
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://mkrunner.duapp.com/help?flow=" + macCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		popupMenu.add(menuItemHelp);
		MenuItem menuItemExit = new MenuItem("Exit");
		menuItemExit.addActionListener((e) -> {
			windowAdapter.windowClosing(null);
			System.exit(0);
		});
		popupMenu.add(menuItemExit);
		icon.setPopupMenu(popupMenu);
		icon.setToolTip("MKLauncher");

		// 托盘
		icon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setExtendedState(NORMAL);
				setVisible(true);
			}
		});
		SystemTray.getSystemTray().add(icon);

		addWindowListener(new WindowAdapter() {
			// 图标化窗口时调用事件
			@Override
			public void windowIconified(WindowEvent e) {
				if (SETING.getBoolean("min-hide"))
				 dispose();
			}
		});
		icon.displayMessage("温馨提示", "右键托盘图标呼出菜单：\n进行个性化设置以及获取帮助信息", MessageType.INFO);
	}

	private void initFromINI() {
		setLocation(SETING.getInt("view-location-x", 500), SETING.getInt("view-location-y", 180));
		setSize(SETING.getInt("view-size-width", 500), SETING.getInt("view-size-height", 618));
	}

	private void initMainTab() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("favicon.png")));
		setTitle("MKLauncher");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		main.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent focusevent) {
				main.transferFocus();
			}
		});
		initTabs();
	}

	private void initTabs() {
		fileList = SETING.getStringList("view-files");
		if (fileList == null) {
			fileList = new ArrayList<>();
			fileList.add(new File("").getAbsolutePath() + File.separatorChar + "demo.mk");
		}
		for (String path : fileList) {
			if (StringUtils.isEmpty(path))
				continue;
			LabPanel tab = new LabPanel();
			tab.setLauchStateChangeListner(change);
			main.addTab(ViewUtils.cutFilePath(path), tab);
			tab.initFromFilePath(path);
			change.changeToCloseable(tab);
		}
		LabPanel _tab = new LabPanel();
		_tab.setLauchStateChangeListner(change);
		_tab.initFromFilePath(null);
		main.addTab("*New", _tab);
	}

	public static boolean inv() {
		String macCode = Encipher.getMacCode();
		try {
			String[] split = Encipher.decode(SETING.get("mk-password")).split("-");
			if (macCode.equals(split[0]) || "QvjvVaRKPcPa".equals(split[0])) {
				long l = Long.parseLong(split[1]) * 1000000000 - System.currentTimeMillis();
				if (l > 0) {
					return true;
				}
			}
		} catch (NumberFormatException e) {
		}
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://mkrunner.duapp.com/access?flow=" + macCode);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

}
