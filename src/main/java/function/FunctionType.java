package function;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;

import static java.lang.Math.pow;

@Getter
@AllArgsConstructor
public enum FunctionType {
    RAND("random", null),
    EXAMPLE_1("x^3", x -> pow(x, 3)),
    EXAMPLE_2("sin(3x)", x -> Math.sin(3 * x)),
    EXAMPLE_3("x^3 + x^2 - x^4", x -> pow(x, 3) + pow(x, 2) - pow(x, 4));

    private String type;
    private Function<Double, Double> userFunction;

    public static FunctionType fromString(String value) {
        return Arrays.stream(FunctionType.values())
                .filter(type -> type.getType().toLowerCase().equals(value))
                .findFirst()
                .orElse(RAND);
    }
}