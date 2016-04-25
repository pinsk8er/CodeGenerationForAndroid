package pin.hobby.cga.util.parser;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import pin.hobby.cga.util.CCGAType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pin-mint on 16. 4. 13.
 */
public abstract class CCGAParser {

    public abstract void parsing() throws Exception;

    /**
     * Parsing 결과 내용
     */
    private Map<CCGAType, ArrayList<String>> mDes;
    protected VirtualFile       mFile;

    private Map<CCGAType, Pattern>   mPatternMap;

    static public CCGAParser makeParser(VirtualFile f)
    {
        String type = f.getFileType().getName().toUpperCase();
        switch (type)
        {
            case "XML":
                return new CCGAXmlParser(f);
            case "JAVA":
                return new CCGAJavaParser(f);
            default:
                return null;
        }
    }

    public CCGAParser(VirtualFile file) {
        mFile = file;

        mDes = new HashMap<>();
        mPatternMap = new HashMap<>();
        for (int i = 0; i < CCGAType.TypeSize; i ++)
        {
            CCGAType type = CCGAType.getType(i);
            Pattern p = Pattern.compile(type.getPattern());
            mPatternMap.put(type, p);
            mDes.put(CCGAType.getType(i), new ArrayList<>());
        }
    }

    public FileType getFileType()
    {
        return mFile.getFileType();
    }

    public Matcher getMatcher(CCGAType type, String data)
    {
        if(mPatternMap.containsKey(type))
            return mPatternMap.get(type).matcher(data);

        return  null;
    }

    public String dataMapping(String data)
    {
        StringBuffer buffer = new StringBuffer();
        boolean isChange = false;
        for (int i = 0; i < CCGAType.TypeSize; i ++)
        {
            CCGAType type = CCGAType.getType(i);
            if (isChange)
                data = buffer.toString();

            Matcher matcher = mPatternMap.get(type).matcher(data);

            if(matcher.find()) {
                if(type == CCGAType.CGAType_FindView)
                {
                    Matcher view_matcher = mPatternMap.get(CCGAType.CGAType_FindView_2).matcher(data);
                    if(view_matcher.find())
                        continue;
                }

                String str = mapping(type, matcher);
                if(str != null){
                    isChange = true;
                    buffer.append(str);
                }
            }
        }

        if (isChange)
            return buffer.toString();
        else
            return data;
    }

    private String mapping(CCGAType type, Matcher matcher)
    {
        StringBuffer buffer = new StringBuffer();
        StringBuffer data = new StringBuffer();
        ArrayList<String>   list = getDesStr(type);

        do {
            switch (type) {
                case CGAType_ClassName:
                case CGAType_Packge:
                    return null;
                case CGAType_FindView_2:
                    for (int i = 0, size = list.size(); i < size; i++) {
                        data.append(String.format(list.get(i), matcher.group(1)));
                    }

                    break;
                case CGAType_FindView:
                case CGAType_Imports:
                case CGAType_LayoutId:
                case CGAType_MemberVariable:
                case CGAType_TargetClass:
                    for (int i = 0, size = list.size(); i < size; i++) {
                        data.append(list.get(i));
                    }
                    break;
            }
        }while (matcher.find());
        buffer.append(matcher.replaceAll(data.toString()));

        return buffer.toString();
    }


    public void addDataInDes(CCGAType type, String data)
    {
        if (mDes.size() > type.getIdx()) {
            mDes.get(type).add(data);
        }
    }

    public ArrayList<String> getDesStr(CCGAType type)
    {
        if (mDes.containsKey(type))
            return mDes.get(type);

        return null;
    }


    public void displayData()
    {
        System.out.println("=========== import ===========");
        displayList(getDesStr(CCGAType.CGAType_Imports));

        System.out.println("=========== Member ===========");
        displayList(getDesStr(CCGAType.CGAType_MemberVariable));

        System.out.println("=========== FindView ===========");
        displayList(getDesStr(CCGAType.CGAType_FindView));

        System.out.println("=========== FindView 2 ===========");
        displayList(getDesStr(CCGAType.CGAType_FindView_2));
    }

    private void displayList(ArrayList<String> list)
    {
        for (int i = 0, size = list.size(); i < size ; i++)
        {
            System.out.println(i + ":" + list.get(i));
        }
    }

    public boolean checkImport(String packageName)
    {
        ArrayList<String>   list = getDesStr(CCGAType.CGAType_Imports);
        for (int i = 0, size = list.size(); i < size ; i++)
        {
            if(packageName.equals(list.get(i)))
            {
                return true;
            }
        }

        return false;
    }
}
