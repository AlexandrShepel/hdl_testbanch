//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	tb	 		       										                            //
//	Author(-s)		:	Alex Shepel								 	 			                            //
//	Company			:	Radiy													                            //
//                                                                                                          //
// 	Description		:	The testbench of the design_under_test module.										//
//                                                                                                          //
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


`include "Interface.sv"
`include "ReadDriver.sv"
`include "WriteDriver.sv"
`include "Checker.sv"

`timescale 1ns/1ps


module tb ();
    

    /*
        Instance properties.
    */
	// The interface is based on the ports and parameters lists of the DUT.
    Interface iface();

    // Path to the folder with files that will be read.
    const string PROJECT_PATH = "<project_path>";

    // Path to the folder with files that will be read.
	const string READ_FILES = {PROJECT_PATH, "/<input_data_folder>"};

    // Path to the folder with files that will be written.
	const string WRITE_FILES = {PROJECT_PATH, "/<output_data_folder>"};

    // The objects of the test environment.
    ReadDriver #(
        .PARAMETER (iface.PARAMETER)
    ) readDriver;

    WriteDriver #(
        .PARAMETER (iface.PARAMETER)
    ) writeDriver;

    Checker #(
        .PARAMETER (iface.PARAMETER)
    ) outChecker;

    // Sets the time of initialization for the simulation clocks.
    localparam CLK_INIT_TIME = 0.5;

    // Sets the period of checking mismatches between actual and expected data (ns).
    localparam CHECK_PERIOD = 0.001;

    // Enables clocking.
    bit clk_enable = 0;


    /*
        Creates clocks that drive simulation.
    */
    clk_driver #(
        .DUT_CLK_FREQ (iface.DUT_CLK_FREQ),
        .SAMPLE_FREQ (iface.SAMPLE_FREQ)
    ) clk_driver (
        .enable (clk_enable),
        .dut_clk (iface.clk),
        .reading_clk (iface.reading_clk),
        .writing_clk (iface.writing_clk)
    );


	/*
        The design under test.
    */
    design_under_test #(
        .PARAMETER (iface.PARAMETER)
	) dut (
		// inputs

		// outputs
	);


    /*
        Initializes the test environment.
    */
    initial begin
        readDriver = new(iface, READ_FILES);
        writeDriver = new(iface, WRITE_FILES);
        outChecker = new(iface);

        #CLK_INIT_TIME
        clk_enable = 1;
    end


    /*
        Checks mismatches between actual and expected data.
    */
    always begin
        #CHECK_PERIOD outChecker.mismatch();
    end


    /*
        Runs reading data from the file.
        Checks the end of the test vector.
        When the last test point is simulated:
            -- displays the simulation log
            -- stops simulation.
    */
    always @(posedge iface.reading_clk) begin
        if (readDriver.isEnding()) begin
            createLogs();
            stopSimulation();
        end else
            readDriver.run();
    end


    /*
        Creates logs.
    */
    task createLogs();
        $display("\n********* THE SIMULATION END **********");

        if (iface.test_passed)
            $display("*********** TEST CASE PASS ************\n");
        else
            $display("*********** TEST CASE FAIL ************\n");

        writeDriver.createLog(PROJECT_PATH, readDriver.getSize());
    endtask


    /*
        Stops simulation.
    */
    task stopSimulation();
        writeDriver.close();
        $stop();
    endtask


    /*
        Runs data logging (console and file).
    */
    always @(posedge iface.writing_clk) begin
        writeDriver.run();
        outChecker.countError();
    end


endmodule
