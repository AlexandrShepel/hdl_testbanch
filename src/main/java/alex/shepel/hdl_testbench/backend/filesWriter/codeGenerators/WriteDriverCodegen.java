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
    private static final String[] GENERATOR_INIT = {
            "\t\tthis.gen_port_name = new();",
            "\t\tthis.gen_port_name.open({$sformatf(\"%s\", filePath), \"/port_name.txt\"});",
            "",
            "\t\tthis.gen_port_name_mismatch = new();",
            "\t\tthis.gen_port_name_mismatch.open({$sformatf(\"%s\", filePath), \"/port_name_mismatch.txt\"});",
    };

    /* The template of code for initialization of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_INIT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_port_name[i] = new();",
            "\t\t    this.gen_port_name[i].open({$sformatf(\"%s\", filePath), \"/port_name_\", $sformatf(\"%0d\", i), \".txt\"});",
            "",
            "\t\t    this.gen_port_name_mismatch[i] = new();",
            "\t\t    this.gen_port_name_mismatch[i].open({$sformatf(\"%s\", filePath), \"/port_name_mismatch_\", $sformatf(\"%0d\", i), \".txt\"});",
            "\t\tend"
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_RUN = {
            "\t\tthis.gen_port_name.write(iface.port_name);",
            "\t\tthis.gen_port_name_mismatch.write(iface.port_name_mismatch);",
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_RUN_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_port_name[i].write(iface.port_name[i]);",
            "\t\t    this.gen_port_name_mismatch[i].write(iface.port_name_mismatch[i]);",
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
            else if (get(index).contains("local function void initGens()"))
                definePackingAddPort(false, ++index, outputs, GENERATOR_INIT, GENERATOR_INIT_UNPACKED);

            /* Fills in WriteGenerator running field. */
            else if (get(index).contains("function void run()"))
                definePackingAddPort(false, ++index, outputs, GENERATOR_RUN, GENERATOR_RUN_UNPACKED);
        }
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
                add(index,
                    "\tWriteGenerator #(" + packedSize + ") gen_" + name + "_mismatch;");
            }

            /* When unpacked size of port is larger then 0. */
            else {
                String packedSize = decodeSizeDeclaration(outputs.get(name).getPackedSize());
                String unpackedSize = decodeSizeDeclaration(outputs.get(name).getUnpackedSize());
                add(index,
                    "\tWriteGenerator #(" + packedSize + ") gen_" + name + " [" + unpackedSize + "];");
                add(index,
                    "\tWriteGenerator #(" + packedSize + ") gen_" + name + "_mismatch [" + unpackedSize + "];");
            }

            add(index, "\t// Port: " + outputs.get(name).toString());
            add(index, "");
        }
    }
}
