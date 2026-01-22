package absorbingChain.applet;

//AbsorbingChain.java

//Written by Julian Devlin, 8/97, for the text book
//"Introduction to Probability," by Charles M. Grinstead & J. Laurie Snell

import java.applet.Applet;
import java.awt.*;

public class AbsorbingChain
	extends java.applet.Applet
{
	Label numl1, numl2;			// Controls
	TextField num1, num2;
	
	Button create;
	
	AbsorbingWindow abw;
	
	GridBagLayout gbl;
	GridBagConstraints cc;
		
	// Initialize applet
	public void init()
	{	
		numl1 = new Label("Transient states = ");			// Create controls
		num1 = new TextField("4", 4);
		numl2 = new Label("Absorbing states = ");			// Create controls
		num2 = new TextField("2", 4);
		create = new Button("Create");
		
		gbl = new GridBagLayout();
		cc = new GridBagConstraints();
		setLayout(gbl);
		
		cc.gridx = 0;
		cc.gridy = 0;
		gbl.setConstraints(numl1, cc);
		add(numl1);
		
		cc.gridx = 1;
		gbl.setConstraints(num1, cc);
		add(num1);
		
		cc.gridx = 0;
		cc.gridy = 1;
		gbl.setConstraints(numl2, cc);
		add(numl2);
		
		cc.gridx = 1;
		gbl.setConstraints(num2, cc);
		add(num2);
		
		cc.gridx = 0;
		cc.gridy = 2;
		cc.gridwidth = 2;
		gbl.setConstraints(create, cc);
		add(create);
		
		validate();
	}
	
	// Handle events
	public boolean handleEvent(Event evt)
	{
		String minStr, maxStr;
		if (evt.target instanceof Button)
		{
			if (evt.target == create && evt.id == Event.ACTION_EVENT)	// When button is clicked
			{
     		create(Integer.valueOf(num1.getText()).intValue(), 
     			Integer.valueOf(num2.getText()).intValue());
     		return true;					// Generate correct number of tosses
			}
		}
		return super.handleEvent(evt);	// Handle other events as usual
	}
	
	// Calculate probabilities
 public void create(int trans, int absorb)
 {	
		abw = new AbsorbingWindow(trans, absorb);
 	Point p = location();
 	abw.move(p.x + 50, p.y + 50);
 	abw.init();
 	abw.show();
 	abw.resize(abw.preferredSize());
	 	abw.validate();
	}
}




