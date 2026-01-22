package alert.ftext;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom2.JDOMException;

public class Alert implements Comparable<Alert>, Serializable {

	private static final long serialVersionUID = 1L;
	private int id;	
	double timestamp;
	String protocol;
	private String ipSrc;
	private int portSrc;
	private String ipDest;
	private int portDest;
	String clss;
	int repetition;
	
	public Alert(int id, double timestamp, String protocol, String ipSrc, int portSrc, String ipDest,
			int portDest, String clss) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.protocol = protocol;
		this.ipSrc = ipSrc;
		this.portSrc = portSrc;
		this.ipDest = ipDest;
		this.portDest = portDest;
		this.clss = clss;
		this.repetition = 1;
	}
	
	public Alert(Alert a) {
		super();
		this.id = a.id;
		this.timestamp = a.timestamp;
		this.protocol = a.protocol;
		this.ipSrc = a.ipSrc;
		this.portSrc = a.portSrc;
		this.ipDest = a.ipDest;
		this.portDest = a.portDest;
		this.clss = a.clss;
		this.repetition = a.repetition;
	}
	
	public Alert(int id, String ipSrc, String ipDest, String time, String date) {
		super();
		this.id = id;
		this.ipSrc = ipSrc;
		this.ipDest = ipDest;
		this.repetition = 1;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void incrementRepetition(){
		this.repetition++;
	}

	public boolean equals(Alert alert, boolean checkSrcPorts, boolean checkDstPorts){
		boolean res=false;
		if(id==alert.id && ipSrc.equals(alert.ipSrc) && ipDest.equals(alert.ipDest)){
			res=true;
			if(checkSrcPorts && portSrc!=alert.portSrc)
				res=false;
			if(checkDstPorts && portDest!=alert.portDest)
				res=false;
		}
		return res;
	}
	
	/*
	 * Returns whether 2 ips are equal.
	 */
	public boolean sameIP(String ip1, String ip2)
	{
		try {
			int[] n1 = new int[4];
			int[] n2 = new int[4];
			String regex = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
			Pattern p = Pattern.compile(regex);
			Matcher m;
			
			m = p.matcher(ip1);
			if (!m.matches()) {
				System.out.println(ip1);
				System.out.println(" ");

				throw new JDOMException("Malformed ip String");
			}

			for (int i = 0; i < n1.length; i++)
				n1[i] = Integer.parseInt(m.group(i+1).trim());
			
			m = p.matcher(ip2);

			if (!m.matches())
				throw new JDOMException("Malformed ip String");
			
			for (int i = 0; i < n1.length; i++)
				n2[i] = Integer.parseInt(m.group(i+1).trim());

			for (int i = 0; i < n1.length; i++)
				if (n1[i] != n2[i])
					return false;

			return true;
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/*
	 * Returns whether alert date in inside truth file interval interval.
	 * I really hate this sloppy method, but hell I can't find a better way.
	 * These files are just shit!
	 */
	public boolean sameTime(String alert, String truth, String duration)
	{
		try {
			long millisecs = Long.parseLong(duration);
			// Format: YYYY-MM-DDThh:mm:ss.ss+hh:mm
			//String regex = "([^,\\.]*)[,\\.](\\d*)Z"; vecchio regex
			
			String regex = "\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\dZ";
			Pattern p = Pattern.compile(regex);
			Matcher m;
			
			m = p.matcher( alert );
			if (!m.matches())
				throw new JDOMException("Malformed IDMEF document: bad time String");

			int alertYear = Integer.parseInt(alert.substring(0, 4));
			int alertMonth = Integer.parseInt(alert.substring(5, 7));
			int alertDay = Integer.parseInt(alert.substring(8, 10));
			int alertHour = Integer.parseInt(alert.substring(11, 13));
			int alertMin = Integer.parseInt(alert.substring(14, 16));
			int alertSec = Integer.parseInt(alert.substring(17, 19));
			
			m = p.matcher( truth );

			if (!m.matches())
				throw new JDOMException("Malformed IDMEF document: bad time String");

			int truthYear = Integer.parseInt(truth.substring(0, 4));
			int truthMonth = Integer.parseInt(truth.substring(5, 7));
			int truthDay = Integer.parseInt(truth.substring(8, 10));
			int truthHour = Integer.parseInt(truth.substring(11, 13));
			int truthMin = Integer.parseInt(truth.substring(14, 16));
			int truthSec = Integer.parseInt(truth.substring(17, 19));
			
			//System.out.println(truthYear+"-"+truthMonth+"-"+truthDay+"T"+
			//		truthHour+":"+truthMin+":"+truthSec+"Z");
			
			TimeZone zone = TimeZone.getTimeZone("GMT"); // Truth file should always GMT!
			Calendar alertCalendar = new GregorianCalendar(zone);
			Calendar truthCalendar = new GregorianCalendar(zone);
			
			alertCalendar.set(alertYear, alertMonth, alertDay, alertHour, alertMin, alertSec);
			truthCalendar.set(truthYear, truthMonth, truthDay, truthHour, truthMin, truthSec);
			
			long alertTime = alertCalendar.getTimeInMillis();
			long startTime = truthCalendar.getTimeInMillis();
			long endTime = startTime + millisecs;
			
			// Relax margins by one second
			startTime -= 1000;
			endTime += 1000;
			
			// OK now compare
			if (alertTime >= startTime && alertTime <= endTime) return true;
	
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		return false;
	}
	public String getIpSrc() {
		return ipSrc;
	}

	public void setIpSrc(String ipSrc) {
		this.ipSrc = ipSrc;
	}

	public String getIpDest() {
		return ipDest;
	}

	public void setIpDest(String ipDest) {
		this.ipDest = ipDest;
	}

	public int getPortSrc() {
		return portSrc;
	}

	public void setPortSrc(int portSrc) {
		this.portSrc = portSrc;
	}

	public int getPortDest() {
		return portDest;
	}

	public void setPortDest(int portDest) {
		this.portDest = portDest;
	}



	public int compareTo(Alert a) {
		if(timestamp==a.timestamp) return 0;
		if(timestamp<a.timestamp) return -1;		
		return 1;
	}

	public double getTimestamp() {
		return timestamp;
	}
	public String toString(){
		return id+" "+timestamp+" "+protocol+" "+ipSrc+" "+portSrc+" "+ipDest+" "+portDest+" "+clss+" "+repetition;
	}
}
