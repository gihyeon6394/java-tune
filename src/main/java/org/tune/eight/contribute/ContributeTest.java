package org.tune.eight.contribute;

public class ContributeTest {

    public static void main(String[] args) {

        Contributor[] contributors = new Contributor[10]; // 기부자 10명
        // Contribution contribution = new Contribution(); // 기부 재단
        for (int i = 0; i < 10; i++) {
            contributors[i] = new Contributor(new ContributionStatic(), "기부자" + i);
        }

        // 기부 시작
        for (int i = 0; i < 10; i++) {
            contributors[i].start();
        }

    }
}
