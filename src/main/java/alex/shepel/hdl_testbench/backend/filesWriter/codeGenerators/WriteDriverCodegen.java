package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.IOException;
import java.util.HashMap;

/*
 * File: WriteDriverCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.WRITE_DRIVER_SV" file
 * based on specified data.
 */
public class WriteDriverCodegen extends Codegen {

    /* The template of code for initialization of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_INITIALIZATION_0UNPACKED_SIZE = {
            "\t\tthis.gen_port_name = new();",
            "\t\tthis.gen_port_name.open({$sformatf(\"%s\", filePath), \"/port_name.txt\"});",
    };

    /* The template of code for initialization of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_INITIALIZATION_DEFAULT_SIZE = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_port_name[i] = new();",
            "\t\t    this.gen_port_name[i].open({$sformatf(\"%s\", filePath), \"/port_name_\", $sformatf(\"%0d\", i), \".txt\"});",
            "\t\tend"
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_RUNNING_0UNPACKED_SIZE = {
            "\t\tthis.gen_port_name.write(iface.port_name);"
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_RUNNING_DEFAULT_SIZE = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_port_name[i].write(iface.port_name[i]);",
            "\t\tend"
    };

    /**
     * The class constructor.
     *
     * @throws IOException "WriteDriver.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public WriteDriverCodegen() throws IOException {
        parseFile(BackendParameters.WRITE_DRIVER_SV);
        setDate();
    }

    /**
     * Overwrites fields in the file
     * where must be placed code
     * for writing output vectors
     * for each of DUT's outputs.
     *
     * @param outputs The HashMap object that contains DUT's outputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    public void setOutputs(HashMap<String, PortDescriptor> outputs) {
        for (int index = 0; index < size(); index++) {
            /* Adds WriteGenerator object declaration. */
            if (get(index).contains("WriteGenerator #(DATA_WIDTH) gen_port_name [PORTS_NUM]")) {
                remove(index);
                addGeneratorsDeclaration(index, outputs);
            }

            /* Fills in WriteGenerator initialization field. */
            else if (get(index).contains("local function void initGens();"))
                addGeneratorsInitialization(++index, outputs);

            /* Fills in WriteGenerator running field. */
            else if (get(index).contains("function void run()"))
                addGeneratorsRunning(++index, outputs);
        }
    }

    /**
     * Adds a code lines for running of WriteGenerator objects.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param outputs The HashMap object that contains DUT's outputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addGeneratorsRunning(int index, HashMap<String, PortDescriptor> outputs) {
        for (String name: outputs.keySet()) {
            /* When unpacked size of port equals 0. */
            if (outputs.get(name).getUnpackedSize().equals("")) {
                for (int lineNum = GENERATOR_RUNNING_0UNPACKED_SIZE.length - 1; lineNum >= 0; lineNum--) {
                    String editedLine = GENERATOR_RUNNING_0UNPACKED_SIZE[lineNum].replace(
                            "port_name", name);
                    editedLine = editedLine.replace("port_name", name);
                    add(index, editedLine);
                }
            }

            /* When unpacked size of port is larger then 0. */
            else {
                String unpackedSize = decodeSizeReferencing(outputs.get(name).getUnpackedSize());

                for (int lineNum = GENERATOR_RUNNING_DEFAULT_SIZE.length - 1; lineNum >= 0; lineNum--) {
                    String editedLine = GENERATOR_RUNNING_DEFAULT_SIZE[lineNum].replace(
                            "port_name", name);
                    editedLine = editedLine.replace("port_name", name);
                    editedLine = editedLine.replace("PARAMETER - 1", decodeSizeReferencing(unpackedSize));
                    add(index, editedLine);
                }
            }

            add(index, "\t\t// Port: " + outputs.get(name).toString());
            add(index, "");
        }

        remove(index);
    }

    /**
     * Adds a code lines for initialization of WriteGenerator objects.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param outputs The HashMap object that contains DUT's outputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addGeneratorsInitialization(int index, HashMap<String, PortDescriptor> outputs) {
        for (String name: outputs.keySet()) {
            /* When unpacked size of port equals 0. */
            if (outputs.get(name).getUnpackedSize().equals("")) {
                for (int lineNum = GENERATOR_INITIALIZATION_0UNPACKED_SIZE.length - 1; lineNum >= 0; lineNum--) {
                    String editedLine = GENERATOR_INITIALIZATION_0UNPACKED_SIZE[lineNum].replace(
                            "port_name", name);
                    editedLine = editedLine.replace("port_name", name);
                    add(index, editedLine);
                }
            }

            /* When unpacked size of port is larger then 0. */
            else {
                String unpackedSize = decodeSizeReferencing(outputs.get(name).getUnpackedSize());

                for (int lineNum = GENERATOR_INITIALIZATION_DEFAULT_SIZE.length - 1; lineNum >= 0; lineNum--) {
                    String editedLine = GENERATOR_INITIALIZATION_DEFAULT_SIZE[lineNum].replace(
                            "port_name", name);
                    editedLine = editedLine.replace("port_name", name);
                    editedLine = editedLine.replace("PARAMETER - 1", decodeSizeReferencing(unpackedSize));
                    add(index, editedLine);
                }
            }

            add(index, "\t\t// Port: " + outputs.get(name).toString());
            add(index, "");
        }

        remove(index);
    }

    /**
     * Adds a code lines for declaration of WriteGenerator objects.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param outputs The HashMap object that contains DUT's outputs.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    private void addGeneratorsDeclaration(int index, HashMap<String, PortDescriptor> outputs) {
        for (String name: outputs.keySet()) {
            /* When unpacked size of port equals 0. */
            if (outputs.get(name).getUnpackedSize().equals("")) {
                String packedSize = decodeSizeDeclaration(outputs.get(name).getPackedSize());
                add(index,
                        "\tWriteGenerator #(" + packedSize + ") gen_" + name + ";");
            }

            /* When unpacked size of port is larger then 0. */
            else {
                String packedSize = decodeSizeDeclaration(outputs.get(name).getPackedSize());
                String unpackedSize = decodeSizeDeclaration(outputs.get(name).getUnpackedSize());
                add(index,
                        "\tWriteGenerator #(" + packedSize + ") gen_" + name + " [" + unpackedSize + "];");
            }

            add(index, "\t// Port: " + outputs.get(name).toString());
            add(index, "");
        }
    }
}
