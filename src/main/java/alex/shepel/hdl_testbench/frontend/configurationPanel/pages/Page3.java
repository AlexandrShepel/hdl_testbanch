package alex.shepel.hdl_testbench.frontend.configurationPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.widgets.ClockSpecificationPanel;
import alex.shepel.hdl_testbench.frontend.widgets.PresetTextArea;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
 * File: Page3.java
 * ----------------------------------------------
 * The panel that represents an interactive page.
 * It allows user to configure sampling frequency of report data.
 */
public class Page3 extends JPanel implements FrontendParameters {

    /* The name of the page.
    It is used for gain access to this page
    from the ActionEvent interface. */
    private static final String PAGE_NAME = "Specify sampling frequency";

    /* The spacing between the top border of application window
    and first row of the displayed text. */
    private static final int TOP_ALIGNMENT = 60;

    /* The vertical spacing between displayed components. */
    private static final int VERTICAL_COMPONENTS_ALIGNMENT = 25;

    /* The horizontal spacing between displayed components. */
    private static final int HORIZONTAL_COMPONENTS_ALIGNMENT = 80;

    /* The panels that allows user to specify connection between
    DUT and "clock_hub" modules. */
    private final ClockSpecificationPanel clockSpecPanel = new ClockSpecificationPanel("clock");

    /* The dialog text that is displayed on the application window. */
    private static final String PAGE_TEXT =
            "Specify the clock signal, that will form sampling frequency of report data.";

    /**
     * The class constructor.
     */
    public Page3() {
        setBounds(0, TOP_ALIGNMENT, APP_WIDTH, IP_HEIGHT - TOP_ALIGNMENT);
        setBackground(BACKGROUND_COLOR);
        setLayout(new FlowLayout(
                FlowLayout.CENTER,
                HORIZONTAL_COMPONENTS_ALIGNMENT,
                VERTICAL_COMPONENTS_ALIGNMENT));

        PresetTextArea textArea = new PresetTextArea(PAGE_TEXT);
        add(textArea);
        add(clockSpecPanel);
    }

    /**
     * Returns a sampling frequency.
     * Simulation points will be written
     * to the report files every tact
     * of this frequency.
     * It is specified by a user.
     *
     * @return Sampling frequency that is used
     *         in the test environment for writing
     *         output data to the report file.
     */
    public String getClock() {
        return clockSpecPanel.getSpecifiedClock();
    }

    /**
     * Sets a list of report sampling frequency clocks.
     * User must chose one of them.
     *
     * @param clocks The ArrayList object that contains all clocks
     *               that can be used as report sampling frequency.
     */
    public void setClocksComboBox(ArrayList<String> clocks) {
        clockSpecPanel.setClocksComboBox(clocks);
    }

    /**
     * Returns a name of the page.
     *
     * @return The String value of the page's name.
     */
    public String getName() {
        return PAGE_NAME;
    }

}