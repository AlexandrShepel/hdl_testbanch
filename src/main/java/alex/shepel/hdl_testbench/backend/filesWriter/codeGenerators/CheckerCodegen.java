package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parsers.detectors.PortDescriptor;

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

    private static final String[] MISMATCH_INIT = {
            "\t\tiface.port_name_errors = 0;",
    };

    private static final String[] MISMATCH_INIT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   iface.port_name_errors[i] = 0;",
            "\t\tend",
    };

    private static final String[] MISMATCH = {
            "\t\tbit isEqual = (iface.port_name !== iface.port_name_expect);",
            "\t\tbit isDefined = (iface.port_name_expect !== 'x);",
            "\t\tiface.port_name_mismatch = isEqual && isDefined;",
    };

    private static final String[] MISMATCH_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   bit isEqual = (iface.port_name[i] !== iface.port_name_expect[i]);",
            "\t\t   bit isDefined = (iface.port_name_expect[i] !== 'x);",
            "\t\t   iface.port_name_mismatch[i] = isEqual && isDefined;",
            "\t\tend",
    };

    private static final String[] MISMATCH_COUNT = {
            "\t\tif (iface.port_name_mismatch)",
            "\t\t   iface.port_name_errors++;",
    };

    private static final String[] MISMATCH_COUNT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   if (iface.port_name_mismatch[i])",
            "\t\t       iface.port_name_errors[i]++;",
            "\t\tend",
    };

    private static final String[] MISMATCH_DISPLAY = {
            "\t\t$display(\"\\tport \\\"port_name\\\": %0d\", iface.port_name_errors);",
    };

    private static final String[] MISMATCH_DISPLAY_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t   $display(\"\\tport \\\"port_name[i]\\\": %0d\", iface.port_name_errors[i]);",
            "\t\tend",
    };

    public CheckerCodegen() throws IOException {
        parseFile(BackendParameters.CHECKER_SV);
        setDate();
    }

    public void setOutputs(HashMap<String, PortDescriptor> outputs) {
        for (int index = 0; index < size(); index++) {
            /* Adds ports checking initialization. */
            if (get(index).contains("iface.port_name_errors = 0;")) {
                remove(index);
                definePackingAddPort(false, index, outputs, MISMATCH_INIT, MISMATCH_INIT_UNPACKED);
            }

            /* Adds ports checking. */
            else if (get(index).contains("function void mismatch();"))
                definePackingAddPort(false, ++index, outputs, MISMATCH, MISMATCH_UNPACKED);

            /* Adds errors counting. */
            else if (get(index).contains("function void countError();"))
                definePackingAddPort(false, ++index, outputs, MISMATCH_COUNT, MISMATCH_COUNT_UNPACKED);

            /* Adds errors count displaying. */
            else if (get(index).contains("function void displayMismatches();"))
                definePackingAddPort(false, index += 2, outputs, MISMATCH_DISPLAY, MISMATCH_DISPLAY_UNPACKED);
        }
    }
}
