import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


public class DBMSMain {
	
	public static void main(String[] arg) throws IOException, SAXException, ParserConfigurationException {
		SAXParserFactory spfac = SAXParserFactory.newInstance();
        SAXParser sp = spfac.newSAXParser();

        ReadMetadata handler = new ReadMetadata();
        sp.parse("metadata.xml", handler);
        handler.readTableList();
        handler.readConfig();
   	}
}
