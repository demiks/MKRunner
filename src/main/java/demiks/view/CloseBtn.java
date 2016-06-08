package demiks.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class CloseBtn extends JLabel {

 private static final long serialVersionUID = 4373742886224413877L;

 public static interface CloseListener {
  void closed();
 }

 public CloseBtn(CloseListener listener) {
  final JLabel label_1 = this;
  label_1.setText("　×　");
  label_1.setHorizontalAlignment(SwingConstants.CENTER);
  label_1.setLabelFor(this);
  label_1.setToolTipText("移除该项");
  label_1.setForeground(Color.RED);
  label_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
  label_1.addMouseListener(new MouseAdapter() {
   @Override
   public void mouseClicked(MouseEvent mouseevent) {
    super.mouseClicked(mouseevent);
    label_1.getParent().setVisible(false);
    label_1.getParent().getParent().remove(label_1.getParent());
    if (listener != null)
     listener.closed();
   }
  });

 }
}
