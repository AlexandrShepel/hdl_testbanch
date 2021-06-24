package alex.shepel.hdl_testbench.backend.parsers;

import alex.shepel.hdl_testbench.backend.parsers.detectors.ClocksDetector;
import alex.shepel.hdl_testbench.backend.parsers.detectors.PortDescriptor;
import alex.shepel.hdl_testbench.backend.parsers.detectors.ParametersDetector;
import alex.shepel.hdl_testbench.backend.parsers.detectors.PortsDetector;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: ModuleParser.java
 * -----------------------------------------------
 * Parses a specified .sv file.
 * Looks for ports, clocks and parameters
 * that are in the parsed .sv module.
 */
public class ModuleParser extends Parser {

    /* Detects needed data in the parsed files.
    Detecting data is covered in the detectors names. */
    private ClocksDetector inputClkParser;
    private PortsDetector portsDetector;
    private ParametersDetector parametersDetector;

    /* The list of parsed file's code lines. */
    private ArrayList<String> parsedFile;

    /* The file that must be opened.
    It is specified by user. */
    private File file;

    /* The file that must be opened.
    It is placed in the resource directory by default. */
    private String filePath;

    /**
     * The class constructor.
     * Used to parse file by specified a File object.
     * Comfortable to use for parsing external resources.
     *
     * @param file The File object that contains a path to specified file.
     * @throws IOException Covers problems with reading of a specified file.
     */
    public ModuleParser(File file) throws IOException {
        this.file = file;
        parsedFile = parseFile(file);
        initDetectors();
    }

    /**
     * The class constructor.
     * Used to parse file by specified a path in the String format.
     * Comfortable to use for parsing internal resources.
     *
     * @param filePath The String object that contains a path to specified file.
     * @throws IOException Covers problems with reading of a specified file.
     */
    public ModuleParser(String filePath) throws IOException {
        this.filePath = filePath;
        parsedFile = parseFile(filePath);
        initDetectors();
    }

    /**
     * Initializes detectors.
     * Detectors looks for ports, clocks and parameters in the parsed file.
     */
    private void initDetectors() {
        portsDetector = new PortsDetector(parsedFile);
        inputClkParser = new ClocksDetector(portsDetector.getInputPorts().keySet());
        parametersDetector = new ParametersDetector(parsedFile);
    }

    /**
     * Returns a parsed file as ArrayList object
     * that contains all code lines of the file.
     *
     * @return The ArrayList object
     *         that contains parsed code.
     */
    public ArrayList<String> fileToArrayList() {
        return parsedFile;
    }

    /**
     * Returns a parsed File object.
     *
     * @return The File object that was parsed.
     */
    public File getFile() {
        return file;
    }

    /* Getters of detectors objects. */
    public ArrayList<String> getInputClocks() {
        return inputClkParser.getClocks();
    }
    public HashMap<String, PortDescriptor> getInputPorts() {
        return portsDetector.getInputPorts();
    }
    public HashMap<String, PortDescriptor> getOutputPorts() {
        return portsDetector.getOutputPorts();
    }
    public HashMap<String, String> getParameters() {
        return parametersDetector.getParameters();
    }
}
