package com.katruk;

import com.katruk.plate.Plate;
import com.katruk.plate.PlateRectangular;
import com.katruk.plate.PlateSimple;
import com.katruk.temperature.TemperatureRectangular;
import com.katruk.temperature.Temperatures;

public final class Main {

  public static void main(String[] args) {

//    Temperatures temperature = new TemperatureRectangular(0, 100, 100, 100);
//    Plate plate = new PlateRectangular(temperature, 20, 20);
//    plate.calculatedTemperatureDistribution(0.05f, "dd");
//    plate.calculatedTemperatureDistribution(20, "dd");
//    plate.printTemperatures();

    String fileName = "Temperatures.json";
    Plate plate = new PlateSimple(20, 20, 0, 100, 100, 100);
    plate.calculatedTemperatureDistribution(.05f, fileName);
    plate.printTemperatures();
    plate.showTemperatures(fileName);

  }
}
