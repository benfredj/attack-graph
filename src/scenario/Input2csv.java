package scenario;


import ged.editpath.*;
import ged.editpath.editoperation.*;
import ged.graph.DecoratedGraph;
import ged.graph.GraphConverter;
import ged.graph.ParseException;
import ged.processor.CostContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import alert.ftext.*;


public class Input2csv {

	public static void main(String[] args)  {
		int year = 2012;
		PrintStream output = null;
		try {
			output = new PrintStream(new FileOutputStream("smia2012-ts-ipsrc-ipdst-alertid.csv"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		File fFile = new File("smia2012.noping.fast");
		int lineFormat = Config.SNORTFAST;//Config.SNORTSHARP
		
		try {
			processLineByLine(fFile, output, lineFormat, year);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** Template method that calls {@link #processLine(String)}.  */
	  public static void processLineByLine(File fFile, PrintStream output, int lineFormat, int year) throws FileNotFoundException {
	    //Note that FileReader is used, not File, since File is not Closeable
	    Scanner scanner = new Scanner(new FileReader(fFile));
	    try {
	    	if(lineFormat == Config.SNORTSHARP){
		      //first use a Scanner to get each line
		      while ( scanner.hasNextLine() ){
		    	  output.println(processLine( scanner.nextLine()));
		      }
	    	}else{
			      //first use a Scanner to get each line
			      while ( scanner.hasNextLine() ){
			    	  output.println(processSnortAlertFastLine( year, scanner.nextLine()));
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
	  protected static String processLine(String aLine){
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
		    //return id+";"+timestamp+";"+ protocol+";"+ipSrc+";"+portSrc+";"+ipDest+";"+portDest+";"+clss;
		    return timestamp+";"+ ipSrc+";"+ipDest+";"+portDest+";"+id;
		  }
		  
		  protected static String processSnortAlertFastLine(int year, String aLine){
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
			    	cal.set(year, month, day, hour, minute, second);
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
			    //return id+";"+timestamp+";"+ protocol+";"+ipSrc+";"+portSrc+";"+ipDest+";"+portDest+";"+clss;
			    return timestamp+";"+ ipSrc+";"+ipDest+";"+portDest+";"+id;
			  }
		  private static void log(Object aObject){
			    System.out.println(String.valueOf(aObject));
			  }
}
