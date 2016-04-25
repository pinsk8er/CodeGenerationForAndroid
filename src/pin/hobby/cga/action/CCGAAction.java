package pin.hobby.cga.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import pin.hobby.cga.setting.SettingData;
import pin.hobby.cga.setting.SettingService;
import pin.hobby.cga.ui.dlg.MakeCodeDlg;
import pin.hobby.cga.ui.dlg.MsgPopupDlg;

import java.util.ArrayList;


/**
 * Created by pin-mint on 16. 4. 5.
 */
public class CCGAAction extends AnAction {

    private final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(this.getClass().getSimpleName());

    @Override
    public void actionPerformed(AnActionEvent e) {

        Document currentDoc = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);

        MakeCodeDlg dlg = new MakeCodeDlg(currentFile, e);
        dlg.show();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        Document currentDoc = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);

        String type = currentFile.getFileType().getName().toUpperCase();
        boolean checkType;
        switch (type)
        {
            case "XML":
            case "JAVA":
                checkType =checkType(type);
                break;
            default:
                checkType = false;
                break;
        }

        e.getPresentation().setEnabled(checkType);
        e.getPresentation().setVisible(checkType);
    }

    private boolean checkType(String fileTypeName)
    {
        boolean result = false;
        SettingData data = SettingService.getInstance().mData;
        ArrayList<String> keys = data.getCustomKey();
        for (int i = 0, size =  keys.size(); i < size; i ++) {
            if(data.getCustomTypeData(keys.get(i)).mFileType.equals(fileTypeName))
            {
                result = true;
                break;
            }
        }

        return result;
    }

}
