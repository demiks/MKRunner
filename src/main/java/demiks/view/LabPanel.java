package demiks.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import demiks.action.ActionLabel;
import demiks.action.Window;
import demiks.action.check.CycleCheck;
import demiks.action.check.MKCheck;
import demiks.action.check.PhotoCheck;
import demiks.action.check.PointCheck;
import demiks.action.event.MKBeepEvent;
import demiks.action.event.MKEvent;
import demiks.action.event.MKFocusEvent;
import demiks.action.event.MKKeyEvent;
import demiks.action.event.MKMouseEvent;
import demiks.action.event.MKQuoteEvent;
import demiks.util.INI;
import demiks.util.StringUtils;
import demiks.view.MKLauncher.LauchStateChangeListner;
import demiks.view.check.CycleCheckPanel;
import demiks.view.check.PhotoCheckPanel;
import demiks.view.check.PointCheckPanel;
import demiks.view.event.BeepEventPanel;
import demiks.view.event.FocusEventPanel;
import demiks.view.event.KeyEventPanel;
import demiks.view.event.MouseEventPanel;
import demiks.view.event.QuoteEventPanel;

public class LabPanel extends JScrollPane {

	private static final SimpleDateFormat MSG_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final long serialVersionUID = -5675757322037079575L;
	private LauchStateChangeListner change;
	ActionLabel action;
	private final JTextPane msgPanel = new JTextPane();

	Window win;
	{
		msgPanel.setEditable(false);
	}

	private void initAll() {
		LabPanel _this = this;
		getVerticalScrollBar().setUnitIncrement(30);
		setBorder(null);
		JPanel panel = new JPanel();
		panel.setForeground(UIManager.getColor("Button.light"));
		panel.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180), 3, true), action.getFileName(), TitledBorder.LEADING,
		  TitledBorder.TOP, null, new Color(51, 0, 51)));
		setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));

		initNorth(panel);

		initCenter(_this, panel);

		initSouth(panel);
	}

 private void initSouth(JPanel panel) {
  JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(191, 205, 219), 2, true), "\u6307\u4EE4", TitledBorder.CENTER,
		  TitledBorder.TOP, null, new Color(255, 0, 102)));
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 3));

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);

		JButton button_9 = new JButton("清除");
		panel_4.add(button_9);
		button_9.addActionListener((e) -> {
			action.getEvents().clear();
			initAll();
		});

		JButton button_10 = new JButton("录制");
		panel_4.add(button_10);
  button_10.addActionListener((e) -> {
  	msgPanel.grabFocus();
		 if (win == null || !win.hasWindow()) {
		  appendMsg("未绑定窗口");
		  return;
		 }
		 win.focus();
		 win.setTitle(action.getWindowTitle() + " -> 按 F12 结束录制");
		 SwingUtilities.invokeLater(() -> {
		  button_10.setSelected(true);
		 });
		 win.startRecording(123, (list) -> {
		  list.forEach((s) -> {
		  	MKEvent fromStr = MKEvent.getFromStr(s);
		  	action.getEvents().add(fromStr);
					appendEvent(panel_1, fromStr);
		  });
		  panel_1.updateUI();
		  SwingUtilities.invokeLater(() -> {
		   button_10.setSelected(false);
		   win.setTitle(action.getWindowTitle() + " -> " + ViewUtils.cutFilePath(action.getFileName()));
		  });
		 });
		});

		JLabel label_1 = new JLabel("  新增指令：");
		panel_4.add(label_1);

		JButton button_3 = new JButton("鼠标");
		panel_4.add(button_3);
		button_3.addActionListener((e) -> {
			MKMouseEvent event = new MKMouseEvent();
			action.getEvents().add(event);
			MouseEventPanel actionPanel = new MouseEventPanel(event, action.getEvents());
			panel_1.add(actionPanel);
			panel_1.updateUI();
		});

		JButton button_4 = new JButton("按键");
		panel_4.add(button_4);
		button_4.addActionListener((e) -> {
			MKKeyEvent event = new MKKeyEvent();
			action.getEvents().add(event);
			KeyEventPanel actionPanel = new KeyEventPanel(event, action.getEvents());
			panel_1.add(actionPanel);
			panel_1.updateUI();
		});
		
		JButton button_14 = new JButton("焦点");
		panel_4.add(button_14);
		button_14.addActionListener((e) -> {
		 MKFocusEvent event = new MKFocusEvent();
		 action.getEvents().add(event);
		 FocusEventPanel actionPanel = new FocusEventPanel(event, action.getEvents());
		 panel_1.add(actionPanel);
		 panel_1.updateUI();
		});

		JButton button_5 = new JButton("引用 *.mk");
		panel_4.add(button_5);
		button_5.addActionListener((e) -> {
			MKQuoteEvent event = new MKQuoteEvent();
			action.getEvents().add(event);
			QuoteEventPanel actionPanel = new QuoteEventPanel(event, action.getEvents());
			panel_1.add(actionPanel);
			panel_1.updateUI();
		});
		
		JButton button_6 = new JButton("提醒");
		panel_4.add(button_6);
		button_6.addActionListener((e) -> {
			MKBeepEvent event = new MKBeepEvent();
			action.getEvents().add(event);
			BeepEventPanel actionPanel = new BeepEventPanel(event, action.getEvents());
			panel_1.add(actionPanel);
			panel_1.updateUI();
		});

		if (action.getEvents() != null)
			for (MKEvent event : action.getEvents()) {
				appendEvent(panel_1, event);
			}
 }

 private void initCenter(LabPanel _this, JPanel panel) {
  JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(new LineBorder(new Color(191, 205, 219), 2, true), "\u8FD0\u884C\u65E5\u5FD7",
		  TitledBorder.CENTER, TitledBorder.TOP, null, new Color(255, 0, 102)));
		panel.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		panel_5.add(msgPanel, BorderLayout.CENTER);

		JPanel panel_7 = new JPanel();
		panel.add(panel_7, BorderLayout.WEST);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.Y_AXIS));

		JButton button_12 = new JButton("加载");
		panel_7.add(button_12);

		JButton button_8 = new JButton("保存");
		panel_7.add(button_8);

		JButton button_7 = new JButton("执行");
		panel_7.add(button_7);

		JButton button_13 = new JButton("停止");
		panel_7.add(button_13);
		button_13.setEnabled(false);
		
		JButton button_14 = new JButton("隐藏");
		panel_7.add(button_14);
		button_14.addActionListener((e) ->{
			msgPanel.grabFocus();
			if (win == null)
				return;
			if (button_14.isSelected()) {
				win.focus();
				SwingUtilities.invokeLater(() -> {
					button_14.setSelected(false);
				});
			} else {
				win.hide();
				SwingUtilities.invokeLater(() -> {
					button_14.setSelected(true);
				});
			}
			
		});

		button_7.addActionListener((e) -> {
			msgPanel.grabFocus();
			if (win == null || !win.hasWindow()) {
				appendMsg("未绑定窗口,无法执行任务");
				return;
			}
			SwingUtilities.invokeLater(() -> {
			 button_13.setEnabled(true);
			 button_7.setEnabled(false);
		 });
			action.doLabel(win, (msg, isEnd) -> {
				if (isEnd)
					SwingUtilities.invokeLater(() -> {
						button_13.setEnabled(false);
						button_7.setEnabled(true);
					});
			 appendMsg(msg);
		 });
		});

		button_13.addActionListener((e) -> {
			msgPanel.grabFocus();
			action.stopLabel();
		});

		button_8.addActionListener((e) -> {
			ViewUtils.WhenSelectFile selectFile = new ViewUtils.WhenSelectFile() {
			 @Override
			 public void openFilePath(String path) {
				 action.setFileName(path);
				 try {
					 action.saveToFile();
				 } catch (Exception e) {
					 appendMsg(e.getMessage());
				 }
				 if (change != null)
					 change.setFilePath(_this, action.getFileName());
			 }
		 };
			if (StringUtils.isEmpty(action.getFileName())) {
				ViewUtils.openFile(button_8, selectFile);
			} else {
				selectFile.openFilePath(action.getFileName());
			}
		});
		button_12.addActionListener((e) -> {
			msgPanel.grabFocus();
			ViewUtils.openFile(button_12, (path) -> {
			 action.setFileName(path);
			 try {
				 action.loadFromFile();
			 } catch (Exception e1) {
				 appendMsg(e1.getMessage());
			 }
			 disable();
			 initAll();
			 if (change != null)
				 change.setFilePath(_this, action.getFileName());
		 });
		});
 }

 private void initNorth(JPanel panel) {
  JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(191, 205, 219), 2, true), "\u6761\u4EF6", TitledBorder.CENTER,
		  TitledBorder.TOP, null, new Color(255, 0, 102)));
		panel.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new GridLayout(0, 1, 0, 2));

		JPanel panel_6 = new JPanel();
		panel_2.add(panel_6);

		JLabel label_2 = new JLabel("窗口：");
		panel_6.add(label_2);

		JTextField windowTitle = new JTextField();
		windowTitle.setColumns(40);
		panel_6.add(windowTitle);
		windowTitle.setText(action.getWindowTitle());

		JButton button_11 = new JButton("绑定");
		panel_6.add(button_11);
		button_11.addActionListener((e) -> {
			msgPanel.grabFocus();
			String str = windowTitle.getText();
			action.setWindowTitle(str);
			if (button_11.isSelected()) {
				if (win != null && win.hasWindow()) {
					win.stopRecording();
					win.show();
					win.setTitle(action.getWindowTitle());
					win.flashWindow(300);
					win = null;
					appendMsg("已解绑窗口");
				}
			} else {
				Window _win = win;
				win = new Window(str, null);
				if (win == null || !win.hasWindow()) {
					win = _win;
					appendMsg("未发现窗口,绑定失败");
					return;
				}
				String cutFilePath = ViewUtils.cutFilePath(action.getFileName());
				if (!StringUtils.isEmpty(cutFilePath))
				 win.setTitle(action.getWindowTitle() + " -> " + cutFilePath);
				win.flashWindow(600);
				appendMsg("已成功绑定窗口");
			}
			button_11.setSelected(!button_11.isSelected());
		});

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3);

		JLabel label = new JLabel("  新增条件：");
		panel_3.add(label);

		JButton button = new JButton("次数");
		panel_3.add(button);
		button.addActionListener((e) -> {
			CycleCheck check = new CycleCheck();
			action.getChecks().add(0, check);
			CycleCheckPanel loop = new CycleCheckPanel(check, action.getChecks());
			panel_2.add(loop);
			panel_2.updateUI();
		});

		JButton button_2 = new JButton("图片");
		panel_3.add(button_2);
		button_2.addActionListener((e) -> {
			PhotoCheck check = new PhotoCheck();
			action.getChecks().add(check);
			PhotoCheckPanel loop = new PhotoCheckPanel(check, action.getChecks());
			panel_2.add(loop);
			panel_2.updateUI();
		});

		if (action.getChecks() != null)
			for (MKCheck check : action.getChecks()) {
				appendCheck(panel_2, check);
			}
 }

 private void appendCheck(JPanel panel_2, MKCheck check) {
  if (check instanceof CycleCheck) {
  	CycleCheckPanel loop = new CycleCheckPanel((CycleCheck) check, action.getChecks());
  	panel_2.add(loop);
  } else if (check instanceof PointCheck) {
  	PointCheckPanel loop = new PointCheckPanel((PointCheck) check, action.getChecks());
  	panel_2.add(loop);
  } else if (check instanceof PhotoCheck) {
  	PhotoCheckPanel loop = new PhotoCheckPanel((PhotoCheck) check, action.getChecks());
  	panel_2.add(loop);
  }
 }

	private void appendEvent(JPanel panel_1, MKEvent event) {
		if (event instanceof MKMouseEvent) {
			MouseEventPanel actionPanel = new MouseEventPanel((MKMouseEvent) event, action.getEvents());
			panel_1.add(actionPanel);
		} else if (event instanceof MKKeyEvent) {
			KeyEventPanel actionPanel1 = new KeyEventPanel((MKKeyEvent) event, action.getEvents());
			panel_1.add(actionPanel1);
		} else if (event instanceof MKQuoteEvent) {
			QuoteEventPanel actionPanel2 = new QuoteEventPanel((MKQuoteEvent) event, action.getEvents());
			panel_1.add(actionPanel2);
		} else if (event instanceof MKBeepEvent) {
			BeepEventPanel actionPanel3 = new BeepEventPanel((MKBeepEvent) event, action.getEvents());
			panel_1.add(actionPanel3);
		} else if (event instanceof MKFocusEvent) {
		 FocusEventPanel actionPanel3 = new FocusEventPanel((MKFocusEvent) event, action.getEvents());
		 panel_1.add(actionPanel3);
		}
	}

	public void setLauchStateChangeListner(LauchStateChangeListner change) {
		this.change = change;
	}

	public void initFromFilePath(String path) {
		action = new ActionLabel();
		action.setFileName(path);
		try {
			action.loadFromFile();
		} catch (Exception e) {
			appendMsg(e.getMessage());
		}
		initAll();
	}

	@Override
	public void disable() {
		if (win != null && win.hasWindow()) {
			win.stopRecording();
			win.show();
			// 还原标题
			win.setTitle(action.getWindowTitle());
		}
		// 停止播放
		action.stopLabel();
	}

	public void appendMsg(String msg) {
		Date date = new Date();
		String _msg = MSG_FORMAT.format(date) + '　' + msg;
		SwingUtilities.invokeLater(() -> {
			try {
				synchronized (msgPanel) {
					String text = msgPanel.getText();
					if (text.length() == 0) {
						msgPanel.setText(_msg);
					} else {
						// 判断有多少行
						int lines = text.split(".+").length;
						if (lines >= INI.INSTANCE.getInt("msg-lines", 3)) {
							msgPanel.setText(text.substring(text.indexOf('\n') + 1) + '\n' + _msg);
						} else
							msgPanel.setText(text + '\n' + _msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}