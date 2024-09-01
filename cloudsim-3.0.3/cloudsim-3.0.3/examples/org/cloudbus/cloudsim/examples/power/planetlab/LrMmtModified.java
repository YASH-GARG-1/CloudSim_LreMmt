package org.cloudbus.cloudsim.examples.power.planetlab;

import java.io.IOException;
import java.util.ArrayList;

import org.cloudbus.cloudsim.power.*;

public class LrMmtModified 
{

    public static void main(String[] args) throws IOException {
        boolean enableOutput = true;
        boolean outputToFile = true;
        String vmSelectionPolicy = "mmt";
        String vmAllocationPolicy = "lre";
        String inputFolder = "C:\\Users\\Yash\\Documents\\Final Year Project\\cloudsim-3.0.3\\cloudsim-3.0.3\\examples\\workload\\planetlab";
        String outputFolder = "D:\\Output\\LreMmt";
        //String Workloads[] = {"20110303"};
        String Workloads[] = {"20110303", "20110306", "20110309", "20110322", "20110325", "20110403", "20110409", "20110411", "20110412", "20110420"};
        String parameters [] = { "1.8","1.9","2.0"};
//        String Workloads[] = {"20110303", "20110306", "20110309", "20110322", "20110325", "20110403", "20110409", "20110411", "20110412", "20110420"};
//        String parameters [] = { "0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9","1.0", "1.1","1.2", "1.3", "1.4", "1.5", "1.6", "1.7","1.8","1.9","2.0"};

        for (String param : parameters) {
            for (String workload : Workloads) {
            	
                new PlanetLabRunner(
                        enableOutput,
                        outputToFile,
                        inputFolder,
                        outputFolder,
                        workload,
                        vmAllocationPolicy,
                        vmSelectionPolicy,
                        param
                );
            }
        }
    }
}
