package alex.shepel.hdl_testbench.frontend.mainPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;

import javax.swing.*;
import java.awt.*;

/*
 * File: ReportTextArea.java
 * -----------------------------------------------
 * Represents a JTextArea object with a preset properties.
 */
class ReportTextArea extends JTextArea implements FrontendParameters {

    /* The text font. */
    private final Font FONT = new Font("Monospaced", Font.BOLD, 14);

    /* The height that is used as a default property,
    when it is not specified during instantiation. */
    private static final int DEFAULT_HEIGHT = 100;

    /**
     * The class constructor.
     *
     * @param text The text to be shown on the text area.
     * @param height The height of the text area.
     */
    public ReportTextArea(String text, int height) {
        super(text);
        setProperties();
        setSize(APP_WIDTH, height);
    }

    /**
     * The class constructor with default height preset.
     *
     * @param text The text to be shown on the text area.
     */
    public ReportTextArea(String text) {
        super(text);
        setProperties();
        setSize(APP_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Sets the main properties of the object.
     */
    private void setProperties() {
        setBorder(BorderFactory.createEmptyBorder(0, TEXT_LEFT_ALIGNMENT, 0, 0));
        setFont(FONT);
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setBackground(BACKGROUND_COLOR);
        setForeground(FONT_COLOR);
    }

}
