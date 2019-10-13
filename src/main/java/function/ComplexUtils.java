package function;

import org.apache.commons.math3.complex.Complex;

import java.util.Random;

public class ComplexUtils {
    private static final int RAND_MAX = 1000;

    public static Complex getRandomComplexNumber(Random random) {
        Complex complex = new Complex(random.nextInt(), random.nextInt());
        complex.divide(RAND_MAX);
        complex.multiply(2);
        complex.add(-1);
        double rand = random.nextDouble();
        rand /= RAND_MAX;
        rand *= 2;
        rand -= 1;
        complex.add(new Complex(0, rand));
        return complex;
    }
}
