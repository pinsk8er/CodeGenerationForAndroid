package pin.hobby.cga.ui.dlg;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdesktop.swingx.JXLabel;
import pin.hobby.cga.setting.SettingData;
import pin.hobby.cga.setting.SettingService;
import pin.hobby.cga.setting.TypeData;
import pin.hobby.cga.util.CCGAType;
import pin.hobby.cga.util.parser.CCGAParser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeCodeDlg extends JDialog {

    private final Logger LOG = Logger.getInstance(this.getClass().getSimpleName());

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton btnSelectPackage;
    private JTextField txtClassName;
    private JComboBox cbType;
    private JLabel txtPackage;
    private JProgressBar pbLoad;
    private JXLabel txtPreview;
    private JLabel ivWarnClass;
    private JLabel ivWarnPackage;

    private AnActionEvent   mEvent;
    private SettingData     mData;
    private String   mCode;
    private String   mWritePath = null;
    private String          mClassName;

    private VirtualFile     mFile;
    private CCGAParser mParser;

    final private  String  PackagePattern = "import ([0-9a-zA-Z.]+)";
    private Pattern         mPackagePattern;

    public MakeCodeDlg(VirtualFile    f, AnActionEvent e) {
        mFile = f;
        mEvent = e;
        mParser = CCGAParser.makeParser(f);
        mPackagePattern = Pattern.compile(PackagePattern);

        String name = mFile.getName();
        name = name.substring(0, name.lastIndexOf(".") );
        if(mFile.getFileType().getName().equals("XML"))
        {
            mClassName = "";
            String[] strs = name.split("_");
            for (int i = 1 ; i < strs.length ; i ++)
            {
                mClassName = mClassName + strs[i].substring(0,1).toUpperCase();
                if(strs[i].length() > 1)
                    mClassName = mClassName +strs[i].substring(1);
            }
        }
        else{
            mClassName = name;
        }
        parsing();

        ivWarnClass.setVisible(false);
        ivWarnPackage.setVisible(false);

        mData = SettingService.getInstance().mData;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        btnSelectPackage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ProjectFileIndex index = ProjectRootManager.getInstance(mEvent.getProject()).getFileIndex();
                Module module = index.getModuleForFile(mFile);

                PackageChooserDialog dlg = new PackageChooserDialog("Select package", module );
                dlg.show();

                if(dlg.getSelectedPackage() != null)
                {
                    String strPackage = dlg.getSelectedPackage().getQualifiedName();
                    mWritePath = dlg.getSelectedPackage().getDirectories()[0].getVirtualFile().getPath();

                    txtPackage.setText(strPackage);
                    ArrayList<String>   list = mParser.getDesStr(CCGAType.CGAType_Packge);
                    if(list.size() > 0)
                        list.set(0, strPackage + ";");
                    else
                        list.add(strPackage + ";");

                    txtPreview.setText(getCode());
                }
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

        setMinimumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 50,
                Toolkit.getDefaultToolkit().getScreenSize().height - 100 ));

        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

        init();
    }

    private void init()
    {
        ArrayList<String> keys = mData.getCustomKey();
        String fileType = mParser.getFileType().getName();
        for (int i = 0, size =  keys.size(); i < size; i ++) {
            if(mData.getCustomTypeData(keys.get(i)).mFileType.equals(fileType))
                cbType.addItem(keys.get(i));
        }

        if(cbType.getItemCount() > 0) {
            cbType.setSelectedIndex(0);
            parse((String)cbType.getSelectedItem());
        }

        cbType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                parse((String)e.getItem());
            }
        });

        txtClassName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setCodeClassName();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                setCodeClassName();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                setCodeClassName();
            }
        });

        setLoading(false);
    }

    private void setCodeClassName()
    {
        ArrayList<String>   list = mParser.getDesStr(CCGAType.CGAType_ClassName);
        if(list.size() > 0)
            list.set(0, txtClassName.getText());
        else
            list.add(txtClassName.getText());

        txtPreview.setText(getCode());
    }


    private void parse(String key) {
        setLoading(true);
        TypeData data = mData.getCustomTypeData(key);

        txtClassName.setText(mClassName + data.mTypeLastName);

        ArrayList<String>   list = mParser.getDesStr(CCGAType.CGAType_ClassName);
        if(list.size() > 0)
            list.set(0, txtClassName.getText());
        else
            list.add(txtClassName.getText());

        mCode = getCode(data.mCode);

        txtPreview.setText(getCode());

        setLoading(false);
    }

    private void parsing()
    {
        if(mParser != null)
        {
            try {
                mParser.parsing();
            }catch (Exception err)
            {
                LOG.error("parsing error:" + err.getMessage());
            }
        }
    }

    private String getCode(String data)
    {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(data));
        try{
            String read;
            do {
                read = reader.readLine();

                if (read == null)
                    break;

                Matcher matcher = mPackagePattern.matcher(read);
                if(matcher.find())
                {
                    String strImport = read + "\n";
                    if (!mParser.checkImport(strImport))
                        buffer.append(strImport);

                    continue;
                }

                String mapper = mParser.dataMapping(read);
                buffer.append(mapper);
                buffer.append("\n");
            }while (read != null);

        }catch (Exception e)
        {
            LOG.error("data read error:" + e.getMessage());
        }

        return buffer.toString();
    }

    private String getCode()
    {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(mCode));
        try{
            String read;
            do {
                read = reader.readLine();

                if (read == null)
                    break;

                CCGAType[] types = {CCGAType.CGAType_ClassName, CCGAType.CGAType_Packge};
                boolean isFind = false;
                for (int i = 0; i < types.length ; i ++) {
                    isFind = chekcMatcher(types[i], buffer, read);
                    if(isFind)
                        break;
                }

                if (!isFind) {
                    buffer.append(read);
                    buffer.append("\n");
                }

            }while (read != null);

        }catch (Exception e)
        {
            LOG.error("data read error:" + e.getMessage());
        }

        return buffer.toString();
    }

    private boolean chekcMatcher(CCGAType type, StringBuffer buffer, String data)
    {
        Matcher matcher = mParser.getMatcher(type, data);

        boolean isFind = false;
        while (matcher.find())
        {
            StringBuffer str= new StringBuffer();
            ArrayList<String> list = mParser.getDesStr(type);
            for (int i = 0 , size = list.size() ; i < size ; i ++)
                str.append(list.get(i));

            if (str.toString().length() > 0) {
                buffer.append(matcher.replaceAll(str.toString()));
                buffer.append("\n");
                isFind = true;
            }
        }

        return isFind;
    }


    private void setLoading(boolean b)
    {
        pbLoad.setVisible(b);
        pbLoad.setIndeterminate(b);
        contentPane.setEnabled(b);
    }

    private String writeFile()
    {
        String result;
        setLoading(true);

        File f = new File(mWritePath,txtClassName.getText()+".java");
        result = f.getPath();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            out.write(txtPreview.getText());
            out.close();
        }catch (Exception e)
        {
            result = null;
            LOG.error("write file error:" + e.getMessage());
        }

        setLoading(false);
        return result;
    }


    private boolean writeCheck()
    {
        boolean[] has = {false, false};
        CCGAType[] types = {CCGAType.CGAType_ClassName, CCGAType.CGAType_Packge};
        for (int i = 0; i < types.length ; i ++) {
            ArrayList<String> list = mParser.getDesStr(types[i]);
            for (int j = 0 , size = list.size() ; j < size ; j ++ )
            {
                if(list.get(j).length()>0)
                {
                    has[i] = true;
                    break;
                }
            }
            switch (types[i])
            {
                case CGAType_ClassName:
                    ivWarnClass.setVisible(!has[i]);
                    break;
                case CGAType_Packge:
                    ivWarnPackage.setVisible(!has[i]);
                    break;
            }
        }
        if(has[0] && has[1])
            return true;
        else
            return false;
    }



    private void onOK() {
// add your code here
        if(writeCheck())
        {
            String filePath = writeFile();
            if(filePath != null) {
                mEvent.getProject().getBaseDir().refresh(false, true);

                VirtualFile vf = LocalFileSystem.getInstance().findFileByPath(filePath);
                FileEditorManager  fm = FileEditorManager.getInstance(mEvent.getProject());
                fm.openFile(vf, true, true);
                dispose();
            }
        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MakeCodeDlg dialog = new MakeCodeDlg(null, null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


}
