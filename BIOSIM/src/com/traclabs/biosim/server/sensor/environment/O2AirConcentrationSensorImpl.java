package biosim.server.sensor.environment;

import biosim.server.sensor.framework.*;
import biosim.idl.sensor.environment.*;
import biosim.idl.simulation.environment.*;

public class O2AirLevelSensorImpl extends EnvironmentSensorImpl implements O2AirLevelSensorOperations{
	public O2AirLevelSensorImpl(int pID){
		super(pID);
	}

	protected void gatherData(){
		float preFilteredValue = getInput().getO2Level();
		myValue = randomFilter(preFilteredValue);
	}

	protected void notifyListeners(){
	}
	
	/**
	* Returns the name of this module (O2AirLevelSensor)
	* @return the name of the module
	*/
	public String getModuleName(){
		return "O2AirLevelSensor"+getID();
	}
}
