package alex.shepel.hdl_testbench.backend;

import alex.shepel.hdl_testbench.backend.filesWriter.FilesWriter;
import alex.shepel.hdl_testbench.backend.parsers.ResultParser;
import alex.shepel.hdl_testbench.backend.parsers.ModuleParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * File: Backend.java
 * -----------------------------------------------
 * Backend of an application.
 *
 * Parses a specified DUT.sv.
 * Creates a test environment files based
 * on the information received.
 * Operates with resource package
 * where templates of the test environment files
 * are placed.
 */
public class Backend {

    /* Parses DUT.sv file. File must be specified by a user. */
    private ModuleParser dutParser;

    private ResultParser resultParser;

    /* Generates the .sv-classes, clk_hub.sv module
    and tb.sv module that is top level module of the testbench. */
    private final FilesWriter filesWriter;

    /**
     * The class constructor.
     */
    public Backend() throws IOException {
        filesWriter = new FilesWriter();
    }

    /**
     * Sets the absolute path of the DUT file.
     * Sends it to the ModuleParser object.
     *
     * @param dutFile The File object.
     */
    public void setDutFile(File dutFile) throws IOException {
        dutParser = new ModuleParser(dutFile);
    }

    /**
     * Sets the absolute path of the working directory.
     * Test environment will be placed there.
     *
     * @param workingFolder The File object that contains absolute path
     *                      of the working folder.
     */
    public void setWorkingFolder(File workingFolder) {
        filesWriter.setWorkingFolder(workingFolder);
        resultParser = new ResultParser(workingFolder.getAbsolutePath() + "/output_data");
    }

    /**
     * Returns the list of clock inputs of the DUT.
     * Gets that data from the ModuleParser object.
     *
     * @return The ArrayList object that contains list of DUT's clock inputs.
     */
    public ArrayList<String> getDutClocks() {
        System.out.println("Dut clocks are: " + dutParser.getInputClocks());
        return dutParser.getInputClocks();
    }

    /**
     * Sets a connection between DUT's and clk_hub's modules.
     * Connection is represented as HashMap
     * which key is name of DUT's clock input
     * and value is name of clk_hub output.
     *
     * @param clocksHashMap The HashMap object that contains correspondence
     *                      between DUT's and clk_hub's modules.
     */
    public void setClocksHashMap(HashMap<String, String> clocksHashMap) {
        filesWriter.setClocksHashMap(clocksHashMap);
        System.out.println("Clocks hashmap is: " + clocksHashMap);
    }

    /**
     * Sets a sampling frequency of the created test environment.
     * Simulation points will be written to the report files
     * every tact of this frequency.
     *
     * @param reportSamplingFrequency Sampling frequency that is used
     *                                in the test environment for writing
     *                                output data to the report file.
     */
    public void setReportSamplingFrequency(String reportSamplingFrequency) {
        filesWriter.setSampleFrequency(reportSamplingFrequency);
        System.out.println("reportSamplingFrequency is: " + reportSamplingFrequency);
    }

    /**
     * Creates ".sv-class" files.
     * Those files describe test environment for a DUT.
     */
    public void generateEnvironment() throws IOException {
        filesWriter.setDutName(dutParser.getFile().getName());
        System.out.println("DUT name is: " + dutParser.getFile().getName());

        filesWriter.setParameters(dutParser.getParameters());
        System.out.println("Parameters are: " + dutParser.getParameters().keySet());

        filesWriter.setDutOutputs(dutParser.getOutputPorts());
        System.out.println("Outputs are: " + dutParser.getOutputPorts().keySet());

        filesWriter.setDutInputs(dutParser.getInputPorts());
        System.out.println("Inputs are: " + dutParser.getInputPorts().keySet());

        filesWriter.generate();
    }

    public Map<String, Integer> getResultStats() throws IOException {
        return resultParser.getResultStats();
    }
}