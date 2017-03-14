package com.katruk.plate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
  public void imageTemperatures(final String fileName, final Color color) {
    final float[][] temperatures = this.temperatures;
    BufferedImage image = makeImage(temperatures, color);
    File file = new File(fileName);
    try {
      ImageIO.write(image, "png", file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private BufferedImage makeImage(float[][] temperatures, final Color color) {
    int height = temperatures.length;
    int width = temperatures[0].length;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    int a = 255;
    int red = 0;
    int green = 0;
    int blue = 0;
    float max = getMax(temperatures);
    float min = getMin(temperatures);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        switch (color) {
          case RED:
            red = normalize(temperatures[y][x], max, min);
            break;
          case GREEN:
            green = normalize(temperatures[y][x], max, min);
            break;
          case BLUE:
            blue = normalize(temperatures[y][x], max, min);
            break;
        }
        int pixel = (a << 24) | (red << 16) | (green << 8) | blue;
        image.setRGB(x, y, pixel);
      }
    }
    return image;
  }

  private int normalize(float value, float max, float min) {
    final int absoluteMax = 255;
    return (int) (((value - min) * absoluteMax) / (max - min));
  }

  private float getMin(float[][] temperatures) {
    float result = temperatures[0][0];
    int width = temperatures[0].length;
    for (float[] temperature : temperatures) {
      for (int j = 0; j < width; j++) {
        if (result > temperature[j]) {
          result = temperature[j];
        }
      }
    }
    return result;
  }

  private float getMax(float[][] temperatures) {
    float result = temperatures[0][0];
    int width = temperatures[0].length;
    for (float[] temperature : temperatures) {
      for (int j = 0; j < width; j++) {
        if (result < temperature[j]) {
          result = temperature[j];
        }
      }
    }
    return result;
  }
}