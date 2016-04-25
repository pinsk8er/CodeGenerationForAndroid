package pin.hobby.cga.setting;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;
import pin.hobby.cga.util.DefaultCodeManager;


/**
 * Created by pin-mint on 16. 4. 9.
 */
@State(
        name = "CGASetting",
        storages = {
            @Storage( id = "CGASetting", file = StoragePathMacros.APP_CONFIG + "/CGASetting.xml")
        }
)
public class SettingService implements PersistentStateComponent<SettingService> {
    public SettingData mData;

    public SettingService()
    {
        mData = null;
    }

    static public SettingService getInstance()
    {
        SettingService service = ServiceManager.getService(SettingService.class);

        if(service.mData == null)
            service.loadState(service);

        return service;
    }

    @Nullable
    @Override
    public SettingService getState() {
        return this;
    }

    @Override
    public void loadState(SettingService settingData) {
        if(settingData == null)
            settingData = new SettingService();

        if(settingData.mData == null)
        {
            DefaultCodeManager dcm = new DefaultCodeManager();
            settingData.mData = new SettingData();
            if(!settingData.mData.ContainsKey(DefaultCodeManager.RES_Fragment) ) {
                TypeData data = new TypeData();
                data.mCode = dcm.getReadData(DefaultCodeManager.RES_Fragment).toString();
                data.mTypeLastName = DefaultCodeManager.RES_Fragment;
                data.mFileType = "XML";
                settingData.mData.putCustomCode(DefaultCodeManager.RES_Fragment, data);
            }

            if(!settingData.mData.ContainsKey(DefaultCodeManager.RES_View) ) {
                TypeData data = new TypeData();
                data.mCode = dcm.getReadData(DefaultCodeManager.RES_View).toString();
                data.mTypeLastName = DefaultCodeManager.RES_View;
                data.mFileType = "XML";
                settingData.mData.putCustomCode(DefaultCodeManager.RES_View, data);
            }


            if(!settingData.mData.ContainsKey(DefaultCodeManager.RES_ViewHolder) ){
                TypeData data = new TypeData();
                data.mCode = dcm.getReadData(DefaultCodeManager.RES_ViewHolder).toString();
                data.mTypeLastName = DefaultCodeManager.RES_ViewHolder;
                data.mFileType = "XML";
                settingData.mData.putCustomCode(DefaultCodeManager.RES_ViewHolder, data);
            }

            if(!settingData.mData.ContainsKey(DefaultCodeManager.RES_Activity) ){
                TypeData data = new TypeData();
                data.mCode = dcm.getReadData(DefaultCodeManager.RES_Activity).toString();
                data.mTypeLastName = DefaultCodeManager.RES_Activity;
                data.mFileType = "XML";
                settingData.mData.putCustomCode(DefaultCodeManager.RES_Activity, data);
            }

            if(!settingData.mData.ContainsKey(DefaultCodeManager.RES_Adapter) ){
                TypeData data = new TypeData();
                data.mCode = dcm.getReadData(DefaultCodeManager.RES_Adapter).toString();
                data.mTypeLastName = DefaultCodeManager.RES_Adapter;
                data.mFileType = "JAVA";
                settingData.mData.putCustomCode(DefaultCodeManager.RES_Adapter, data);
            }

        }

        XmlSerializerUtil.copyBean(settingData, this);
    }

}
