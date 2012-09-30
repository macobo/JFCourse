package xmlparse;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Filters by tag names and for each returns attribute **/
public class DomFilter {
	private String tagname;
	private String attribute;
	
	
	public DomFilter(String tagname, String attribute) {
		this.tagname = tagname;
		this.attribute = attribute;
	}

	public ArrayList<String> parse(Document dom) {
		NodeList tagMatches = dom.getElementsByTagName(tagname);
		ArrayList<String> result = new ArrayList<String>(tagMatches.getLength());
		
		for (int i = 0; i < tagMatches.getLength(); i++) {
			Element e = (Element)tagMatches.item(i);
			result.add(e.getAttribute(this.attribute));
		}
		
		return result;
	}
	
	
}
