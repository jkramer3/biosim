package biosim.server.simulation.air;

import biosim.idl.simulation.air.*;
import biosim.idl.simulation.framework.*;
import biosim.idl.framework.*;
import biosim.idl.simulation.environment.*;
import biosim.idl.simulation.power.*;
import biosim.idl.util.log.*;
import java.util.*;
import biosim.server.util.*;
import biosim.server.simulation.framework.*;

/**
 * The Air Revitalization System Implementation.  Takes in Air (O2, CO2, other) from the environment and
 * power from the Power Production System and produces air with less CO2 and more O2.
 *
 * @author    Scott Bell
 */

public class AirRSImpl extends SimBioModuleImpl implements AirRSOperations, PowerConsumerOperations, AirConsumerOperations, O2ProducerOperations, CO2ProducerOperations, AirProducerOperations, CO2ConsumerOperations{
	private LogIndex myLogIndex;
	private VCCR myVCCR;
	private CRS myCRS;
	private H2Tank myH2Tank;
	private CH4Tank myCH4Tank;
	private OGS myOGS;
	private O2Store[] myO2Stores;
	private PowerStore[] myPowerStores;
	private CO2Store[] myCO2InputStores;
	private CO2Store[] myCO2OutputStores;
	private SimEnvironment[] mySimEnvironmentInputs;
	private SimEnvironment[] mySimEnvironmentOutputs;
	private float[] powerMaxFlowRates;
	private float[] O2MaxFlowRates;
	private float[] CO2InputMaxFlowRates;
	private float[] CO2OutputMaxFlowRates;
	private float[] airInMaxFlowRates;
	private float[] airOutMaxFlowRates;
	private float[] powerActualFlowRates;
	private float[] O2ActualFlowRates;
	private float[] CO2InputActualFlowRates;
	private float[] CO2OutputActualFlowRates;
	private float[] airInActualFlowRates;
	private float[] airOutActualFlowRates;
	private float[] powerDesiredFlowRates;
	private float[] O2DesiredFlowRates;
	private float[] CO2InputDesiredFlowRates;
	private float[] CO2OutputDesiredFlowRates;
	private float[] airInDesiredFlowRates;
	private float[] airOutDesiredFlowRates;
	private static final int NUMBER_OF_SUBSYSTEMS_CONSUMING_POWER = 3;
	private float myProductionRate = 1f;

	public AirRSImpl(int pID){
		super(pID);
		myVCCR = new VCCR(this);
		myCRS = new CRS(this);
		myH2Tank = new H2Tank(this);
		myCH4Tank = new CH4Tank(this);
		myOGS = new OGS(this);

		myO2Stores = new O2Store[0];
		myPowerStores = new PowerStore[0];
		myCO2InputStores = new CO2Store[0];
		myCO2OutputStores = new CO2Store[0];
		mySimEnvironmentInputs = new SimEnvironment[0];
		mySimEnvironmentOutputs = new SimEnvironment[0];
		powerMaxFlowRates = new float[0];
		O2MaxFlowRates = new float[0];
		CO2InputMaxFlowRates = new float[0];
		CO2OutputMaxFlowRates = new float[0];
		airInMaxFlowRates = new float[0];
		airOutMaxFlowRates = new float[0];
		powerActualFlowRates = new float[0];
		O2ActualFlowRates = new float[0];
		CO2InputActualFlowRates = new float[0];
		CO2OutputActualFlowRates = new float[0];
		airInActualFlowRates = new float[0];
		airOutActualFlowRates = new float[0];
		powerDesiredFlowRates = new float[0];
		O2DesiredFlowRates = new float[0];
		CO2InputDesiredFlowRates = new float[0];
		CO2OutputDesiredFlowRates = new float[0];
		airInDesiredFlowRates = new float[0];
		airOutDesiredFlowRates = new float[0];
	}

	public boolean VCCRHasPower(){
		return myVCCR.hasPower();
	}

	public boolean VCCRHasEnoughAir(){
		return myVCCR.hasEnoughAir();
	}

	public boolean CRSHasPower(){
		return myCRS.hasPower();
	}

	public boolean CRSHasEnoughCO2(){
		return myCRS.hasEnoughCO2();
	}

	public boolean CRSHasEnoughH2(){
		return myCRS.hasEnoughH2();
	}

	public boolean OGSHasPower(){
		return myOGS.hasPower();
	}

	public boolean OGSHasEnoughH2O(){
		return myOGS.hasEnoughH2O();
	}

	VCCR getVCCR(){
		return myVCCR;
	}

	CRS getCRS(){
		return myCRS;
	}

	H2Tank getH2Tank(){
		return myH2Tank;
	}

	CH4Tank getCH4Tank(){
		return myCH4Tank;
	}

	OGS getOGS(){
		return myOGS;
	}

	/**
	* Returns the power consumption (in watts) of the AirRS at the current tick.
	* @return the power consumed (in watts) at the current tick
	*/
	public float getPowerConsumed(){
		return myVCCR.getPowerConsumed() + myOGS.getPowerConsumed() + myCRS.getPowerConsumed();
	}

	/**
	* Returns the CO2 consumption (in liters) of the AirRS at the current tick.
	* @return the CO2 consumed at the current tick
	*/
	public float getCO2Consumed(){
		return myVCCR.getCO2Consumed();
	}

	/**
	* Returns the O2 produced (in liters) of the AirRS at the current tick.
	* @return the O2 produced (in liters) at the current tick
	*/
	public float getO2Produced(){
		return myOGS.getO2Produced();
	}

	/**
	* Returns the CO2 produced (in liters) of the AirRS at the current tick.
	* @return the CO2 produced (in liters) at the current tick
	*/
	public float getCO2Produced(){
		return 0;
	}

	/**
	* Processes a tick by collecting referernces (if needed), resources, and pushing the new air out.
	*/
	public void tick(){
		Arrays.fill(powerActualFlowRates, 0f);
		myVCCR.tick();
		myCRS.tick();
		myH2Tank.tick();
		myCH4Tank.tick();
		myOGS.tick();
		if (isMalfunctioning())
			performMalfunctions();
		if (moduleLogging)
			log();
	}

	public void setProductionRate(float percentage){
		myVCCR.setProductionRate(percentage);
		myOGS.setProductionRate(percentage);
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
			returnBuffer.append("Production Rate Decrease (Temporary)");
		else if (pLength == MalfunctionLength.PERMANENT_MALF)
			returnBuffer.append("Production Rate Decrease (Permanent)");
		return returnBuffer.toString();
	}

	private void performMalfunctions(){
		float productionRate = 1f;
		for (Iterator iter = myMalfunctions.values().iterator(); iter.hasNext(); ){
			Malfunction currentMalfunction = (Malfunction)(iter.next());
			if (currentMalfunction.getLength() == MalfunctionLength.TEMPORARY_MALF){
				if (currentMalfunction.getIntensity() == MalfunctionIntensity.SEVERE_MALF)
					productionRate *= 0.50;
				else if (currentMalfunction.getIntensity() == MalfunctionIntensity.MEDIUM_MALF)
					productionRate *= 0.25;
				else if (currentMalfunction.getIntensity() == MalfunctionIntensity.LOW_MALF)
					productionRate *= 0.10;
			}
			else if (currentMalfunction.getLength() == MalfunctionLength.PERMANENT_MALF){
				if (currentMalfunction.getIntensity() == MalfunctionIntensity.SEVERE_MALF)
					productionRate *= 0.50;
				else if (currentMalfunction.getIntensity() == MalfunctionIntensity.MEDIUM_MALF)
					productionRate *= 0.25;
				else if (currentMalfunction.getIntensity() == MalfunctionIntensity.LOW_MALF)
					productionRate *= 0.10;
			}
		}
		setProductionRate(productionRate);
	}

	/**
	* Resets production/consumption levels.
	*/
	public void reset(){
		super.reset();
		myVCCR.reset();
		myCRS.reset();
		myH2Tank.reset();
		myCH4Tank.reset();
		myOGS.reset();
	}

	/**
	* Returns the name of this module (AirRS)
	* @return the name of this module
	*/
	public String getModuleName(){
		return "AirRS"+getID();
	}

	private void log(){
		//If not initialized, fill in the log
		if (!logInitialized){
			myLogIndex = new LogIndex();
			LogNode CO2NeededHead = myLog.addChild("CO2 Needed");
			LogNode currentO2ProducedHead = myLog.addChild("O2 Produced");
			LogNode currentCO2ProducedHead = myLog.addChild("CO2 Produced");
			LogNode currentCO2ConsumedHead = myLog.addChild("CO2 Consumed");
			LogNode currentPowerConsumedHead = myLog.addChild("Power Consumed");
			logInitialized = true;
		}
		else{
		}
		sendLog(myLog);
	}

	/**
	* For fast reference to the log tree
	*/
	private class LogIndex{
		public LogNode CO2NeededIndex;
		public LogNode currentO2ProducedIndex;
		public LogNode currentCO2ConsumedIndex;
		public LogNode currentCO2ProducedIndex;
		public LogNode currentPowerConsumedIndex;
	}
	
	
	int getSubsystemsConsumingPower(){
		return NUMBER_OF_SUBSYSTEMS_CONSUMING_POWER;
	}
	
	//Power Inputs
	public void setPowerInputMaxFlowRate(float watts, int index){
		powerMaxFlowRates[index] = watts;
	}
	public float getPowerInputMaxFlowRate(int index){
		return powerMaxFlowRates[index];
	}
	public float[] getPowerInputMaxFlowRates(){
		return powerMaxFlowRates;
	}
	public void setPowerInputDesiredFlowRate(float watts, int index){
		powerDesiredFlowRates[index] = watts;
	}
	public float getPowerInputDesiredFlowRate(int index){
		return powerDesiredFlowRates[index];
	}
	public float[] getPowerInputDesiredFlowRates(){
		return powerDesiredFlowRates;
	}
	public float getPowerInputActualFlowRate(int index){
		return powerActualFlowRates[index];
	}
	public float[] getPowerInputActualFlowRates(){
		return powerActualFlowRates;
	}
	public void setPowerInputs(PowerStore[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myPowerStores = sources;
		powerMaxFlowRates = maxFlowRates;
		powerDesiredFlowRates = desiredFlowRates;
		powerActualFlowRates = new float[powerDesiredFlowRates.length];
	}
	public PowerStore[] getPowerInputs(){
		return myPowerStores;
	}
	void addPowerInputActualFlowRate(float watts, int index){
		powerActualFlowRates[index] += watts;
	}
	
	//Air Inputs
	public void setAirInputMaxFlowRate(float liters, int index){
		airInMaxFlowRates[index] = liters;
	}
	public float getAirInputMaxFlowRate(int index){
		return airInMaxFlowRates[index];
	}
	public float[] getAirInputMaxFlowRates(){
		return airInMaxFlowRates;
	}
	public void setAirInputDesiredFlowRate(float liters, int index){
		airInDesiredFlowRates[index] = liters;
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
		mySimEnvironmentInputs = sources;
		airInMaxFlowRates = maxFlowRates;
		airInDesiredFlowRates = desiredFlowRates;
		airInActualFlowRates = new float[airInDesiredFlowRates.length];
	}
	public SimEnvironment[] getAirInputs(){
		return mySimEnvironmentInputs;
	}
	void setAirInputActualFlowRate(float liters, int index){
		airInActualFlowRates[index] = liters;
	}
	
	//Air Ouputs
	public void setAirOutputMaxFlowRate(float liters, int index){
		airOutMaxFlowRates[index] = liters;
	}
	public float getAirOutputMaxFlowRate(int index){
		return airOutMaxFlowRates[index];
	}
	public float[] getAirOutputMaxFlowRates(){
		return airOutMaxFlowRates;
	}
	public void setAirOutputDesiredFlowRate(float liters, int index){
		airOutDesiredFlowRates[index] = liters;
	}
	public float getAirOutputDesiredFlowRate(int index){
		return airOutDesiredFlowRates[index];
	}
	public float[] getAirOutputDesiredFlowRates(){
		return airOutDesiredFlowRates;
	}
	public float getAirOutputActualFlowRate(int index){
		return airOutActualFlowRates[index];
	}
	public float[] getAirOutputActualFlowRates(){
		return airOutActualFlowRates;
	}
	public void setAirOutputs(SimEnvironment[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		mySimEnvironmentOutputs = sources;
		airOutMaxFlowRates = maxFlowRates;
		airOutDesiredFlowRates = desiredFlowRates;
		airOutActualFlowRates = new float[airOutDesiredFlowRates.length]; 
	}
	public SimEnvironment[] getAirOutputs(){
		return mySimEnvironmentOutputs;
	}
	void setAirOutputActualFlowRate(float liters, int index){
		airOutActualFlowRates[index] = liters;
	}
	
	//O2 Ouputs
	public void setO2OutputMaxFlowRate(float liters, int index){
		O2MaxFlowRates[index] = liters;
	}
	public float getO2OutputMaxFlowRate(int index){
		return O2MaxFlowRates[index];
	}
	public float[] getO2OutputMaxFlowRates(){
		return O2MaxFlowRates;
	}
	public void setO2OutputDesiredFlowRate(float liters, int index){
		O2DesiredFlowRates[index] = liters;
	}
	public float getO2OutputDesiredFlowRate(int index){
		return O2DesiredFlowRates[index];
	}
	public float[] getO2OutputDesiredFlowRates(){
		return O2DesiredFlowRates;
	}
	public float getO2OutputActualFlowRate(int index){
		return O2ActualFlowRates[index];
	}
	public float[] getO2OutputActualFlowRates(){
		return O2ActualFlowRates;
	}
	public void setO2Outputs(O2Store[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myO2Stores = sources;
		O2MaxFlowRates = maxFlowRates;
		O2DesiredFlowRates = desiredFlowRates;
		O2ActualFlowRates = new float[O2DesiredFlowRates.length]; 
	}
	public O2Store[] getO2Outputs(){
		return myO2Stores;
	}
	void setO2OutputActualFlowRate(float liters, int index){
		O2ActualFlowRates[index] = liters;
	}
	
	//CO2 Ouputs
	public void setCO2OutputMaxFlowRate(float liters, int index){
		CO2OutputMaxFlowRates[index] = liters;
	}
	public float getCO2OutputMaxFlowRate(int index){
		return CO2OutputMaxFlowRates[index];
	}
	public float[] getCO2OutputMaxFlowRates(){
		return CO2OutputMaxFlowRates;
	}
	public void setCO2OutputDesiredFlowRate(float liters, int index){
		CO2OutputDesiredFlowRates[index] = liters;
	}
	public float getCO2OutputDesiredFlowRate(int index){
		return CO2OutputDesiredFlowRates[index];
	}
	public float[] getCO2OutputDesiredFlowRates(){
		return CO2OutputDesiredFlowRates;
	}
	public float getCO2OutputActualFlowRate(int index){
		return CO2OutputActualFlowRates[index];
	}
	public float[] getCO2OutputActualFlowRates(){
		return CO2OutputActualFlowRates;
	}
	public void setCO2Outputs(CO2Store[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myCO2OutputStores = sources;
		CO2OutputMaxFlowRates = maxFlowRates;
		CO2OutputDesiredFlowRates = desiredFlowRates;
		CO2OutputActualFlowRates = new float[CO2OutputDesiredFlowRates.length]; 
	}
	public CO2Store[] getCO2Outputs(){
		return myCO2OutputStores;
	}
	void setCO2OutputActualFlowRate(float liters, int index){
		CO2OutputActualFlowRates[index] = liters;
	}
	
	//CO2 Inputs
	public void setCO2InputMaxFlowRate(float liters, int index){
		CO2InputMaxFlowRates[index] = liters;
	}
	public float getCO2InputMaxFlowRate(int index){
		return CO2InputMaxFlowRates[index];
	}
	public float[] getCO2InputMaxFlowRates(){
		return CO2InputMaxFlowRates;
	}
	public void setCO2InputDesiredFlowRate(float liters, int index){
		CO2InputDesiredFlowRates[index] = liters;
	}
	public float getCO2InputDesiredFlowRate(int index){
		return CO2InputDesiredFlowRates[index];
	}
	public float[] getCO2InputDesiredFlowRates(){
		return CO2InputDesiredFlowRates;
	}
	public float getCO2InputActualFlowRate(int index){
		return CO2InputActualFlowRates[index];
	}
	public float[] getCO2InputActualFlowRates(){
		return CO2InputActualFlowRates;
	}
	public void setCO2Inputs(CO2Store[] sources, float[] maxFlowRates, float[] desiredFlowRates){
		myCO2InputStores = sources;
		CO2InputMaxFlowRates = maxFlowRates;
		CO2InputDesiredFlowRates = desiredFlowRates;
		CO2InputActualFlowRates = new float[CO2InputDesiredFlowRates.length]; 
	}
	public CO2Store[] getCO2Inputs(){
		return myCO2InputStores;
	}
	void setCO2InputActualFlowRate(float liters, int index){
		CO2InputActualFlowRates[index] = liters;
	}
}
