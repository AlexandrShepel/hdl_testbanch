package alex.shepel.hdl_testbench.frontend.widgets;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
 * File: ClockSpecificationPanel.java
 * ----------------------------------------------
 * The panel that allows to configure clock signals
 * from "clock_hub.sv" module that will be connected
 * to the DUT clock ports.
 */
public class ClockSpecificationPanel extends JPanel implements FrontendParameters {

    /* The instance properties (widgets). */
    private final JLabel clockLabel = new JLabel();
    private final JComboBox<String> clockBox = new JComboBox<>();

    /**
     * The class constructor.
     *
     * @param clockName The name of the custom clock.
     */
    public ClockSpecificationPanel(String clockName) {
        setBackground(BACKGROUND_COLOR);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 25));
        setClockLabel(clockName);
    }

    /**
     * Adds JLabel object that represents the name
     * of custom clock to the panel.
     *
     * @param clockName The name of the custom clock.
     */
    private void setClockLabel(String clockName) {
        clockLabel.setText(clockName);
        clockLabel.setFont(PAGE_FONT);
        clockLabel.setForeground(FONT_COLOR);
        add(clockLabel);
    }

    /**
     * Adds JComboBox objects to the panel.
     *
     * @param clocks The list of the clocks that will be added to the combo box.
     */
    public void setClocksComboBox(ArrayList<String> clocks) {
        clockBox.removeAllItems();

        for (String clock : clocks) {
            clockBox.removeItem(clock);
            clockBox.addItem(clock);
        }

        add(clockBox);
    }

    /**
     * Returns the clock that was specified by user.
     *
     * @return The String value of clock name.
     */
    public String getSpecifiedClock() {
        return String.valueOf(clockBox.getSelectedItem());
    }

}
