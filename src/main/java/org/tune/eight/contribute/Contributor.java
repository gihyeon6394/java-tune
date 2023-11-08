package org.tune.eight.contribute;

/**
 * 기부자
 */
public class Contributor extends Thread {

    private ContributionStatic myContribution;
    private String myName;

    public Contributor(ContributionStatic myContribution, String myName) {
        this.myContribution = myContribution;
        this.myName = myName;
    }

    /**
     * 1000번 기부
     */
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            myContribution.donate();
        }
        System.out.println(myName + " : " + myContribution.getAmount());
    }
}
