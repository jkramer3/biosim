package biosim.server.sensor.power;

import biosim.server.sensor.framework.*;
import biosim.idl.sensor.power.*;
import biosim.idl.simulation.power.*;

public class PowerInFlowRateSensorImpl extends GenericSensorImpl implements PowerInFlowRateSensorOperations{
	private PowerConsumer myConsumer;
	private int myIndex;
	
	public PowerInFlowRateSensorImpl(int pID){
		super(pID);
	}

	protected void gatherData(){
		double preFilteredValue = getInput().getPowerInputActualFlowRate(myIndex);
		myValue = randomFilter(preFilteredValue);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setInput(PowerConsumer pConsumer, int pIndex){
		myConsumer = pConsumer;
		myIndex = pIndex;
	}
	
	public PowerConsumer getInput(){
		return myConsumer;
	}
	
	public int getIndex(){
		return myIndex;
	}
	
	/**
	* Returns the name of this module (PowerInFlowRateSensor)
	* @return the name of the module
	*/
	public String getModuleName(){
		return "PowerInFlowRateSensor"+getID();
	}
}