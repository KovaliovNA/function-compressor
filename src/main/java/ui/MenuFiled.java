package ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum MenuFiled {
    CALCULATE_BUTTON("Calculate"),
    PERCENT_SLIDER("Percent: "),
    N_NUMBER_INPUT_FIELD("N: "),
    FUNCTION_POP_UP("fn: ");

    private String fieldName;

    public static Optional<MenuFiled> fromString(String value) {
        return Arrays.stream(MenuFiled.values())
                .filter(menuFiled -> menuFiled.getFieldName().toLowerCase().equals(value.toLowerCase()))
                .findFirst();
    }
}
