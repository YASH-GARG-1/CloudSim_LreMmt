package org.cloudbus.cloudsim.examples.power.planetlab;

import java.io.IOException;

/**
 * A simulation of a heterogeneous power aware data center that applies the Inter Quartile Range
 * (IQR) VM allocation policy and Random Selection (RS) VM selection policy.
 * 
 * This example uses a real PlanetLab workload: 20110303.
 * 
 * The remaining configuration parameters are in the Constants and PlanetLabConstants classes.
 * 
 * If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since Jan 5, 2012
 */
public class IqrRs {

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
//		boolean enableOutput = true;
//		boolean outputToFile = false;
//		String inputFolder = IqrRs.class.getClassLoader().getResource("workload/planetlab").getPath();
//		String outputFolder = "output";
		String workload = "20110303"; // PlanetLab workload
		String vmAllocationPolicy = "iqr"; // Inter Quartile Range (IQR) VM allocation policy
		String vmSelectionPolicy = "rs"; // Random Selection (RS) VM selection policy
		String parameter = "1.5"; // the safety parameter of the IQR policy

		boolean enableOutput = true;
		boolean outputToFile = true;
		String inputFolder = "C:\\Users\\Yash\\Documents\\Final Year Project\\cloudsim-3.0.3\\cloudsim-3.0.3\\examples\\workload\\planetlab";
		String outputFolder = "F:\\Output\\IqrRs";
		String Workloads[] = {"20110303", "20110306","20110309","20110322","20110325","20110403","20110409","20110411","20110412","20110420"};
		String parameters [] = { "0.5", "0.6", "0.7", "0.8", "0.9","1.0", "1.1","1.2", "1.3", "1.4", "1.5", "1.6", "1.7","1.8","1.9","2.0"};
		//String parameters [] = { "0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9","1.0", "1.1","1.2", "1.3", "1.4", "1.5", "1.6", "1.7","1.8","1.9","2.0"};
		
		for(String param : parameters)
		{
			parameter = param;
			for(String wrkload : Workloads)
			{
				workload = wrkload;
				new PlanetLabRunner(
					enableOutput,
					outputToFile,
					inputFolder,
					outputFolder,
					workload,
					vmAllocationPolicy,
					vmSelectionPolicy,
					parameter);
				
			}

		}
	}

}
