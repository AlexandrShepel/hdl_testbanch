package backend;

/*
 * File: BackendParameters.java
 * ----------------------------------------------
 * Represents an interface that stores constants.
 */
public interface BackendParameters {

    /* Paths of the resource files. */
    // modules
    String TB_SV =
            "modules/tb.sv";
    String CLK_DRIVER_SV =
            "modules/clk_driver.sv";
    String CLK_GENERATOR_SV =
            "modules/clk_generator.sv";

    // classes
    String INTERFACE_SV =
            "classes/Interface.sv";
    String CHECKER_SV =
            "classes/Checker.sv";
    String READ_DRIVER_SV =
            "classes/ReadDriver.sv";
    String WRITE_DRIVER_SV =
            "classes/WriteDriver.sv";
    String READ_GENERATOR_SV =
            "classes/ReadGenerator.sv";
    String WRITE_GENERATOR_SV =
            "classes/WriteGenerator.sv";

    // scripts
    String PRJ_MPF =
            "scripts/prj.mpf";

//    /* Default directories that stores created .sv and .mpf files */
//    String DEFAULT_SV_DIR = "sv";
//    String DEFAULT_MPF_DIR = "modelsim";

}
