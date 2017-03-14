package com.katruk.plate;

import com.katruk.temperature.TemperatureRectangular;
import com.katruk.temperature.Temperatures;

import java.util.Arrays;

public final class PlateRectangular implements Plate {

  private final Temperatures beginTemperature;
  private Temperatures[][] temperatures;

  public PlateRectangular(Temperatures temperature, int height, int width) {
    this.beginTemperature = temperature;
    this.temperatures = new Temperatures[height][width];
  }

  @Override
  public void calculatedTemperatureDistribution(final float tolerance, final String fileName) {
    this.temperatures = initTemperatures(this.temperatures, this.beginTemperature);
//    System.out.println("begin");
    Temperatures[][] tempArray = this.temperatures.clone();
//    System.out.println("diff=" + tempArray[0][0].difference());
    float difference;
    do {
      int n = tempArray.length;
      int m = tempArray[0].length;
      Temperatures[][] newTemperatures = copyTemperatures(tempArray, n, m);

      for (int i = 1; i < n - 1; i++) {
        for (int j = 1; j < m - 1; j++) {
          float value = (tempArray[i - 1][j].avg() + tempArray[i + 1][j].avg()
                         + tempArray[i][j - 1].avg() + tempArray[i][j + 1].avg()) / 4.0f;

          newTemperatures[i][j] = new TemperatureRectangular(value, value, value, value);
        }
      }
      difference = newTemperatures[1][1].avg() - tempArray[1][1].avg();
      tempArray = newTemperatures;
    } while (tolerance < difference);
    this.temperatures = tempArray;
  }

  private Temperatures[][] copyTemperatures(Temperatures[][] tempArray, int n, int m) {
    Temperatures[][] newTemperatures = new Temperatures[n][m];
    for (int i = 0; i < n; i++) {
      System.arraycopy(tempArray[i], 0, newTemperatures[i], 0, m);
    }
    return newTemperatures;
  }

  private Temperatures[][] initTemperatures(Temperatures[][] temperatures,
                                            Temperatures beginTemperature) {
    int height = temperatures.length;
    int width = temperatures[0].length;
    Temperatures north = new TemperatureRectangular(beginTemperature.north(), 0, 0, 0);
    Temperatures east = new TemperatureRectangular(0, beginTemperature.east(), 0, 0);
    Temperatures south = new TemperatureRectangular(0, 0, beginTemperature.south(), 0);
    Temperatures west = new TemperatureRectangular(0, 0, 0, beginTemperature.west());
    for (int j = 0; j < width; j++) {
      temperatures[0][j] = north;
    }
    for (int i = 0; i < height; i++) {
      temperatures[i][width - 1] = east;
    }
    for (int j = 0; j < width; j++) {
      temperatures[height - 1][j] = south;
    }
    for (int i = 0; i < height; i++) {
      temperatures[i][0] = west;
    }
    for (int i = 1; i < height - 1; i++) {
      for (int j = 1; j < width - 1; j++) {
        temperatures[i][j] = new TemperatureRectangular(0, 0, 0, 0);
      }
    }
    return temperatures;
  }

  private void calculate(final Temperatures[][] newTemperatures,
                         final Temperatures[][] tempArray,
                         final int n, final int m) {
    final float centralTemperature = tempArray[n][m].avg();
    final float northTemperature = tempArray[n][m].north();
    final float southTemperature = tempArray[n][m].south();
    final float westTemperature = tempArray[n][m].west();
    final float eastTemperature = tempArray[n][m].east();
    System.out.println(">> central=" + centralTemperature + " north=" + northTemperature + " south="
                       + southTemperature + " west=" + westTemperature + " east="
                       + eastTemperature);
    newTemperatures[n][m] = new TemperatureRectangular(northTemperature,
                                                       centralTemperature,
                                                       centralTemperature,
                                                       westTemperature);
    newTemperatures[n + 1][m] = new TemperatureRectangular(northTemperature,
                                                           southTemperature,
                                                           centralTemperature,
                                                           centralTemperature);
    newTemperatures[n][m + 1] = new TemperatureRectangular(centralTemperature,
                                                           centralTemperature,
                                                           eastTemperature,
                                                           westTemperature);
    newTemperatures[n + 1][m + 1] = new TemperatureRectangular(centralTemperature,
                                                               southTemperature,
                                                               eastTemperature,
                                                               centralTemperature);
//    System.out.println(">> newTemperatures=" + newTemperatures);
//    System.out.println(">> newTemperatures avg=" + newTemperatures[n][n].avg());
  }

  @Override
  public void printTemperatures() {
    int n = this.temperatures.length;
    int m = this.temperatures[0].length;
    System.out.println("n=" + n + " m=" + m);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
//        System.out.print(">> n=" + this.temperatures[i][j].north());
//        System.out.print(">> s=" + this.temperatures[i][j].south());
//        System.out.print(">> e=" + this.temperatures[i][j].east());
//        System.out.print(">> w=" + this.temperatures[i][j].west()+"\n");
        System.out.printf("%.2f  ", this.temperatures[i][j].avg());
      }
      System.out.print("\n");
    }
  }

  @Override
  public void imageTemperatures(String fileName, Color color) {

  }


}