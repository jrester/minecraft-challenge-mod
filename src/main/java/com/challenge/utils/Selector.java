package com.challenge.utils;

public interface Selector<T> {
   T selectWithModifier(final int modifier); 
   int getMaxSelectionModifier();
}
