package ui;

import function.FunctionCompressor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;

public class PlotsBuilder {

    public ChartPanel build(UIData data) {
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
