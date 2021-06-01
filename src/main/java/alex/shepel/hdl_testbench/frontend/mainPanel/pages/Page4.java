package alex.shepel.hdl_testbench.frontend.mainPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.widgets.PresetButton;
import alex.shepel.hdl_testbench.frontend.widgets.PresetTextArea;

import javax.swing.*;
import java.awt.*;

/*
 * File: Page3.java
 * ----------------------------------------------
 * The panel represents an informative
 * interactive page.
 *
 * On this step test environment is created.
 * It gives user a link to the file of tests
 * results.
 *
 * This page also allows user to use built-in
 * test environment report viewer.
 * It helps to monitor failed and passed tests.
 */
public class Page4 extends JPanel implements FrontendParameters {

    /* The name of the page.
    It is used for gain access to this page
    from the ActionEvent interface. */
    private static final String PAGE_NAME = "Run ModelSim";

    /* The spacing between the top border of application window
    and first row of the displayed text. */
    private static final int TOP_ALIGNMENT = 20;

    /* The vertical spacing between displayed components. */
    private static final int VERTICAL_COMPONENTS_ALIGNMENT = 50;

    /* The horizontal spacing between displayed components. */
    private static final int HORIZONTAL_COMPONENTS_ALIGNMENT = 20;

    /* The dialog text that is displayed on the application window. */
    private static final String PAGE_TEXT =
            "Test environment is created. \n" +
            "Run it with the ModelSim software. \n" +
            "You can find instruction of using ModelSim \n" +
            "by the click on \"Help\" button. \n" +
            "\n" +
            "When simulation is done, \n" +
            "press the \"Next >\" button to see fast test bench report. \n" +
            "It is also available as a \"TestBenchReport.txt\" \n" +
            "and stores in the test bench working directory.";

    /**
     *  The class constructor.
     */
    public Page4() {
        setBounds(0, TOP_ALIGNMENT, APP_WIDTH, IP_HEIGHT - TOP_ALIGNMENT);
        setBackground(BACKGROUND_COLOR);
        setLayout(new FlowLayout(
                FlowLayout.CENTER,
                HORIZONTAL_COMPONENTS_ALIGNMENT,
                VERTICAL_COMPONENTS_ALIGNMENT));
        setWidgets();
    }

    /**
     * Sets and adds the widgets to the page.
     */
    private void setWidgets() {
        setTextArea();
        setButton();
    }

    /**
     * Sets and adds the JTextArea object to the application window.
     */
    private void setTextArea() {
        PresetTextArea textArea = new PresetTextArea(PAGE_TEXT);
        add(textArea);
    }

    /**
     * Sets and adds the JButton object to the page.
     */
    private void setButton() {
        PresetButton button = new PresetButton("Open TestBenchReport.txt");
        button.setPreferredSize(new Dimension(BUTTON_WIDTH * 3, BUTTON_HEIGHT));
        button.addActionListener(e -> openReportFile());
        add(button);
    }

    /**
     * Opens "TestBenchReport.txt" file.
     */
    private void openReportFile() {
        // TODO: Make implementation of opening TestBenchReport.txt file.
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
