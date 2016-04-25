package pin.hobby.cga.util;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pin-mint on 16. 4. 10.
 */
public class DefaultCodeManager {

    private final Logger LOG = Logger.getInstance(this.getClass().getSimpleName());

    static public final String      RES_Activity = "Activity";
    static public final String      RES_Fragment = "Fragment";
    static public final String      RES_View = "View";
    static public final String      RES_ViewHolder = "ViewHolder";
    static public final String      RES_Adapter = "Adapter";

    public StringBuffer getReadData(String res)
    {
        InputStream is = getClass().getClassLoader().getResourceAsStream(res);

        String str = "";
        StringBuffer buf = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is != null) {
            boolean isEmpty = false;
            while (!isEmpty) {
                try {
                    str = reader.readLine();
                    isEmpty = (str == null);
                }catch (Exception e)
                {
                    LOG.error("reader error:" + e.getMessage());
                }
                if(!isEmpty)
                    buf.append(str + "\n" );
            }
        }

        return buf;
    }

}
