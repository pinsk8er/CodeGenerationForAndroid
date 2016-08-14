package pin.hobby.cga.util.parser;

import com.intellij.openapi.vfs.VirtualFile;
import org.xml.sax.Attributes;
import pin.hobby.cga.util.CCGAType;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;

/**
 * Created by pin-mint on 16. 4. 4.
 */
public class CCGAXmlParser extends CCGAParser {


    public CCGAXmlParser(VirtualFile f) {
        super(f);
        String name = f.getName();
        name = name.substring(0, name.lastIndexOf("."));
        addDataInDes(CCGAType.CGAType_LayoutId, "R.layout." + name);
    }

    @Override
    public void parsing() throws Exception {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(mFile.getInputStream(), new SaxXmlParser(mListener));
        }catch (Exception er)
        {
            throw er;
        }
    }

    private XmlParserListener   mListener = new XmlParserListener() {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            String objName = "";
            String packageName = "";
            if (qName.equals("include")) {
                packageName = "android.view.View";
            } else {
                if (qName.contains("."))
                    packageName = qName;
                else
                    packageName = "android.widget." + qName;
            }

            objName = packageName.substring(packageName.lastIndexOf(".")+1);

            boolean containId = false;
            for (int i = 0; i < attributes.getLength(); i++) {
                if(attributes.getQName(i).equals("android:id"))
                {
                    String id = attributes.getValue(i);
                    id = id.substring(id.indexOf("/")+1);
                    addDataInDes(CCGAType.CGAType_MemberVariable,"private " + objName + " " + id + ";\n");

                    objName = (objName.equals("View"))?"": "("+objName+")";

                    addDataInDes(CCGAType.CGAType_FindView,  id +" = " +objName+ "findViewById(R.id." + id + ");\n");
                    addDataInDes(CCGAType.CGAType_FindView_2,  id + "= " +objName+ "%s.findViewById(R.id." + id + ");\n");

                    containId = true;
                    break;
                }
            }


            if(containId) {
                String packageStr = "import "+packageName+";\n";
                if (!checkImport(packageStr)) {
                    addDataInDes(CCGAType.CGAType_Imports, packageStr);
                }
            }


        }
    };




}
