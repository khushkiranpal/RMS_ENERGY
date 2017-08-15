/**
 * 
 */
package platform;

import java.util.TreeMap;

/**
 * @author KHUSHKIRAN PAL
 *
 */
public class Energy {
	
	private final static double C_EFF = 0.8;
	private final static double Max_Freq_Dependent_Power = 0.8*1*1*1;



	
	public double powerDynamic(double freq)
	{
		
		double power = C_EFF * freq*freq*freq;
		return power;
	}

	public double powerStatic()
	{
		
		
		return (5/100)*Max_Freq_Dependent_Power;
	}

	public double powerIND()
	{
		return(15/100)*Max_Freq_Dependent_Power;
	}
	
	public double powerIDLE ()
	{
		return (20/100)*Max_Freq_Dependent_Power;
	}
	
	public double powerSLEEP()
	{
		return .00000080;
	}
	
	
	public double energyActive(long exec_time, double freq)
	{
		double total_power =0, activeEnergy;
		total_power = powerDynamic(freq)+powerStatic()+powerIND();
		activeEnergy = total_power*exec_time;
		return activeEnergy;
	}

	public double energy_IDLE(long exec_time)
	{
		return powerIDLE ()* exec_time;
	}
	
	public double energySLEEP (long exec_time)
	{
		return powerSLEEP() * exec_time;
	}

	public double energyOverhead ()
	{
		return 0.000000385;
	}
}

