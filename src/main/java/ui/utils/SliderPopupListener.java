package ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SliderPopupListener extends MouseAdapter {
    private final JWindow toolTip = new JWindow();
    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private final Dimension size = new Dimension(60, 20);
    private int prevValue = -1;

    public SliderPopupListener() {
        super();
        label.setOpaque(false);
        label.setBackground(UIManager.getColor("ToolTip.background"));
        //label.setBorder(UIManager.getBorder("ToolTip.border"));
        label.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker()));
        toolTip.add(label);
        toolTip.setSize(size);
    }

    private void updateToolTip(MouseEvent me) {
        JSlider slider = (JSlider) me.getComponent();
        int intValue = (int) slider.getValue();
        if (prevValue != intValue) {
            label.setText(String.valueOf(slider.getValue()));
            Point pt = me.getPoint();
            pt.y = -size.height;
            SwingUtilities.convertPointToScreen(pt, me.getComponent());
            pt.translate(-size.width / 2, 0);
            toolTip.setLocation(pt);
        }
        prevValue = intValue;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        toolTip.setVisible(true);
        updateToolTip(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        toolTip.setVisible(false);
    }
}