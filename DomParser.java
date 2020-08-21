
package com.tutorialspoint.xml;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class DomParser {
	
	//for each type of experiment/per folder
	public static ArrayList<HashMap<String, Integer>> hashMaps;
	public static HashMap<String, Integer> combinedHashMap;
	public static int numberOfExperiments;
	public static HashMap<String, Integer> initialHashMap;
	public static HashMap<String, Integer> combiningHashMap;
	public static Object[] keys;
	
	//for all experiments/whole folder
	public static ArrayList<HashMap<String, Integer>> totalHashMaps;
	public static HashMap<String, Integer> totalCombinedHashMap;
	public static int totalNumberOfExperiments;
	public static HashMap<String, Integer> totalInitialHashMap;
	public static HashMap<String, Integer> totalCombiningHashMap;
	public static Set<String> totalKeys;
	public static int total;
	
	public static DecimalFormat df = new DecimalFormat("0.00");
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException, SAXException, IOException, XPathExpressionException {
		
		String ELNDataPathname = "C:\\Users\\kaursima\\Desktop\\Experiment Examples\\";
		File exampleData = new File(ELNDataPathname);
		File[] experimentTypes = exampleData.listFiles();
		total = 0;
		
		totalHashMaps = new ArrayList<HashMap<String, Integer>>();
		
		//getting the counts per fields for each individual experiment
		for (File file_ : experimentTypes) {
			String pathName = ELNDataPathname + file_.getName();
			File ELNData = new File(pathName);
			File[] allFiles = ELNData.listFiles();
			hashMaps = new ArrayList<HashMap<String, Integer>>();

			for (File file : allFiles) {
				String experimentPath = ELNDataPathname + file_.getName() + "\\" + file.getName();
				xmlFile experiment = new xmlFile(experimentPath);
				hashMaps.add(experiment.returnFieldCountsMap());
				total++;
			}
			numberOfExperiments = hashMaps.size();
			keys = hashMaps.get(0).keySet().toArray();
			if (numberOfExperiments > 1) {
				for (int i = 0; i < numberOfExperiments - 1; i++) {
					initialHashMap = hashMaps.get(i);
					combiningHashMap = hashMaps.get(i+1);
					for (Object key : initialHashMap.keySet()) {
						Integer initialCount = initialHashMap.get(key);
						Integer combiningCount = 0;
						if (combiningHashMap.containsKey(key)) {
							combiningCount = combiningHashMap.get(key);
						} 
						combiningHashMap.put((String) key,  initialCount + combiningCount);
					}
					initialHashMap = combiningHashMap;
				}
			} else {
				initialHashMap = hashMaps.get(0);
			}
			totalHashMaps.add(initialHashMap);

			
			//printing out the values and keys of the combined hashmap, experiment type, number of experiments
			Set<String> keySet = initialHashMap.keySet();
			ArrayList<String> keys = new ArrayList<String>();
			for (String x : keySet) {
				keys.add(x);
			}
			Collections.sort(keys);
			
			System.out.println("--------------");
			System.out.println(file_.getName() + ", Count: " + numberOfExperiments);
			System.out.println("--------------"); 
			for (Object key : keys) {
				int value = initialHashMap.get(key);
		    	float percentage = ((float) value / (float) numberOfExperiments) * 100;
				System.out.println(key + ": " + value + " - " + df.format(percentage));
			} 
			
			//outputing the results into CSV files
			File finalData = new File("C:\\Users\\kaursima\\Desktop\\CSV Data");
			 try (FileWriter writer = new FileWriter(new File(finalData + "\\" + file_.getName() + " - " + numberOfExperiments + ".csv"))) {

				      StringBuilder sb = new StringBuilder();
				      sb.append("Tab");
				      sb.append('\t');
				      sb.append("Section");
				      sb.append('\t');
				      sb.append("Count");
				      sb.append('\t');
				      sb.append("Percentage of Total");
				      sb.append('\n');

				      for (String key : keys) {
				    	  Integer value = initialHashMap.get(key);
				    	  Integer dash = key.indexOf("-");
				    	  
				    	  String tab = key.substring(0, dash);
				    	  String section = key.substring(dash + 1, key.length());
				    	  
				    	  float percentage = ((float) value / (float) numberOfExperiments) * 100;
				    	  
				    	  sb.append(tab);
					      sb.append('\t');
				    	  sb.append(section);
					      sb.append('\t');
				    	  sb.append(value);
				    	  sb.append('\t');
				    	  sb.append(df.format(percentage));
					      sb.append('\n');
				      }
				      				      
				      writer.write(sb.toString());
				      writer.flush();
				      writer.close();
				     
				    } catch (FileNotFoundException e) {
				      System.out.println(e.getMessage());
				    }
				    
			
		}
		
		//repeating same process for all files
		
		totalKeys = totalHashMaps.get(0).keySet();
		
		for (int j = 0; j < totalHashMaps.size() - 1; j++) {
			totalInitialHashMap = totalHashMaps.get(j);
			totalCombiningHashMap = totalHashMaps.get(j+1);
			for (String totalKey : totalInitialHashMap.keySet()) {
				Integer initial = totalInitialHashMap.get(totalKey);
				Integer combining = 0;
				if (totalCombiningHashMap.containsKey(totalKey)) {
					combining = totalCombiningHashMap.get(totalKey);
				} 
				totalCombiningHashMap.put(totalKey, initial + combining);
			}
			totalInitialHashMap = totalCombiningHashMap;
		}
		
		//printing out the counts for all files
		
		System.out.println("--------------");
		System.out.println("All Files" + " : " + total);
		System.out.println("--------------"); 
		
		Set<String> keySet1 = totalInitialHashMap.keySet();
		ArrayList<String> totalKeys = new ArrayList<String>();
		for (String x : keySet1) {
			totalKeys.add(x);
		}
		Collections.sort(totalKeys);
		
		for (String key_ : totalKeys) {
			Integer count = totalInitialHashMap.get(key_);
			float percentage = (float) count / (float) total;
			System.out.println(key_ + ": " + count + " - " + df.format(percentage));
		}
	
		//putting all files data into a CSV file
		
		File finalData1 = new File("C:\\Users\\kaursima\\Desktop\\CSV Data");
		 try (FileWriter writer = new FileWriter(new File(finalData1 + "\\All Files - " + total + ".csv"))) {

			      StringBuilder sb = new StringBuilder();
			      sb.append("Tab");
			      sb.append('\t');
			      sb.append("Section");
			      sb.append('\t');
			      sb.append("Count");
			      sb.append('\t');
			      sb.append("Percentage of Total");
			      sb.append('\n');

			      for (String key : totalKeys) {
			    	  Integer value = totalInitialHashMap.get(key);
			    	  Integer dash = key.indexOf("-");
			    	  
			    	  String tab = key.substring(0, dash);
			    	  String section = key.substring(dash + 1, key.length());
			    	  
			    	  float percentage = ((float) value / (float) total) * 100;
			 
			    	  sb.append(tab);
				      sb.append('\t');
			    	  sb.append(section);
				      sb.append('\t');
			    	  sb.append(value);
				      sb.append('\t');
			    	  sb.append(df.format(percentage));
				      sb.append('\n');
				      
			      }
			      			      
			      writer.write(sb.toString());
			      writer.flush();
			      writer.close();
			     
			    } catch (FileNotFoundException e) {
			      System.out.println(e.getMessage());
			    }
		}	
	}
