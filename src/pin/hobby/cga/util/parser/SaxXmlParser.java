package pin.hobby.cga.util.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by pin-mint on 16. 4. 13.
 *
 */
public class SaxXmlParser extends DefaultHandler{

    private XmlParserListener mListener;

    public SaxXmlParser(XmlParserListener l) {
        mListener = l;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
//        System.out.println("startElement ....");
//        // qName: 객체 name
//        for (int i = 0; i < attributes.getLength(); i++) {
//            System.out.println("Attribute: " + attributes.getQName(i) + "=" + attributes.getValue(i));
//        }
        mListener.startElement(uri, localName, qName, attributes);

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
