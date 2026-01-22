package absorbingChain.applet;
// AbsorbingWindow.java

// Written by Julian Devlin, 8/97, for the text book
// "Introduction to Probability," by Charles M. Grinstead & J. Laurie Snell

import java.applet.Applet;
import java.awt.*;

import absorbingChain.utilities.*;


public class AbsorbingWindow
	extends java.awt.Frame
{
	Panel dispArea;
	Panel controls;		// Panel for user controls
	Panel qPanel, rPanel;
	
	TextField[][] Q;
	TextField[][] R;
	Matrix qMat, rMat, iqMat, nMat, cMat, tMat, bMat;
	double[][] rls;
	double[][] cls1;
	double[][] cls2;
	Matrix rows1, rows2, rows3, rows4, rows5, columns1, columns2, cols1, cols2;
	Label qLab, rLab, nLab, tLab, bLab;
	
	Button go;
	
	GridBagLayout gbl, qgbl, rgbl, dgbl;
	GridBagConstraints cc, qcc, rcc, dcc;
	
	int trans, absorb;
	
	Color labC;
		
	public AbsorbingWindow(int t, int a) {
		super("AbsorbingChain");
		trans = t;
		absorb = a;
	}
		
	// Initialize applet
	public void init()
	{	
		rls = new double[trans][1];
		for (int i = 0; i < trans; i++) {
			rls[i][0] = i + 1;	
		}
		
		cls1 = new double[1][trans];
		for (int i = 0; i < trans; i++) {
			cls1[0][i] = i + 1;	
		}
		
		cls2 = new double[1][absorb];
		for (int i = 0; i < absorb; i++) {
			cls2[0][i] = trans + i + 1;	
		}
		
		
		go = new Button("Go");
		
		if (trans == 4 && absorb == 2) {
			Q = new TextField[4][4];
			Q[0][0] = new TextField("0", 4);
			Q[0][1] = new TextField(".5", 4);
			Q[0][2] = new TextField("0", 4);
			Q[0][3] = new TextField("0", 4);
			Q[1][0] = new TextField(".5", 4);
			Q[1][1] = new TextField("0", 4);
			Q[1][2] = new TextField(".5", 4);
			Q[1][3] = new TextField("0", 4);
			Q[2][0] = new TextField("0", 4);
			Q[2][1] = new TextField(".5", 4);
			Q[2][2] = new TextField("0", 4);
			Q[2][3] = new TextField(".5", 4);
			Q[3][0] = new TextField("0", 4);
			Q[3][1] = new TextField("0", 4);
			Q[3][2] = new TextField(".5", 4);
			Q[3][3] = new TextField("0", 4);
			
			R = new TextField[4][2];
			R[0][0] = new TextField(".5", 4);
			R[0][1] = new TextField("0", 4);
			R[1][0] = new TextField("0", 4);
			R[1][1] = new TextField("0", 4);
			R[2][0] = new TextField("0", 4);
			R[2][1] = new TextField("0", 4);
			R[3][0] = new TextField("0", 4);
			R[3][1] = new TextField(".5", 4);
		}
		else {
			Q = new TextField[trans][trans];
			for (int i = 0; i < trans; i++) {
				for (int j = 0; j < trans; j++) {
					Q[i][j] = new TextField("0", 4);
				}
			}
			R = new TextField[trans][absorb];
			for (int i = 0; i < trans; i++) {
				for (int j = 0; j < absorb; j++) {
					R[i][j] = new TextField("0", 4);
				}
			}	
		}
		
		qLab = new Label("Q = ");
		qLab.setAlignment(Label.RIGHT);
		rLab = new Label("R = ");
		rLab.setAlignment(Label.RIGHT);
		nLab = new Label("N = ");
		nLab.setAlignment(Label.RIGHT);
		tLab = new Label("t = ");
		tLab.setAlignment(Label.RIGHT);
		bLab = new Label("B = ");
		bLab.setAlignment(Label.RIGHT);
		
		
		labC = new Color(100, 0, 0);
		rows1 = new Matrix(rls);
		rows1.setColor(labC);
		rows2 = new Matrix(rls);
		rows2.setColor(labC);
		rows3 = new Matrix(rls);
		rows3.setColor(labC);
		rows4 = new Matrix(rls);
		rows4.setColor(labC);
		rows5 = new Matrix(rls);
		rows5.setColor(labC);
		columns1 = new Matrix(cls1);
		columns1.setColor(labC);
		columns2 = new Matrix(cls1);
		columns2.setColor(labC);
		cols1 = new Matrix(cls2); 
		cols1.setColor(labC);
		cols2 = new Matrix(cls2);
		cols2.setColor(labC);
		
		dispArea = new Panel();				// Set up window
		controls = new Panel();
		qPanel = new Panel();
		rPanel = new Panel();
		setLayout(new BorderLayout(5, 5));
		
		add("South", controls);
		add("Center", dispArea);
		
		qgbl = new GridBagLayout();
		qcc = new GridBagConstraints();
		qPanel.setLayout(qgbl);
		for (int r = 0; r < trans; r++) {
			for (int c = 0; c < trans; c++) {
				qcc.gridx = c;
				qcc.gridy = r;
				qgbl.setConstraints(Q[r][c], qcc);
				qPanel.add(Q[r][c]);
			}	
		}
		
		rgbl = new GridBagLayout();
		rcc = new GridBagConstraints();
		rPanel.setLayout(rgbl);
		for (int r = 0; r < trans; r++) {
			for (int c = 0; c < absorb; c++) {
				rcc.gridx = c;
				rcc.gridy = r;
				rgbl.setConstraints(R[r][c], rcc);
				rPanel.add(R[r][c]);
			}	
		}
		
		dgbl = new GridBagLayout();
		dcc = new GridBagConstraints();
		dispArea.setLayout(dgbl);
		dcc.fill = GridBagConstraints.BOTH;
		dcc.gridx = 0;
		dcc.gridy = trans / 2;
		dcc.gridwidth = 2;
		dcc.gridheight = 1;
		dgbl.setConstraints(qLab, dcc);
		dispArea.add(qLab);
		
		dcc.gridx = 2;
		dcc.gridy = 1;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(rows1, dcc);
		dispArea.add(rows1);
		
		dcc.gridx = 3;
		dcc.gridy = 0;
		dcc.gridwidth = trans;
		dcc.gridheight = 1;
		dgbl.setConstraints(columns1, dcc);
		dispArea.add(columns1);
		
		dcc.gridx = 3;
		dcc.gridy = 1;
		dcc.gridwidth = trans;
		dcc.gridheight = trans;
		dgbl.setConstraints(qPanel, dcc);
		dispArea.add(qPanel);
		
		dcc.gridx = 3 + trans;
		dcc.gridy = trans / 2;
		dcc.gridwidth = 2;	
		dcc.gridheight = 1;
		dgbl.setConstraints(rLab, dcc);
		dispArea.add(rLab);
		
		dcc.gridx = 5 + trans;
		dcc.gridy = 1;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(rows2, dcc);
		dispArea.add(rows2);
		
		dcc.gridx = 6 + trans;
		dcc.gridy = 0;
		dcc.gridwidth = absorb;
		dcc.gridheight = 1;
		dgbl.setConstraints(cols1, dcc);
		dispArea.add(cols1);
		
		dcc.gridx = 6 + trans;
		dcc.gridy = 1;
		dcc.gridwidth = absorb;
		dcc.gridheight = trans;
		dgbl.setConstraints(rPanel, dcc);
		dispArea.add(rPanel);
		
		qMat = new Matrix(Q);
		rMat = new Matrix(R);
		
		dcc.gridx = 0;
		dcc.gridy = 1 + trans + trans / 2;
		dcc.gridwidth = 2;
		dcc.gridheight = 1;
		dgbl.setConstraints(nLab, dcc);
		dispArea.add(nLab);
		
		dcc.gridx = 2;
		dcc.gridy = 2 + trans;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(rows3, dcc);
		dispArea.add(rows3);
		
		dcc.gridx = 3;
		dcc.gridy = 1 + trans;
		dcc.gridwidth = trans;
		dcc.gridheight = 1;
		dgbl.setConstraints(columns2, dcc);
		dispArea.add(columns2);
		
		iqMat = Matrix.subtract(Matrix.identity(trans), qMat);
		nMat = iqMat.inverse();
		nMat.round(2);
		
		dcc.gridx = 3;
		dcc.gridy = 2 + trans;
		dcc.gridwidth = trans;
		dcc.gridheight = trans;
		dgbl.setConstraints(nMat, dcc);
		dispArea.add(nMat);
		
		dcc.gridx = 3 + trans;
		dcc.gridy = 1 + trans + trans / 2;
		dcc.gridwidth = 2;	
		dcc.gridheight = 1;
		dgbl.setConstraints(tLab, dcc);
		dispArea.add(tLab);
		
		dcc.gridx = 5 + trans;
		dcc.gridy = 2 + trans;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(rows4, dcc);
		dispArea.add(rows4);
		
		cMat = Matrix.con(trans, 1);
		
		tMat = Matrix.multiply(nMat, cMat);
		tMat.round(2);
		dcc.gridx = 6 + trans;
		dcc.gridy = 2 + trans;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(tMat, dcc);
		dispArea.add(tMat);
		
		dcc.gridx = 0;
		dcc.gridy = 2 + 2 * trans + trans / 2;
		dcc.gridwidth = 2;	
		dcc.gridheight = 1;
		dgbl.setConstraints(bLab, dcc);
		dispArea.add(bLab);
		
		dcc.gridx = 3;
		dcc.gridy = 3 + 2 * trans;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(rows5, dcc);
		dispArea.add(rows5);
		
		dcc.gridx = 4;
		dcc.gridy = 2 + 2 * trans;
		dcc.gridwidth = absorb;
		dcc.gridheight = 1;
		dgbl.setConstraints(cols2, dcc);
		dispArea.add(cols2);
		
		bMat = Matrix.multiply(nMat, rMat);
		bMat.round(2);
		dcc.gridx = 4;
		dcc.gridy = 3 + 2 * trans;
		dcc.gridwidth = absorb;
		dcc.gridheight = trans;
		dgbl.setConstraints(bMat, dcc);
		dispArea.add(bMat);
		
		gbl = new GridBagLayout();
		controls.setLayout(gbl);
		
		cc = new GridBagConstraints();
		
		cc.gridx = 0;
		cc.gridy = 0;
		cc.gridwidth = 2;
		gbl.setConstraints(go, cc);
		controls.add(go);
		
		validate();
	}
	
	// Handle events
	public boolean handleEvent(Event evt)
	{
		
		if (evt.target instanceof Button)
		{
			if (evt.target == go && evt.id == Event.ACTION_EVENT)	// When button is clicked
			{
        		simulate();//Integer.valueOf(num.getText()).intValue());
        		return true;					// Generate correct number of tosses
			}
		}
		if (evt.id == Event.WINDOW_DESTROY) {
			
			dispose();	
		}
		return super.handleEvent(evt);	// Handle other events as usual
	}
	
	// Calculate probabilities
    public void simulate()//int num)
    {	
    	dispArea.remove(nMat);
    	dispArea.remove(tMat);
    	dispArea.remove(bMat);
    	
    	qMat = new Matrix(Q);
		rMat = new Matrix(R);
		double[][] qArr = qMat.toArray();
		double[][] rArr = rMat.toArray();
		double total;
		for (int r = 0; r < qArr.length; r++) {
			total = 0;
			for (int c = 0; c < qArr[0].length; c++) {
				total += qArr[r][c];
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				total += rArr[r][c];
			}
			for (int c = 0; c < qArr[0].length; c++) {
				qArr[r][c] = round(qArr[r][c] / total, .001f);
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				rArr[r][c] = round(rArr[r][c] / total, .001f);
			}		
		}
		
		for (int r = 0; r < qArr.length; r++) {
			for (int c = 0; c < qArr[0].length; c++) {
				Q[r][c].setText(String.valueOf(qArr[r][c]));
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				R[r][c].setText(String.valueOf(rArr[r][c]));
			}
		}
		
		qMat = new Matrix(qArr);
		rMat = new Matrix(rArr);
		
		iqMat = Matrix.subtract(Matrix.identity(trans), qMat);
		nMat = iqMat.inverse();
		nMat.round(2);
		
		dcc.gridx = 3;
		dcc.gridy = 2 + trans;
		dcc.gridwidth = trans;
		dcc.gridheight = trans;
		dgbl.setConstraints(nMat, dcc);
		dispArea.add(nMat);
		
		tMat = Matrix.multiply(nMat, cMat);
		tMat.round(2);
		
		dcc.gridx = 6 + trans;
		dcc.gridy = 2 + trans;
		dcc.gridwidth = 1;
		dcc.gridheight = trans;
		dgbl.setConstraints(tMat, dcc);
		dispArea.add(tMat);
		
		bMat = Matrix.multiply(nMat, rMat);
		bMat.round(2);
		
		dcc.gridx = 4;
		dcc.gridy = 3 + 2 * trans;
		dcc.gridwidth = absorb;
		dcc.gridheight = trans;
		dgbl.setConstraints(bMat, dcc);
		dispArea.add(bMat);
    	
   	 	validate();
	}
	
	public double round(double num, double accuracy) {
		return accuracy * Math.round(num / accuracy);
	}
	
}




