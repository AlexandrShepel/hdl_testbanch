package frontend;

import java.awt.*;

/*
 * File: FrontendParameters.java
 * ----------------------------------------------
 * Represents an interface that stores constants.
 */
public interface FrontendParameters {

    /* The main colors of the application window. */
    Color BACKGROUND_COLOR = new Color(50, 50, 50);
    Color GREY = new Color(115, 115, 115);
    Color READY = new Color(180, 255, 135);
    Color NOT_READY = GREY;

    /* The main font of the application window. */
    Color FONT_COLOR = new Color(200, 200, 200);
    Font PAGE_FONT = new Font("Dialog", Font.BOLD, 16);

    /* The number of configuration steps. */
    int NUM_OF_STEPS = 5;

    /* The size of the ovals that represent the configuration steps. */
    int PROGRESS_OVAL_SIZE = 12;

    /* The size of the button from the ButtonsPanel object. */
    int BUTTON_WIDTH = 80;
    int BUTTON_HEIGHT = 30;

    /* The size of the application window. */
    int APP_WIDTH = 720;
    int APP_HEIGHT = 480;

    /* The size of the button panel. */
    int BP_HEIGHT = BUTTON_HEIGHT * 3 >> 1;

    /* The size of the progress panel. */
    int PP_HEIGHT = PROGRESS_OVAL_SIZE * 3;

    /* The size of the configuration panel. */
    int IP_HEIGHT = APP_HEIGHT - PP_HEIGHT - BP_HEIGHT;

    /*
     * The spacing between the text and left border
     * of the application window.
     */
    int TEXT_LEFT_ALIGNMENT = (int) (APP_WIDTH * 0.1);

}
