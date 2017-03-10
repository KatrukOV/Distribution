package com.katruk;

import com.katruk.plate.Plate;
import com.katruk.plate.PlateRectangular;
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

    Temperatures[][] temperatures = new Temperatures[1][1];
    temperatures[0][0] = new TemperatureRectangular(0, 100, 100, 100);
    Plate plate = new PlateRectangular(10, 10, temperatures);
    plate.calculatedTemperatureDistribution(3);
    plate.printTemperatures();

  }
}
