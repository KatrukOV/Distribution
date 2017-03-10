package com.katruk.temperature;

import com.katruk.min.MinFloat;

public final class TemperatureRectangular implements Temperatures {

  private final float temperatureNorth;
  private final float temperatureSouth;
  private final float temperatureEast;
  private final float temperatureWest;

  public TemperatureRectangular(float temperatureNorth, float temperatureSouth,
                                float temperatureEast, float temperatureWest) {
    this.temperatureNorth = temperatureNorth;
    this.temperatureSouth = temperatureSouth;
    this.temperatureEast = temperatureEast;
    this.temperatureWest = temperatureWest;
  }

  @Override
  public float avg() {
    return (this.temperatureNorth + this.temperatureSouth
            + this.temperatureEast + this.temperatureWest) / 4;
  }

  @Override
  public float difference() {
    final float avg = avg();
    final float diff1 = Math.abs(avg - this.temperatureNorth);
    final float diff2 = Math.abs(avg - this.temperatureSouth);
    final float diff3 = Math.abs(avg - this.temperatureEast);
    final float diff4 = Math.abs(avg - this.temperatureWest);
    return new MinFloat()
        .of(diff1)
        .of(diff2)
        .of(diff3)
        .of(diff4)
        .floatValue();
  }

  @Override
  public float north() {
    return this.temperatureNorth;
  }

  @Override
  public float south() {
    return this.temperatureSouth;
  }

  @Override
  public float east() {
    return this.temperatureEast;
  }

  @Override
  public float west() {
    return this.temperatureWest;
  }


}
