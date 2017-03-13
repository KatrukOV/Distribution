package com.katruk;

import com.katruk.plate.Plate;
import com.katruk.plate.PlateRectangular;
import com.katruk.plate.PlateSimple;
import com.katruk.temperature.TemperatureRectangular;
import com.katruk.temperature.Temperatures;

public final class Main {

  public static void main(String[] args) {

//    Temperatures[][] temperatures = new Temperatures[1][2];
//    temperatures[0][0] = new TemperatureRectangular(10, 13, 20, 15);
//    System.out.println(">> " + temperatures[0][0]);
//    System.out.println(">> " + temperatures[0][0].difference());
//    System.out.println(">> " + temperatures[0][0].avg());
//    System.out.println(">> " + temperatures.length);
//    System.out.println(">> " + temperatures[0].length);

//    Temperatures[][] temperatures = new Temperatures[1][1];
//    temperatures[0][0] = new TemperatureRectangular(0, 100, 100, 100);
    Temperatures temperature = new TemperatureRectangular(0, 100, 100, 100);
    Plate plate = new PlateRectangular(temperature, 20, 20);
    plate.calculatedTemperatureDistribution(20, "dd");

//    Plate plate = new PlateSimple(20, 20, 0, 50, 70, 100);
//    plate.calculatedTemperatureDistribution(0.5f, "dd");
//    plate.printTemperatures();

  }
}
