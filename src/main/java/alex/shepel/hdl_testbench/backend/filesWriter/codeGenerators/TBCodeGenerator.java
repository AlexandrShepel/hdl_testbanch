package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;
import alex.shepel.hdl_testbench.backend.parser.detectors.PortDescriptor;

import java.io.IOException;
import java.util.HashMap;

/*
 * File: TBCodeGenerator.java
 * -----------------------------------------------
 * Inherits CodeGenerator object.
 * Overwrites "BackendParameters.TB_SV" file
 * based on specified data.
 */
public class TBCodeGenerator extends CodeGenerator {

    /**
     * The class constructor.
     *
     * @throws IOException "Environment.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public TBCodeGenerator() throws IOException {
        parseFile(BackendParameters.TB_SV);
        setDate();
    }

    /**
     * Sets DUT module's input ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param inputs The HashMap object with names
     *               and descriptions of DUT's inputs.
     */
    public void setInputs(HashMap<String, PortDescriptor> inputs) {
        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\t\t// inputs")) {
                for (String name: inputs.keySet()) {
                    String editedLine = "\t\t." + name + " (iface." + name + "),";
                    add(index + 1, editedLine);
                }

                break;
            }
        }
    }

    /**
     * Sets DUT module's output ports HashMap,
     * that contains all names and all descriptions.
     *
     * @param outputs The HashMap object with names
     *              and descriptions of DUT's outputs.
     */
    public void setOutputs(HashMap<String, PortDescriptor> outputs) {
        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\t\t// outputs")) {
                for (String name: outputs.keySet()) {
                    String editedLine = "\t\t." + name + " (iface." + name + "),";
                    add(index + 1, editedLine);
                }

                /* Deletes last comma in the outputs declaration field. */
                int lastDutPortIndex =
                        indexOf("\t\t// outputs") + outputs.keySet().size();
                String lastDutPortLine = get(lastDutPortIndex);
                set(lastDutPortIndex, lastDutPortLine.substring(0, lastDutPortLine.indexOf(",")));

                break;
            }
        }
    }

    /**
     * Sets a name of a DUT module.
     * Name used in the DUT module connection field.
     *
     * @param name The name of a DUT module.
     */
    public void setDutName(String name) {
        for (int index = 0; index < size(); index++) {
            /* Replaces template "design_under_test" name with a new one in a testbench description field. */
            if (get(index).contains("design_under_test module.")) {
                /* Replaces template name with new specified name. */
                String editedLine = get(index).replace(
                        "design_under_test", name.substring(0, name.indexOf(".sv")));

                /* Adds spaces to the end of the line. */
                int tabsNum = ("design_under_test".length() - name.length()) >> 2;
                for (int cnt = 0; cnt <= tabsNum; cnt++) {
                    editedLine = editedLine.replace("module.", "module.\t");
                }

                /* Replace old line with a new one. */
                set(index, editedLine);

                continue;
            }

            /* Replaces template "design_under_test" name with a new one in DUT declaration field. */
            if (get(index).contains("design_under_test #(")) {
                String editedLine = get(index).replace(
                        "design_under_test", name.substring(0, name.indexOf(".sv")));
                set(index, editedLine);

                break;
            }
        }
    }

    /**
     * Adds clocks to the clocks correspondence declaration filed.
     *
     * @param clocksHashMap The HashMap object that contains correspondence
     *                      between DUT's and clk_hub's modules.
     */
    public void setDutClocksPorts(HashMap<String, String> clocksHashMap) {
        for (int index = 0; index < size(); index++) {
            if (get(index).equals("\t\tiface.dut_clk = iface.hub_clk;")) {
                remove(index);

                for (String name: clocksHashMap.keySet()) {
                    String editedLine = "\t\tiface." + name + " = iface." + clocksHashMap.get(name) + ";";
                    add(index, editedLine);
                }

                break;
            }
        }
    }

    /**
     * Sets a sampling frequency of the created test environment.
     * Simulation points will be written to the report files
     * every tact of this frequency.
     *
     * @param samplingClock Sampling frequency that is used
     *                                in the test environment for writing
     *                                output data to the report file.
     */
    public void setReportSamplingFrequency(String samplingClock) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("iface.sampling_clk")) {
                String ongoingLine = get(index);
                int clkNameIndex = ongoingLine.indexOf("sampling_clk");
                String editedLine =
                        ongoingLine.substring(0, clkNameIndex) +
                        samplingClock +
                        ongoingLine.substring(clkNameIndex + "sampling_clk".length());
                set(index, editedLine);
            }
        }
    }
}
