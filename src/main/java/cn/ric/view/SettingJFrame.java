package cn.ric.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import cn.ric.action.Window;
import cn.ric.util.INI;

public class SettingJFrame extends JFrame {
 private static final long serialVersionUID = 6689041682824480876L;
 private JTextField textField;
 private JTextField textField_2;
 private JTextField textField_3;
 private JLabel textField_4;
 private JTextField textField_1;

 public SettingJFrame() {
  addWindowListener(new WindowAdapter() {
   @Override
   public void windowClosing(WindowEvent e) {
    super.windowClosed(e);
    getFocusOwner().transferFocus();
    INI.INSTANCE.save();
   }
  });
  setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
  setTitle("MKLuncher 设置");
  setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("favicon.png")));
  setType(Type.UTILITY);
  setLocation(INI.INSTANCE.getInt("view-location-x", 500), INI.INSTANCE.getInt("view-location-y", 180) + 30);
  setSize(500, 430);
  setResizable(false);
  setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
  getContentPane().setLayout(new BorderLayout(0, 0));

  JScrollPane scrollPane = new JScrollPane();
  getContentPane().add(scrollPane, BorderLayout.CENTER);

  JPanel panel = new JPanel();
  scrollPane.setViewportView(panel);
  panel.setLayout(null);

  JPanel panel_1 = new JPanel();
  panel_1.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128), 2, true), "\u6307\u4EE4", TitledBorder.CENTER,
    TitledBorder.TOP, null, new Color(0, 0, 0)));
  panel_1.setBounds(26, 81, 436, 231);
  panel.add(panel_1);
  panel_1.setLayout(null);

  JLabel lblNewLabel = new JLabel("截图保存路径");
  lblNewLabel.setBounds(32, 30, 84, 15);
  lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
  panel_1.add(lblNewLabel);

  textField = new JTextField();
  lblNewLabel.setLabelFor(textField);
  textField.setBounds(117, 27, 276, 21);
  textField.setHorizontalAlignment(SwingConstants.LEFT);
  panel_1.add(textField);
  textField.setColumns(10);
  textField.setText(INI.INSTANCE.get("screen-path", "/MKRunner/"));
  textField.addFocusListener(new FocusAdapter() {
   @Override
   public void focusLost(FocusEvent e) {
    INI.INSTANCE.set("screen-path", textField.getText().trim());
   }
  });
  textField.addMouseListener(new MouseAdapter() {
   @Override
   public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
     ViewUtils.openPath(textField, (path) -> {
      textField.setText(path);
      INI.INSTANCE.set("screen-path", path);
     });
    }
   }
  });

  JLabel label_1 = new JLabel("滞留时间 ?");
  label_1.setBounds(41, 106, 66, 18);
  panel_1.add(label_1);
  label_1.setToolTipText("<html>窗口被程序从最小化状态恢复过来时，滞留一段时间后再将目标窗口恢复到最小化状态<br>请输入合理的滞留时间，单位为毫秒</html>");
  textField_2 = new JTextField();
  label_1.setLabelFor(textField_2);
  textField_2.setBounds(117, 105, 66, 21);
  panel_1.add(textField_2);
  textField_2.setColumns(10);
  ViewUtils.initNumberTextField(textField_2, (number, text) -> {
   if (number <= 0)
    number = 800;
   Window.stayTime = number;
   INI.INSTANCE.setLong("stay-time", number);
  });
  textField_2.setText(INI.INSTANCE.get("stay-time", "800"));

  JCheckBox chckbxNewCheckBox = new JCheckBox("  截图、取色时是否需要获取焦点 ?");
  chckbxNewCheckBox.setBounds(32, 64, 232, 23);
  panel_1.add(chckbxNewCheckBox);
  chckbxNewCheckBox.setToolTipText("如果目标窗口无需获取焦点也能随时刷新画面，请勾选此项");
  chckbxNewCheckBox.setSelected(INI.INSTANCE.getBoolean("shoot-focuse"));
  chckbxNewCheckBox.addActionListener((e) -> {
   Window.shootFocuse = chckbxNewCheckBox.isSelected();
   INI.INSTANCE.setBoolean("shoot-focuse", chckbxNewCheckBox.isSelected());
  });

  textField_4 = new JLabel();
  textField_4.setText("按键模式 ?");
  textField_4.setBounds(41, 146, 66, 21);
  textField_4.setToolTipText("按键不起作用？试试切换该模式。");
  panel_1.add(textField_4);

  ButtonGroup keyModeRadioBtn = new ButtonGroup();
  JRadioButton radioButton = new JRadioButton("普通按键");
  radioButton.setMnemonic(2);
  radioButton.setBounds(107, 145, 81, 23);
  panel_1.add(radioButton);
  keyModeRadioBtn.add(radioButton);
  radioButton.addActionListener((e) -> {
   INI.INSTANCE.setInt("key-mode", 2);
   Window.keyMode = 2;
  });

  JRadioButton radioButton_1 = new JRadioButton("系统按键");
  radioButton_1.setMnemonic(1);
  radioButton_1.setBounds(190, 145, 74, 23);
  panel_1.add(radioButton_1);
  keyModeRadioBtn.add(radioButton_1);
  radioButton_1.addActionListener((e) -> {
   INI.INSTANCE.setInt("key-mode", 1);
   Window.keyMode = 1;
  });

  JRadioButton radioButton_2 = new JRadioButton("兼容按键");
  radioButton_2.setMnemonic(0);
  radioButton_2.setBounds(276, 145, 74, 23);
  panel_1.add(radioButton_2);
  keyModeRadioBtn.add(radioButton_2);
  radioButton_2.addActionListener((e) -> {
   INI.INSTANCE.setInt("key-mode", 0);
   Window.keyMode = 0;
  });
  switch (INI.INSTANCE.getShort("key-mode")) {
  case 0:
   radioButton_2.setSelected(true);
   break;
  case 1:
   radioButton_1.setSelected(true);
   break;
  case 2:
   radioButton.setSelected(true);
   break;
  }

  JCheckBox checkBox_1 = new JCheckBox("  按键时获取焦点");
  checkBox_1.setBounds(32, 181, 129, 23);
  panel_1.add(checkBox_1);
  checkBox_1.addActionListener((e) -> {
   Window.keyFocuse = checkBox_1.isSelected();
   INI.INSTANCE.setBoolean("key-focuse", checkBox_1.isSelected());
  });
  checkBox_1.setSelected(INI.INSTANCE.getBoolean("key-focuse"));

  JCheckBox checkBox_2 = new JCheckBox("  鼠标点击时获取焦点");
  checkBox_2.setBounds(228, 181, 145, 23);
  panel_1.add(checkBox_2);
  checkBox_2.addActionListener((e) -> {
   Window.mouseFocuse = checkBox_2.isSelected();
   INI.INSTANCE.setBoolean("mouse-focuse", checkBox_2.isSelected());
  });
  checkBox_2.setSelected(INI.INSTANCE.getBoolean("mouse-focuse"));

  JPanel panel_2 = new JPanel();
  panel_2.setLayout(null);
  panel_2.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128), 2, true), "\u57FA\u7840", TitledBorder.CENTER,
    TitledBorder.TOP, null, new Color(0, 0, 0)));
  panel_2.setBounds(26, 10, 436, 61);
  panel.add(panel_2);

  JCheckBox checkBox = new JCheckBox("  最小化时隐藏");
  checkBox.setBounds(35, 20, 127, 23);
  panel_2.add(checkBox);
  checkBox.addActionListener((e) -> {
   INI.INSTANCE.setBoolean("min-hide", checkBox.isSelected());
  });
  checkBox.setSelected(INI.INSTANCE.getBoolean("min-hide"));

  JLabel label_2 = new JLabel("运行日志行数");
  label_2.setBounds(228, 24, 72, 15);
  panel_2.add(label_2);
  textField_3 = new JTextField();
  textField_3.setBounds(318, 21, 66, 21);
  panel_2.add(textField_3);
  textField_3.setColumns(3);
  label_2.setLabelFor(textField_3);
  ViewUtils.initNumberTextField(textField_3, (number, text) -> {
   INI.INSTANCE.setInt("msg-lines", Math.max(number, 3));
   textField_3.setText(String.valueOf(Math.max(number, 3)));
  });
  textField_3.setText(INI.INSTANCE.get("msg-lines", "3"));

  JPanel panel_3 = new JPanel();
  panel_3.setLayout(null);
  panel_3.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128), 2, true), "其他", TitledBorder.CENTER,
    TitledBorder.TOP, null, new Color(0, 0, 0)));
  panel_3.setBounds(26, 322, 436, 61);
  panel.add(panel_3);

  JLabel label = new JLabel("捐款码 ?");
  label.setToolTipText("填写正确的捐款码可屏蔽公告页面。");
  label.setBounds(52, 27, 56, 15);
  panel_3.add(label);

  textField_1 = new JTextField();
  textField_1.setText(INI.INSTANCE.get("mk-password", ""));
  textField_1.setBounds(118, 24, 265, 21);
  panel_3.add(textField_1);
  textField_1.addFocusListener(new FocusAdapter() {
   @Override
   public void focusLost(FocusEvent focusevent) {
    INI.INSTANCE.set("mk-password", textField_1.getText());
   }
  });
 }
}
