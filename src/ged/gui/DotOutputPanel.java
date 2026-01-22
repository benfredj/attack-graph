package ged.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

/**
 * DOT expression input rendering component.
 * 
 * @author Ouissem Ben Fredj
 */
class DotOutputPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextArea combine;
	
	
	DotOutputPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		combine = new JTextArea();
		
		initTextArea(combine);
		
				
		setBorder(BorderFactory.createTitledBorder(BorderFactory.
				createEtchedBorder(EtchedBorder.LOWERED), "Edit Path: "));
	}

	DotOutputPanel(String dot) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		combine = new JTextArea();
		
		initTextArea(combine);
		
				
		setBorder(BorderFactory.createTitledBorder(BorderFactory.
				createEtchedBorder(EtchedBorder.LOWERED), "Edit Path: "));
		combine.setText(dot);
	}
	
	private void initTextArea(JTextArea textArea) {
		JScrollPane areaScrollPane = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		areaScrollPane.setPreferredSize(new Dimension(400, 100));
		
		add(areaScrollPane);
	}
	
	
	
	String getCombineDot() {
		return combine.getText();
	}
	


}
