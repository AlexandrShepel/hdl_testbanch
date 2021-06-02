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
public class ReadDriverCodeGenerator extends CodeGenerator {

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_INITIALIZATION_0UNPACKED_SIZE = {
            "\t\tthis.gen_port_name = new();",
            "\t\tthis.gen_port_name.open({$sformatf(\"%s\", filePath), \"/port_name.txt\"});",
    };

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_INITIALIZATION_DEFAULT_SIZE = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_port_name[i] = new();",
            "\t\t    this.gen_port_name[i].open({$sformatf(\"%s\", filePath), \"/port_name_\", $sformatf(\"%0d\", i), \".txt\"});",
            "\t\tend"
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_RUNNING_0UNPACKED_SIZE = {
            "\t\tiface.port_name = gen_port_name.getPoint();",
            "\t\tgen_port_name.setIndex(gen_port_name.getIndex() + 1);"
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_RUNNING_DEFAULT_SIZE = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    iface.port_name[i] = gen_port_name[i].getPoint();",
            "\t\t    gen_port_name[i].setIndex(gen_port_name[i].getIndex() + 1);",
            "\t\tend"
    };

    /* The template of code for description of isOneSize() function.
    Used when unpacked size of input port equals to 0. */
    private static final String[] IS_ONE_SIZE_UNPACKED_PORTS = {
            "\t\tfor (int i = 0; i < PARAMETER - 1; i++) begin",
            "\t\t    isTrue = isTrue && (this.gen_port_name[i].getSize() == this.gen_port_name[i + 1].getSize());",
            "\t\tend"
    };

    /* The template of code for description of isOneSize() function.
    Used when unpacked size of input port larger then 0. */
    private static final String IS_ONE_SIZE_DEFAULT_PORTS =
            "\t\tisTrue = isTrue && (this.gen_prev_port_name.getSize() == this.gen_next_port_name.getSize());";

    /**
     * The class constructor.
     *
     * @throws IOException "ReadDriver.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public ReadDriverCodeGenerator() throws IOException {
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
    public void addPorts(HashMap<String, PortDescriptor> inputs) {
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
            else if (get(index).contains("function run()"))
                addGeneratorsRunning(++index, inputs);

            /* Fills in body of the function that controls correctness of input data. */
            else if (get(index).contains("local function bit isOneSize()"))
                addOneSizeChecking(index = index + 2, inputs);
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
                    for (int lineNum = GENERATOR_INITIALIZATION_0UNPACKED_SIZE.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_INITIALIZATION_0UNPACKED_SIZE[lineNum].replace(
                                "port_name", name);
                        editedLine = editedLine.replace("port_name", name);
                        add(index, editedLine);
                    }
                }

                /* When unpacked size of port is larger then 0. */
                else {
                    String unpackedSize = decodeSizeReferencing(inputs.get(name).getUnpackedSize());

                    for (int lineNum = GENERATOR_INITIALIZATION_DEFAULT_SIZE.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_INITIALIZATION_DEFAULT_SIZE[lineNum].replace(
                                "port_name", name);
                        editedLine = editedLine.replace("port_name", name);
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
                    for (int lineNum = GENERATOR_RUNNING_0UNPACKED_SIZE.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_RUNNING_0UNPACKED_SIZE[lineNum].replace(
                                "port_name", name);
                        editedLine = editedLine.replace("port_name", name);
                        add(index, editedLine);
                    }
                }

                /* When unpacked size of port is larger then 0. */
                else {
                    String unpackedSize = decodeSizeReferencing(inputs.get(name).getUnpackedSize());

                    for (int lineNum = GENERATOR_RUNNING_DEFAULT_SIZE.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = GENERATOR_RUNNING_DEFAULT_SIZE[lineNum].replace(
                                "port_name", name);
                        editedLine = editedLine.replace("port_name", name);
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
    private void addOneSizeChecking(int index, HashMap<String, PortDescriptor> inputs) {
        String previousName = "null";

        for (String name: inputs.keySet()) {
            if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                /* Checking unpacked inputs. */
                if (!inputs.get(name).getUnpackedSize().equals("")) {
                    String unpackedSize = decodeSizeReferencing(inputs.get(name).getUnpackedSize());

                    for (int lineNum = IS_ONE_SIZE_UNPACKED_PORTS.length - 1; lineNum >= 0; lineNum--) {
                        String editedLine = IS_ONE_SIZE_UNPACKED_PORTS[lineNum].replace("port_name", name);
                        editedLine = editedLine.replace("port_name", name);
                        editedLine = editedLine.replace("PARAMETER - 1", decodeSizeReferencing(unpackedSize));
                        add(index, editedLine);
                    }

                    add(index, "\t\t// Port: " + inputs.get(name).toString());
                    add(index, "");
                }

                /* Checking packed inputs. */
                if (!previousName.equals("null")) {
                    String editedLine = IS_ONE_SIZE_DEFAULT_PORTS.replace("prev_port_name", previousName);
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
}
