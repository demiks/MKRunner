package demiks.view.check;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import demiks.action.check.CycleCheck;
import demiks.action.check.MKCheck;
import demiks.view.CloseBtn;
import demiks.view.ViewUtils;

public class CycleCheckPanel extends JPanel {
	private static final long serialVersionUID = -3999304724185933030L;
	public CycleCheck check;

	public CycleCheckPanel(CycleCheck check, List<MKCheck> checks) {
		this.check = check;
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		initCycleCheckPanel();
		add(new CloseBtn(() -> {
			checks.remove(check);
		}));
	}

	private void initCycleCheckPanel() {
		final JTextField textField = new JTextField();
		final JLabel label = new JLabel("执行次数　");
		ViewUtils.initNumberTextField(textField, (number, text) -> {
			check.setTimes(number);
		});
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label);
		JComboBox<String> comboBox = new JComboBox<String>(new String[] { "小于", "整数倍于" });
		comboBox.setSelectedIndex(check.getCheckType());
		comboBox.addActionListener((e) -> {
			check.setCheckType(comboBox.getSelectedIndex());
		});
		add(comboBox);
		add(textField);
		textField.setColumns(6);
		textField.setText(String.valueOf(check.getTimes()));
	}

}
