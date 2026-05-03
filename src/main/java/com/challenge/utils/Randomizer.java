package com.challenge.utils;

import java.util.Random;

public class Randomizer<T> {
  private Random random = new Random();
  private final Selector<T> selector;

  public Randomizer(Selector<T> selector) {
    this.selector = selector;
  }

  public T getRandom() {
    int modifier = random.nextInt(this.selector.getMaxSelectionModifier());
    return this.selector.selectWithModifier(modifier);
  }
}
