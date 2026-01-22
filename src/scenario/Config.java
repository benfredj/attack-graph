package scenario;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Config {
	private int destNetworkPrefixes;
	private boolean alertSeriesCheckSrcPorts;
	private boolean alertSeriesCheckDstPorts;
	private boolean scenarioCheckDstPorts;
	private boolean saveAlertSeries;
	private String outputFilename;
	private String datasetFile;
	private PrintStream initialAlertsFile;
	private PrintStream IPsFile;
	private PrintStream objectivesFile;
	private PrintStream matrixFile;
	private int maxScenarioNodesForMatrix;
	private int maxScenarioNodesForGraphImage;
	
	private String alertSeriesFileExt;
	private int defaultYear;


	private int lineFormat;
	public static final int SNORTSHARP = 1;
	public static final int SNORTFAST = 2;
	
	public Config(int destNetworkPrefixes, boolean alertSeriesCheckSrcPorts,
			boolean alertSeriesCheckDstPorts, boolean scenarioCheckDstPorts,
			boolean saveAlertSeries, String alertSeriesFileExt, String outputFilename,
			String datasetFile, int lineFormat, int maxScenarioNodesForMatrix,
			int maxScenarioNodesForGraphImage, int defaultYear) {
		super();
		this.destNetworkPrefixes = destNetworkPrefixes;
		this.alertSeriesCheckSrcPorts = alertSeriesCheckSrcPorts;
		this.alertSeriesCheckDstPorts = alertSeriesCheckDstPorts;
		this.scenarioCheckDstPorts = scenarioCheckDstPorts;
		this.saveAlertSeries = saveAlertSeries;
		this.alertSeriesFileExt = alertSeriesFileExt;
		this.outputFilename = outputFilename;
		this.datasetFile = datasetFile;
		this.lineFormat = lineFormat;
		this.defaultYear = defaultYear;
		this.maxScenarioNodesForMatrix = maxScenarioNodesForMatrix;
		this.maxScenarioNodesForGraphImage = maxScenarioNodesForGraphImage;
		try {
			initialAlertsFile = new PrintStream(new FileOutputStream(this.outputFilename));
			IPsFile = initialAlertsFile;
			objectivesFile = initialAlertsFile;
			matrixFile = initialAlertsFile;
		 } catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public int getDefaultYear() {
		return defaultYear;
	}
	public void setDefaultYear(int defaultYear) {
		this.defaultYear = defaultYear;
	}
	public int getLineFormat() {
		return lineFormat;
	}
	public void setLineFormat(int lineFormat) {
		this.lineFormat = lineFormat;
	}
	public int getMaxScenarioNodesForMatrix() {
		return maxScenarioNodesForMatrix;
	}
	public void setMaxScenarioNodesForMatrix(int maxScenarioNodesForMatrix) {
		this.maxScenarioNodesForMatrix = maxScenarioNodesForMatrix;
	}
	public int getMaxScenarioNodesForGraphImage() {
		return maxScenarioNodesForGraphImage;
	}
	public void setMaxScenarioNodesForGraphImage(int maxScenarioNodesForGraphImage) {
		this.maxScenarioNodesForGraphImage = maxScenarioNodesForGraphImage;
	}
	public String getDatasetFile() {
		return datasetFile;
	}
	public PrintStream getOutputFile() {
		return initialAlertsFile;
	}
	public int getDestNetworkPrefixes() {
		return destNetworkPrefixes;
	}
	public boolean isAlertSeriesCheckSrcPorts() {
		return alertSeriesCheckSrcPorts;
	}
	public boolean isAlertSeriesCheckDstPorts() {
		return alertSeriesCheckDstPorts;
	}
	public boolean isScenarioCheckDstPorts() {
		return scenarioCheckDstPorts;
	}
	public boolean isSaveAlertSeries() {
		return saveAlertSeries;
	}
	public PrintStream getInitialAlertsFile() {
		return initialAlertsFile;
	}
	public PrintStream getIPsFile() {
		return IPsFile;
	}
	public PrintStream getObjectivesFile() {
		return objectivesFile;
	}
	public PrintStream getMatrixFile() {
		return matrixFile;
	}
	public String getAlertSeriesFileExt() {
		return alertSeriesFileExt;
	}
	

	public void closeOutputFile() {
		initialAlertsFile.close();
	}
	
}
