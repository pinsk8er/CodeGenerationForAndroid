package pin.hobby.cga.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by pin-mint on 16. 4. 9.
 */
public class SettingData {

    public Map<String, TypeData>  mCustomCode;

    public SettingData() {
        mCustomCode = new HashMap<>();
    }

    /**
     *  new add or Edite
     * @param key  the key
     * @param code the code
     */
    public void putCustomCode(String key, TypeData data)
    {
        deletCustomCode(key);
        mCustomCode.put(key, data);
    }

    public void setData(String key , TypeData data)
    {
        mCustomCode.get(key).mCode = data.mCode;
        mCustomCode.get(key).mTypeLastName = data.mTypeLastName;
        mCustomCode.get(key).mFileType = data.mFileType;
    }

    public boolean ContainsKey(String key)
    {
        return mCustomCode.containsKey(key);
    }

    public String getCustomCode(String key)
    {
        if(ContainsKey(key))
            return mCustomCode.get(key).mCode;
        else
            return "";
    }

    public void putAll(SettingData data)
    {
        ArrayList<String> keySet = data.getCustomKey();
        for (int i = 0 , size = keySet.size() ; i < size ; i ++) {
            TypeData newData = new TypeData();
            TypeData orgData = data.getCustomTypeData(keySet.get(i));
            newData.mCode = orgData.mCode;
            newData.mTypeLastName = orgData.mTypeLastName;
            newData.mFileType = orgData.mFileType;

            putCustomCode(keySet.get(i), newData);
        }

    }

    public TypeData getCustomTypeData(String key)
    {
        return mCustomCode.get(key);
    }

    public ArrayList<String>    getCustomKey()
    {
        ArrayList<String> result = new ArrayList<>();
        Set<String> keys = mCustomCode.keySet();
        for (String key : keys)
        {
            result.add(key);
        }

        return result;
    }

    public void deletCustomCode(String key)
    {
        if(mCustomCode.containsKey(key))
            mCustomCode.remove(key);
    }




}
