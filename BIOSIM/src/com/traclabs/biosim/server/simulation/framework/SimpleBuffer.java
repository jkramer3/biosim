package biosim.server.simulation.framework;

import java.util.*;
/**
 * @author    Scott Bell
 */

public class SimpleBuffer {
	//The level of whatever this store is holding (at t)
	protected float level = 0f;
	//The capacity of what this store can hold (at t)
	protected float capacity = 0f;
	//What this store has leaked (at t)
	protected float overflow = 0f;
	//Whether this Store has collected a reference to the BioDriver or not.
	private boolean hasCollectedReferences = false;
	protected float initialLevel = 0f;
	protected float initialCapacity = 0f;

	/**
	* Creates a Store with an initial level and capacity of 0
	*/
	public SimpleBuffer(){
		level = initialLevel = 0f;
		capacity = initialCapacity = 10f;
	}

	/**
	* Creates a Store with an initial level and capacity user specified
	* @param pInitialLevel the initial level of the store
	* @param pInitialCapacity the initial capacity of the store
	*/
	public SimpleBuffer(float pInitialLevel, float pInitialCapacity){
		level = initialLevel = pInitialLevel;
		capacity = initialCapacity = pInitialCapacity;
	}
	
	/**
	* Sets the capacity of the store (how much it can hold)
	* @param metricAmount the new volume of the store
	*/
	public void setCapacity(float newCapacity){
		if (newCapacity <= 0){
			System.out.println("SimpleBuffer: told to change capacity to 0");
			newCapacity = Float.MIN_VALUE;
		}
		float percentage = level / capacity;
		System.out.print("SimpleBuffer: level = "+level+", capacity = "+capacity+" percentage = "+percentage);
		capacity = newCapacity;
		level = percentage * newCapacity;
		System.out.println(", newCapacity = "+newCapacity+", newLevel = "+level);
	}

	/**
	* Sets the level to a set amount
	* @param metricAmount the level to set the store to
	*/
	public void setLevel(float metricAmount){
		level = Math.min(metricAmount, capacity);
	}

	/**
	* Attempts to add to the store.  If the level is near capacity, it will only up to capacity
	* @param amountRequested the amount wanted to add to the store
	* @return the amount actually added to the store
	*/
	public float add(float amountRequested){
		//idiot check
		if (amountRequested <= 0)
			return 0f;
		float acutallyAdded = 0f;
		if ((amountRequested + level) > capacity){
			//adding more than capacity
			acutallyAdded = capacity - level;
			level += acutallyAdded;
			overflow += (amountRequested - acutallyAdded);
			return  acutallyAdded;
		}
		else{
			acutallyAdded = amountRequested;
			level += acutallyAdded;
			return acutallyAdded;
		}
	}

	/**
	* Attemps to take the amount requested from the store
	* @param amountRequested the amount wanted
	* @return the amount actually retrieved
	*/
	public float take(float amountRequested){
		//idiot check
		if (amountRequested <= 0f){
			return 0f;
		}
		float takenAmount;
		//asking for more stuff than exists
		if (amountRequested > level){
			takenAmount = level;
			level = 0f;
		}
		//stuff exists for request
		else{
			takenAmount = amountRequested;
			level -= takenAmount;
		}
		return takenAmount;
	}

	/**
	* Retrieves the level of the store
	* @return the level of the store
	*/
	public float getLevel(){
		return level;
	}
	
	/**
	* Retrieves the overflow of the store
	* @return the overflow of the store
	*/
	public float getOverflow(){
		return overflow;
	}

	/**
	* Retrieves the capacity of the store
	* @return the capacity of the store
	*/
	public float getCapacity(){
		return capacity;
	}

	/**
	* Resets the level to 0
	*/
	public void reset(){
		level = initialLevel;
		capacity = initialCapacity;
		overflow = 0f;
	}
}
