package biosim.server.food;

import biosim.idl.food.*;
import biosim.idl.water.*;
import biosim.server.util.*;
import biosim.idl.power.*;
import biosim.idl.util.*;
import java.util.*;
/**
 * Tray contains Plants
 * @author    Scott Bell
 */

public class ShelfImpl extends ShelfPOA {
	private Plant myCrop;
	private float currentPowerConsumed = 0f;
	private float totalArea = 8.24f;
	private float currentGreyWaterConsumed = 0f;
	private float currentPotableWaterConsumed = 0f;
	private boolean hasCollectedReferences = false;
	private boolean hasEnoughWater = false;
	private boolean hasEnoughPower = false;
	private PotableWaterStore myPotableWaterStore;
	private GreyWaterStore myGreyWaterStore;
	private PowerStore myPowerStore;
	private LogIndex myLogIndex;
	private boolean logInitialized = false;
	
	public ShelfImpl(){
		myCrop = new Wheat(totalArea);
	}
	
	public ShelfImpl(float pTotalArea){
		totalArea = pTotalArea;
		myCrop = new Wheat(totalArea);
	}
	
	/**
	* Collects references to subsystems needed for putting/getting resources
	*/
	private void collectReferences(){
		if (!hasCollectedReferences){
			try{
				myPotableWaterStore = PotableWaterStoreHelper.narrow(OrbUtils.getNCRef().resolve_str("PotableWaterStore"));
				myGreyWaterStore = GreyWaterStoreHelper.narrow(OrbUtils.getNCRef().resolve_str("GreyWaterStore"));
				myPowerStore = PowerStoreHelper.narrow(OrbUtils.getNCRef().resolve_str("PowerStore"));
				hasCollectedReferences = true;
			}
			catch (org.omg.CORBA.UserException e){
				e.printStackTrace(System.out);
			}
		}
	}
	
	public boolean hasWater(){
		return myCrop.hasWater();
	}
	
	public boolean hasLight(){
		return myCrop.hasLight();
	}
	
	public boolean isDead(){
		return myCrop.isDead();
	}
	
	public boolean hasCO2(){
		return myCrop.hasCO2();
	}
	
	public float getArea(){
		return totalArea;
	}
	
	public float getCO2Consumed(){
		return myCrop.getCO2Consumed();
	}
	
	public float getBiomassProduced(){
		return myCrop.getBiomassProduced();
	}
	
	public float getO2Produced(){
		return myCrop.getO2Produced();
	}
	
	public float getPowerConsumed(){
		return currentPowerConsumed;
	}
	
	public float getGreyWaterConsumed(){
		return currentGreyWaterConsumed;
	}
	
	public float getPotableWaterConsumed(){
		return currentPotableWaterConsumed;
	}
	
	private void waterPlants(){
		myCrop.addWater(currentGreyWaterConsumed + currentPotableWaterConsumed);
	}
	
	private void lightPlants(){
		myCrop.lightPlants(currentPowerConsumed);
	}
	
	/**
	* Adds power for this tick
	*/
	private void collectPower(){
		currentPowerConsumed = myPowerStore.take(myCrop.getPowerNeeded());
		if (currentPowerConsumed < myCrop.getPowerNeeded()){
			hasEnoughPower = false;
		}
		else{
			hasEnoughPower = true;
		}
	}
	
	public String getPlantType(){
		return myCrop.getPlantType();
	}
	
	/**
	* Adds power for this tick
	*/
	private void collectWater(){
		currentGreyWaterConsumed = myGreyWaterStore.take(myCrop.getWaterNeeded());
		if (currentGreyWaterConsumed < myCrop.getWaterNeeded()){
			hasEnoughWater = false;
		}
		else{
			hasEnoughWater = true;
		}
	}
	
	public void reset(){
		 currentPowerConsumed = 0f;
		 currentGreyWaterConsumed = 0f;
		 currentPotableWaterConsumed = 0f;
		 hasCollectedReferences = false;
		 hasEnoughWater = false;
		 hasEnoughPower = false;
		 myCrop.reset();
	}
	
	public void tick(){
		collectReferences();
		collectPower();
		if (hasEnoughPower){
			collectWater();
			lightPlants();
			waterPlants();
		}
		myCrop.tick();
	}
	
	public void log(LogNode myLogHead){
		//If not initialized, fill in the log
		if (!logInitialized){
			myLogIndex = new LogIndex();
			myLogIndex.plantHead = myLogHead.addChild("Plant");
			myCrop.log(myLogIndex.plantHead);
			LogNode currentPowerConsumedHead = myLogHead.addChild("Power Consumed");
			myLogIndex.currentPowerConsumedIndex = currentPowerConsumedHead.addChild(""+currentPowerConsumed);
			LogNode totalAreaHead = myLogHead.addChild("Total Area");
			myLogIndex.totalAreaIndex = totalAreaHead.addChild(""+totalArea);
			LogNode currentGreyWaterConsumedHead = myLogHead.addChild("Grey Water Consumed");
			myLogIndex.currentGreyWaterConsumedIndex = currentGreyWaterConsumedHead.addChild(""+currentGreyWaterConsumed);
			LogNode PotableWaterConsumedHead = myLogHead.addChild("Potable Water Consumed");
			myLogIndex.currentPotableWaterConsumedIndex = PotableWaterConsumedHead.addChild(""+currentPotableWaterConsumed);
			LogNode hasEnoughWaterHead = myLogHead.addChild("Has Enough Water");
			myLogIndex.hasEnoughWaterIndex = hasEnoughWaterHead.addChild(""+hasEnoughWater);
			LogNode hasEnoughPowerHead = myLogHead.addChild("Has Enough Power");
			myLogIndex.hasEnoughPowerIndex = hasEnoughPowerHead.addChild(""+hasEnoughPower);
			logInitialized = true; 
		}
		else{
			myCrop.log(myLogIndex.plantHead);
			myLogIndex.currentPowerConsumedIndex.setValue(""+currentPowerConsumed);
			myLogIndex.totalAreaIndex.setValue(""+totalArea);
			myLogIndex.currentGreyWaterConsumedIndex.setValue(""+currentGreyWaterConsumed);
			myLogIndex.currentPotableWaterConsumedIndex.setValue(""+currentPotableWaterConsumed);
			myLogIndex.hasEnoughWaterIndex.setValue(""+hasEnoughWater);
			myLogIndex.hasEnoughPowerIndex.setValue(""+hasEnoughPower);
		}
	}
	
	/**
	* For fast reference to the log tree
	*/
	private class LogIndex{
		public LogNode plantHead;
		public LogNode currentPowerConsumedIndex;
		public LogNode totalAreaIndex;
		public LogNode currentGreyWaterConsumedIndex;
		public LogNode currentPotableWaterConsumedIndex;
		public LogNode hasEnoughWaterIndex;
		public LogNode hasEnoughPowerIndex;
	}
}
