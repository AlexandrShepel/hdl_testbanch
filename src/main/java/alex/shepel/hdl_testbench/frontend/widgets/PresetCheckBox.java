package alex.shepel.hdl_testbench.frontend.widgets;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;

import javax.swing.*;
import java.awt.*;

/*
 * File: PresetCheckBox.java
 * --------------------------------------------------------------
 * Represents a JCheckBox object with a preset properties.
 */
public class PresetCheckBox extends JCheckBox implements FrontendParameters {

    /* Font that uses for the check box. */
    private static final Font FONT = new Font("Dialog", Font.PLAIN, 14);

    /**
     * The class constructor.
     *
     * @param label The label that is shown right to the checkbox.
     * @param foreground The font color.
     */
    public PresetCheckBox(String label, Color foreground) {
        setText(label);
        setFont(FONT);
        setBackground(BACKGROUND_COLOR);
        setForeground(foreground);
        setFocusable(false);
    }

}
