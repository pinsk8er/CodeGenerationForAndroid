package pin.hobby.cga.util.parser;

import org.xml.sax.Attributes;

/**
 * Created by pin-mint on 16. 4. 13.
 */
public interface XmlParserListener {
    void startElement(String uri, String localName, String qName, Attributes attributes);
}
