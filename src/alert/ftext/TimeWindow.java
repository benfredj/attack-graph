package alert.ftext;

import java.util.ArrayList;
import java.util.List;

public class TimeWindow {
	double timewindow;
	int currentAlertIndex;
	double initialTimestamp;
	
	public TimeWindow(double timewindow) {
		this.timewindow = timewindow;
		currentAlertIndex = 0;
		initialTimestamp = 0;
	}
	//window starts from 0
	public List<Alert> getNextWindowAlert(List<Alert> alerts, int window){
		
		boolean done = false;
		
		List<Alert> l = new ArrayList<Alert>();
		if(currentAlertIndex==0)
			initialTimestamp = alerts.get(0).getTimestamp();
		
		
		
		while(!done){
			if(currentAlertIndex>=alerts.size()) done = true;
			else if(alerts.get(currentAlertIndex).getTimestamp()<(window*timewindow+initialTimestamp)){
				l.add(new Alert(alerts.get(currentAlertIndex)));
				currentAlertIndex++;
				//System.out.println(currentAlertIndex);
			}else
				done = true;
		}
		return l;
	}
}
