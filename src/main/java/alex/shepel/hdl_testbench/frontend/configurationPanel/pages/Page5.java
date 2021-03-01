package alex.shepel.hdl_testbench.frontend.configurationPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.widgets.PresetButton;
import alex.shepel.hdl_testbench.frontend.widgets.PresetCheckBox;

import javax.swing.*;
import java.awt.*;

/*
 * File: Page4.java
 * ----------------------------------------------
 * The panel represents a built-in test
 * environment report viewer.
 * It helps to monitor failed and passed tests.
 *
 * Page3 also contains a link to the
 * "TestBenchReport.txt" where tests results
 * are stored.
 */
public class Page5 extends JPanel implements FrontendParameters {

    /* The name of the page.
    It is used for gain access to this page
    from the ActionEvent interface. */
    private static final String PAGE_NAME = "Results displaying";

    /* The spacing between the top border of application window
    and first row of the displayed text. */
    private static final int TOP_ALIGNMENT = 0;

    /* The vertical spacing between displayed components. */
    private static final int VERTICAL_COMPONENTS_ALIGNMENT = 10;

    /* The horizontal spacing between displayed components. */
    private static final int HORIZONTAL_COMPONENTS_ALIGNMENT = 20;

    /* The height of the top JTextArea object that
    represents main report information. */
    private static final int MAIN_INFO_HEIGHT = 130;

    /* The height of the bottom JTextArea object that
    displays passed or failed testing points. */
    private static final int SPEC_POINTS_HEIGHT = 210;

    /* The colors of testing points.
    It is green when passed and red otherwise. */
    private final Color PASSED_COLOR = new Color(125, 250, 125);
    private final Color FAILED_COLOR = new Color(250, 125, 125);

    /*
     * The information that is displayed on the top of application window.
     *
     * TODO: It must be replaced with the information
     *       from test environment report.
     */
    private static final String PAGE_TEXT_MAIN_INFO =
            "----------------------- TESTBENCH REPORT -----------------------\n" +
            "                                                                \n" +
            "DUT name: name_of_dut.sv                                        \n" +
            "Test name: test_1                                               \n" +
            "Simulation points: 6000                                         \n" +
            "Passed points: 5950                                               " ;

    /*
     * The information that is displayed on the bottom of application window.
     *
     * TODO: It must be replaced with the information
     *       from test environment report.
     */
    private static final String PAGE_TEXT_SPEC_POINTS =
            "----------------------- SPECIFIC POINTS -----------------------\n" +
            "TIME          SIGNAL          VALUE          EXPECTED          \n" +
            "505           ready           0              1                 \n" +
            "1640          phase           4568           [ 4450  :  4460 ] \n" +
            "3010          magnitude       328            [ 310   :  320  ] \n" +
            "4090          phase           -101           [-110   : -120  ] \n" +
            "5060          ready           0              1                 \n" +
            "9040          phase           4568           [ 4450  :  4460 ] \n" +
            "10080         magnitude       328            [ 310   :  320  ] \n" +
            "12360         phase           -101           [-110   : -120  ] \n" +
            "14580         ready           0              1                 \n" +
            "17960         phase           4568           [ 4450  :  4460 ] \n" +
            "20030         magnitude       328            [ 310   :  320  ] \n" +
            "22500         phase           -101           [-110   : -120  ] \n" +
            "25600         ready           0              1                 \n" +
            "29500         phase           4568           [ 4450  :  4460 ] \n" +
            "42000         magnitude       328            [ 310   :  320  ] \n" +
            "56855         phase           -101           [-110   : -120  ]   " ;

    /**
     * The class constructor.
     */
    public Page5() {
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
        setMainInfoArea();
        setCheckBoxes();
        setButton();
        setSpecPointsArea();
    }

    /**
     * Sets and adds the top JTextArea object to the application window.
     * It represents main report information.
     */
    private void setMainInfoArea() {
        ReportTextArea textArea = new ReportTextArea(PAGE_TEXT_MAIN_INFO, MAIN_INFO_HEIGHT);
        add(textArea);
    }

    private void setCheckBoxes() {
        PresetCheckBox passBox = new PresetCheckBox("show passed", PASSED_COLOR);
        PresetCheckBox failBox = new PresetCheckBox("show failed", FAILED_COLOR);
        add(passBox);
        add(failBox);
    }

    /**
     * Sets and adds the JButton object to the page.
     */
    private void setButton() {
        PresetButton button = new PresetButton("Open TestBenchReport.txt");
        button.setPreferredSize(new Dimension((int) (BUTTON_WIDTH * 2.5), BUTTON_HEIGHT));
        button.addActionListener(e -> openReportFile());
        add(button);
    }

    /**
     * Sets and adds the bottom JTextArea object to the application window.
     * It displays passed or failed testing points.
     */
    private void setSpecPointsArea() {
        ReportTextArea textArea = new ReportTextArea(PAGE_TEXT_SPEC_POINTS);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(textArea.getWidth(), SPEC_POINTS_HEIGHT));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(GREY, 1));
        add(scrollPane);
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
