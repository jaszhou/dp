package com.ibm.wcts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XLM2JSON {

	public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	public static String TEST_XML_STRING = "<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String source="D:\\download\\xmltodict-master\\xmltodict-master\\sdn.xml";
		String dest="D:\\download\\xmltodict-master\\xmltodict-master\\sdn.json";
		
		try {

//			ByteArrayInputStream bais = null;
//
//			bais = new ByteArrayInputStream(xml.getBytes());

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			File input = new File(source);
			
			
			
//			PrintWriter out = new PrintWriter(dest);
			
			PrintWriter out =
			         new PrintWriter(new BufferedWriter(new FileWriter(dest)));
			
			Document xmlDoc = builder.parse(input);

			List <Element> entryList = findAllElementsByTagName(xmlDoc.getDocumentElement(),"sdnEntry");

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			
			for (Element entry: entryList){
			
				transformer.transform(new DOMSource(entry),
				      new StreamResult(buffer));
				
				String str = buffer.toString();
				
				JSONObject xmlJSONObj = XML.toJSONObject(str);

				String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				
				System.out.println(jsonPrettyPrintString);
				
				out.println(jsonPrettyPrintString);

			}
			
			buffer.close();
			out.close();
			
			
		} catch (Exception je) {
			System.out.println(je.toString());
		}

	}

	public static List<Element> findAllElementsByTagName(Element elem, String tagName) {
		List<Element> ret = new LinkedList<Element>();
		findAllElementsByTagName(elem, tagName, ret);
		return ret;
	}

	private static void findAllElementsByTagName(Element el, String tagName, List<Element> elementList) {

		if (tagName.equals(el.getTagName())) {
			elementList.add(el);
		}
		Element elem = getFirstElement(el);
		while (elem != null) {
			findAllElementsByTagName(elem, tagName, elementList);
			elem = getNextElement(elem);
		}
	}

	private static void findAllElementsByTagNameNS(Element el, String nameSpaceURI, String localName,
			List<Element> elementList) {

		if (localName.equals(el.getLocalName()) && nameSpaceURI.contains(el.getNamespaceURI())) {
			elementList.add(el);
		}
		Element elem = getFirstElement(el);
		while (elem != null) {
			findAllElementsByTagNameNS(elem, nameSpaceURI, localName, elementList);
			elem = getNextElement(elem);
		}
	}

	public static Element getFirstElement(Node parent) {
		Node n = parent.getFirstChild();
		while (n != null && Node.ELEMENT_NODE != n.getNodeType()) {
			n = n.getNextSibling();
		}
		if (n == null) {
			return null;
		}
		return (Element) n;
	}

	public static Element getNextElement(Element el) {
		Node nd = el.getNextSibling();
		while (nd != null) {
			if (nd.getNodeType() == Node.ELEMENT_NODE) {
				return (Element) nd;
			}
			nd = nd.getNextSibling();
		}
		return null;
	}

}
