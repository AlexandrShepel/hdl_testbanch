package backend.filesWriter.codegens.sv;

import backend.BackendParameters;
import backend.parsers.detectors.PortDescriptor;

import java.io.IOException;
import java.util.HashMap;

/*
 * File: WriteDriverCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.WRITE_DRIVER_SV" file
 * based on specified data.
 */
public class WriteDriverCodegen extends SVCodegen implements BackendParameters {

    /* The template of code for declaration of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_DECLARE = {
            "\tWriteGenerator gen_<port_name>;",
            "\tWriteGenerator gen_<port_name>_mismatch;",
    };

    /* The template of code for declaration of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_DECLARE_UNPACKED = {
            "\tWriteGenerator gen_<port_name> [PARAMETER - 1 + 1];",
            "\tWriteGenerator gen_<port_name>_mismatch [PARAMETER - 1 + 1];",
    };

    /* The template of code for initialization of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_INIT = {
            "\t\tthis.gen_<port_name> = new();",
            "\t\tthis.gen_<port_name>.open({$sformatf(\"%s\", filePath), \"/<port_name>.tbv\"}, \"w\");",
            "",
            "\t\tthis.gen_<port_name>_mismatch = new();",
            "\t\tthis.gen_<port_name>_mismatch.open({$sformatf(\"%s\", filePath), \"/<port_name>_mismatch.tbv\"}, \"w\");",
    };

    /* The template of code for initialization of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_INIT_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_<port_name>[i] = new();",
            "\t\t    this.gen_<port_name>[i].open({$sformatf(\"%s\", filePath), \"/<port_name>_\", $sformatf(\"%0d\", i), \".tbv\"}, \"w\");",
            "",
            "\t\t    this.gen_<port_name>_mismatch[i] = new();",
            "\t\t    this.gen_<port_name>_mismatch[i].open({$sformatf(\"%s\", filePath), \"/<port_name>_mismatch_\", $sformatf(\"%0d\", i), \".tbv\"}, \"w\");",
            "\t\tend"
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_RUN = {
            "\t\tthis.gen_<port_name>.writeStr($sformatf(\"%h\", iface.<port_name>));",
            "\t\tthis.gen_<port_name>_mismatch.writeStr($sformatf(\"%h\", iface.<port_name>_mismatch));",
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_RUN_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    this.gen_<port_name>[i].writeStr($sformatf(\"%h\", iface.<port_name>[i]));",
            "\t\t    this.gen_<port_name>_mismatch[i].writeStr($sformatf(\"%h\", iface.<port_name>_mismatch[i]));",
            "\t\tend"
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] LOG_ERRORS = {
            "\t\tgen_log.writeStr($sformatf(\"\\t\\t\\t\\t<port_name>: %0d\", iface.<port_name>_errors));",
    };

    /* The template of code for running of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] LOG_ERRORS_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    gen_log.writeStr($sformatf(\"\\t\\t\\t\\t<port_name>[%0d]: %0d\", i, iface.<port_name>_errors[i]));",
            "\t\tend"
    };

    /* The template of code for closing of WriteGenerator object.
    Used when unpacked size of input port equals to 0. */
    private static final String[] GENERATOR_CLOSE = {
            "\t\tgen_<port_name>.close();",
            "\t\tgen_<port_name>_mismatch.close();",
    };

    /* The template of code for closing of WriteGenerator object.
    Used when unpacked size of input port larger then 0. */
    private static final String[] GENERATOR_CLOSE_UNPACKED = {
            "\t\tfor (int i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    gen_<port_name>[i].close();",
            "\t\t    gen_<port_name>_mismatch[i].close();",
            "\t\tend"
    };

    /**
     * The class constructor.
     *
     * @throws IOException "WriteDriver.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public WriteDriverCodegen() throws IOException {
        super(WRITE_DRIVER_SV);
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
            if (get(index).contains("WriteGenerator objects declaration"))
                definePackingAddPort(false, ++index, outputs, GENERATOR_DECLARE, GENERATOR_DECLARE_UNPACKED);

            /* Fills in WriteGenerator initialization field. */
            else if (get(index).contains("local function void initGens()"))
                definePackingAddPort(false, ++index, outputs, GENERATOR_INIT, GENERATOR_INIT_UNPACKED);

            /* Fills in WriteGenerator running field. */
            else if (get(index).contains("function void run()"))
                definePackingAddPort(false, ++index, outputs, GENERATOR_RUN, GENERATOR_RUN_UNPACKED);

            /* Adds mismatches logging. */
            else if (get(index).contains("gen_log.writeStr") && get(index).contains("Mismatches:"))
                definePackingAddPort(false, ++index, outputs, LOG_ERRORS, LOG_ERRORS_UNPACKED);

            /* Adds closing of generator objects. */
            else if (get(index).contains("function void close()"))
                definePackingAddPort(false, ++index, outputs, GENERATOR_CLOSE, GENERATOR_CLOSE_UNPACKED);
        }
    }
}
