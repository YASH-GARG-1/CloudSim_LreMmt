package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.models.PowerModel;

import java.util.*;
import java.util.List;
import java.util.Set;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * The Local Regression (LR) VM allocation policy with enhanced energy efficiency and dynamic threshold.
 */
public class PowerVmAllocationPolicyMigrationLocalRegressionEnhanced extends PowerVmAllocationPolicyMigrationLocalRegression {

    /** The base lower utilization threshold. */
    private double baseLowerUtilizationThreshold;

    /** The dynamically adjusted lower utilization threshold. */
    private double lowerUtilizationThreshold;

    /**
     * Instantiates a new power vm allocation policy migration local regression enhanced.
     * 
     * @param hostList the host list
     * @param vmSelectionPolicy the vm selection policy
     * @param safetyParameter the safety parameter
     * @param schedulingInterval the scheduling interval
     * @param fallbackVmAllocationPolicy the fallback vm allocation policy
     * @param baseLowerUtilizationThreshold the base lower utilization threshold for underutilized hosts
     */
    public PowerVmAllocationPolicyMigrationLocalRegressionEnhanced(
            List<? extends Host> hostList,
            PowerVmSelectionPolicy vmSelectionPolicy,
            double safetyParameter,
            double schedulingInterval,
            PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy,
            double baseLowerUtilizationThreshold) {
        super(hostList, vmSelectionPolicy, safetyParameter, schedulingInterval, fallbackVmAllocationPolicy);
        this.baseLowerUtilizationThreshold = baseLowerUtilizationThreshold;
        this.lowerUtilizationThreshold = baseLowerUtilizationThreshold; // Start with the base threshold
    }

    /**
     * Adjusts the lower utilization threshold dynamically based on the current workload.
     */
    private void adjustLowerUtilizationThreshold() {
        double avgUtilization = calculateAverageUtilization();

        // Example of dynamic adjustment: lower threshold if average utilization is low
        if (avgUtilization < 0.3) {
            lowerUtilizationThreshold = Math.max(0.2, baseLowerUtilizationThreshold - 0.1);
        } else if (avgUtilization > 0.7) {
            lowerUtilizationThreshold = Math.min(0.5, baseLowerUtilizationThreshold + 0.1);
        } else {
            lowerUtilizationThreshold = baseLowerUtilizationThreshold;
        }

        Log.printLine("Adjusted lower utilization threshold to: " + lowerUtilizationThreshold);
    }

    /**
     * Calculates the average CPU utilization across all hosts.
     * 
     * @return the average CPU utilization
     */
    private double calculateAverageUtilization() {
        double totalUtilization = 0;
        int hostCount = 0;

        for (Host host : getHostList()) {
        	PowerHost powerHost = (PowerHost) host;
            double utilization = powerHost.getUtilizationOfCpu();
            if (utilization > 0) {
                totalUtilization += utilization;
                hostCount++;
            }
        }

        return (hostCount > 0) ? totalUtilization / hostCount : 0;
    }

    /**
     * Optimizes the allocation of VMs.
     */
    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
        // Adjust threshold before optimizing allocation
        adjustLowerUtilizationThreshold();
        return super.optimizeAllocation(vmList);
    }

    /**
     * Checks if a host is underutilized with dynamically adjusted threshold.
     * 
     * @param excludedHosts the excluded hosts
     * @return true, if the host is underutilized
     */
    @Override
    protected PowerHost getUnderUtilizedHost(Set<? extends Host> excludedHosts) {
        // Adjust threshold before checking underutilization
        adjustLowerUtilizationThreshold();

        double minUtilization = lowerUtilizationThreshold;
        PowerHost underUtilizedHost = null;

        for (PowerHost host : this.<PowerHost>getHostList()) {
            if (excludedHosts.contains(host)) {
                continue;
            }
            double utilization = host.getUtilizationOfCpu();
            if (utilization > 0 && utilization < minUtilization && !areAllVmsMigratingOutOrAnyVmMigratingIn(host)) {
                minUtilization = utilization;
                underUtilizedHost = host;
            }
        }

        if (underUtilizedHost != null) {
            Log.printLine("Under-utilized host found: host #" + underUtilizedHost.getId() + " with utilization " + minUtilization);
        } else {
            Log.printLine("No under-utilized host found below threshold " + lowerUtilizationThreshold);
        }

        return underUtilizedHost;
    }

    /**
     * Checks if the host is overutilized.
     * 
     * @param host the host
     * @return true, if is host over utilized
     */
    @Override
    protected boolean isHostOverUtilized(PowerHost host) {
        PowerHostUtilizationHistory _host = (PowerHostUtilizationHistory) host;
        double[] utilizationHistory = _host.getUtilizationHistory();
        int length = 10; // we use 10 to make the regression responsive enough to latest values
        if (utilizationHistory.length < length) {
            return getFallbackVmAllocationPolicy().isHostOverUtilized(host);
        }
        double[] utilizationHistoryReversed = new double[length];
        for (int i = 0; i < length; i++) {
            utilizationHistoryReversed[i] = utilizationHistory[length - i - 1];
        }
        double[] estimates = null;
        try {
            estimates = getParameterEstimates(utilizationHistoryReversed);
        } catch (IllegalArgumentException e) {
            return getFallbackVmAllocationPolicy().isHostOverUtilized(host);
        }
        double migrationIntervals = Math.ceil(getMaximumVmMigrationTime(_host) / getSchedulingInterval());
        double predictedUtilization = estimates[0] + estimates[1] * (length + migrationIntervals);
        predictedUtilization *= getSafetyParameter();

        addHistoryEntry(host, predictedUtilization);

        return predictedUtilization >= 1;
    }
}
