package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parsers.detectors.PortDescriptor;

import java.io.IOException;
import java.util.HashMap;

/*
 * File: InterfaceCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.INTERFACE_SV" file
 * based on specified data.
 */
public class InterfaceCodegen extends Codegen {

    /**
     * The class constructor.
     *
     * @throws IOException "Interface.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public InterfaceCodegen() throws IOException {
        parseFile(BackendParameters.INTERFACE_SV);
        setDate();
    }

    /**
     * Sets clocks that must be connected to the DUT.
     *
     * @param clocksHashMap The ArrayList object that stores ports
     *                  of the "clk_driver.sv" file.
     */
    @SuppressWarnings("SuspiciousListRemoveInLoop")
    public void setClocks(HashMap<String, String> clocksHashMap) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("localparam DUT_CLK_FREQ")) {
                remove(index);

                for (String name : clocksHashMap.keySet()) {
                    String editedLine =
                            "\tlocalparam DUT_" + name.toUpperCase() + "_FREQ = " + clocksHashMap.get(name) + ";";
                    add(index, editedLine);
                }
            }
        }
    }

    /**
     * Sets names of DUT's input ports.
     *
     * @param inputs The HashMap object that contains DUT's inputs.
     *               Key contain a port name.
     *               Value contain PortDescriptor object.
     */
    public void setDutInputs(HashMap<String, PortDescriptor> inputs) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("DUT inputs")) {
                for (PortDescriptor portDescriptor: inputs.values())
                    add(++index, "\t" + portDescriptor + ";");

                break;
            }
        }
    }

    /**
     * Sets names of DUT's output ports.
     *
     * @param outputs The HashMap object that contains DUT's outputs.
     *                Key contain a port name.
     *                Value contain PortDescriptor object.
     */
    public void setDutOutputs(HashMap<String, PortDescriptor> outputs) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("DUT outputs"))
                for (PortDescriptor desc : outputs.values())
                    add(++index, "\t" + desc + ";");

            else if (get(index).contains("TB internal signals"))
                for (PortDescriptor desc : outputs.values()) {
                    final String expectName = desc.getName() + "_expect";
                    add(++index, "\t" + desc.toString().replace(desc.getName(), expectName) + ";");
                    add(++index, "\t" + toExtraPort(desc, "mismatch") + ";");
                    add(++index, "\t" + toExtraPort(desc, "errors") + ";");
                }
        }
    }

    private String toExtraPort(PortDescriptor desc, String suffix) {
        StringBuilder result = new StringBuilder(
                suffix.equals("errors") ? "int" : "bit");

        result.append(" ").append(desc.getName()).append("_").append(suffix);

        if (!desc.getUnpackedSize().equals(""))
            result.append(" ").append(desc.getUnpackedSize());

        return result.toString();
    }

    public void setSampleFreq(String freq) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("localparam SAMPLE_FREQ")) {
                String editedLine = get(index).replace("0000", freq);
                set(index, editedLine);

                break;
            }
        }
    }
}
