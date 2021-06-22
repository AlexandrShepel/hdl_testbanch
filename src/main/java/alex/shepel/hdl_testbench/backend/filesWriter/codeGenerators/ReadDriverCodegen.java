package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.IOException;
import java.util.HashMap;

/*
 * File: ReadDriverCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.READ_DRIVER_SV" file
 * based on specified data.
 */
public class ReadDriverCodegen extends Codegen {

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_INIT = {
            "\t\tthis.gen_port_name = new();",
            "\t\tthis.gen_port_name.open({$sformatf(\"%s\", filePath), \"/port_name.txt\"});",
    };

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_INIT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_port_name[i] = new();",
            "\t\t    this.gen_port_name[i].open({$sformatf(\"%s\", filePath), \"/port_name_\", $sformatf(\"%0d\", i), \".txt\"});",
            "\t\tend"
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_RUN = {
            "\t\tiface.port_name = gen_port_name.getPoint();",
            "\t\tgen_port_name.setIndex(gen_port_name.getIndex() + 1);"
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_RUN_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    iface.port_name[i] = gen_port_name[i].getPoint();",
            "\t\t    gen_port_name[i].setIndex(gen_port_name[i].getIndex() + 1);",
            "\t\tend"
    };

    /* The template of code for description of isOneSize() function.
    Used when unpacked size of input port larger then 0. */
    private static final String CHECK_SIZE =
            "\t\tisTrue = isTrue && (this.gen_prev_port_name.getSize() == this.gen_next_port_name.getSize());";

    /* The template of code for description of isOneSize() function.
    Used when unpacked size of input port equals to 0. */
    private static final String[] CHECK_SIZE_UNPACKED = {
            "\t\tfor (int i = 0; i < PARAMETER - 1; i++) begin",
            "\t\t    isTrue = isTrue && (this.gen_port_name[i].getSize() == this.gen_port_name[i + 1].getSize());",
            "\t\tend"
    };

    /**
     * The class constructor.
     *
     * @throws IOException "ReadDriver.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public ReadDriverCodegen() throws IOException {
        parseFile(BackendParameters.READ_DRIVER_SV);
        setDate();
    }

    /**
     * Overwrites fields in the file
     * where must be placed code
     * for reading input vectors
     * for each of DUT's inputs.
     *
     * @param inputs The HashMap object that contains DUT's inputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    public void setInputs(HashMap<String, PortDescriptor> inputs) {
        for (int index = 0; index < size(); index++) {
            /* Adds ReadGenerator object declaration. */
            if (get(index).contains("ReadGenerator #(DATA_WIDTH) gen_port_name [PORTS_NUM]")) {
                remove(index);
                addGeneratorsDeclaration(index, inputs);
            }

            /* Fills in ReadGenerator initialization field. */
            else if (get(index).contains("local function void initGens();"))
                addGeneratorsInitialization(++index, inputs);

            /* Fills in ReadGenerator running field. */
            else if (get(index).contains("function void run()"))
                if (get(++index).contains("checkEnding()"))
                    addGeneratorsRunning(index = index + 2, inputs);

            /* Fills in body of the function that controls correctness of input data. */
            else if (get(index).contains("local function bit isOneSize()"))
                addSizeChecking(index = index + 2, inputs);
        }
    }

    /**
     * Adds a code lines for declaration of ReadGenerator objects.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param inputs The HashMap object that contains DUT's inputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addGeneratorsDeclaration(int index, HashMap<String, PortDescriptor> inputs) {
        for (String name: inputs.keySet()) {
            if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                /* When unpacked size of port equals 0. */
                if (inputs.get(name).getUnpackedSize().equals("")) {
                    String packedSize = decodeSizeDeclaration(inputs.get(name).getPackedSize());
                    add(index,
                            "\tReadGenerator #(" + packedSize + ") gen_" + name + ";");
                }

                /* When unpacked size of port is larger then 0. */
                else {
                    String packedSize = decodeSizeDeclaration(inputs.get(name).getPackedSize());
                    String unpackedSize = decodeSizeDeclaration(inputs.get(name).getUnpackedSize());
                    add(index,
                            "\tReadGenerator #(" + packedSize + ") gen_" + name + " [" + unpackedSize + "];");
                }

                add(index, "\t// Port: " + inputs.get(name).toString());
                add(index, "");
            }
        }
    }

    /**
     * Adds a code lines for initialization of ReadGenerator objects.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param inputs The HashMap object that contains DUT's inputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addGeneratorsInitialization(int index, HashMap<String, PortDescriptor> inputs) {
        for (String name: inputs.keySet()) {
            if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                /* When unpacked size of port equals 0. */
                if (inputs.get(name).getUnpackedSize().equals("")) {
                    for (int lineNum = GENERATOR_INIT.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_INIT[lineNum].replace(
                                "port_name", name);
                        add(index, editedLine);
                    }
                }

                /* When unpacked size of port is larger then 0. */
                else {
                    String unpackedSize = decodeSizeReferencing(inputs.get(name).getUnpackedSize());

                    for (int lineNum = GENERATOR_INIT_UNPACKED.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_INIT_UNPACKED[lineNum].replace(
                                "port_name", name);
                        editedLine = editedLine.replace("PARAMETER - 1", unpackedSize);
                        add(index, editedLine);
                    }
                }

                add(index, "\t\t// Port: " + inputs.get(name).toString());
                add(index, "");
            }
        }

        remove(index);
    }

    /**
     * Adds a code lines for running of ReadGenerator objects.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param inputs The HashMap object that contains DUT's inputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addGeneratorsRunning(int index, HashMap<String, PortDescriptor> inputs) {
        for (String name: inputs.keySet()) {
            if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                /* When unpacked size of port equals 0. */
                if (inputs.get(name).getUnpackedSize().equals("")) {
                    for (int lineNum = GENERATOR_RUN.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_RUN[lineNum].replace(
                                "port_name", name);
                        add(index, editedLine);
                    }
                }

                /* When unpacked size of port is larger then 0. */
                else {
                    String unpackedSize = decodeSizeReferencing(inputs.get(name).getUnpackedSize());

                    for (int lineNum = GENERATOR_RUN_UNPACKED.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_RUN_UNPACKED[lineNum].replace(
                                "port_name", name);
                        editedLine = editedLine.replace("PARAMETER - 1", decodeSizeReferencing(unpackedSize));
                        add(index, editedLine);
                    }
                }

                add(index, "\t\t// Port: " + inputs.get(name).toString());
                add(index, "");
            }
        }

        remove(index);
    }

    /**
     * Adds a code lines for description of checkOneSize() function.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param inputs The HashMap object that contains DUT's inputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addSizeChecking(int index, HashMap<String, PortDescriptor> inputs) {
        String previousName = "null";

        for (String name: inputs.keySet()) {
            if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                /* Checking unpacked inputs. */
                if (!inputs.get(name).getUnpackedSize().equals("")) {
                    String unpackedSize = decodeSizeReferencing(inputs.get(name).getUnpackedSize());

                    for (int lineNum = CHECK_SIZE_UNPACKED.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = CHECK_SIZE_UNPACKED[lineNum].replace("port_name", name);
                        editedLine = editedLine.replace("PARAMETER - 1", decodeSizeReferencing(unpackedSize));
                        add(index, editedLine);
                    }

                    add(index, "\t\t// Port: " + inputs.get(name).toString());
                    add(index, "");
                }

                /* Checking packed inputs. */
                if (!previousName.equals("null")) {
                    String editedLine = CHECK_SIZE.replace("prev_port_name", previousName);
                    if (!inputs.get(previousName).getUnpackedSize().equals("")) {
                        editedLine = editedLine.replace(previousName, previousName + "[0]");
                    }
                    editedLine = editedLine.replace("next_port_name", name);
                    if (!inputs.get(name).getUnpackedSize().equals("")) {
                        editedLine = editedLine.replace(name, name + "[0]");
                    }
                    add(index, editedLine);
                    add(index, "\t\t//    " + inputs.get(name).toString());
                    add(index, "\t\t//    " + inputs.get(previousName).toString());
                    add(index, "\t\t// Ports: ");
                    add(index, "");
                }

                previousName = name;
            }
        }
    }

    private void definePackingAddPorts(final int index, final HashMap<String, PortDescriptor> outputs,
                                       final String[] packedMacro, final String[] unpackedMacro) {

        for (String name : outputs.keySet()) {
            String unpackedSize = decodeSizeReferencing(outputs.get(name).getUnpackedSize());
            boolean isUnpacked = unpackedSize.length() > 0;

            addPortReplaceName(index, name, unpackedSize,
                    (isUnpacked) ? packedMacro : unpackedMacro);
        }
    }

    private void addPortReplaceName(final int index, final String name, final String size, final String[] macro) {
        for (int i = macro.length - 1; i >= 0; i--)
            add(index, macro[i].replace("port_name", name).replace("PARAMETER - 1", size));
    }
}
