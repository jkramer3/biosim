package biosim.client.water.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import biosim.client.framework.*;
import biosim.client.framework.gui.*;
import biosim.idl.water.*;
import biosim.idl.environment.*;
import com.jrefinery.chart.*;
import com.jrefinery.data.*;
import com.jrefinery.ui.*;

/**
 * This is the JPanel that displays a chart about the WaterStores
 *
 * @author    Scott Bell
 */
public class WaterStorePanel extends JPanel
{
	private WaterRS myWaterRS;
	private PotableWaterStore myPotableWaterStore;
	private GreyWaterStore myGreyWaterStore;
	private DirtyWaterStore myDirtyWaterStore;
	private SimEnvironment mySimEnvironment;
	private DefaultCategoryDataset myDataset;
	private JButton refreshButton;
	private JButton trackingButton;
	private ChartPanel myChartPanel;
	private RefreshAction myRefreshAction;
	private TrackingAction myTrackingAction;
	private Timer refreshTimer;
	private final static int TIMER_DELAY=500;
	private boolean trackingWanted = false;

	/**
	 * Default constructor.
	 */
	public WaterStorePanel() {
		myWaterRS = (WaterRS)(BioHolder.getBioModule(BioHolder.waterRSName));
		myPotableWaterStore = (PotableWaterStore)(BioHolder.getBioModule(BioHolder.potableWaterStoreName));
		myDirtyWaterStore = (DirtyWaterStore)(BioHolder.getBioModule(BioHolder.dirtyWaterStoreName));
		myGreyWaterStore = (GreyWaterStore)(BioHolder.getBioModule(BioHolder.greyWaterStoreName));
		mySimEnvironment = (SimEnvironment)(BioHolder.getBioModule(BioHolder.simEnvironmentName));
		createGraph();
		myRefreshAction = new RefreshAction("Refresh");
		refreshButton = new JButton(myRefreshAction);
		myTrackingAction = new TrackingAction("Start Tracking");
		trackingButton = new JButton(myTrackingAction);
		refreshTimer = new Timer (TIMER_DELAY, myRefreshAction);
		add(myChartPanel, BorderLayout.CENTER);
		add(refreshButton, BorderLayout.NORTH);
		add(trackingButton, BorderLayout.NORTH);
	}

	private void createGraph(){
		// create the chart...
		refresh();
		JFreeChart chart = ChartFactory.createVerticalBarChart3D(
		                           "Water Store Levels",  // chart title
		                           "Stores",              // domain axis label
		                           "Water Level",                 // range axis label
		                           myDataset,                 // data
		                           true                     // include legend
		                   );
		// add the chart to a panel...
		CategoryPlot plot = chart.getCategoryPlot();
		ValueAxis axis = plot.getRangeAxis();
		axis.setAutoRange(false);
		axis.setRange(0.0, myPotableWaterStore.getCapacity());
		plot.setSeriesPaint(new Paint[] { Color.BLUE, Color.GRAY, Color.YELLOW });
		myChartPanel = new ChartPanel(chart);
	}

	public void refresh() {
		if (myDataset == null){
			double[][] data = { {myPotableWaterStore.getLevel()}, {myGreyWaterStore.getLevel()}, {myDirtyWaterStore.getLevel()}};
			myDataset = new DefaultCategoryDataset(data);
			String[] theSeries = {"Potable Water", "Grey Water", "Dirty Water"};
			String[] theCategory = {""};
			myDataset.setSeriesNames(theSeries);
			myDataset.setCategories(theCategory);
		}
		else{
			myDataset.setValue(0, "", new Float(myPotableWaterStore.getLevel()));
			myDataset.setValue(1, "", new Float(myGreyWaterStore.getLevel()));
			myDataset.setValue(2, "", new Float(myDirtyWaterStore.getLevel()));
		}
	}
	
	public void visibilityChange(boolean nowVisible){
		if (nowVisible && trackingWanted){
			refreshTimer.start();
		}
		else{
			refreshTimer.stop();
		}
	}
	
	/**
	* Action that displays the power panel in an internal frame on the desktop.
	*/
	private class RefreshAction extends AbstractAction{
		public RefreshAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent ae){
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			refresh();
			setCursor(Cursor.getDefaultCursor());
		}
	}
	
	/**
	* Action that displays the power panel in an internal frame on the desktop.
	*/
	private class TrackingAction extends AbstractAction{
		public TrackingAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent ae){
			if (refreshTimer.isRunning()){
				refreshTimer.stop();
				trackingButton.setText("Start Tracking");
				trackingWanted = false;
			}
			else{
				refreshTimer.start();
				trackingButton.setText("Stop Tracking");
				trackingWanted = true;
			}
		}
	}
}
