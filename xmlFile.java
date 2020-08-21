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
		
		//turning the filePath into a document/parsable thing
		xmlFile.filePath = filePath;
		//File inputFile = new File(filePath);
		Document doc = null;
		doc = builder.parse(new FileInputStream(filePath), "UTF8");
		//doc.getDocumentElement().normalize();
		
		//trying to get individual sections 

		XPath xPath =  XPathFactory.newInstance().newXPath();
		String sectionPath = "/collection/sectionSetView/section";	        
		NodeList sections = (NodeList) xPath.compile(sectionPath).evaluate(
		   doc, XPathConstants.NODESET);
		
		fieldCountsMap = new HashMap<String, Integer>();

		for (int temp = 0; temp < sections.getLength(); temp++) {
			Node sectionNode = sections.item(temp);
			if (sectionNode.getNodeType() == Node.ELEMENT_NODE) {
				//System.out.println(((Element) sectionNode).getAttribute("name"));
				String sectionName = ((Element) sectionNode).getAttribute("name");
				//Look for "object" nodes
				String objectPath = "object";
				NodeList objects = (NodeList) xPath.compile(objectPath).evaluate(sectionNode, XPathConstants.NODESET);
				
				//System.out.println(objects.getLength());
				
				for (int i = 0; i < objects.getLength(); i++) {
					Node object = objects.item(i);
										
					String fieldPath = "field";
					Node fieldNode = (Node) xPath.compile(fieldPath).evaluate(object, XPathConstants.NODE);
					
					String fieldName = ((Element) fieldNode).getAttribute("name");
					/*
					if (fieldName.equals("Subsection")) {
						
						
						//issue is something with the for loop - goes on for too long and makes the counts very long
						//other issue is that this if statement doesnt have an else.  needs an else because then it'll have the same subsection listed twice 
						
						
						String subsectionPath = "/subsection/sectionSetView/section";
						NodeList subsections = (NodeList) xPath.compile(subsectionPath).evaluate(fieldNode, XPathConstants.NODE);
						System.out.println();
						if (subsections != null) {
							System.out.println("SUBSECTIONS NOT EMPTY");
						for (int j = 0; j < subsections.getLength(); j++) {
							Node subsectionNode = subsections.item(j);
							if (subsectionNode.getNodeType() == Node.ELEMENT_NODE) {
								//String subsectionName = ((Element) subsectionNode).getAttribute("name");
								NodeList subsectionObjects = (NodeList) xPath.compile(objectPath).evaluate(sectionNode, XPathConstants.NODESET);
								for (int k = 0; k < subsectionObjects.getLength(); k++) {
									Node subsectionObject = subsectionObjects.item(k);
									Node subsectionFieldNode = (Node)  xPath.compile(fieldPath).evaluate(subsectionObject, XPathConstants.NODE);
									String subsectionFieldName = ((Element) subsectionFieldNode).getAttribute("name");
									
									if (!fieldCountsMap.containsKey(sectionName + "." + fieldName + "." + subsectionFieldName)) {
										fieldCountsMap.put(sectionName + "." + fieldName + "." + subsectionFieldName, 0);
									}
									NodeList subsectionObjectChildren = subsectionObject.getChildNodes();
									for (int l = 0; l < subsectionObjectChildren.getLength(); l++) {
										Node subsectionChild = subsectionObjectChildren.item(l);
										if (!subsectionChild.getNodeName().equals("field")) {
											if (subsectionChild.getNodeType() == Node.ELEMENT_NODE && subsectionChild.getChildNodes().getLength() != 0) {
												Integer count_ = fieldCountsMap.get(sectionName + "." + fieldName + "." + subsectionFieldName);
												fieldCountsMap.put(sectionName + "." + fieldName + "." + subsectionFieldName, count_ + 1);
											}
										}
									}
								}
							}
						}
					}
					}*/
					
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
		
		/*Set<String> keySet = fieldCountsMap.keySet();
		ArrayList<String> keys = new ArrayList<String>();
		for (String x : keySet) {
			keys.add(x);
		}
		Collections.sort(keys);*/
		
	/*	for (String key : keys) {
			Integer value = fieldCountsMap.get(key);
			System.out.println(key + ": " + value);
		}*/
		
//		for (HashMap.Entry<String, Integer> entry : fieldCountsMap.entrySet()) {
//			System.out.println(entry.getKey() + ": " + entry.getValue().toString());
//		}
		
		//trying by fields
		
//		NodeList fields = doc.getElementsByTagName("field");
//		for (int i = 0; i < fields.getLength(); i++) {
//			Node fieldNode = fields.item(i);
//			if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
//				System.out.println(((Element) fieldNode).getAttribute("name"));
//			}
//		}
		
	}
		/* xmlFile.sectionCount = sections.getLength();
		System.out.println(sectionCount);
		for (int temp = 0; temp < sectionCount; temp++) {
			Node nNode = sections.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				System.out.println(((Element) nNode).getAttribute("name"));
			}
			//System.out.println(nNode.getTextContent());
			}
		*/
		//getting all the fields (only the ones that show up with "name" 
		//and printing them out
		
		//gettingAttributes(doc);
		
		//for (int i = 0; i < allFields.getLength(); i++) {
			//System.out.println(allFields.item(i).getLocalName());
		//}
 		//String name = allFields.getNamedItem("name"). getNodeValue();
		//System.out.println(name);
		
		//remember attributes are nested in the node directly, it's considered a child element
		//if it's below a parent element with its own tag name
		
	
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
