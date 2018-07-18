package cn.ric.view.event;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.ric.action.event.MKBeepEvent;
import cn.ric.action.event.MKEvent;
import cn.ric.view.CloseBtn;
import cn.ric.view.ViewUtils;

public class BeepEventPanel extends JPanel {
 private static final long serialVersionUID = -3999304724185933030L;

 public BeepEventPanel(MKBeepEvent event, ArrayList<MKEvent> events) {
  setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
  JLabel label = new JLabel("蜂鸣");
  add(label);
  
  final JTextField textField = new JTextField();
  add(textField);
  textField.setColumns(6);
  ViewUtils.initNumberTextField(textField, (number, text) -> {
   event.setTime(number);
  });
  textField.setText(String.valueOf(event.getTime()));
  
  JLabel label_1 = new JLabel("毫秒，并截图");
  add(label_1);
  add(new CloseBtn(() -> {
   events.remove(event);
  }));
 }

}
