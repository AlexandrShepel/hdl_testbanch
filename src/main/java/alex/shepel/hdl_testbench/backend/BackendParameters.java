package alex.shepel.hdl_testbench.backend;

/*
 * File: BackendParameters.java
 * ----------------------------------------------
 * Represents an interface that stores constants.
 */
public interface BackendParameters {

    /* Paths of the resource files. */
    String CLK_HUB_SV =
            "constructor/clk_hub/clk_hub.sv";
    String CLK_DIVIDER_SV =
            "constructor/clk_hub/clk_divider.sv";
    String TB_SV =
            "constructor/tb.sv";
    String ENVIRONMENT_SV =
            "constructor/test_environment/Environment.sv";
    String INTERFACE_SV =
            "constructor/test_environment/Interface.sv";
    String MONITOR_SV =
            "constructor/test_environment/Monitor.sv";
    String READ_DRIVER_SV =
            "constructor/test_environment/ReadDriver.sv";
    String WRITE_DRIVER_SV =
            "constructor/test_environment/WriteDriver.sv";
    String READ_GENERATOR_SV =
            "constructor/test_environment/ReadGenerator.sv";
    String WRITE_GENERATOR_SV =
            "constructor/test_environment/WriteGenerator.sv";

}
