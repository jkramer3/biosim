package biosim.server.simulation.food;

import biosim.idl.simulation.food.*;
/**
 * Soybean
 * @author    Scott Bell
 */

public class Soybean extends Legume{
	public Soybean(ShelfImpl pShelfImpl){
		super(pShelfImpl);
		canopyClosureConstants[0] = 6797800f;
		canopyClosureConstants[1] = -4365.8f;
		canopyClosureConstants[2] = 1.5573f;
		canopyClosureConstants[5] = -43260f;
		canopyClosureConstants[6] = 33.959f;
		canopyClosureConstants[10] = 112.63f;
		canopyClosureConstants[13] = -.000000004911f;
		canopyClosureConstants[15] = -0.13637f;
		canopyClosureConstants[20] = .000066918f;
		canopyClosureConstants[21] = -.000000021367f;
		canopyClosureConstants[22] = .000000000015467f;

		canopyQYConstants[6] = 0.041513f;
		canopyQYConstants[7] = 0.000051157f;
		canopyQYConstants[8] = -0.000000020992f;
		canopyQYConstants[12] = 0.000000040864f;
		canopyQYConstants[16] = -0.000000021582f;
		canopyQYConstants[17] = -0.00000000010468f;
		canopyQYConstants[22] = 0.000000000000048541f;
		canopyQYConstants[24] = 0.0000000000000000000039259f;
	}

	public PlantType getPlantType(){
		return PlantType.SOYBEAN;
	}

	protected float getConstantPPF(){
		return 324.07f;
	}

	protected float getBCF(){
		return 0.46f;
	}
	
	protected float getCUEMax(){
		return 0.65f;
	}
	
	protected float getCUEMin(){
		return 0.30f;
	}

	protected float getPhotoperiod(){
		return 12f;
	}
	
	protected float getTimeAtOrganFormation(){
		return 46f;
	}

	protected float getN(){
		return 1.5f;
	}

	protected float getCQYMin(){
		return 0.02f;
	}

	protected float getTimeAtCanopySenescence(){
		return 48f;
	}

	protected float getTimeAtCropMaturity(){
		return 86f;
	}

	protected float getOPF(){
		return 1.16f;
	}

	public static float getFractionOfEdibleBiomass(){
		return 0.95f;
	}
	
	protected float getProtectedFractionOfEdibleBiomass(){
		return getFractionOfEdibleBiomass();
	}
	
	/**
	 * Returns calories per kilogram
	*/
	public static float getCaloriesPerKilogram(){
		return 1340f;
	}

	public static float getEdibleFreshBasisWaterContent(){
		return 0.10f;
	}

	public static float getInedibleFreshBasisWaterContent(){
		return 0.90f;
	}
	
	protected float getProtectedEdibleFreshBasisWaterContent(){
		return getEdibleFreshBasisWaterContent();
	}

	protected float getProtectedInedibleFreshBasisWaterContent(){
		return getInedibleFreshBasisWaterContent();
	}
}
