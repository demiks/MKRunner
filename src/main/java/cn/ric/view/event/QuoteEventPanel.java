package cn.ric.view.event;

import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.ric.action.event.MKEvent;
import cn.ric.action.event.MKQuoteEvent;
import cn.ric.util.StringUtils;
import cn.ric.view.CloseBtn;
import cn.ric.view.ViewUtils;

public class QuoteEventPanel extends JPanel {
	private static final long serialVersionUID = -3999304724185933030L;

	public QuoteEventPanel(MKQuoteEvent event, ArrayList<MKEvent> events) {
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

		JLabel label = new JLabel("引用:");
		add(label);

		JTextField textField_1;
		textField_1 = new JTextField();
		add(textField_1);
		textField_1.setColumns(50);
		if (!StringUtils.isEmpty(event.getMkPath()))
		 textField_1.setText(event.getMkPath());
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				event.setMkPath(textField_1.getText());
			}
		});
		textField_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					ViewUtils.openFile(textField_1, (path) -> {
						textField_1.setText(path);
						event.setMkPath(path);
					});
				}
			}
		});
		
		add(new CloseBtn(() -> {
			events.remove(event);
		}));
	}

}
