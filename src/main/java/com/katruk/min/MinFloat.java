package com.katruk.min;

import java.util.TreeSet;

public final class MinFloat /*extends Number*/ implements Min {

  private final TreeSet<Float> numbers = new TreeSet<>();

  public MinFloat of(float value){
    this.numbers.add(value);
    return this;
  }

//  @Override
//  public int intValue() {
//    throw new RuntimeException();
//  }

//  @Override
//  public long longValue() {
//    throw new RuntimeException();
//  }

  @Override
  public float floatValue() {
    return this.numbers.first();
  }
//
//  @Override
//  public double doubleValue() {
//    throw new RuntimeException();
//  }
}
