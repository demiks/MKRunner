package demiks.view.check;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import demiks.action.RGB;
import demiks.action.check.MKCheck;
import demiks.action.check.PointCheck;
import demiks.util.StringUtils;
import demiks.view.CloseBtn;
import demiks.view.ViewUtils;

public class PointCheckPanel extends JPanel {
	private static final long serialVersionUID = -3999304724185933030L;

	public PointCheckPanel(PointCheck check, ArrayList<MKCheck> checks) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		initPointCheckPanel(check);
		add(new CloseBtn(()->{
		 checks.remove(check);
		}));
	}

	private void initPointCheckPanel(PointCheck check) {
		final JTextField textField_1;
		final JTextField textField_2;
		final JComboBox<String> comboBox;
		final JTextField textField_3;
		final JLabel label = new JLabel("点　");
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.setToolTipText("点击使用鼠标从目标窗口获取坐标与颜色值");
		add(label);
		label.setHorizontalAlignment(SwingConstants.RIGHT);

		textField_1 = new JTextField();
		add(textField_1);
		textField_1.setColumns(6);
		textField_1.setText(String.valueOf(check.getXy().getX()));
		ViewUtils.initNumberTextField(textField_1, (number, text) -> {
			check.getXy().setX(number);
		});

		JLabel label_5 = new JLabel("，");
		add(label_5);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);

		textField_2 = new JTextField();
		add(textField_2);
		textField_2.setColumns(6);
		textField_2.setText(String.valueOf(check.getXy().getY()));
		ViewUtils.initNumberTextField(textField_2, (number, text) -> {
			check.getXy().setY(number);
		});

		JLabel label_1 = new JLabel("颜色值");
		add(label_1);

		comboBox = new JComboBox<String>(new String[] { "是", "不是" });
		add(comboBox);
		comboBox.setSelectedIndex(check.getCheckType());
		comboBox.addActionListener((e) -> {
			check.setCheckType(comboBox.getSelectedIndex());
		});

		textField_3 = new JTextField();
		add(textField_3);
		textField_3.setColumns(10);
		textField_3.setText(check.getRGB().toHex());
		textField_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED)
					return;
				if (StringUtils.isEmpty(textField_3.getText()))
					textField_3.setText("#FFFFFFF");
				Integer valueOf = Integer.parseInt(textField_3.getText().substring(1), 16);
				System.out.println(valueOf);
				check.setRGB(new RGB(valueOf));
			}
		});
	}
}
