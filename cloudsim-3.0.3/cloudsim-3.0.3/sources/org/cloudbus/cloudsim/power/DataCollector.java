package org.cloudbus.cloudsim.power;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {
    private List<DataPoint> data;

    public DataCollector() {
        this.data = new ArrayList<>();
    }

    public void collectData(double cpuUtilization, double ramUtilization, double powerConsumption, double vmMips, boolean migrationOccurred, boolean energyImproved) {
        DataPoint dp = new DataPoint(cpuUtilization, ramUtilization, powerConsumption, vmMips, migrationOccurred, energyImproved);
        data.add(dp);
    }

    public void saveDataToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (DataPoint dp : data) {
                writer.write(dp.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DataPoint {
    double cpuUtilization;
    double ramUtilization;
    double powerConsumption;
    double vmMips;
    boolean migrationOccurred;
    boolean energyImproved;

    public DataPoint(double cpuUtilization, double ramUtilization, double powerConsumption, double vmMips, boolean migrationOccurred, boolean energyImproved) {
        this.cpuUtilization = cpuUtilization;
        this.ramUtilization = ramUtilization;
        this.powerConsumption = powerConsumption;
        this.vmMips = vmMips;
        this.migrationOccurred = migrationOccurred;
        this.energyImproved = energyImproved;
    }

    public String toCSV() {
        return cpuUtilization + "," + ramUtilization + "," + powerConsumption + "," + vmMips + "," + migrationOccurred + "," + energyImproved;
    }
}
