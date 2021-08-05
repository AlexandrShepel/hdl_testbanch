package frontend.widgets;

import frontend.FrontendParameters;

import javax.swing.*;

/*
 * File: PresetTextArea.java
 * -----------------------------------------------
 * Represents a JTextArea object with a preset properties.
 */
public class PresetTextArea extends JTextArea implements FrontendParameters {

    /* The height that is used as a default property,
    when it is not specified during instantiation. */
    private static final int DEFAULT_HEIGHT = 100;

    /**
     * The class constructor.
     * Sets the main properties of the object.
     *
     * @param text The text to be shown on the text area.
     */
    public PresetTextArea(String text) {
        super(text);
        setBorder(BorderFactory.createEmptyBorder(0, TEXT_LEFT_ALIGNMENT, 0, 0));
        setSize(APP_WIDTH, DEFAULT_HEIGHT);
        setFont(PAGE_FONT);
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setBackground(BACKGROUND_COLOR);
        setForeground(FONT_COLOR);
    }

}
