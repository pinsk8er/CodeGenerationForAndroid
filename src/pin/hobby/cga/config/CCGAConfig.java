package pin.hobby.cga.config;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import pin.hobby.cga.setting.SettingData;
import pin.hobby.cga.setting.SettingService;
import pin.hobby.cga.ui.CCGASettingUi;

import javax.swing.*;

/**
 * Created by pin-mint on 16. 4. 7.
 */
public class CCGAConfig implements  Configurable{
    private final Logger LOG = Logger.getInstance(this.getClass().getSimpleName());

    // resutn true : btn apply enable   false: btn apply unenable
    static public boolean   IsEnableApply = false;


    private SettingService      mService;
    private CCGASettingUi mSettingUi;

    public CCGAConfig() {
        mService = SettingService.getInstance();
        mSettingUi = new CCGASettingUi(mService);
    }

    static public void setEnableApplyBtn(boolean b)
    {
        IsEnableApply = b;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Custom Code Generate for Android";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return mSettingUi .getRootPanel();
    }

    @Override
    public boolean isModified() {
        return IsEnableApply;
    }

    @Override
    public void apply() throws ConfigurationException {
        IsEnableApply = false;

        SettingData data = mSettingUi.getData();
        mService.mData.mCustomCode.clear();
        mService.mData.mCustomCode.putAll(data.mCustomCode);
    }

    @Override
    public void reset() {
        IsEnableApply = false;
        mSettingUi.reset();
    }

    @Override
    public void disposeUIResources() {
        // settings OK btn..
        // cancle reset -> disposeUIResources
        if(IsEnableApply)
        {
            try {
                apply();
            }catch (Exception e)
            {
                LOG.error("apply error:" + e.getMessage());
            }
        }
        IsEnableApply = false;
    }
}
