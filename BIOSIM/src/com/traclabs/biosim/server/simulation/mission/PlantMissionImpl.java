package biosim.server.simulation.mission;

import biosim.idl.simulation.framework.*;
import biosim.idl.framework.*;
import biosim.idl.simulation.environment.*;
import biosim.idl.simulation.power.*;
import biosim.idl.simulation.mission.*;
import biosim.idl.simulation.air.*;
import biosim.idl.simulation.water.*;
import java.util.*;
import biosim.server.util.*;
import biosim.idl.util.log.*;
import biosim.server.simulation.framework.*;
/**
 * The basic PlantMission Implementation.
 * @author    Scott Bell
 */

public class PlantMissionImpl extends MissionModuleImpl implements PlantMissionOperations, PowerConsumerOperations, AirConsumerOperations, PotableWaterConsumerOperations{
	private LogIndex myLogIndex;

	private PowerStore[] myPowerInputs;
	private float[] powerInMaxFlowRates;
	private float[] powerInActualFlowRates;
	private float[] powerInDesiredFlowRates;

	private SimEnvironment[] myAirInputs;
	private float[] airInMaxFlowRates;
	private float[] airInActualFlowRates;
	private float[] airInDesiredFlowRates;
	
	private PotableWaterStore[] myPotableWaterInputs;
	private float[] potableWaterInMaxFlowRates;
	private float[] potableWaterInActualFlowRates;
	private float[] potableWaterInDesiredFlowRates;

	public PlantMissionImpl(int pID, String pName){
		super(pID, pName);
		myPowerInputs = new PowerStore[0];
		powerInMaxFlowRates = new float[0];
		powerInActualFlowRates = new float[0];
		powerInDesiredFlowRates = new float[0];

		myAirInputs = new SimEnvironment[0];
		airInMaxFlowRates = new float[0];
		airInActualFlowRates = new float[0];
		airInDesiredFlowRates = new float[0];
		
		myPotableWaterInputs = new PotableWaterStore[0];
		potableWaterInMaxFlowRates = new float[0];
		potableWaterInActualFlowRates = new float[0];
		potableWaterInDesiredFlowRates = new float[0];
	}

	public void tick(){
		super.tick();
		getAndPushResources();
	}

	private void getAndPushResources(){
		float powerGathered = getMostResourceFromStore(myPowerInputs, powerInMaxFlowRates, powerInDesiredFlowRates, powerInActualFlowRates);
		float potableWaterGathered = getMostResourceFromStore(myPotableWaterInputs, potableWaterInMaxFlowRates, potableWaterInDesiredFlowRates, potableWaterInActualFlowRates);
		
		Breath gatheredAir = new Breath(0f, 0f, 0f, 0f, 0f);
		for (int i = 0; (i < myAirInputs.length); i++){
			float amountToTake = Math.min(airInMaxFlowRates[i], airInDesiredFlowRates[i]);
			Breath currentBreath = myAirInputs[i].takeAirMoles(amountToTake);
			gatheredAir.O2 += currentBreath.O2;
			gatheredAir.CO2 += currentBreath.CO2;
			gatheredAir.other += currentBreath.other;
			airInActualFlowRates[i] = currentBreath.O2 + currentBreath.CO2 + currentBreath.other + currentBreath.nitrogen;
		}
	}
	
	protected String getMalfunctionName(MalfunctionIntensity pIntensity, MalfunctionLength pLength){
		StringBuffer returnBuffer = new StringBuffer();
		if (pIntensity == MalfunctionIntensity.SEVERE_MALF)
			returnBuffer.append("Severe ");
		else if (pIntensity == MalfunctionIntensity.MEDIUM_MALF)
			returnBuffer.append("Medium ");
		else if (pIntensity == MalfunctionIntensity.LOW_MALF)
			returnBuffer.append("Low ");
		if (pLength == MalfunctionLength.TEMPORARY_MALF)
			returnBuffer.append("Temporary Production Reduction");
		else if (pLength == MalfunctionLength.PERMANENT_MALF)
			returnBuffer.append("Permanent Production Reduction");
		return returnBuffer.toString();
	}

	protected void performMalfunctions(){
	}

	public void reset(){
		super.reset();
	}

	public void log(){
		//If not initialized, fill in the log
		if (!logInitialized){
			myLogIndex = new LogIndex();
			LogNode intHead = myLog.addChild("in");
			//myLogIndex.levelIndex = levelHead.addChild((""+in));
			LogNode outHead = myLog.addChild("out");
			//myLogIndex.capacityIndex = capacityHead.addChild((""+out));
			logInitialized = true;
		}
		else{
			//myLogIndex.intHead.setValue(""+in);
			//myLogIndex.outHead.setValue(""+out);
		}
		sendLog(myLog);
	}

	/**
	* For fast reference to the log tree
	*/
	private class LogIndex{
		public LogNode inIndex;
		public LogNode outIndex;
	}

	//Power Inputs
	public void setPowerInputMaxFlowRate(float amount, int index){
		powerInMaxFlowRates[index] = amount;
	}
	public float getPowerInputMaxFlowRate(int index){
		return powerInMaxFlowRates[index];
	}
	public float[] getPowerInputMaxFlowRates(){
		return powerInMaxFlowRates;
	}
	public void setPowerInputDesiredFlowRate(float amount, int index){
		powerInDesiredFlowRates[index] = amount;
	}
	public float getPowerInputDesiredFlowRate(int index){
		return powerInDesiredFlowRates[index];
	}
	public float[] getPowerInputDesiredFlowRates(){
		return powerInDesiredFlowRates;
	}
	public float getPowerInputActualFlowRate(int index){
		return powerInActualFlowRates[index];
	}
	public float[] getPowerInputActualFlowRates(){
		return powerInActualFlowRates;
	}
	public void setPowerInputs(PowerStore[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myPowerInputs = sources;
		powerInMaxFlowRates = maxFlowRates;
		powerInDesiredFlowRates = desiredFlowRates;
		powerInActualFlowRates = new float[powerInDesiredFlowRates.length];
	}
	public PowerStore[] getPowerInputs(){
		return myPowerInputs;
	}

	//Air Inputs
	public void setAirInputMaxFlowRate(float amount, int index){
		airInMaxFlowRates[index] = amount;
	}
	public float getAirInputMaxFlowRate(int index){
		return airInMaxFlowRates[index];
	}
	public float[] getAirInputMaxFlowRates(){
		return airInMaxFlowRates;
	}
	public void setAirInputDesiredFlowRate(float amount, int index){
		airInDesiredFlowRates[index] = amount;
	}
	public float getAirInputDesiredFlowRate(int index){
		return airInDesiredFlowRates[index];
	}
	public float[] getAirInputDesiredFlowRates(){
		return airInDesiredFlowRates;
	}
	public float getAirInputActualFlowRate(int index){
		return airInActualFlowRates[index];
	}
	public float[] getAirInputActualFlowRates(){
		return airInActualFlowRates;
	}
	public void setAirInputs(SimEnvironment[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myAirInputs = sources;
		airInMaxFlowRates = maxFlowRates;
		airInDesiredFlowRates = desiredFlowRates;
		airInActualFlowRates = new float[airInDesiredFlowRates.length];
	}
	public SimEnvironment[] getAirInputs(){
		return myAirInputs;
	}
	
	//Potable Water Inputs
	public void setPotableWaterInputMaxFlowRate(float amount, int index){
		potableWaterInMaxFlowRates[index] = amount;
	}
	public float getPotableWaterInputMaxFlowRate(int index){
		return potableWaterInMaxFlowRates[index];
	}
	public float[] getPotableWaterInputMaxFlowRates(){
		return potableWaterInMaxFlowRates;
	}
	public void setPotableWaterInputDesiredFlowRate(float amount, int index){
		potableWaterInDesiredFlowRates[index] = amount;
	}
	public float getPotableWaterInputDesiredFlowRate(int index){
		return potableWaterInDesiredFlowRates[index];
	}
	public float[] getPotableWaterInputDesiredFlowRates(){
		return potableWaterInDesiredFlowRates;
	}
	public float getPotableWaterInputActualFlowRate(int index){
		return potableWaterInActualFlowRates[index];
	}
	public float[] getPotableWaterInputActualFlowRates(){
		return potableWaterInActualFlowRates;
	}
	public void setPotableWaterInputs(PotableWaterStore[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myPotableWaterInputs = sources;
		potableWaterInMaxFlowRates = maxFlowRates;
		potableWaterInDesiredFlowRates = desiredFlowRates;
		potableWaterInActualFlowRates = new float[potableWaterInDesiredFlowRates.length];
	}
	public PotableWaterStore[] getPotableWaterInputs(){
		return myPotableWaterInputs;
	}
}