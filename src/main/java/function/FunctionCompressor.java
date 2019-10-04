package function;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Pair;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ui.UIData;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.math3.complex.Complex.ZERO;

public class FunctionCompressor {

    private static final int RAND_MAX = 1000;

    private XYSeriesCollection series = new XYSeriesCollection();

    public XYDataset compress(UIData data) {
        int n = data.getN();

        Complex[][] eWave = initialize(n, true);

        Complex[] x = findX(data.getFn(), eWave, n);

        Complex[] fWave = findFWave(data, n, eWave, x);

        series.addSeries(generateDataSet("f wave", fWave));
        return series;
    }

    /**
     * e^(2*pi*i*jk/n) kj from 0 to N-1
     */
    private Complex[][] initialize(int n, boolean positive) {
        Complex[][] e = new Complex[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double imaginary = (2 * Math.PI * i * j) / n;
                e[i][j] = new Complex(0, positive ? imaginary : -1 * imaginary).exp();
            }
        }

        return e;
    }

    private Complex[] findX(FunctionType type, Complex[][] eWave, int n) {
        Complex[] f = type.equals(FunctionType.RAND) ? findRandomFunctionValues(n)
                : findFunctionValues(type.getUserFunction(), n);

        series.addSeries(generateDataSet("f", f));

        Complex[] x = initializeZeroComplexArray(n);
        Complex[][] eWave1 = initialize(n, false);

        //Matrix multiplying and calculating N
        Complex N = ZERO;
        for (int j = 0; j < n; j++) {
            N = N.add(eWave[1][j].multiply(eWave1[j][1]));
        }

        //x = (eWave1 * f) / N
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                x[i] = x[i].add(eWave1[i][j].multiply(f[j].getImaginary()));
            }
            x[i] = x[i].divide(N);
        }

        return x;
    }

    private Complex[] findRandomFunctionValues(int n) {
        Random random = new Random();
        Complex[] f = new Complex[n];

        for (int i = 0; i < n; i++) {
            Complex complex = new Complex(random.nextInt(), random.nextInt());
            complex.divide(RAND_MAX);
            complex.multiply(2);
            complex.add(-1);
            double rand = random.nextDouble();
            rand /= RAND_MAX;
            rand *= 2;
            rand -= 1;
            complex.add(new Complex(0, rand));

            f[i] = complex;
        }

        return f;
    }

    private Complex[] findFunctionValues(Function<Double, Double> userFunction, int n) {
        Complex[] f = new Complex[n];

        for (int i = 0; i < n; i++) {
            //step [-1;1]
            double x = -1.0 + i * (2.0 / n);
            double y = userFunction.apply(x);
            f[i] = new Complex(x, y);
        }

        return f;
    }

    private Complex[] findFWave(UIData data, int n, Complex[][] eWave, Complex[] x) {
        Complex[] xWave = compress(x, data.getPercents());
        Complex[] fWave = initializeZeroComplexArray(n);

        //getting fWave by multiplying of eWave to xWave.
        if (data.getFn() == FunctionType.RAND) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    fWave[i] = fWave[i].add(eWave[i][j].multiply(xWave[j]));
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                double step = -1.0 + i * (2.0 / n);
                Complex complex = ZERO;
                for (int j = 0; j < n; j++) {
                    complex = complex.add(eWave[i][j].multiply(xWave[j]));
                }

                fWave[i] = new Complex(step, complex.getReal());
            }
        }
        return fWave;
    }

    /**
     * Обнуление указанного процента наименьших элементов
     */
    private Complex[] compress(Complex[] x, int percents) {
        int q = (int) (percents / 100.0 * x.length);

        LinkedList<Pair<Integer, Complex>> enumeratedAndSortedByComplexNumberX = IntStream.range(0, x.length)
                .boxed()
                .map(i -> new Pair<>(i, x[i]))
                .sorted(Comparator.comparingDouble(pair -> pair.getSecond().getImaginary()))
                .collect(Collectors.toCollection(LinkedList::new));

        for (int i = 0; i < q; i++) {
            Pair<Integer, Complex> pair = enumeratedAndSortedByComplexNumberX.get(i);
            enumeratedAndSortedByComplexNumberX.set(i, new Pair<>(pair.getFirst(), ZERO));
        }

        return enumeratedAndSortedByComplexNumberX.stream()
                .sorted(Comparator.comparingInt(Pair::getFirst))
                .map(Pair::getSecond)
                .toArray(Complex[]::new);
    }

    private XYSeries generateDataSet(String title, Complex[] complexes) {
        XYSeries series = new XYSeries(title);

        Arrays.stream(complexes)
                .forEach(complex -> series.add(complex.getImaginary(), complex.getReal()));

        return series;
    }

    private Complex[] initializeZeroComplexArray(int n) {
        return IntStream.range(0, n)
                .boxed()
                .map(i -> ZERO)
                .toArray(Complex[]::new);
    }
}
