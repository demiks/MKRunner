package cn.ric.view.check;

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

import cn.ric.action.check.MKCheck;
import cn.ric.action.check.PhotoCheck;
import cn.ric.util.StringUtils;
import cn.ric.view.CloseBtn;
import cn.ric.view.ViewUtils;

public class PhotoCheckPanel extends JPanel {
 private static final long serialVersionUID = -3999304724185933030L;
 public PhotoCheck check;

 public PhotoCheckPanel(PhotoCheck check, ArrayList<MKCheck> checks) {
  this.check = check;
  setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
  initPhotoCheckPanel();
  add(new CloseBtn(() -> {
   checks.remove(check);
  }));
 }

 private void initPhotoCheckPanel() {
  final JTextField textField_4 = new JTextField();
  final JTextField textField_5 = new JTextField();
  JLabel btnNewButton = new JLabel("图片 ");
  btnNewButton.setFont(new Font("宋体", Font.PLAIN, 9));
  btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
  btnNewButton.setToolTipText("点击设置图片");
  if (!StringUtils.isEmpty(check.getPicPath())) {
   ImageIcon image = new ImageIcon(check.getPicPath());
   image.setImage(image.getImage().getScaledInstance(40, 25, Image.SCALE_DEFAULT));
   btnNewButton.setIcon(image);
  }
  add(btnNewButton);
  ViewUtils.initPicLabel(btnNewButton, (path) -> {
   check.setPicPath(path);
  });

  JComboBox<String> comboBox = new JComboBox<String>(new String[] { "存在", "存在于", "不存在", "不存在于" });
  add(comboBox);
  comboBox.setSelectedIndex(check.getCheckType());
  comboBox.addActionListener((e) -> {
  	int selectedIndex = comboBox.getSelectedIndex();
			textField_4.setEnabled(selectedIndex == 1 || selectedIndex == 3);
  	textField_5.setEnabled(selectedIndex == 1 || selectedIndex == 3);
   check.setCheckType(selectedIndex);
  });

  textField_4.setEnabled(check.getCheckType() == 1 || check.getCheckType() == 3);
  add(textField_4);
  textField_4.setColumns(6);
  textField_4.setText(String.valueOf(check.getXY().getX()));
  ViewUtils.initNumberTextField(textField_4, (number, text) -> {
   check.getXY().setX(number);
  });

  JLabel label_2 = new JLabel("，");
  add(label_2);

  textField_5.setEnabled(check.getCheckType() == 1 || check.getCheckType() == 3);
  add(textField_5);
  textField_5.setColumns(6);
  textField_5.setText(String.valueOf(check.getXY().getY()));
  ViewUtils.initNumberTextField(textField_5, (number, text) -> {
   check.getXY().setY(number);
  });
 }

}
