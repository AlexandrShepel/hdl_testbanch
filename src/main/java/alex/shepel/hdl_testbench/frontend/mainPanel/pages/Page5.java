package alex.shepel.hdl_testbench.frontend.mainPanel.pages;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.widgets.PresetButton;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private static final int COMMON_REPORT_HEIGHT = 130;

    /* The height of the bottom JTextArea object that
    displays passed or failed testing points. */
    private static final int DETAILED_REPORT_HEIGHT = 210;

    private static final int DEFAULT_TABULATION = 16;

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
    private static final String COMMON_REPORT =
            "------------------------ COMMON REPORT -------------------------\n" +
            "                                                                \n" +
            "DUT name: <dut_name>.sv                                         \n" +
            "Test name: test_1                                               \n" +
            "Simulation frames: <total_frames>                               \n" +
            "Total mismatches:  <total_mismatches>                             " ;

    /*
     * The information that is displayed on the bottom of application window.
     *
     * TODO: It must be replaced with the information
     *       from test environment report.
     */
    private static final String DETAILED_REPORT_HEADER =
            "----------------------- DETAILED REPORT -----------------------\n";

    private static final String[] DETAILED_REPORT_COLS = {"PORT", "FRAMES", "FAILED", "PERCENT"};

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
    }

    public void showResults(Map<String, Integer> resultStats, String dutName) {
        setCommonReport(resultStats, dutName);
        setButton();
        setDetailedReport(resultStats);
    }

    /**
     * Sets and adds the top JTextArea object to the application window.
     * It represents main report information.
     * @param resultStats
     * @param dutName
     */
    private void setCommonReport(final Map<String, Integer> resultStats, final String dutName) {
        String mainInfo = COMMON_REPORT;
        mainInfo = mainInfo.replace("<dut_name>.sv", dutName);
        mainInfo = mainInfo.replace("<total_frames>", "" + resultStats.get("total_frames"));
        mainInfo = mainInfo.replace("<total_mismatches>", "" + resultStats.get("total_mismatches"));

        final ReportTextArea textArea = new ReportTextArea(mainInfo, COMMON_REPORT_HEIGHT);
        add(textArea);
    }

    /**
     * Sets and adds the JButton object to the page.
     */
    private void setButton() {
        PresetButton button = new PresetButton("Open log.txt");
        button.setPreferredSize(new Dimension((int) (BUTTON_WIDTH * 2.5), BUTTON_HEIGHT));
        button.addActionListener(e -> openReportFile());
        add(button);
    }

    /**
     * Sets and adds the bottom JTextArea object to the application window.
     * It displays passed or failed testing points.
     * @param resultStats
     */
    private void setDetailedReport(Map<String, Integer> resultStats) {
        Set<String> ignoreList = new HashSet<>(Arrays.asList("port_frames", "total_frames", "total_mismatches"));
        StringBuilder detailedReport = getReportHeader();

        for (String port: resultStats.keySet()) {
            if (ignoreList.contains(port))
                continue;

            detailedReport.append(
                    getPortReport(port, resultStats.get(port), resultStats.get("port_frames")));
        }

        setScrollPane(detailedReport.toString());
    }

    private StringBuilder getReportHeader() {
        StringBuilder reportHeader = new StringBuilder(DETAILED_REPORT_HEADER);

        for (String col: DETAILED_REPORT_COLS)
            reportHeader.append(appendSpaces(col));

        return reportHeader.append("\n");
    }

    private String getPortReport(String portName, int portMismatches, int portFrames) {
        return  appendSpaces(portName) +
                appendSpaces("" + portFrames) +
                appendSpaces("" + portMismatches) +
                appendSpaces("" + portMismatches * 100 / portFrames + " %\n");
    }

    private String appendSpaces(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        strBuilder.append(" ".repeat(Math.max(0, DEFAULT_TABULATION - strBuilder.length())));

        return strBuilder.toString();
    }

    private void setScrollPane(String text) {
        ReportTextArea textArea = new ReportTextArea(text);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(textArea.getWidth(), DETAILED_REPORT_HEIGHT));
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
