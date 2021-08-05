package frontend.panels;

import frontend.FrontendParameters;
import frontend.widgets.PresetButton;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/*
 * File: ButtonsPanel.java
 * --------------------------------------------------------------------------
 * Represents navigation & help panel.
 * Consists of the 4 buttons:
 *  -- "< Back" directs user to the previous configuration step.
 *  -- "Next >" directs user to the next configuration step.
 *  -- "Help" opens the program documentation.
 *  -- "Finish" stops the program when all configuration steps are completed.
 */
public class ButtonsPanel extends JPanel implements FrontendParameters {

    /* Displayed buttons. */
    private static final HashMap<String, PresetButton> buttons = new HashMap<>();

    /**
     * The class constructor.
     */
    public ButtonsPanel() {
        setPreferredSize(new Dimension(APP_WIDTH, BUTTON_HEIGHT * 3 >> 1));
        setBackground(BACKGROUND_COLOR);
        setLayout(new FlowLayout(FlowLayout.CENTER, (int) (APP_WIDTH * 0.02), (int) (BP_HEIGHT * 0.1)));
        addButtons();
    }

    /**
     * Adds buttons to the panel.
     */
    private void addButtons() {
        buttons.put("< Back", new PresetButton("< Back"));
        buttons.put("Next >", new PresetButton("Next >"));
        buttons.put("Help", new PresetButton("Help"));
        buttons.put("Finish", new PresetButton("Finish"));

        setOngoingButtonStatus(0);

        add(buttons.get("< Back"));
        add(buttons.get("Next >"));
        add(buttons.get("Help"));
        add(buttons.get("Finish"));
    }

    /**
     * Sets an ongoing status of buttons that are
     * placed on the panel.
     *
     * Buttons have 2 states.
     * They are "enabled" and "disabled":
     *  -- Back button is disabled on the first
     * configuration page, enabled otherwise.
     *  -- Next button is disabled on the last
     * configuration page, enabled otherwise.
     *  -- Finish button is enabled on the last
     * configuration page, disabled otherwise.
     *  -- Help button is always enabled.
     *
     * @param step The ongoing configuration step.
     */
    public void setOngoingButtonStatus(int step) {
        buttons.get("< Back").setEnabled(
                !(step == 0));
        buttons.get("Next >").setEnabled(
                !(step == NUM_OF_STEPS - 1));
        buttons.get("Finish").setEnabled(
                step == NUM_OF_STEPS - 1);
    }

    /**
     *  Returns the buttons of the ButtonPanel object.
     */
    public static HashMap<String, PresetButton> getButtonsHashMap() {
        return buttons;
    }

}
