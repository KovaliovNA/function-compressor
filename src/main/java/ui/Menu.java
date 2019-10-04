package ui;

import function.FunctionCompressor;
import function.FunctionType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import ui.utils.SliderPopupListener;
import ui.utils.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Menu {

    private boolean graphicShowed = false;

    public void display() {
        //Create and set up the window.
        JFrame frame = new JFrame("");
        frame.setPreferredSize(new Dimension(400, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel parameters = createParametersPanel(frame);

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(parameters,
                4, 2, //rows, cols
                7, 10,        //initX, initY
                7, 10);       //xPad, yPad

        frame.add(parameters, BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createParametersPanel(JFrame frame) {
        JPanel parameters = new JPanel(new SpringLayout());
        parameters.setOpaque(true);

        initPopupMenu(parameters);
        initInputField(parameters);
        initSlider(parameters);
        initButton(frame, parameters);

        return parameters;
    }

    private void initPopupMenu(JPanel parameters) {
        JLabel jLabel = new JLabel(MenuFiled.FUNCTION_POP_UP.getFieldName(), JLabel.TRAILING);
        JComboBox comboBox = new JComboBox();

        Arrays.stream(FunctionType.values())
                .map(FunctionType::getType)
                .map(String::toLowerCase)
                .forEach(comboBox::addItem);

        jLabel.setLabelFor(comboBox);
        parameters.add(jLabel);
        parameters.add(comboBox);
    }

    private void initInputField(JPanel parameters) {
        JLabel jLabel = new JLabel(MenuFiled.N_NUMBER_INPUT_FIELD.getFieldName(), JLabel.TRAILING);
        JTextField jTextField = new JTextField(10);
        jTextField.setText("200");
        jLabel.setLabelFor(jTextField);
        parameters.add(jLabel);
        parameters.add(jTextField);
    }

    private void initSlider(JPanel parameters) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10) {
            private SliderPopupListener popupHandler;

            @Override
            public void updateUI() {
                removeMouseMotionListener(popupHandler);
                removeMouseListener(popupHandler);
                removeMouseWheelListener(popupHandler);
                super.updateUI();
                popupHandler = new SliderPopupListener();
                addMouseMotionListener(popupHandler);
                addMouseListener(popupHandler);
                addMouseWheelListener(popupHandler);
            }
        };

        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JLabel label = new JLabel(MenuFiled.PERCENT_SLIDER.getFieldName(), JLabel.TRAILING);

        label.setLabelFor(slider);

        parameters.add(label);
        parameters.add(slider);

    }

    private void initButton(JFrame frame, JPanel parameters) {
        JButton calculate = new JButton(MenuFiled.CALCULATE_BUTTON.getFieldName());
        calculate.addActionListener(actionListener -> {
            final Container contentPane = frame.getContentPane();

            Arrays.stream(contentPane.getComponents())
                    .filter(component -> component instanceof ChartPanel)
                    .forEach(contentPane::remove);

            UIData data = new UIData(((JPanel) contentPane.getComponents()[0]).getComponents());

            contentPane.add(Menu.this.buildPlots(data));
            frame.pack();
        });

        parameters.add(new JLabel());
        parameters.add(calculate);
    }

    private ChartPanel buildPlots(UIData data) {
        FunctionCompressor functionCompressor = new FunctionCompressor();
        XYDataset dataset = functionCompressor.compress(data);

        JFreeChart chart = buildFreeChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        chartPanel.setDomainZoomable(true);

        return chartPanel;
    }

    private JFreeChart buildFreeChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
                "",
                "X",
                "Y",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        return chart;
    }
}
