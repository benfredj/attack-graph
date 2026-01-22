package alert.ftext;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import scenario.Config;
import alert.ftext.Alert;


public class AlertSet implements Serializable{

	private static final long serialVersionUID = 1L;
	// PRIVATE 
	private final File fFile;
	private List<Alert> alerts;
	private Config config;

	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public static void main(String[] args) throws FileNotFoundException {
	    
		/*File directory = new File (".");
		try {
			 System.out.println ("Current directory's canonical path: " 
			  + directory.getCanonicalPath()); 
			   System.out.println ("Current directory's absolute  path: " 
			  + directory.getAbsolutePath());
			 }catch(Exception e) {
			 System.out.println("Exceptione is ="+e.getMessage());
			  }
		*/
		Config config = new Config(31/*destNetworkPrefixes*/, false/*alertSeriesCheckSrcPorts*/,
				false/*alertSeriesCheckDstPorts*/, false/*scenarioCheckDstPorts*/,
				false/*saveAlertSeries*/, "-2-alert.fast.alerts",
				"output-alert.fast.txt", "alert.fast", Config.SNORTFAST,
				//"output-ctf17-alerts-dataset-human-readable-10000.txt", "ctf17-alerts-dataset-human-readable-10000.txt",
				20 /*maxScenarioNodesForMatrix*/, 60/*maxScenarioNodesForGraphImage*/, 2012);
		
		AlertSet parser = new AlertSet(config);
	    parser.processLineByLine();
	    //for (Alert alert : parser.alerts)
        //    System.out.println(alert);
	    //for (int i=0; i<20; i++)
        //    System.out.println(parser.alerts.get(i));
	    log("Done.");
	  }
	  
	  /**
	   Constructor.
	   @param aFileName full name of an existing, readable file.
	  */
	  public AlertSet(Config config){
	    fFile = new File(config.getDatasetFile());
	    alerts = new ArrayList<Alert>();
	    this.config = config;
	  }
	  
	  /** Template method that calls {@link #processLine(String)}.  */
	  public final void processLineByLine() throws FileNotFoundException {
	    //Note that FileReader is used, not File, since File is not Closeable
	    Scanner scanner = new Scanner(new FileReader(fFile));
	    try {
	    	if(config.getLineFormat() == Config.SNORTSHARP){
		      //first use a Scanner to get each line
		      while ( scanner.hasNextLine() ){
		        alerts.add(processLine( scanner.nextLine()));
		      }
	    	}else{
			      //first use a Scanner to get each line
			      while ( scanner.hasNextLine() ){
			        alerts.add(processSnortAlertFastLine( scanner.nextLine()));
			      }
	    		
	    	}
	    }
	    finally {
	      //ensure the underlying stream is always closed
	      //this only has any effect if the item passed to the Scanner
	      //constructor implements Closeable (which it does in this case).
	      scanner.close();
	      
	    }
	  }

	  public void sort(){
		  Collections.sort(alerts);
		  
	  }
	  /** 
	   Overridable method for processing lines in different ways.
	    
	   <P>This simple default implementation expects simple name-value pairs, separated by an 
	   '=' sign. Examples of valid input : 
	  */
	  protected Alert processLine(String aLine){
	    //use a second Scanner to parse the content of each line 
	    double timestamp;
		String protocol;
		String ipSrc;
		int portSrc;
		String ipDest;
		int portDest;
		String clss;
		int id;
		//!TimeStamp#Protocol#SRC_IP#SRC_PORT#DST_IP#DST_PORT#SIG_GEN#SIG_ID#SIG_REV#ALERT_MSG#CLASS#PRIORITY#REF_LIST sparated by "|"
		  Scanner scanner = new Scanner(aLine);
	    scanner.useDelimiter("#");
	    if ( scanner.hasNext() ){
	    	timestamp = Double.parseDouble(scanner.next().trim());
	    	protocol = scanner.next().trim();
	      	ipSrc = scanner.next().trim();
			portSrc = Integer.parseInt(scanner.next().trim());
			ipDest = scanner.next().trim();
			portDest = Integer.parseInt(scanner.next().trim());
			scanner.next(); //SIG_GEN
			id = Integer.parseInt(scanner.next().trim());
			scanner.next(); //SIG_REV
			scanner.next(); //#ALERT_MSG
			clss = scanner.next().trim();
	     
	      
	    }
	    else {
	      log("Empty or invalid line. Unable to process.");
	      scanner.close();
	      return null;
	    }
	    scanner.close();
	    return new Alert(id,timestamp, protocol,ipSrc,portSrc,ipDest,portDest,clss);
	  }
	  
	  protected Alert processSnortAlertFastLine(String aLine){
		    //use a second Scanner to parse the content of each line 
		    double timestamp;
			String protocol;
			String ipSrc;
			int portSrc;
			String ipDest;
			int portDest;
			String clss="";
			int id;
			
			int month,day,hour,minute,second,nanosecond;
			String dummy="";
			//format: MONTH/DAY-HOUR:MIN:SEC.MILIIS [**] [Generator ID:SID:revision] alert_msg [**] [Classification: ?] [Priority: ?] {protocol} IP_SRC:port_src -> IP_DEST:port_dest
			  Scanner scanner = new Scanner(aLine);
			  //System.out.println(aLine);
		    scanner.useDelimiter("/");
		    if ( scanner.hasNext() ){
		    	month = scanner.nextInt();
		    	scanner.skip("/");
		    	//System.out.println(month+" "+scanner.next());
		    	scanner.reset().useDelimiter("-");
		    	day = scanner.nextInt();scanner.skip("-");
		    	scanner.reset().useDelimiter(":");		    	
		    	hour = scanner.nextInt();scanner.skip(":");
		    	scanner.reset().useDelimiter(":");
		    	minute = scanner.nextInt();scanner.skip(":");
		    	//System.out.println(month+" "+day+" "+hour+" "+minute);
				scanner.reset().useDelimiter("\\.");
		    	second = scanner.nextInt();scanner.skip("\\.");
		    	scanner.reset().useDelimiter(" ");
		    	nanosecond = scanner.nextInt();
		    	Calendar cal = Calendar.getInstance();
		    	cal.set(config.getDefaultYear(), month, day, hour, minute, second);
		    	//Timestamp ts = new Timestamp(cal.getTimeInMillis());
		    	timestamp = Double.parseDouble(cal.getTimeInMillis()+"."+nanosecond);
		    	scanner.reset().useDelimiter(":");scanner.next();
		    	id = scanner.nextInt();
		    	scanner.reset().useDelimiter("\\{");
		    	scanner.next();
		    	scanner.skip("\\{");
		    	scanner.reset().useDelimiter("\\}");
		    	protocol = scanner.next().trim();scanner.skip("\\} ");
		    	
		    	if(protocol.equals("ICMP") || protocol.equals("PROTO:255")){
		    		scanner.reset().useDelimiter(" ");
		    		ipSrc = scanner.next().trim();
		    		portSrc = 0;
		    		scanner.next();scanner.skip(" ");
		    		scanner.reset().useDelimiter("\\s");
		    		ipDest = scanner.next().trim();
		    		portDest = 0;
		    	}else{
		    		scanner.reset().useDelimiter(":");
		    		ipSrc = scanner.next().trim();scanner.skip(":");
		    		scanner.reset().useDelimiter(" ");
		    		portSrc = scanner.nextInt();
		    		scanner.next();
		    		scanner.reset().useDelimiter(":");
		    		ipDest = scanner.next().trim();scanner.skip(":");
		    		scanner.useDelimiter("\\s");
			    	portDest = scanner.nextInt();
		    	}
		    }
		    else {
		      log("Empty or invalid line. Unable to process.");
		      scanner.close();
		      return null;
		    }
		    scanner.close();
		    return new Alert(id,timestamp, protocol,ipSrc,portSrc,ipDest,portDest,clss);
		  }
	  
	  private static void log(Object aObject){
	    System.out.println(String.valueOf(aObject));
	  }
	  
	  /*private String quote(String aText){
	    String QUOTE = "'";
	    return QUOTE + aText + QUOTE;
	  }*/

}
