package org.tune.eight.contribute;

/**
 * 기부 재단
 */
public class ContributionStatic {
    private static int amount = 0;

    public static synchronized void donate() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
