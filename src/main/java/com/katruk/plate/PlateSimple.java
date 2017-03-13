package com.katruk.plate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katruk.temperature.Temperatures;

import java.io.File;
import java.io.IOException;

public final class PlateSimple implements Plate {

  private final float north;
  private final float south;
  private final float east;
  private final float west;
  private float[][] temperatures;

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
    float[][] tempArray = this.temperatures.clone();
    float difference;
    int n = tempArray.length;
    int m = tempArray[0].length;
    do {
      float[][] newTemperatures = copyTemperatures(tempArray, n, m);
      calculate(tempArray, n, m, newTemperatures);
      difference = newTemperatures[1][1] - tempArray[1][1];
      tempArray = newTemperatures;
    } while (tolerance < difference);
    saveToFile(tempArray, fileName);
    this.temperatures = tempArray;
  }

  private void saveToFile(float[][] temperatures, String fileName) {
    File jsonFile = new File(fileName);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.writeValue(jsonFile, temperatures);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void calculate(float[][] tempArray, int n, int m, float[][] newTemperatures) {
    for (int i = 1; i < n - 1; i++) {
      for (int j = 1; j < m - 1; j++) {
        newTemperatures[i][j] = (tempArray[i - 1][j] + tempArray[i + 1][j]
                                 + tempArray[i][j - 1] + tempArray[i][j + 1]) / 4.0f;
      }
    }
  }

  private float[][] copyTemperatures(float[][] tempArray, int n, int m) {
    float[][] newTemperatures = new float[n][m];
    for (int i = 0; i < n; i++) {
      System.arraycopy(tempArray[i], 0, newTemperatures[i], 0, m);
    }
    return newTemperatures;
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
    int m = this.temperatures[0].length;
    for (float[] temperature : this.temperatures) {
      for (int j = 0; j < m; j++) {
        System.out.printf("%.2f  ", temperature[j]);
      }
      System.out.print("\n");
    }
  }

  @Override
  public void showTemperatures(String fileName) {
    float[][] temperatures = readFromFile(fileName);

    makePicture();
//    printTemperatures();

  }

  private void makePicture() {

  }

  private float[][] readFromFile(String fileName) {
    File jsonFile = new File(fileName);
    ObjectMapper objectMapper = new ObjectMapper();
    float[][] result = new float[0][];
    try {
      result = objectMapper.readValue(jsonFile, new TypeReference<float[][]>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

}