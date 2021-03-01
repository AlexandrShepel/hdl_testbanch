package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;

import java.io.File;
import java.io.IOException;

/*
 * File: EnvironmentCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.ENVIRONMENT_SV" file
 * based on specified data.
 */
public class EnvironmentCodeGenerator extends CodeGenerator {

    /* The working folder, where must be placed generated file. */
    private File workingFolder;

    /* Folders where will be placed input and output vectors
    that used to test DUT and check correctness of its work. */
    private static final String DEFAULT_INPUT_FOLDER = "\\input_data";
    private static final String DEFAULT_OUTPUT_FOLDER = "\\output_data";

    /**
     * The class constructor.
     *
     * @throws IOException "Environment.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public EnvironmentCodeGenerator() throws IOException {
        parseFile(BackendParameters.ENVIRONMENT_SV);
        setDate();
    }

    /**
     * Sets a directory where will be placed
     * overwritten file.
     *
     * @param workingFolder The File object that contains path
     *                      to the working folder,
     *                      where overwritten file
     *                      will be placed.
     */
    public void setWorkingFolder(File workingFolder) {
        this.workingFolder = workingFolder;
        setInputDataFolder();
        setOutputDataFolder();
    }

    /**
     * Sets a name of directory where will be placed input vectors,
     * that contains input data for simulation.
     */
    private void setInputDataFolder() {
        File inputData = new File(workingFolder.getAbsolutePath() + DEFAULT_INPUT_FOLDER);
        boolean isInputFolderCreated = inputData.mkdir();
        if (!isInputFolderCreated)
            System.out.println("Error when creating a new folder: " + inputData.getAbsolutePath() +
                    ". Check that specified working folder is empty.");

        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\tconst string READ_FILES = \"./readFolder/\";")) {
                String path = inputData.getAbsolutePath();

                while (path.contains("\\")) {
                    path = path.replace("\\", "/");
                }

                String editedLine = get(index).replace(
                        "./readFolder/", path);
                set(index, editedLine);

                break;
            }
        }
    }

    /**
     * Sets a name of directory where will be placed output vectors,
     * that contains results of DUT's simulation.
     */
    private void setOutputDataFolder() {
        File outputData = new File(workingFolder.getAbsolutePath() + DEFAULT_OUTPUT_FOLDER);
        boolean isOutputFolderCreated = outputData.mkdir();
        if (!isOutputFolderCreated)
            System.out.println("Error when creating a new folder: " + outputData.getAbsolutePath() +
                    ". Check that specified working folder is empty.");

        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\tconst string WRITE_FILES = \"./writeFolder/\";")) {
                String path = outputData.getAbsolutePath();

                while (path.contains("\\")) {
                    path = path.replace("\\", "/");
                }

                String editedLine = get(index).replace("./writeFolder/", path);
                set(index, editedLine);

                break;
            }
        }
    }
}
