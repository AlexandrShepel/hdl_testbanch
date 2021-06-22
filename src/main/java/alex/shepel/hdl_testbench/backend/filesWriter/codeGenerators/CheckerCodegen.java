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
public class CheckerCodegen extends Codegen {

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] PORT_MISMATCH_INIT = {
            "\t\tiface.port_name_errors = 0;",
    };

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] PORT_MISMATCH_INIT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   iface.port_name_errors[i] = 0;",
            "\t\tend",
    };

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] PORT_MISMATCH = {
            "\t\tbit isEqual = (iface.port_name !== iface.port_name_expect);",
            "\t\tbit isDefined = (iface.port_name_expect !== 'x);",
            "\t\tiface.port_name_mismatch = isEqual && isDefined;",
    };

    /* The template of code for initialization of ReadGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] PORT_MISMATCH_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   bit isEqual = (iface.port_name[i] !== iface.port_name_expect[i]);",
            "\t\t   bit isDefined = (iface.port_name_expect[i] !== 'x);",
            "\t\t   iface.port_name_mismatch[i] = isEqual && isDefined;",
            "\t\tend",
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] PORT_MISMATCH_COUNT = {
            "\t\tif (iface.port_name_mismatch)",
            "\t\t   iface.port_name_errors++;",
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] PORT_MISMATCH_COUNT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   if (iface.port_name_mismatch[i])",
            "\t\t       iface.port_name_errors[i]++;",
            "\t\tend",
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] PORT_MISMATCH_DISPLAY = {
            "\t\t$display(\"\\tport \"port_name\": %0d\", iface.port_name_errors);",
    };

    /* The template of code for running of ReadGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] PORT_MISMATCH_DISPLAY_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   $display(\"\\tport \"port_name[i]\": %0d\", iface.port_name_errors[i]);",
            "\t\tend",
    };

    /**
     * The class constructor.
     *
     * @throws IOException "ReadDriver.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public CheckerCodegen() throws IOException {
        parseFile(BackendParameters.CHECKER_SV);
        setDate();
    }

    public void setOutputs(HashMap<String, PortDescriptor> outputs) {
        for (int index = 0; index < size(); index++) {
            /* Adds ports checking initialization. */
            if (get(index).contains("iface.port_name_errors = 0;")) {
                remove(index);
                definePackingAddPorts(index, outputs, PORT_MISMATCH_INIT, PORT_MISMATCH_INIT_UNPACKED);
            }

            /* Adds ports checking. */
            else if (get(index).contains("function void mismatch();"))
                definePackingAddPorts(++index, outputs, PORT_MISMATCH, PORT_MISMATCH_UNPACKED);

            /* Adds errors counting. */
            else if (get(index).contains("function void countError();"))
                definePackingAddPorts(++index, outputs, PORT_MISMATCH_COUNT, PORT_MISMATCH_COUNT_UNPACKED);

            /* Adds errors count displaying. */
            else if (get(index).contains("function void displayMismatches();"))
                definePackingAddPorts(index += 2, outputs, PORT_MISMATCH_DISPLAY, PORT_MISMATCH_DISPLAY_UNPACKED);
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
