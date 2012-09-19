package jemstone.util.compare;

import java.util.Comparator;

import jemstone.model.HasAmount;


public class AmountComparator implements Comparator<HasAmount> {
  @Override
  public int compare(HasAmount o1, HasAmount o2) {
    double amount1 = o1.getAmount();
    double amount2 = o2.getAmount();
    return (amount1 < amount2 ? -1 : (amount1 == amount2 ? 0 : 1));
  }
}
