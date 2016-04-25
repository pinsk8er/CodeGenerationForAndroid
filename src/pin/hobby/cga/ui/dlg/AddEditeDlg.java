package pin.hobby.cga.ui.dlg;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddEditeDlg extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtTypeName;
    private JTextField txtLastName;
    private JComboBox cbFileType;
    private JLabel warnTypeName;
    private JLabel warnLastName;


    static public class Data {
        public String mTypeName = "";
        public  String mLastName= "";
        public  String mFileType= "";

        @Override
        public String toString() {
            return "TypeName:" + mTypeName + ", lastName:" + mLastName + ", fileType:" + mFileType;
        }
    }

    private Data    mData;
    private boolean mIsAdd;
    private DlgResultListener<Data> mListener;

    public AddEditeDlg(boolean isAdd, Data data, DlgResultListener<Data> l) {
        mIsAdd = isAdd;
        mListener = l;
        warnLastName.setVisible(false);
        warnTypeName.setVisible(false);


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        cbFileType.addItem("XML");
        cbFileType.addItem("JAVA");

        if(mIsAdd) {
            mData = new Data();
            setTitle("Add Type");
        }
        else {
            mData = data;
            setTitle("Edit Type");
            initData();
        }

        cbFileType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mData.mFileType = e.toString();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        setMinimumSize(new Dimension(270, 150));

        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

    }

    private void onOK() {
// add your code here
        if(StringUtils.isEmpty(txtTypeName.getText()))
        {
            warnTypeName.setVisible(true);
            return;
        }
        warnTypeName.setVisible(false);


        if(StringUtils.isEmpty(txtLastName.getText()))
        {
            warnLastName.setVisible(true);
            return;
        }
        warnLastName.setVisible(false);

        mData.mFileType = cbFileType.getSelectedItem().toString();
        mData.mLastName = txtLastName.getText();
        mData.mTypeName = txtTypeName.getText();

        dispose();

        if(mListener != null)
            mListener.Ok(mData);

    }

    private void onCancel() {
// add your code here if necessary
        dispose();
        if(mListener != null)
            mListener.Cancel();
    }

    private void initData() {

        if(StringUtils.isNotEmpty(mData.mTypeName))
            txtTypeName.setText(mData.mTypeName);
        if(StringUtils.isNotEmpty(mData.mLastName))
            txtLastName.setText(mData.mLastName);

        if(StringUtils.isNotEmpty(mData.mFileType))
            cbFileType.setSelectedItem(mData.mFileType.toUpperCase());
    }

    public static void main(String[] args) {
        AddEditeDlg dialog = new AddEditeDlg(true, null, null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
