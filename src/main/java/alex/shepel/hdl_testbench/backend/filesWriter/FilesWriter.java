package alex.shepel.hdl_testbench.backend.filesWriter;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators.*;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: FilesWriter.java
 * -----------------------------------------------
 * Writes a test environment files based
 * on the information received.
 * Operates with resource package
 * where templates of the test environment files
 * are placed.
 */
public class FilesWriter {

    /* The working directory that must be opened.
    It is specified by user. */
    private File workingFolder;

    /* Files creators. */
    private final TBCodeGenerator tbCodeGen;
    private final EnvironmentCodeGenerator envCodeGen;
    private final InterfaceCodeGenerator ifaceCodeGen;
    private final MonitorCodeGenerator monitorCodeGen;
    private final ReadDriverCodeGenerator readCodeGen;
    private final WriteDriverCodeGenerator writeCodeGen;

    /**
     * The class constructor.
     *
     * @throws IOException The error of reading a resource files,
     *                     when initialization of generators.
     */
    public FilesWriter() throws IOException {
        tbCodeGen = new TBCodeGenerator();
        envCodeGen = new EnvironmentCodeGenerator();
        ifaceCodeGen = new InterfaceCodeGenerator();
        monitorCodeGen = new MonitorCodeGenerator();
        readCodeGen = new ReadDriverCodeGenerator();
        writeCodeGen = new WriteDriverCodeGenerator();
    }

    /**
     * Generates all files needed for test environment.
     *
     * @throws IOException The error of creating a new files.
     */
    public void generate() throws IOException {
        /* Overwrites resource files and copies them to a destination folder. */
        writeFileTo(tbCodeGen.getParsedFile(), tbCodeGen.getName(), "");
        writeFileTo(envCodeGen.getParsedFile(), envCodeGen.getName(), "classes");
        writeFileTo(ifaceCodeGen.getParsedFile(), ifaceCodeGen.getName(), "classes");
        writeFileTo(monitorCodeGen.getParsedFile(), monitorCodeGen.getName(), "classes");
        writeFileTo(readCodeGen.getParsedFile(), readCodeGen.getName(), "classes");
        writeFileTo(writeCodeGen.getParsedFile(), writeCodeGen.getName(), "classes");

        /* Copies a resource files to the destination folder without overwriting. */
        copyFileTo(BackendParameters.READ_GENERATOR_SV, "classes");
        copyFileTo(BackendParameters.WRITE_GENERATOR_SV, "classes");
        copyFileTo(BackendParameters.CLK_HUB_SV, "clocks_hub");
        copyFileTo(BackendParameters.CLK_DIVIDER_SV, "clocks_hub");
    }

    /**
     * Writes a parsed file into specified directory.
     *
     * @param parsedFile The ArrayList object that contains parsed code.
     * @param fileName The name of parsed file.
     * @param newDirectoryName A new directory that must be created.
     * @throws IOException Error when creating a new folder.
     */
    public void writeFileTo(ArrayList<String> parsedFile, String fileName, String newDirectoryName) throws IOException {
        File writtenFile = new File(workingFolder.getAbsolutePath() + "\\" + fileName);

        /* Creates a subdirectory for a new file
        (if such directory does not already exist). */
        try {
            if (!newDirectoryName.equals("")) {
                writtenFile = new File(workingFolder.getAbsolutePath() + "\\" + newDirectoryName + "\\" + fileName);
                Files.createDirectory(Paths.get(workingFolder.getAbsolutePath() + "\\" + newDirectoryName));
                System.out.println("Directory created: " + workingFolder.getAbsolutePath() + "\\" + newDirectoryName);
            }
        }
        catch (FileAlreadyExistsException ignored) {}

        /* Creating empty file to writing in. */
        if (!writtenFile.createNewFile()) {
            throw new IOException("File " + writtenFile.getName() + " can't be created.");
        }
        System.out.println("File created: " + newDirectoryName + "\\" + writtenFile.getName());

        /* Writes data to a new file. */
        FileWriter fileWriter = new FileWriter(writtenFile);
        for (String line: parsedFile) {
            fileWriter.append(line).append("\n");
        }
        fileWriter.close();
    }

    /**
     * Copies a resource files to the destination folder without overwriting.
     *
     * @param file The file to copy.
     * @param newDirectoryName The name of the new subdirectory.
     * @throws IOException The error of copying a new files.
     */
    private void copyFileTo(String file, String newDirectoryName) throws IOException {
        CodeGenerator codeGen = new CodeGenerator();
        codeGen.parseFile(file);
        writeFileTo(codeGen.getParsedFile(), codeGen.getName(), newDirectoryName);
    }

    /**
     * Sets the absolute path of the working directory.
     * Test environment will be placed there.
     *
     * @param workingFolder The File object that contains absolute path
     *                      of the working folder.
     */
    public void setWorkingFolder(File workingFolder) {
        this.workingFolder = workingFolder;
        envCodeGen.setWorkingFolder(workingFolder);
        System.out.println("Working folder is set. Folder = " + workingFolder.getAbsolutePath());
    }

    /**
     * Sets the ports list of clk_hub.sv module.
     * Any of that ports can be connected to the DUT
     * and be used as clock signals.
     *
     * @param hubClocks The list of available clocks.
     */
    public void setHubClocks(ArrayList<String> hubClocks) {
        ifaceCodeGen.setHubClocks(hubClocks);
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
        tbCodeGen.setDutClocksPorts(clocksHashMap);
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
        tbCodeGen.setReportSamplingFrequency(reportSamplingFrequency);
    }

    /**
     * Sets a name of a DUT module.
     *
     * @param name The name of a DUT module.
     */
    public void setDutName(String name) {
        tbCodeGen.setDutName(name);
    }

    /**
     * Sets DUT module's parameters HashMap,
     * that contains all names and all values.
     *
     * @param parameters The HashMap object with names
     *                   and values of DUT's parameters.
     */
    public void setParameters(HashMap<String, String> parameters) {
        tbCodeGen.setParameters(parameters);
        ifaceCodeGen.setParameters(parameters);
        monitorCodeGen.setParameters(parameters);
        readCodeGen.setParameters(parameters);
        writeCodeGen.setParameters(parameters);
    }

    /**
     * Sets DUT module's output ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param ports The HashMap object with names
     *              and descriptions of DUT's outputs.
     */
    public void setDutOutputs(HashMap<String, PortDescriptor> ports) {
        tbCodeGen.setOutputs(ports);
        ifaceCodeGen.setDutOutputs(ports);
        monitorCodeGen.addPorts(ports);
        writeCodeGen.addPorts(ports);
    }

    /**
     * Sets DUT module's input ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param ports The HashMap object with names
     *              and descriptions of DUT's inputs.
     */
    public void setDutInputs(HashMap<String, PortDescriptor> ports) {
        tbCodeGen.setInputs(ports);
        ifaceCodeGen.setDutInputs(ports);
        monitorCodeGen.addPorts(ports);
        readCodeGen.addPorts(ports);
    }
}
