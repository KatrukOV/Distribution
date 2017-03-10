package com.katruk.plate;

public interface Plate {

  void calculatedTemperatureDistribution(final float tolerance, final String fileName);

  void printTemperatures();
}
