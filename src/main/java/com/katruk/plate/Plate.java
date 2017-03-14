package com.katruk.plate;

public interface Plate {

  void calculatedTemperatureDistribution(final float tolerance, final String fileName);

  void printTemperatures();

  void imageTemperatures(final String fileName, final Color color);

  enum Color {
    RED,
    GREEN,
    BLUE
  }
}
