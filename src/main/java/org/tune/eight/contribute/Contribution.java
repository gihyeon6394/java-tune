package org.tune.eight.contribute;

/**
 * 기부 재단
 * */
public class Contribution {
    private int amount = 0;

    public synchronized void donate() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
