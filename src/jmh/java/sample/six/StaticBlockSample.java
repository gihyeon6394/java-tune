package sample.six;

public class StaticBlockSample {
    static String staticVal;

    static {
        staticVal = "Karina is the best";
        staticVal = StaticBlockSample.staticVal + " in the world";
    }

    public static void main(String[] args) {
        System.out.println(StaticBlockSample.staticVal); // I love Karina
    }

    static {
        staticVal = "I love Karina";
    }
}
