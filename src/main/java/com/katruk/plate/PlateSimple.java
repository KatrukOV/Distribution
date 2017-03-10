package com.katruk.plate;

import com.katruk.temperature.TemperatureRectangular;
import com.katruk.temperature.Temperatures;

import java.util.Arrays;

public final class PlateSimple implements Plate {

  private final float north;
  private final float south;
  private final float east;
  private final float west;
  private float[][] temperatures;
  private int count = 0;

  public PlateSimple(int height, int width, float north, float east, float south, float west) {
    this.north = north;
    this.south = south;
    this.east = east;
    this.west = west;
    this.temperatures = new float[height][width];
  }

  @Override
  public void calculatedTemperatureDistribution(final float tolerance, final String fileName) {

    this.temperatures =
        initTemperatures(this.temperatures, this.north, this.east, this.south, this.west);

//    Temperatures[][] tempArray = this.temperatures.clone();
    float[][] tempArray = this.temperatures.clone();

    float difference;

    do {
      int n = tempArray.length;
      int m = tempArray[0].length;
      System.out.println(">> n = " + n);
//      float[][] newTemperatures = tempArray.clone();
      float[][] newTemperatures = new float[n][m];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          newTemperatures[i][j] = tempArray[i][j];
        }
      }
      for (int i = 1; i < n - 1; i++) {
        for (int j = 1; j < m - 1; j++) {
          newTemperatures[i][j] = (tempArray[i - 1][j] + tempArray[i + 1][j]
                                   + tempArray[i][j - 1] + tempArray[i][j + 1]) / 4.0f;
        }
      }
      System.out.println(" before tempArray " + Arrays.deepToString(tempArray));

      difference = newTemperatures[1][1] - tempArray[1][1];
      System.out.println(" after tempArray" + Arrays.deepToString(tempArray));
      System.out.println(
          "diff=" + difference + " tempArray=" + tempArray[1][1] + " newTemperatures[1][1]="
          + newTemperatures[1][1]);
      tempArray = newTemperatures;
      count++;
    } while (tolerance < difference);

    System.out.println("count=" + count);
    this.temperatures = tempArray;//.clone()
  }

  private float[][] initTemperatures(final float[][] temperatures, final float north,
                                     final float east, final float south, final float west) {
    int height = temperatures.length;
    int width = temperatures[0].length;

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
    return temperatures;
  }

  @Override
  public void printTemperatures() {
    int n = this.temperatures.length;
    int m = this.temperatures[0].length;
    System.out.println("n=" + n + " m=" + m);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        System.out.printf("%.2f  ", this.temperatures[i][j]);
      }
      System.out.print("\n");
    }
  }
}


/*
  Temperatures[][] tempArray = this.temperatures;

    while (tolerance > tempArray[0][0].difference().floatValue()) {
        int n = tempArray.length;
        tempArray = new Temperatures[2 * n][2 * n];

        calculate(tempArray,
//              0, this.temperatures.length,
//              0, this.temperatures[0].length,
        tolerance);

        }

        this.temperatures = tempArray;
        }

private void calculate(final Temperatures[][] temperatures,
//                         final int startN, final int endN,
//                         final int startM, final int endM,
final float tolerance
    ) {
//    if (tolerance > temperatures[0][0].difference().floatValue()) {
//      return;
//    }
    float centralTemperature = temperatures[0][0].avg();
//    int midN = startN + (endN - startN)/2;
//    int midM = startM + (endM - startM)/2;
    int n = temperatures.length;
//    int m = temperatures[0].length;
    Temperatures[][] newTemperatures = new Temperatures[2 * n][2 * n];
    for (int i = 0; i < n; i++) {
    for (int j = 0; i < n; i++) {
    newTemperatures[i][j] = new TemperatureRectangular(temperatures[0][0].north(),
    temperatures[0][0].avg(),
    temperatures[0][0].avg(),
    temperatures[0][0].west());
    }
  }
}

*/