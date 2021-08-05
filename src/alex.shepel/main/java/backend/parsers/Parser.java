package backend.parsers;

import backend.parsers.detectors.ClocksDetector;
import backend.parsers.detectors.PortDescriptor;
import backend.parsers.detectors.ParametersDetector;
import backend.parsers.detectors.PortsDetector;

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
    private PortsDetector portsDetector;
    private ParametersDetector parametersDetector;

    /* The list of parsed file's code lines. */
    private final ArrayList<String> parsedFile;

    /* The file that must be opened.
    It is specified by user. */
    private File file;

    /**
     * The class constructor.
     * Used to parse file by specified a File object.
     * Comfortable to use for parsing external resources.
     *
     * @param file The File object that contains a path to specified file.
     * @throws IOException Covers problems with reading of a specified file.
     */
    public Parser(File file) throws IOException {
        this.file = file;
        InputStream inputStream = new FileInputStream(file);
        parsedFile = toArrayList(inputStream);
        System.out.println("File parsed: " + file);
        initDetectors(parsedFile);
    }

    /**
     * The class constructor.
     * Used to parse file by specified a path in the String format.
     * Comfortable to use for parsing internal resources.
     *
     * @param filePath The String object that contains a path to specified file.
     * @throws IOException Covers problems with reading of a specified file.
     */
    public Parser(String filePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        parsedFile = toArrayList(inputStream);
        System.out.println("File parsed: " + filePath);
        initDetectors(parsedFile);
    }

    /**
     * Initializes detectors.
     * Detectors looks for ports, clocks and parameters in the parsed file.
     * @param parsedFile The list of parsed file's code lines.
     */
    private void initDetectors(final ArrayList<String> parsedFile) {
        portsDetector = new PortsDetector(parsedFile);
        inputClkParser = new ClocksDetector(portsDetector.getInputPorts().keySet());
        parametersDetector = new ParametersDetector(parsedFile);
    }

    private ArrayList<String> toArrayList(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(inputStream)));
        ArrayList<String> parsedFile = new ArrayList<>();

        String newLine;
        while ((newLine = bufferedReader.readLine()) != null)
            parsedFile.add(newLine);

        inputStream.close();
        bufferedReader.close();

        return parsedFile;
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
