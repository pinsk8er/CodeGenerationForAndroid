package pin.hobby.cga.ui;

import org.apache.commons.lang.StringUtils;
import pin.hobby.cga.config.CCGAConfig;
import pin.hobby.cga.model.CustomTableModel;
import pin.hobby.cga.setting.SettingData;
import pin.hobby.cga.setting.SettingService;
import pin.hobby.cga.setting.TypeData;
import pin.hobby.cga.ui.dlg.AddEditeDlg;
import pin.hobby.cga.ui.dlg.DlgResultListener;
import pin.hobby.cga.util.DefaultCodeManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by pin-mint on 16. 4. 9.
 */
public class CCGASettingUi {

    private final String[]  mColumns = {"TypeName", "Class last name", "File Type"};

    private JPanel rootPanel;
    private JComboBox cbType;
    private JComboBox cbDefaultType;
    private JButton btnLoad;
    private JTextArea txtCode;
    private JButton btnDelete;
    private JTable tbType;
    private JButton btnAdd;
    private JButton btnEdit;

    private SettingService mSetting;

    private DefaultCodeManager mDcm;
    private boolean         mIsChange = false;

    private SettingData mData;

    private CustomTableModel mTbModel;


    public CCGASettingUi(SettingService setting) {
        mSetting = setting;
        init();
    }

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    private void init()
    {
        mDcm = new DefaultCodeManager();

        mData = new SettingData();

        mTbModel = new CustomTableModel( null, mColumns);
        tbType.setModel(mTbModel);

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        tbType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbType.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });

        cbType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                txtCode.setText( mData.getCustomCode((String)e.getItem()) );
            }
        });

        cbDefaultType.addItem(DefaultCodeManager.RES_Activity);
        cbDefaultType.addItem(DefaultCodeManager.RES_Fragment);
        cbDefaultType.addItem(DefaultCodeManager.RES_View);
        cbDefaultType.addItem(DefaultCodeManager.RES_ViewHolder);
        cbDefaultType.addItem(DefaultCodeManager.RES_Adapter);

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String res = (String) cbDefaultType.getSelectedItem();
                txtCode.setText(mDcm.getReadData(res).toString());
                codeChange();
            }
        });


        txtCode.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                mIsChange = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                mIsChange = false;
            }
        });

        txtCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                codeChange();
                super.keyReleased(e);
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEditeDlg dlg = new AddEditeDlg(true, null, new DlgResultListener<AddEditeDlg.Data>() {
                    @Override
                    public void Ok(AddEditeDlg.Data result) {
                        System.out.println("add data:" + result);
                        add(result);
                    }
                    @Override
                    public void Cancel() {
                    }
                });
                dlg.show();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AddEditeDlg.Data data = new AddEditeDlg.Data();
                final int selectIdx = tbType.getSelectedRow();

                data.mTypeName = (mTbModel.getValueAt(selectIdx, 0) == null)?
                        "" : mTbModel.getValueAt(selectIdx, 0).toString();

                data.mLastName = (mTbModel.getValueAt(selectIdx, 1) == null)?
                        "" : mTbModel.getValueAt(selectIdx, 1).toString();

                data.mFileType = (mTbModel.getValueAt(selectIdx, 2) == null)?
                        "" : mTbModel.getValueAt(selectIdx, 2).toString();


                AddEditeDlg dlg = new AddEditeDlg(false, data, new DlgResultListener<AddEditeDlg.Data>() {
                    @Override
                    public void Ok(AddEditeDlg.Data result) {
                        System.out.println("edite data:" + result);
                        edit(result, selectIdx);
                    }
                    @Override
                    public void Cancel() {
                    }
                });
                dlg.show();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] select = tbType.getSelectedRows();
                if (select.length < 1)
                    return;

                for (int i = 0 ; i < select.length ; i ++)
                {
                    String key = (String)mTbModel.getValueAt(select[i], 0);
                    mTbModel.removeRow(select[i]);
                    cbType.removeItem(key);
                    deleteData(key);
                }

                mIsChange = true;
                CCGAConfig.setEnableApplyBtn(mIsChange);
            }
        });
    }

    private void add(AddEditeDlg.Data data)
    {
        String name = data.mTypeName;
        String lastname = data.mLastName;
        String fileType = data.mFileType;

        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(lastname))
            return;

        boolean isContain = false;
        for (int i = 0 , size = mTbModel.getRowCount(); i < size ; i ++)
        {
            if (mTbModel.getValueAt(i, 0).equals(name))
            {
                isContain = true;
                break;
            }
        }

        if(isContain)
            return;

        String[] row = {name, lastname, fileType};

        mTbModel.addRow(row);
        cbType.addItem(name);

        TypeData tdata = new TypeData();
        tdata.mTypeLastName = lastname;
        tdata.mFileType = fileType;

        addData(name, tdata);

        mIsChange = true;
        CCGAConfig.setEnableApplyBtn(mIsChange);
    }


    private void edit(AddEditeDlg.Data data, int selectRow )
    {

        String name = data.mTypeName;
        String lastName = data.mLastName;
        String fileType = data.mFileType;

        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(lastName))
            return;

        boolean isContain = false;

        for (int i = 0 , size = mTbModel.getRowCount(); i < size ; i ++)
        {
            if(selectRow == i)
                continue;

            if (mTbModel.getValueAt(i, 0).equals(name))
            {
                isContain = true;
                break;
            }
        }

        if(isContain)
            return;

        String oldKey = tbType.getValueAt(selectRow, 0).toString();

        mTbModel.setValueAt(name, selectRow, 0);
        mTbModel.setValueAt(lastName, selectRow, 1);
        mTbModel.setValueAt(fileType, selectRow, 2);

        cbType.removeItem(oldKey);
        cbType.addItem(name);


        TypeData tdata  = mData.getCustomTypeData(name);
        tdata.mFileType = fileType;
        tdata.mTypeLastName = lastName;

        setData(name, tdata);

        mIsChange = true;
        CCGAConfig.setEnableApplyBtn(mIsChange);
    }

    private void addData(String key , TypeData data)
    {
        mData.putCustomCode(key, data);
    }

    private void setData(String key, TypeData data)
    {
        mData.setData(key, data);
    }

    private void deleteData(String key)
    {
        mData.deletCustomCode(key);
    }


    public SettingData  getData()
    {
        return mData;
    }


    public void reset()
    {
        mData.mCustomCode.clear();
        mData.putAll(mSetting.mData);

        cbType.removeAllItems();
        while (mTbModel.getRowCount() > 0)
            mTbModel.removeRow(0);

        // type Table
        ArrayList<String>   keys = mData.getCustomKey();
        for (int i = 0, size =  keys.size(); i < size; i ++) {
            TypeData data = mData.getCustomTypeData(keys.get(i));

            String[] row = {keys.get(i), data.mTypeLastName, data.mFileType};

            cbType.addItem(keys.get(i));

            mTbModel.addRow(row);
        }

    }

    private void codeChange()
    {
        mIsChange = true;
        CCGAConfig.setEnableApplyBtn(mIsChange);
        String key = (String) cbType.getSelectedItem();
        TypeData data = mData.getCustomTypeData(key);
        data.mCode = txtCode.getText();
        mData.setData(key, data);
    }

}
