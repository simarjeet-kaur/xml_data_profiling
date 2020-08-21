package com.tutorialspoint.xml;
import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlFile {
	
	static String filePath;
	static int sectionCount;
	static String experimentType;
	static NodeList sections;
	static DocumentBuilder builder;
	static NamedNodeMap allFields;
	static HashMap<String, Integer> fieldCountsMap;
	
	public xmlFile(String filePath) throws FileNotFoundException, SAXException, IOException, XPathExpressionException {
		  
		//creating the document builder to parse the document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		//turning the filePath into a parsable object
		xmlFile.filePath = filePath;
		Document doc = null;
		doc = builder.parse(new FileInputStream(filePath), "UTF8");
		
		//getting individual sections
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String sectionPath = "/collection/sectionSetView/section";	        
		NodeList sections = (NodeList) xPath.compile(sectionPath).evaluate(
		   doc, XPathConstants.NODESET);
		
		fieldCountsMap = new HashMap<String, Integer>();

		for (int temp = 0; temp < sections.getLength(); temp++) {
			Node sectionNode = sections.item(temp);
			if (sectionNode.getNodeType() == Node.ELEMENT_NODE) {
				String sectionName = ((Element) sectionNode).getAttribute("name");
				//Looking for "object" nodes
				String objectPath = "object";
				NodeList objects = (NodeList) xPath.compile(objectPath).evaluate(sectionNode, XPathConstants.NODESET);
								
				for (int i = 0; i < objects.getLength(); i++) {
					Node object = objects.item(i);
										
					String fieldPath = "field";
					Node fieldNode = (Node) xPath.compile(fieldPath).evaluate(object, XPathConstants.NODE);
					
					String fieldName = ((Element) fieldNode).getAttribute("name");
					
					if (!fieldCountsMap.containsKey(sectionName + "-" + fieldName)) {
						fieldCountsMap.put(sectionName + "-" + fieldName, 0);
					} 
					
					NodeList objectChildren = object.getChildNodes();
										
					for (int k = 0; k < objectChildren.getLength(); k++) {
						Node child = objectChildren.item(k);
						if (!child.getNodeName().equals("field")) {
							if (child.getNodeType() == Node.ELEMENT_NODE && child.getChildNodes().getLength() != 0) {
								Integer count = fieldCountsMap.get(sectionName + "-" + fieldName);
								fieldCountsMap.put(sectionName + "-" + fieldName, count + 1);
							}
						}
					}
				}	
			}
		}
	}
	
	public HashMap<String, Integer> returnFieldCountsMap() {
		return fieldCountsMap;
	}
	
	public NodeList returnSectionNames() {
		return sections;
	}

	public int returnSectionCount() {
		return sectionCount;
	}
	
	public String returnExperimentType() {
		return experimentType;
	}
	
	public String returnFilePath() {
		return filePath;
	}
	
	public static void gettingAttributes(Document doc) {
		   // do something with the current node instead of System.out
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try {
			DocumentBuilder db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    doc.getDocumentElement().normalize();
	    System.out.println("Root element " + doc.getDocumentElement().getNodeName());

	    NodeList nodeList=doc.getElementsByTagName("section");
	    for (int i=0; i<nodeList.getLength(); i++) 
	    {
	        // Get element
	        Element element = (Element)nodeList.item(i);
	        System.out.println(element.getNodeName());
	    }
	}
	
}
