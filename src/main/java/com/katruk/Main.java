package com.katruk;

import com.katruk.plate.Plate;
import com.katruk.plate.PlateParallel;
import com.katruk.plate.PlateSimple;
import com.katruk.util.CompareUtil;

public final class Main {

    public static void main(String[] args) {
        int height = 20;
        int width = 30;
        float tolerance = 0.0005f;
        float north = 0;
        float east = 100;
        float south = 100;
        float west = 100;

        System.out.println("Starting single processor run...");
        PlateSimple plateSingle = new PlateSimple(height, width, north, east, south, west);
        plateSingle.calculatedTemperatureDistribution(tolerance, "Temperatures_Single.json");
        plateSingle.imageTemperatures("Temperatures_Single.png", Plate.Color.GREEN);
        System.out.println("Single processor run finished.");

        System.out.println("Starting four-processor run...");
        PlateParallel plateParallel = new PlateParallel(height, width, north, east, south, west);
        plateParallel.calculatedTemperatureDistribution(tolerance, "Temperatures_Parallel.json");
        plateParallel.imageTemperatures("Temperatures_Parallel.png", Plate.Color.BLUE);
        System.out.println("Four-processor run finished.");

        double stdDev = CompareUtil.calculateStdDevOfDifferences(
                plateSingle.getTemperatures(),
                plateParallel.getTemperatures()
        );

        System.out.printf("Standard deviation of differences: %.10f\n", stdDev);
    }

}
