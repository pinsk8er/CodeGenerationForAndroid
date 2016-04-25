package pin.hobby.cga.util.parser;

import com.intellij.openapi.vfs.VirtualFile;
import pin.hobby.cga.util.CCGAType;

/**
 * Created by pin-mint on 16. 4. 13.
 */
public class CCGAJavaParser extends CCGAParser {

    public CCGAJavaParser(VirtualFile file) {
        super(file);
    }

    @Override
    public void parsing() throws Exception {
        String name = mFile.getName();
        name = name.substring(0, name.lastIndexOf(".") );
        addDataInDes(CCGAType.CGAType_TargetClass,  name);
    }
}
