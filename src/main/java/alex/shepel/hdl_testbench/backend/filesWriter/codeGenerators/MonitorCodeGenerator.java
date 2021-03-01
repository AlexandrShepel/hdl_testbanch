package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.IOException;
import java.util.*;

/*
 * File: MonitorCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.MONITOR_SV" file
 * based on specified data.
 */
public class MonitorCodeGenerator extends CodeGenerator {

    /* The template of code.
    Used when packed and unpacked sizes of monitored port equals to 0. */
    private static final String[] MONITOR_0PACKED_0UNPACKED_SIZES = {
            "\t\tif (iface.port_name === 1'bx) begin",
            "\t\t    $display (\"WARNING: port_name = %0h\", iface.port_name);",
            "\t\t    isIncorrect = 1;",
            "\t\tend else begin",
            "\t\t    $display (\"         port_name = %0h\", iface.port_name);",
            "\t\tend"
    };

    /* The template of code.
    Used when packed size of monitored port is larger then 0
    and unpacked size equals to 0. */
    private static final String[] MONITOR_0UNPACKED_SIZE = {
            "\t\tif (iface.port_name[0] === 1'bx) begin",
            "\t\t    $display (\"WARNING: port_name = %0h\", iface.port_name);",
            "\t\t    isIncorrect = 1;",
            "\t\tend else begin",
            "\t\t    $display (\"         port_name = %0h\", iface.port_name);",
            "\t\tend"
    };

    /* The template of code.
    Used when packed size of monitored port equals to 0
    and unpacked size is larger then 0. */
    private static final String[] MONITOR_0PACKED_SIZE = {
            "\t\tfor (integer i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    if (iface.port_name[i] === 1'bx) begin",
            "\t\t        $display (\"WARNING: port_name[%0d] = %0h\", i, iface.port_name[i]);",
            "\t\t        isIncorrect = 1;",
            "\t\t    end else begin",
            "\t\t        $display (\"         port_name[%0d] = %0h\", i, iface.port_name[i]);",
            "\t\t    end",
            "\t\tend"
    };

    /* The template of code.
    Used when packed and unpacked sizes of monitored port are larger then 0. */
    private static final String[] DEFAULT_MONITOR = {
            "\t\tfor (integer i = 0; i <= PARAMETER - 1; i++) begin",
            "\t\t    if (iface.port_name[i][0] === 1'bx) begin",
            "\t\t        $display (\"WARNING: port_name[%0d] = %0h\", i, iface.port_name[i]);",
            "\t\t        isIncorrect = 1;",
            "\t\t    end else begin",
            "\t\t        $display (\"         port_name[%0d] = %0h\", i, iface.port_name[i]);",
            "\t\t    end",
            "\t\tend"
    };

    /**
     * The class constructor.
     *
     * @throws IOException "Monitor.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public MonitorCodeGenerator() throws IOException {
        parseFile(BackendParameters.MONITOR_SV);
        setDate();
    }

    /**
     * Overwrites field in the file
     * where must be placed code
     * for monitoring each of DUT's port.
     *
     * @param ports The HashMap object that contains DUT's ports.
     *              Key contain a port name.
     *              Value contain PortDescriptor object.
     */
    public void addPorts(HashMap<String, PortDescriptor> ports) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("local function bit setPortsMonitors()")) {
                /* Goes to the line, where monitors declaration begins. */
                index = index + 2;

                /* Adds a monitor for each DUT input. */
                for (String name: ports.keySet()) {
                    if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                        if (ports.get(name).getPackedSize().equals("")
                                && ports.get(name).getUnpackedSize().equals(""))
                            add0Packed0UnpackedSizesMonitor(index, ports.get(name));

                        else if (ports.get(name).getPackedSize().equals(""))
                            add0PackedSizeMonitor(index, ports.get(name));

                        else if (ports.get(name).getUnpackedSize().equals(""))
                            add0UnpackedSizeMonitor(index, ports.get(name));

                        else addDefaultMonitor(index, ports.get(name));
                    }
                }

                /* Breaks the method running as far as all monitors are declared. */
                break;
            }
        }
    }

    /**
     * Adds a code lines for monitoring a port.
     * Used when packed and unpacked sizes of monitored port equals to 0.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param port The port that must be monitored.
     */
    private void add0Packed0UnpackedSizesMonitor(int index, PortDescriptor port) {
        for (int lineNum = MONITOR_0PACKED_0UNPACKED_SIZES.length - 1; lineNum >= 0; lineNum--) {
            add(index, MONITOR_0PACKED_0UNPACKED_SIZES[lineNum].replace("port_name", port.getName()));
        }

        add(index, "\t\t// Port: " + port.toString());
        add(index, "");
    }

    /**
     * Adds a code lines for monitoring a port.
     * Used when packed size of monitored port is larger then 0
     * and unpacked size equals to 0.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param port The port that must be monitored.
     */
    private void add0UnpackedSizeMonitor(int index, PortDescriptor port) {
        for (int lineNum = MONITOR_0UNPACKED_SIZE.length - 1; lineNum >= 0; lineNum--) {
            add(index, MONITOR_0UNPACKED_SIZE[lineNum].replace("port_name", port.getName()));
        }

        add(index, "\t\t// Port: " + port.toString());
        add(index, "");
    }

    /**
     * Adds a code lines for monitoring a port.
     * Used when packed size of monitored port equals to 0
     * and unpacked size is larger then 0
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param port The port that must be monitored.
     */
    private void add0PackedSizeMonitor(int index, PortDescriptor port) {
        String unpackedSize = decodeSizeReferencing(port.getUnpackedSize());

        for (int lineNum = MONITOR_0PACKED_SIZE.length - 1; lineNum >= 0; lineNum--) {
            String editedLine = MONITOR_0PACKED_SIZE[lineNum].replace("port_name", port.getName());
            editedLine = editedLine.replace("PARAMETER - 1", unpackedSize);
            add(index, editedLine);
        }

        add(index, "\t\t// Port: " + port.toString());
        add(index, "");
    }

    /**
     * Adds a code lines for monitoring a port.
     * Used when packed and unpacked sizes of monitored port are larger then 0.
     *
     * @param index The index of a line in the parsed file
     *              where must be placed current code.
     * @param port The port that must be monitored.
     */
    private void addDefaultMonitor(int index, PortDescriptor port) {
        String unpackedSize = decodeSizeReferencing(port.getUnpackedSize());

        for (int lineNum = DEFAULT_MONITOR.length - 1; lineNum >= 0; lineNum--) {
            String editedLine = DEFAULT_MONITOR[lineNum].replace("port_name", port.getName());
            editedLine = editedLine.replace("PARAMETER - 1", unpackedSize);
            add(index, editedLine);
        }

        add(index, "\t\t// Port: " + port.toString());
        add(index, "");
    }
}
