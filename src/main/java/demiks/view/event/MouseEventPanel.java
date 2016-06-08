package demiks.view.event;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import demiks.action.event.MKEvent;
import demiks.action.event.MKMouseEvent;
import demiks.util.StringUtils;
import demiks.view.CloseBtn;
import demiks.view.ViewUtils;

public class MouseEventPanel extends JPanel {
 private static final long serialVersionUID = -3999304724185933030L;

 public MouseEventPanel(MKMouseEvent event, ArrayList<MKEvent> events) {
  setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
  final JTextField textField;
  JLabel label_2 = new JLabel("休眠");
  add(label_2);

  textField = new JTextField();
  add(textField);
  textField.setColumns(6);
  textField.setText(String.valueOf(event.getSleepTime()));
  ViewUtils.initNumberTextField(textField, (number, text) -> {
   event.setSleepTime(number);
  });

  JLabel label = new JLabel("，鼠标");
  add(label);

  JComboBox<String> comboBox = new JComboBox<String>(new String[] { "左键点击", "左键按住", "左键松开", "右键点击", "右键按住", "右键松开" });
  if (event.getType() == MKMouseEvent.Type.LeftButton) {
   switch (event.getReleaseType()) {
   case 0:
    comboBox.setSelectedIndex(0);
    break;
   case 1:
    comboBox.setSelectedIndex(2);
    break;
   case 2:
    comboBox.setSelectedIndex(1);
    break;
   }
  } else if (event.getType() == MKMouseEvent.Type.RightButton) {
   switch (event.getReleaseType()) {
   case 0:
    comboBox.setSelectedIndex(3);
    break;
   case 1:
    comboBox.setSelectedIndex(5);
    break;
   case 2:
    comboBox.setSelectedIndex(4);
    break;
   }
  }
  add(comboBox);
  comboBox.addActionListener((e) -> {
  	switch (comboBox.getSelectedIndex()) {
			case 0:
				event.setType(MKMouseEvent.Type.LeftButton);
				event.setReleaseType(0);
				break;
			case 1:
				event.setType(MKMouseEvent.Type.LeftButton);
				event.setReleaseType(2);
				break;
			case 2:
				event.setType(MKMouseEvent.Type.LeftButton);
				event.setReleaseType(1);
				break;
			case 3:
				event.setType(MKMouseEvent.Type.RightButton);
				event.setReleaseType(0);
				break;
			case 4:
			 event.setType(MKMouseEvent.Type.RightButton);
			 event.setReleaseType(2);
			 break;
			case 5:
			 event.setType(MKMouseEvent.Type.RightButton);
			 event.setReleaseType(1);
			 break;
			default:
				break;
			}
  });

  JLabel label1 = new JLabel("相对于");
  add(label1);
  JLabel btnNewButton = new JLabel("窗口");
  btnNewButton.setFont(new Font("宋体", Font.PLAIN, 9));
  btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
  btnNewButton.setToolTipText("点击设置图片");
  if (!StringUtils.isEmpty(event.getPicPath())) {
   ImageIcon image = new ImageIcon(event.getPicPath());
   image.setImage(image.getImage().getScaledInstance(40, 25, Image.SCALE_DEFAULT));
   btnNewButton.setIcon(image);
   btnNewButton.setText("图片 ");
  }
  add(btnNewButton);
  ViewUtils.initPicLabel(btnNewButton, (path) -> {
   event.setPicPath(path);
   btnNewButton.setText("图片 ");
  });

  final JTextField textField_1;
  final JTextField textField_2;
  textField_1 = new JTextField();
  add(textField_1);
  textField_1.setColumns(5);
  textField_1.setText(String.valueOf(event.getXY().getX()));
  ViewUtils.initNumberTextField(textField_1, (number, text) -> {
   event.getXY().setX(number);
  });

  JLabel label11 = new JLabel("，");
  add(label11);

  textField_2 = new JTextField();
  add(textField_2);
  textField_2.setColumns(5);
  textField_2.setText(String.valueOf(event.getXY().getY()));
  ViewUtils.initNumberTextField(textField_2, (number, text) -> {
   event.getXY().setY(number);
  });
  add(new CloseBtn(() -> {
   events.remove(event);
  }));
 }

}
