package alex.shepel.hdl_testbench.frontend.widgets;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;

import javax.swing.*;
import java.awt.*;

/*
 * File: PresetButton.java
 * --------------------------------------------------------------
 * Represents a JButton object with a preset properties.
 */
public class PresetButton extends JButton implements FrontendParameters {

    /**
     * The class constructor.
     *
     * @param title The title of the button.
     */
    public PresetButton(String title) {
        super(title);
        setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        setFocusable(false);
    }

}
