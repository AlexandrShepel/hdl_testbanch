package backend.filesWriter.codegens.sv;

<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/filesWriter/codegens/sv/TBCodegen.java
import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parsers.detectors.PortDescriptor;
=======
import backend.BackendParameters;
import backend.parsers.detectors.PortDescriptor;
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/filesWriter/codeGenerators/TBCodeGenerator.java

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
<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/filesWriter/codegens/sv/TBCodegen.java
public class TBCodegen extends Codegen {

    /* The working folder, where must be placed generated file. */
    private File workingFolder;
=======
public class TBCodegen extends SVCodegen implements BackendParameters {
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/filesWriter/codeGenerators/TBCodeGenerator.java

    /* Folders where will be placed input and output vectors
    that used to test DUT and check correctness of its work. */
    private static final String DEFAULT_INPUT_FOLDER = "input_data";
    private static final String DEFAULT_OUTPUT_FOLDER = "output_data";

    /**
     * The class constructor.
     *
     * @throws IOException "Environment.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public TBCodegen() throws IOException {
<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/filesWriter/codegens/sv/TBCodegen.java
        parseFile(BackendParameters.TB_SV);
        setDate();
=======
        super(TB_SV);
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/filesWriter/codeGenerators/TBCodeGenerator.java
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
<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/filesWriter/codegens/sv/TBCodegen.java
    public void setClockDriver(HashMap<String, String> clocksHashMap) {
=======
    public void setClocks(HashMap<String, String> clocksHashMap) {
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/filesWriter/codeGenerators/TBCodeGenerator.java
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
<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/filesWriter/codegens/sv/TBCodegen.java
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
        setDataFolders();
    }

    /**
     * Sets a name of directory where will be placed input vectors,
     * that contains input data for simulation.
     */
    private void setDataFolders() {
        File inputFolder = new File(workingFolder.getAbsolutePath() + "\\" + DEFAULT_INPUT_FOLDER);
        File outputFolder = new File(workingFolder.getAbsolutePath() + "\\" + DEFAULT_OUTPUT_FOLDER);

        if (!inputFolder.mkdir() || !outputFolder.mkdir())
            System.out.println("Error when creating a new folder: " + inputFolder.getAbsolutePath() +
                    ". Check that specified working folder is empty.");

        for (int index = 0; index < size(); index++) {
            set(index, get(index).replace(
                    "<project_path>", workingFolder.getAbsolutePath().replace("\\", "/")));
=======
     * Sets a name of directory where will be placed input vectors,
     * that contains input data for simulation.
     *
     * @param dir The File object that contains path
     *            to the working folder.
     */
    public void setDirectory(final File dir) {
        final File inputFolder = new File(dir.getAbsolutePath() + "\\" + DEFAULT_INPUT_FOLDER);
        final File outputFolder = new File(dir.getAbsolutePath() + "\\" + DEFAULT_OUTPUT_FOLDER);

        final String errMessage = "Error when creating a new folder. Check that specified working folder is empty.\n";

        if (!inputFolder.mkdir())
            System.out.println(errMessage + inputFolder.getAbsolutePath() + "\n");

        if (!outputFolder.mkdir())
            System.out.println(errMessage + outputFolder.getAbsolutePath() + "\n");

        for (int index = 0; index < size(); index++) {
            set(index, get(index).replace(
                    "<project_path>", dir.getAbsolutePath().replace("\\", "/")));
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/filesWriter/codeGenerators/TBCodeGenerator.java
            set(index, get(index).replace("<input_data_folder>", DEFAULT_INPUT_FOLDER));
            set(index, get(index).replace("<output_data_folder>", DEFAULT_OUTPUT_FOLDER));
        }
    }
}
