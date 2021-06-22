package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.BackendParameters;

import java.io.IOException;
import java.util.HashMap;

public class ClockDriverCodegen extends Codegen {

    /* The template of code.
    Used when packed and unpacked sizes of monitored port equals to 0. */
    private static final String[] CLK_GEN_INSTANCE = {
        "\tclk_generator #(",
        "\t\t.FREQ       (DUT_CLK_FREQ),",
        "\t\t.PHASE      (0),",
        "\t\t.DUTY       (50)",
        "\t) dut_clk_gen (",
        "\t\t.enable     (enable),",
        "\t\t.clk        (dut_clk)",
        "\t);",
        ""
    };

    /**
     * The class constructor.
     *
     * @throws IOException "clk_driver.sv" file can't be read
     *                     (file stores in the resource directory).
     */
    public ClockDriverCodegen() throws IOException {
        parseFile(BackendParameters.CLK_DRIVER_SV);
        setDate();
    }

    public void setDutClocks(HashMap<String, String> clocksHashMap) {
        setLocalParameters(clocksHashMap);
        setClockOutputs(clocksHashMap);
        connectClockGenerators(clocksHashMap);
    }

    private void setClockOutputs(HashMap<String, String> clocksHashMap) {
        int editLinesIndex = 0;

        while (!get(editLinesIndex).contains("output bit dut_clk"))
            editLinesIndex++;

        remove(editLinesIndex);

        for (String name : clocksHashMap.keySet()) {
            String editedLine = "\toutput bit dut_clk,";
            editedLine = editedLine.replace("dut_clk", name);
            add(editLinesIndex, editedLine);
        }
    }

    private void connectClockGenerators(HashMap<String, String> clocksHashMap) {
        int editLinesIndex = 0;

        while (!get(editLinesIndex).contains("Place for generating of DUT clocks."))
            editLinesIndex++;

        remove(editLinesIndex);

        for (String name : clocksHashMap.keySet()) {
            for (String line : CLK_GEN_INSTANCE) {
                String editedLine = line;

                if (line.contains("DUT_CLK_FREQ")) {
                    editedLine = line.replace("CLK", name.toUpperCase());
                } else if (line.contains("dut_clk")) {
                    editedLine = line.replace("dut_clk", name);
                }

                add(editLinesIndex++, editedLine);
            }
        }
    }

    @SuppressWarnings("SuspiciousListRemoveInLoop")
    private void setLocalParameters(HashMap<String, String> clocksHashMap) {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("DUT_CLK_FREQ = ")) {
                remove(index);

                for (String name : clocksHashMap.keySet()) {
                    String editedLine =
                            "\tDUT_" + name.toUpperCase() + "_FREQ = " + clocksHashMap.get(name) + ",";
                    add(index, editedLine);
                }
            }
        }
    }

}
