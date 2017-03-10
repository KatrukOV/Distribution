package com.katruk.plate;

import com.katruk.temperature.TemperatureRectangular;
import com.katruk.temperature.Temperatures;

import java.util.Arrays;

public final class PlateRectangular implements Plate {

  private final int height;
  private final int width;
  private Temperatures[][] temperatures;

  public PlateRectangular(int height, int width, Temperatures[][] temperatures) {
    this.height = height;
    this.width = width;
    this.temperatures = temperatures;
  }

  @Override
  public void calculatedTemperatureDistribution(final float tolerance) {
    System.out.println("begin");
    Temperatures[][] tempArray = this.temperatures.clone();
    System.out.println("diff=" + tempArray[0][0].difference());

    while (tolerance < tempArray[0][0].difference().floatValue()) {
      int n = tempArray.length;
      System.out.println(">> n = " + n);
      Temperatures[][] newTemperatures = new Temperatures[2 * n][2 * n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          System.out.println("i = " + i);
          System.out.println("j = " + j);
          calculate(newTemperatures,
                    tempArray,
                    i, j);
          System.out.println(" in loop newTemperatures= " + newTemperatures[i][i].avg());
        }
      }
      System.out.println(" before tempArray " + Arrays.deepToString(newTemperatures));
      tempArray = newTemperatures;
      int n1 = tempArray.length;
      System.out.println(">> n1 = " + n1);
      System.out.println(" after tempArray" + Arrays.deepToString(newTemperatures));
      System.out.println("diff=" + tempArray[0][0].difference());
    }
    this.temperatures = tempArray//.clone()
    ;
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
    newTemperatures[2 * n][2 * m] = new TemperatureRectangular(northTemperature,
                                                               centralTemperature,
                                                               centralTemperature,
                                                               westTemperature);
    newTemperatures[2 * n + 1][2 * m] = new TemperatureRectangular(northTemperature,
                                                                   southTemperature,
                                                                   centralTemperature,
                                                                   centralTemperature);
    newTemperatures[2 * n][2 * m + 1] = new TemperatureRectangular(centralTemperature,
                                                                   centralTemperature,
                                                                   eastTemperature,
                                                                   westTemperature);
    newTemperatures[2 * n + 1][2 * m + 1] = new TemperatureRectangular(centralTemperature,
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
        System.out.print(this.temperatures[i][j].avg() +"   ");
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