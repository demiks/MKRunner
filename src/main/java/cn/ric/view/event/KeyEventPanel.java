package cn.ric.view.event;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.ric.action.event.MKEvent;
import cn.ric.action.event.MKKeyEvent;
import cn.ric.view.CloseBtn;
import cn.ric.view.ViewUtils;

public class KeyEventPanel extends JPanel {
 private static final long serialVersionUID = -3999304724185933030L;

 public KeyEventPanel(MKKeyEvent event, ArrayList<MKEvent> events) {
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

  JLabel label = new JLabel("，键");
  add(label);

  final JTextField textField_3;
  textField_3 = new JTextField();
  add(textField_3);
  textField_3.setColumns(5);
  textField_3.setText(String.valueOf(event.getKeyValue()));
  ViewUtils.initNumberTextField(textField_3, (number, text) -> {
  	if (text.length() == 1)
  		event.setKeyValue(text.toUpperCase().charAt(0));
  	else
    event.setKeyValue(number);
  });

  JComboBox<String> comboBox = new JComboBox<String>(new String[] {"敲击", "按住", "松开" });
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
  comboBox.addActionListener((e) -> {
   switch (comboBox.getSelectedIndex()) {
   case 0:
    event.setReleaseType(0);
    break;
   case 1:
    event.setReleaseType(2);
    break;
   case 2:
    event.setReleaseType(1);
    break;
   }
  });
  add(comboBox);
  add(new CloseBtn(() -> {
   events.remove(event);
  }));
 }

}
