package alert.idmef;

import java.util.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;


public class AlertSet {

	
	private Document alertDoc;
	// private DocType type; // Not used now
	private Namespace alertNameSpace;
	private Element alertRoot;
	private List<Element> alertElements;

	private List<Alert> alerts;
	
	/*
	 * Variables needed to handle file
	 */
	private String alertFile;
	private File f;
	
	
	
	AlertSet(){
		setAlerts(null);
		setF(null);
		setAlertFile(null);
	}
	public String getAlertFile() {
		return alertFile;
	}
	public void setAlertFile(String alertFile) {
		this.alertFile = alertFile;
	}
	AlertSet(String idmefFile){
		setAlerts(null);
		setF(null);
		setAlertFile(idmefFile);
		
		readAlertFile(idmefFile);
		readAlerts();
		
	}
	
	public void readAlerts() {
		try {
					
			// Get the list of alerts in alert file
			alertElements = alertRoot.getChildren("Alert", alertNameSpace);
			int numAlerts = alerts.size();

			if (numAlerts == 0)
				throw new JDOMException("Empty or malformed IDMEF document");
			
			for (int i = 0; i < numAlerts; i++) {
				alerts.add(readAlert(alertElements.get(i)));				
			}
			
			
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}
	
	private Alert readAlert(Element alertEl)
	{
		int id;	
		String ipSrc;
		String ipDest;
		String time;
		String date;
		Alert alert = null;
		try {
			
			
			id = Integer.parseInt(alertEl.getAttributeValue("alertid"));
			
				Element alertTrg = alertEl.getChild("Target", alertNameSpace).
					getChild("Node", alertNameSpace).
					getChild("Address", alertNameSpace).
					getChild("address", alertNameSpace);

				Element alertSource = alertEl.getChild("Source", alertNameSpace).
					getChild("Node", alertNameSpace).
					getChild("Address", alertNameSpace).
					getChild("address", alertNameSpace);

				if (alertTrg == null || alertSource == null) 
					throw new JDOMException("Malformed IDMEF document: no Target address");
				
				ipDest = alertTrg.getTextNormalize();
				ipSrc = alertSource.getTextNormalize();
				
				Element alertDate = alertEl.getChild("Time", alertNameSpace).
						getChild("date", alertNameSpace);
				
				date = alertDate.getTextNormalize();
				
				Element alertTime = alertEl.getChild("Time", alertNameSpace).
						getChild("time", alertNameSpace);
				
				time = alertTime.getTextNormalize();
				
				alert = new Alert(id, ipSrc, ipDest, time, date);
				
			
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		return alert;
	}
	

	
	/*
	 * Reads a file into a DOM document
	 * Alert file version
	 */
	private void readAlertFile(String path)
	{

		FileInputStream fis;
		InputStreamReader isr;
		BufferedReader br;
		File f;
		f = new File(path);
		

		try {
			fis = new FileInputStream(f);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			SAXBuilder builder = new SAXBuilder();
			alertDoc = builder.build(br);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		alertRoot = alertDoc.getRootElement();
		alertNameSpace = alertRoot.getNamespace();
	}
	

	public void setAlerts(ArrayList<Alert> alerts) {
		this.alerts = alerts;
	}
	public File getF() {
		return f;
	}

	public void setF(File f) {
		this.f = f;
	}

	

	public void addAlert(Alert a){
		alerts.add(a);
	}
	public Alert getAlert(int i){
		return alerts.get(i);
	}
	public void parseIDMEFfile(){
		
	}

}
