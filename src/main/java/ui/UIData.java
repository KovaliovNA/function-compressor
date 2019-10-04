package ui;

import function.FunctionType;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

@Getter
public class UIData {

    private FunctionType fn;
    private int n;
    private int percents;

    @Builder
    public UIData(Component[] components) {
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel jLabel = (JLabel) component;
                MenuFiled.fromString(jLabel.getText())
                        .ifPresent(menuFiled -> fillFieldIfNeeded(menuFiled, jLabel.getLabelFor()));
            }
        }
    }

    private void fillFieldIfNeeded(MenuFiled menuFiled, Component labelFor) {
        switch (menuFiled) {
            case FUNCTION_POP_UP:
                JComboBox comboBox = (JComboBox) labelFor;
                this.fn = FunctionType.fromString((String) comboBox.getSelectedItem());
                break;
            case N_NUMBER_INPUT_FIELD:
                JTextField textField = (JTextField) labelFor;
                if (StringUtils.isBlank(textField.getText())) {
                    this.n = 0;
                } else {
                    this.n = Integer.parseInt(textField.getText().trim());
                }
                break;
            case PERCENT_SLIDER:
                JSlider slider = (JSlider) labelFor;
                this.percents = slider.getValue();
                break;
        }
    }
}
