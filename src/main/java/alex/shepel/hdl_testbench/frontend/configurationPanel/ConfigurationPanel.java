package alex.shepel.hdl_testbench.frontend.configurationPanel;

import alex.shepel.hdl_testbench.frontend.FrontendParameters;
import alex.shepel.hdl_testbench.frontend.configurationPanel.pages.Page0;
import alex.shepel.hdl_testbench.frontend.configurationPanel.pages.Page2;
import alex.shepel.hdl_testbench.frontend.configurationPanel.pages.Page1;
import alex.shepel.hdl_testbench.frontend.configurationPanel.pages.Page3;
import alex.shepel.hdl_testbench.frontend.configurationPanel.pages.Page4;
import alex.shepel.hdl_testbench.frontend.configurationPanel.pages.Page5;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: ConfigurationPanel.java
 * ------------------------------------------------------------
 * Interactive panel that helps user to create
 * test environment for FPGA design file.
 *
 * Consists of the 5 configuration steps.
 * Each of them is represented by its own page.
 * See src.alex.shepel.hdl_testbanch.frontend.configurationPanel.pages package.
 *
 * Pages representation is realized
 * with help of changing pages visibility.
 * Thus, when program starts all pages are added to the panel.
 * But only one page is visible at the every moment.
 */
public class ConfigurationPanel extends JLayeredPane implements FrontendParameters {

    /* Pages that will be added to the panel when program starts. */
    private final Page0 p0 = new Page0();
    private final Page1 p1 = new Page1();
    private final Page2 p2 = new Page2();
    private final Page3 p3 = new Page3();
    private final Page4 p4 = new Page4();
    private final Page5 p5 = new Page5();

    /**
     * The class constructor.
     */
    public ConfigurationPanel() {
        setPreferredSize(new Dimension(APP_WIDTH, IP_HEIGHT));
        setOngoingPage(0);

        add(p0, Integer.valueOf(0));
        add(p1, Integer.valueOf(1));
        add(p2, Integer.valueOf(2));
        add(p3, Integer.valueOf(3));
        add(p4, Integer.valueOf(4));
        add(p5, Integer.valueOf(5));
    }

    /**
     * Sets a panel background.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        setBackground(g);
    }

    /**
     * Sets an ongoing configuration page.
     *
     * @param pageNum The number of ongoing configuration page.
     */
    public void setOngoingPage(int pageNum) {
        p0.setVisible(pageNum == 0);
        p1.setVisible(pageNum == 1);
        p2.setVisible(pageNum == 2);
        p3.setVisible(pageNum == 3);
        p4.setVisible(pageNum == 4);
        p5.setVisible(pageNum == 5);
    }

    /**
     * Sets a panel background.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     */
    private void setBackground(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Returns a DUT file.
     *
     * @return The File object
     *         that contains path
     *         of the specified DUT file.
     */
    public File getDutFile() {
        return p0.getDutFile();
    }

    /**
     * Returns a name of an ongoing page.
     *
     * @return The String value of ongoing page's name.
     */
    public String getPageName() {
        if (p0.isVisible()) return p0.getName();
        if (p1.isVisible()) return p1.getName();
        if (p2.isVisible()) return p2.getName();
        if (p3.isVisible()) return p3.getName();
        if (p4.isVisible()) return p4.getName();
        if (p5.isVisible()) return p5.getName();
        return null;
    }

    /**
     * Returns a HashMap object that sets correspondence
     * between DUT's clock's ports and clocks hub ports.
     *
     * @return The HashMap object.
     *         Key is DUT's port.
     *         Value is clocks hub port.
     */
    public HashMap<String, String> getClocksHashMap() {
        setClocksComboBox(new ArrayList<>(p2.getClocksHashMap().values()));

        return p2.getClocksHashMap();
    }

    /**
     * Sends selected clocks to the page #3.
     * There user will chose report sampling frequency from this clocks list.
     *
     * @param clocks Clocks that was selected by user for the DUT clocking.
     */
    private void setClocksComboBox(ArrayList<String> clocks) {
        if (clocks.size() != 0)
            p3.setClocksComboBox(clocks);
        else
            p3.setClocksComboBox(p2.getHubClocks());
    }

    /**
     * Returns a working folder that was specified by a user
     * and where will be created test environment.
     *
     * @return The File object that contains path
     *         of the working folder.
     */
    public File getWorkingFolder() {
        return p1.getWorkingFolder();
    }

    /**
     * Sets a list of available clocks
     * that can be connected to the DUT's ports.
     *
     * @param hubClocks The ArrayList object
     *                  that contains a list
     *                  of available clocks.
     */
    public void setHubClocks(ArrayList<String> hubClocks) {
        p2.setHubClocks(hubClocks);
    }

    /**
     * Sets names of the DUT's clock's ports.
     *
     * @param dutClocks The ArrayList object
     *                  that contains a list
     *                  of DUT's clock's ports.
     */
    public void setDutClocks(ArrayList<String> dutClocks) {
        p2.setDutClocks(dutClocks);
    }

    /**
     * Returns a sampling frequency.
     * Simulation points will be written
     * to the report files every tact
     * of this frequency.
     * It is specified by a user.
     *
     * @return Sampling frequency that is used
     *         in the test environment for writing
     *         output data to the report file.
     */
    public String getReportSamplingFrequency() {
        return p3.getClock();
    }
}