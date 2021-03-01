package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: InterfaceCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.INTERFACE_SV" file
 * based on specified data.
 */
public class InterfaceCodeGenerator extends CodeGenerator {

    /**
     * The class constructor.
     *
     * @throws IOException "Interface.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public InterfaceCodeGenerator() throws IOException {
        parseFile(BackendParameters.INTERFACE_SV);
        setDate();
    }

    /**
     * Sets available hub clocks that can be connected to the DUT.
     *
     * @param hubClocks The ArrayList object that stores ports
     *                  of the "clk_hub.sv" file.
     */
    public void setHubClocks(ArrayList<String> hubClocks) {
        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\tlogic hub_clocks;")) {
                String editedLine = get(index).replace("hub_clocks;", "");

                for (String clock: hubClocks) {
                    editedLine = editedLine.concat(clock + ", ");
                }

                editedLine = editedLine.substring(0, editedLine.length() - 2) + ";";
                remove(index);
                add(index, editedLine);

                break;
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
            if (get(index).equals("\tlogic dut_inputs;")) {
                remove(index);

                for (PortDescriptor portDescriptor: inputs.values()) {
                    add(index, "\t" + portDescriptor.toString() + ";");
                }

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
            if (get(index).equals("\tlogic dut_outputs;")) {
                remove(index);

                for (PortDescriptor portDescriptor: outputs.values()) {
                    add(index, "\t" + portDescriptor.toString() + ";");
                }

                break;
            }
        }
    }
}
