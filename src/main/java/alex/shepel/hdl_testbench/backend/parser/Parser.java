package alex.shepel.hdl_testbench.backend.parser;

import alex.shepel.hdl_testbench.backend.parser.detectors.ClocksDetector;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;
import alex.shepel.hdl_testbench.backend.parser.detectors.ParametersDetector;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortsDetector;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/*
 * File: Parser.java
 * -----------------------------------------------
 * Parses a specified .sv file.
 * Looks for ports, clocks and parameters
 * that are in the parsed .sv module.
 */
public class Parser {

    /* Detects needed data in the parsed files.
    Detecting data is covered in the detectors names. */
    private ClocksDetector inputClkParser;
    private ClocksDetector outputClkParser;
    private PortsDetector portsDetector;
    private ParametersDetector parametersDetector;

    /* The file that must be opened.
    It is specified by user. */
    private File file;

    /* The file that must be opened.
    It is placed in the resource directory by default. */
    private String filePath;

    /* The list of parsed file's code lines. */
    private ArrayList<String> parsedFile;

    /* The boolean property that says if parsing resource
    is placed inside a java archive or not. */
    public static final boolean INTERNAL_RESOURCE = true;
    public static final boolean EXTERNAL_RESOURCE = false;

    /**
     * The class constructor.
     * Used to parse file by specified a File object.
     * Comfortable to use for parsing external resources.
     *
     * @param file The File object that contains a path to specified file.
     * @param isPlacedInternal The boolean property that says if parsing resource
     *                         is placed inside a java archive or not.
     * @throws IOException Covers problems with reading of a specified file.
     */
    public Parser(File file, boolean isPlacedInternal) throws IOException {
        this.file = file;
        initialize(isPlacedInternal);
    }

    /**
     * The class constructor.
     * Used to parse file by specified a path in the String format.
     * Comfortable to use for parsing internal resources.
     *
     * @param filePath The String object that contains a path to specified file.
     * @param isPlacedInternal The boolean property that says if parsing resource
     *                         is placed inside a java archive or not.
     * @throws IOException Covers problems with reading of a specified file.
     */
    public Parser(String filePath, boolean isPlacedInternal) throws IOException {
        this.filePath = filePath;
        initialize(isPlacedInternal);
    }

    /**
     * Reads a specified file.
     * Initializes little parsers.
     *
     * @param isPlacedInternal The boolean property that says if parsing resource
     *                         is placed inside a java archive or not.
     * @throws IOException Covers problems with reading of a specified file.
     */
    private void initialize(boolean isPlacedInternal) throws IOException {
        /* Parses file and saves it as ArrayList<String> object. */
        if (isPlacedInternal)
            parsedFile = parseInternalResource();
        else
            parsedFile = parseExternalResource();

        /* Looks for ports, clocks and parameters in the parsed file. */
        portsDetector = new PortsDetector(parsedFile);
        inputClkParser = new ClocksDetector(portsDetector.getInputPorts().keySet());
        outputClkParser = new ClocksDetector(portsDetector.getOutputPorts().keySet());
        parametersDetector = new ParametersDetector(parsedFile);
    }

    /**
     * Transform specified file to the list of code lines.
     * Used for files that are placed outside a java archive.
     *
     * @throws IOException Covers problems with reading of a specified file.
     */
    private ArrayList<String> parseExternalResource() throws IOException {
        ArrayList<String> parsedFile = new ArrayList<>();
        InputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String newLine;
        while ((newLine = bufferedReader.readLine()) != null) {
            parsedFile.add(newLine);
        }

        System.out.println("File parsed: " + file);
        inputStream.close();
        bufferedReader.close();

        return parsedFile;
    }

    /**
     * Transform specified file to the list of code lines.
     * Used for files that are placed inside a java archive.
     *
     * @throws IOException Covers problems with reading of a specified file.
     */
    private ArrayList<String> parseInternalResource() throws IOException {
        ArrayList<String> parsedFile = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(inputStream)));

        String newLine;
        while ((newLine = bufferedReader.readLine()) != null) {
            parsedFile.add(newLine);
        }

        System.out.println("File parsed: " + filePath);
        inputStream.close();
        bufferedReader.close();

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

    /* Getters of detectors objects. */
    public ArrayList<String> getInputClocks() {
        return inputClkParser.getClocks();
    }
    public ArrayList<String> getOutputClocks() {
        return outputClkParser.getClocks();
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
