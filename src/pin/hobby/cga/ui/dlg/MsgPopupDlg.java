package pin.hobby.cga.ui.dlg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MsgPopupDlg extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel lbMsg;

    public MsgPopupDlg() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });


// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        setMinimumSize(new Dimension(210, 110));

        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MsgPopupDlg dialog = new MsgPopupDlg();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void setMsg(String msg, String title)
    {
        lbMsg.setText(msg);
        setTitle(title);
    }
}
