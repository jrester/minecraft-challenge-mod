package com.challenge.utils;

public enum ItemCategory {
  GENERAL(0.85),
  POTTERY_SHERD(0.025),
  BANNER_PATTERN(0.025),
  COPPER(0.025),
  SMITHING_TEMPLATE(0.025),
  ENCHANTED_BOOK(0.05);

  private double probability;

  ItemCategory(double probability) {
    this.probability = probability;
  }

  public double getProbability() {
    return this.probability;
  }
}
