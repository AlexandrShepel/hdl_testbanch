package backend.filesWriter;

import backend.BackendParameters;
import backend.filesWriter.codegens.mpf.MPFCodegen;
import backend.filesWriter.codegens.sv.*;
import backend.parsers.detectors.PortDescriptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class FilesWriter implements BackendParameters {

    /* The working directory that must be opened.
    It is specified by user. */
    private File dir;

    private final SVCodegen[] svCodegens;
    private final MPFCodegen mpfCodegen;

    /**
     * The class constructor.
     *
     * @throws IOException The error of reading a resource files,
     *                     when initialization of generators.
     */
    public FilesWriter() throws IOException {
        svCodegens = new SVCodegen[]{
            new TBCodegen(),
            new ClockDriverCodegen(),
            new InterfaceCodegen(),
            new CheckerCodegen(),
            new ReadDriverCodegen(),
            new WriteDriverCodegen(),
            new SVCodegen(READ_GENERATOR_SV),
            new SVCodegen(WRITE_GENERATOR_SV),
            new SVCodegen(CLK_GENERATOR_SV),
        };
        mpfCodegen = new MPFCodegen(PRJ_MPF);
    }

    /**
     * Generates all files needed for test environment.
     * Overwrites resource files (if needed) and copies them to a destination folder.
     *
     * @throws IOException The error of creating a new files.
     */
    public void run() throws IOException {
        final ArrayList<String> svFilesNames = new ArrayList<>();

        for (SVCodegen codegen : svCodegens) {
            writeFile(codegen.getParsedFile(), codegen.getName());
            svFilesNames.add(codegen.getName());
        }

        createModelsimProject(svFilesNames);
    }

    private void createModelsimProject(ArrayList<String> svFilesNames) throws IOException {
        mpfCodegen.setFiles(svFilesNames.toArray(new String[0]));
        writeFile(mpfCodegen.getParsedFile(), mpfCodegen.getName());
        Files.createDirectory(Path.of(dir.getAbsolutePath() + "\\work"));
        System.out.println("Modelsim project created.");
    }

    /**
     * Writes a parsed file into specified directory.
     *
     * @throws IOException Error when creating a new folder or file.
     */
    public void writeFile(final ArrayList<String> lines, final String fileName)
            throws IOException {

        final FileWriter fileWriter = new FileWriter(createFile(fileName));

        for (final String line: lines)
            fileWriter.append(line).append("\n");

        fileWriter.close();
    }

    /**
     * Creates a subdirectory for a new file.
     * Writes new empty file into it.
     *
     * @param fileName The name of parsed file.
     * @return The created file.
     * @throws IOException Error when creating a new folder or file.
     */
    private File createFile(String fileName) throws IOException {
        File file = new File(dir.getAbsolutePath() + "\\" + fileName);

        if (!file.createNewFile())
            throw new IOException("File " + file.getAbsolutePath() + " can't be created.");
        System.out.println("File created: " + file.getAbsolutePath());

        return file;
    }

    /**
     * Sets the absolute path of the working directory.
     * Test environment will be placed there.
     *
     * @param dir The File object that contains absolute path
     *                      of the working folder.
     */
    public void setDirectory(File dir) {
        if (dir.mkdir())
            System.out.println("Directory created: " + dir.getAbsolutePath());
        else
            System.out.println("Directory can't be created: " + dir.getAbsolutePath());

        this.dir = dir;

        for (SVCodegen codegen : svCodegens)
            codegen.setDirectory(dir);

        mpfCodegen.setDirectory(dir);
        System.out.println("Working folder is set. Folder = " + dir.getAbsolutePath());
    }

    /**
     * Sets a connection between DUT's and clk_hub's sv.
     * Connection is represented as HashMap
     * which key is name of DUT's clock input
     * and value is name of clk_hub output.
     *
     * @param clocksHashMap The HashMap object that contains correspondence
     *                      between DUT's and clk_hub's sv.
     */
    public void setClocksHashMap(HashMap<String, String> clocksHashMap) {
        for (SVCodegen codegen : svCodegens)
            codegen.setClocks(clocksHashMap);
    }

    /**
     * Sets a sampling frequency of the created test environment.
     * Simulation points will be written to the report files
     * every tact of this frequency.
     *
     * @param freq Sampling frequency that is used
     *             in the test environment for writing
     *             output data to the report file.
     */
    public void setSampleFrequency(String freq) {
        for (SVCodegen codegen : svCodegens)
            codegen.setSampleFreq(freq);
    }

    /**
     * Sets a name of a DUT module.
     *
     * @param dutFile The file of a DUT module.
     */
    public void setDutFile(File dutFile) {
        for (SVCodegen codegen : svCodegens)
            codegen.setDutName(dutFile.getName());

        mpfCodegen.setDutFile(dutFile);
    }

    /**
     * Sets DUT module's parameters HashMap,
     * that contains all names and all values.
     *
     * @param parameters The HashMap object with names
     *                   and values of DUT's parameters.
     */
    public void setParameters(HashMap<String, String> parameters) {
        for (SVCodegen codegen : svCodegens)
            codegen.setParameters(parameters);
    }

    /**
     * Sets DUT module's output ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param ports The HashMap object with names
     *              and descriptions of DUT's outputs.
     */
    public void setDutOutputs(HashMap<String, PortDescriptor> ports) {
        for (SVCodegen codegen : svCodegens)
            codegen.setOutputs(ports);
    }

    /**
     * Sets DUT module's input ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param ports The HashMap object with names
     *              and descriptions of DUT's inputs.
     */
    public void setDutInputs(HashMap<String, PortDescriptor> ports) {
        for (SVCodegen codegen : svCodegens)
            codegen.setInputs(ports);
    }
}
