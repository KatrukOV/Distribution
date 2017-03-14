package com.katruk;

import com.katruk.plate.Plate;
import com.katruk.plate.PlateSimple;

public final class Main {

  public static void main(String[] args) {

//    Temperatures temperature = new TemperatureRectangular(0, 100, 100, 100);
//    Plate plate = new PlateRectangular(temperature, 20, 20);
//    plate.calculatedTemperatureDistribution(0.05f, "dd");
//    plate.calculatedTemperatureDistribution(20, "dd");
//    plate.printTemperatures();

    String fileNameArray = "Temperatures.json";
    String fileNameImage = "Temperatures.png";
    Plate plate = new PlateSimple(20, 30, 0, 70, 100, 500);
    plate.calculatedTemperatureDistribution(.0005f, fileNameArray);
    plate.printTemperatures();

    plate.imageTemperatures(fileNameImage, Plate.Color.GREEN);

  }
}
