package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/*
 * File: TBCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.TB_SV" file
 * based on specified data.
 */
public class TBCodegen extends Codegen {

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
    public TBCodegen() throws IOException {
        parseFile(BackendParameters.TB_SV);
        setDate();
    }

    /**
     * Sets DUT module's input ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param inputs The HashMap object with names
     *               and descriptions of DUT's inputs.
     */
    public void setInputs(HashMap<String, PortDescriptor> inputs) {
        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\t\t// inputs")) {
                for (String name: inputs.keySet()) {
                    String editedLine = "\t\t." + name + " (iface." + name + "),";
                    add(index + 1, editedLine);
                }

                break;
            }
        }
    }

    /**
     * Sets DUT module's output ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param outputs The HashMap object with names
     *              and descriptions of DUT's outputs.
     */
    public void setOutputs(HashMap<String, PortDescriptor> outputs) {
        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\t\t// outputs")) {
                for (String name: outputs.keySet()) {
                    String editedLine = "\t\t." + name + " (iface." + name + "),";
                    add(index + 1, editedLine);
                }

                /* Deletes last comma in the outputs declaration field. */
                int lastDutPortIndex = indexOf("\t\t// outputs") + outputs.keySet().size();
                String lastDutPortLine = get(lastDutPortIndex);
                set(lastDutPortIndex, lastDutPortLine.substring(0, lastDutPortLine.indexOf(",")));

                break;
            }
        }
    }

    /**
     * Sets a name of a DUT module.
     * Name used in the DUT module connection field.
     *
     * @param name The name of a DUT module.
     */
    public void setDutName(String name) {
        for (int index = 0; index < size(); index++) {
            /* Replaces template "design_under_test" name with a new one in a testbench description field. */
            if (get(index).contains("design_under_test module.")) {
                /* Replaces template name with new specified name. */
                String editedLine = get(index).replace(
                        "design_under_test", name.substring(0, name.indexOf(".sv")));

                /* Adds spaces to the end of the line. */
                int tabsNum = ("design_under_test".length() - name.length()) >> 2;
                for (int cnt = 0; cnt <= tabsNum; cnt++) {
                    editedLine = editedLine.replace("module.", "module.\t");
                }

                /* Replace old line with a new one. */
                set(index, editedLine);

                continue;
            }

            /* Replaces template "design_under_test" name with a new one in DUT declaration field. */
            if (get(index).contains("design_under_test #(")) {
                String editedLine = get(index).replace(
                        "design_under_test", name.substring(0, name.indexOf(".sv")));
                set(index, editedLine);

                break;
            }
        }
    }

    /**
     * Adds clocks to the clocks correspondence declaration field.
     *
     * @param clocksHashMap The HashMap object that contains correspondence
     *                      between DUT's and clk_hub's modules.
     */
    @SuppressWarnings("SuspiciousListRemoveInLoop")
    public void setClockDriver(HashMap<String, String> clocksHashMap) {
        for (int index = 0; index < size(); index++) {
            /* Sets clock driver parameters. */
            if (get(index).contains(".DUT_CLK_FREQ")) {
                remove(index);

                for (String name : clocksHashMap.keySet()) {
                    String editedLine =
                            "\t\t.DUT_" + name.toUpperCase() + "_FREQ (iface.DUT_" + name.toUpperCase() + "_FREQ),";
                    add(index, editedLine);
                }
            }

            /* Sets clock driver ports. */
            if (get(index).contains(".dut_clk")) {
                remove(index);

                for (String name : clocksHashMap.keySet()) {
                    String editedLine = "\t\t." + name + " (iface." + name + "),";
                    add(index, editedLine);
                }
            }
        }
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
