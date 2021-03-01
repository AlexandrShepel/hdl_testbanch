package alex.shepel.hdl_testbench.frontend.configurationPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.widgets.PresetButton;
import alex.shepel.hdl_testbench.frontend.widgets.PresetTextArea;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/*
 * File: Page1.java
 * ----------------------------------------------
 * The panel that represents an interactive page.
 * It allows user to configure project directory
 * where test environment and test results will
 * be placed. Implementation is based on using
 * the Windows Explorer application.
 */
public class Page1 extends JPanel implements FrontendParameters {

    /* The name of the page.
    It is used for gain access to this page
    from the ActionEvent interface. */
    private static final String PAGE_NAME = "Specify working folder";

    /* The spacing between the top border of application window
    and first row of the displayed text. */
    private static final int TOP_ALIGNMENT = 99;

    /* The vertical spacing between displayed components. */
    private static  final int VERTICAL_COMPONENTS_ALIGNMENT = 25;

    /* The horizontal spacing between displayed components. */
    private static final int HORIZONTAL_COMPONENTS_ALIGNMENT = 20;

    /* The maximum characters number that can be displayed
    on the JTextField object. */
    private static final int TEXT_FIELD_CHARS = 35;

    /* JTextField object that implements action listener interface. */
    private final JTextField textField = new JTextField();

    private static final String DEFAULT_TEXT_FIELD_TEXT = ".\\assets\\dutTests\\test0001";

    /* he working directory that must be opened.
    It is specified by user. */
    private File workingFolder;

    /* Default text that is placed in the text field. */
    private static final String DEFAULT_FOLDER_NAME = "\\testEnvironment";

    /* The dialog text that is displayed on the application window. */
    private static final String PAGE_TEXT =
            "Specify the working folder. \n" +
            "Here will be created test environment for your DUT.";

    /**
     * The class constructor.
     */
    public Page1() {
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
        button.addActionListener(e -> selectFolder());
        add(button);
    }

    /**
     * Selects folder that is specified by user
     * for further work of application.
     */
    private void selectFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        FileNameExtensionFilter foldersFilter = new FileNameExtensionFilter(
                "Folders",
                "dir"
        );
        fileChooser.addChoosableFileFilter(foldersFilter);
        fileChooser.setApproveButtonText("Select");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            createTestEnvironmentFolder(fileChooser);
            textField.setText(String.valueOf(workingFolder));
        }
    }

    /**
     * Creates a directory
     * where will be placed generated files.
     *
     * @param fileChooser The JFileChooser object
     *                    that contains path
     *                    where must be created
     *                    a new directory.
     */
    private void createTestEnvironmentFolder(JFileChooser fileChooser) {
        workingFolder = new File(fileChooser.getSelectedFile().getAbsolutePath() + DEFAULT_FOLDER_NAME);
        workingFolder.mkdir();
    }

    /**
     * Returns the File object that represents a working folder
     * that will be used for further work of application.
     *
     * @return The File object that represents a working folder.
     */
    public File getWorkingFolder() {
        return workingFolder;
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