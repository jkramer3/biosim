package com.traclabs.biosim.client.control.ultra;

import java.io.IOException;
import java.util.List;

public abstract class CrewDeathController extends ScenarioController {

	
	
	public CrewDeathController(int numberOfRuns, String outputDirectoryPath)
			throws IOException {
		super(numberOfRuns, outputDirectoryPath);

	}

	protected boolean endConditionMet(int systemsAllowedToFail, List modules) 
	{
		boolean isCrewDead;
		
		if(myBioHolder.theCrewGroups.get(0).isDead())
		{
			return true;
		}
		else
		{
			return false;
		}
			
				
		
	}

}
