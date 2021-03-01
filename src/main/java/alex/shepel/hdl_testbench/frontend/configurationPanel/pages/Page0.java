package alex.shepel.hdl_testbench.frontend.configurationPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.widgets.PresetButton;
import alex.shepel.hdl_testbench.frontend.widgets.PresetTextArea;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/*
 * File: Page0.java
 * ----------------------------------------------
 * The panel that represents an interactive page.
 * It allows user to configure directory of DUT file
 * through the Windows Explorer application.
 */
public class Page0 extends JPanel implements FrontendParameters {

    /* The name of the page.
    It is used for gain access to this page
    from the ActionEvent interface. */
    private static final String PAGE_NAME = "Specify DUT file";

    /* The spacing between the top border of application window
    and first row of the displayed text. */
    private static final int TOP_ALIGNMENT = 120;

    /* The vertical spacing between displayed components. */
    private static final int VERTICAL_COMPONENTS_ALIGNMENT = 25;

    /* The horizontal spacing between displayed components. */
    private static final int HORIZONTAL_COMPONENTS_ALIGNMENT = 20;

    /* The maximum characters number that can be displayed
    on the JTextField object. */
    private static final int TEXT_FIELD_CHARS = 35;

    /* JTextField object that implements action listener interface. */
    private static final JTextField textField = new JTextField();

    /* Default text that is placed in the text field. */
    private static final String DEFAULT_TEXT_FIELD_TEXT = "c:\\fpga\\psp\\pdis\\main.sv";

    /* The file that must be opened.
    It is specified by user. */
    private File dutFile;

    /* The dialog text that is displayed on the application window. */
    private static final String PAGE_TEXT =
            "Specify the DUT.sv file:";

    /**
     * The class constructor.
     */
    public Page0() {
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
        setTextField();
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
     * Sets and adds the JTextField object to the application window.
     */
    private void setTextField() {
        textField.setColumns(TEXT_FIELD_CHARS);
        textField.setText(DEFAULT_TEXT_FIELD_TEXT);
        textField.setBorder(BorderFactory.createBevelBorder(1));
        textField.setFont(PAGE_FONT);
        textField.setBackground(GREY);
        textField.setForeground(FONT_COLOR);
        add(textField);
    }

    /**
     * Sets and adds the JButton object to the page.
     */
    private void setButton() {
        PresetButton button = new PresetButton("Browse");
        button.addActionListener(e -> openFile());
        add(button);
    }

    /**
     * Opens file that is selected by user.
     */
    private void openFile() {
        /* sets JFileChooser object properties */
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Verilog & SystemVerilog",
                "v", "sv"
        );
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("."));
        int response = fileChooser.showOpenDialog(null);

        /* when file is specified successfully */
        if (response == JFileChooser.APPROVE_OPTION) {
            dutFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
            textField.setText(String.valueOf(dutFile));
        }
    }

    /**
     * Returns a DUT file.
     *
     * @return The File object
     *         that contains path
     *         of the specified DUT file.
     */
    public File getDutFile() {
        return dutFile;
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