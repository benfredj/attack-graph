package absorbingChain.algorithm;
// AbsorbingWindow.java

// Written by Julian Devlin, 8/97, for the text book
// "Introduction to Probability," by Charles M. Grinstead & J. Laurie Snell


import absorbingChain.utilities.*;

import java.util.*;

import absorbingChain.utilities.Matrix;

public class AbsorbingChain
{
	double[][] Q, R;
	Matrix qMat, rMat, iqMat, nMat, cMat, tMat, bMat;
	double[][] rls;
	double[][] cls1;
	double[][] cls2;
	Matrix rows1, rows2, rows3, rows4, rows5, columns1, columns2, cols1, cols2;
	
	
		
	int trans, absorb;
	

	public AbsorbingChain(int t, int a) {
		
		trans = t;
		absorb = a;
	}
	public static void main(String [ ] args){
		AbsorbingChain ac = new AbsorbingChain(2,1);
		ac.init();
		ac.simulate();
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
		
		
	
		
		if (trans == 2 && absorb == 1) {
			
			Q = new double[2][2];
			Q[0][0] = 0;
			Q[0][1] = 1; //46
			Q[1][0] = 0.97;//45/46;//45
			Q[1][1] = 0;
			
			R = new double[2][1];
			R[0][0] = 0;
			R[1][0] = 0.03;//1/46;//1
		}
		else if (trans == 4 && absorb == 2) {
			
			Q = new double[4][4];
			Q[0][0] = 0;
			Q[0][1] = 0.5;
			Q[0][2] = 0;
			Q[0][3] = 0;
			Q[1][0] = 0.5;
			Q[1][1] = 0;
			Q[1][2] = 0.5;
			Q[1][3] = 0;
			Q[2][0] = 0;
			Q[2][1] = 0.5;
			Q[2][2] = 0;
			Q[2][3] = 0.5;
			Q[3][0] = 0;
			Q[3][1] = 0;
			Q[3][2] = 0.5;
			Q[3][3] = 0;
			
			R = new double[4][2];
			R[0][0] = 0.5;
			R[0][1] = 0;
			R[1][0] = 0;
			R[1][1] = 0;
			R[2][0] = 0;
			R[2][1] = 0;
			R[3][0] = 0;
			R[3][1] = 0.5;
		}
		else if (trans == 8 && absorb == 1) {
			
			Q = new double[8][8];
			Q[0][0]=0; Q[0][1]=0.5; Q[0][2]=0.5;Q[0][3]=0;Q[0][4]=0;Q[0][5]=0;Q[0][6]=0;Q[0][7]=0;
			Q[1][0]=0; Q[1][1]=0; Q[1][2]=0;Q[1][3]=0;Q[1][4]=1;Q[1][5]=0;Q[1][6]=0;Q[1][7]=0;
			Q[2][0]=0; Q[2][1]=0; Q[2][2]=0.5;Q[2][3]=0.25;Q[2][4]=0;Q[2][5]=0.25;Q[2][6]=0;Q[2][7]=0;
			Q[3][0]=1; Q[3][1]=0; Q[3][2]=0;Q[3][3]=0;Q[3][4]=0;Q[3][5]=0;Q[3][6]=0;Q[3][7]=0;
			Q[4][0]=0; Q[4][1]=0; Q[4][2]=0;Q[4][3]=0;Q[4][4]=0.67;Q[4][5]=0;Q[4][6]=0.33;Q[4][7]=0;
			Q[5][0]=0; Q[5][1]=0; Q[5][2]=0;Q[5][3]=0;Q[5][4]=0;Q[5][5]=0;Q[5][6]=1;Q[5][7]=0;
			Q[6][0]=0; Q[6][1]=0; Q[6][2]=0;Q[6][3]=0;Q[6][4]=0;Q[6][5]=0;Q[6][6]=0;Q[6][7]=0.67;
			Q[7][0]=0; Q[7][1]=0; Q[7][2]=0.5;Q[7][3]=0;Q[7][4]=0;Q[7][5]=0;Q[7][6]=0.5;Q[7][7]=0;
			
			/*double [][] Q={{0, 0.5, 0.5, 0, 0, 0, 0, 0},
							{0, 0, 0, 0 , 1, 0, 0, 0},
							{0, 0, 0.5, 0.25, 0, 0.25, 0, 0},
							{1, 0, 0, 0, 0, 0, 0, 0},
							{0, 0, 0, 0, 0.67, 0, 0.33, 0},
							{0, 0, 0, 0, 0, 0, 1, 0},
							{0, 0, 0, 0, 0, 0, 0, 0.67},
							{0, 0, 0.5, 0, 0, 0, 0.5, 0}};
			
			*/
			R = new double[8][1];
			R[0][0] = 0;
			R[1][0] = 0;
			R[2][0] = 0;
			R[3][0] = 0;
			R[4][0] = 0;
			R[5][0] = 0;
			R[6][0] = 0.33;
			R[7][0] = 0;
		}
		else{
			Scanner scanner = new Scanner (System.in);
		
			Q = new double[trans][trans];
			for (int i = 0; i < trans; i++) {
				for (int j = 0; j < trans; j++) {
					System.out.print("Q["+i+"]["+j+"] =");
					try {
						Q[i][j] = scanner.nextDouble ();
					}catch  (InputMismatchException e) {
						      
					}
				}
			}
			R = new double[trans][absorb];
			for (int i = 0; i < trans; i++) {
				for (int j = 0; j < absorb; j++) {
					System.out.print("R["+i+"]["+j+"] =");
					try {
						R[i][j] = scanner.nextDouble ();
					}catch  (InputMismatchException e) {
						      
					}
				}
			}	
		}
		


		rows1 = new Matrix(rls);
		rows2 = new Matrix(rls);
		rows3 = new Matrix(rls);
		rows4 = new Matrix(rls);
		rows5 = new Matrix(rls);
		columns1 = new Matrix(cls1);
		columns2 = new Matrix(cls1);
		cols1 = new Matrix(cls2); 
		cols2 = new Matrix(cls2);
		

		qMat = new Matrix(Q);
		rMat = new Matrix(R);
		
		
		iqMat = Matrix.subtract(Matrix.identity(trans), qMat);
		nMat = iqMat.inverse();
		nMat.round(2);
		

		
		cMat = Matrix.con(trans, 1);
		
		tMat = Matrix.multiply(nMat, cMat);
		tMat.round(2);

		
		bMat = Matrix.multiply(nMat, rMat);
		bMat.round(2);
		
		
		
	}
	
	
	// Calculate probabilities
    public void simulate()//int num)
    {	

    	
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
				qArr[r][c] = round(qArr[r][c] / total, .001);
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				rArr[r][c] = round(rArr[r][c] / total, .001);
			}		
		}
		
		for (int r = 0; r < qArr.length; r++) {
			for (int c = 0; c < qArr[0].length; c++) {
				Q[r][c] = qArr[r][c];
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				R[r][c] = rArr[r][c];
			}
		}
		Q.toString();
	
		
		qMat = new Matrix(qArr);
		rMat = new Matrix(rArr);
		
		iqMat = Matrix.subtract(Matrix.identity(trans), qMat);
		nMat = iqMat.inverse();
		nMat.round(2);
		
	
		
		tMat = Matrix.multiply(nMat, cMat);
		tMat.round(2);
		
			
		bMat = Matrix.multiply(nMat, rMat);
		bMat.round(2);
	
		qMat.display("Q",2);			
		rMat.display("R",2);	
		
		nMat.display("N",2);			
		tMat.display("T",2);	
		bMat.display("B",2);	
	}
	
	public double round(double num, double accuracy) {
		return accuracy * Math.round(num / accuracy);
	}

	
}




