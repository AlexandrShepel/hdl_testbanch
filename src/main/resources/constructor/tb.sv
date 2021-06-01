//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	tb	 		       										                            //
//	Author(-s)		:	Alex Shepel								 	 			                            //
//	Company			:	Radiy													                            //
//                                                                                                          //
// 	Description		:	The testbench of the design_under_test module.										//
//                                                                                                          //
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


`include "classes/Interface.sv"
`include "classes/ReadDriver.sv"
`include "classes/WriteDriver.sv"
`include "classes/Monitor.sv"

`timescale 1ns/1ps


module tb ();
    

    /*
        Instance properties.
    */
	// The interface is based on the ports and parameters lists of the DUT.
    Interface iface();

    // Path to the files that will be read.
	const string READ_FILES = "./readFolder/";

    // Path to the files that will be written.
	const string WRITE_FILES = "./writeFolder/";

    // The objects of the test environment.
    ReadDriver #(
        .PARAMETER (iface.PARAMETER)
    ) readDriver;
    WriteDriver #(
        .PARAMETER (iface.PARAMETER)
    ) writeDriver;
    Monitor #(
        .PARAMETER (iface.PARAMETER)
    ) monitor;

    // Sets the time of initialization for the simulation clocks.
    localparam CLK_INIT_TIME = 0.5;

    // Enables clocking.
    bit clk_enable = 0;


    /*
        Creates clocks that drive simulation.
    */
    clk_driver #(
        .DUT_CLK_FREQ (iface.DUT_CLK_FREQ),
        .SAMPLE_FREQ (iface.SAMPLE_FREQ)
    ) clk_driver (
        .enable             (clk_enable),
        .dut_clk            (iface.clk),
        .reading_clk        (iface.reading_clk),
        .writing_clk        (iface.writing_clk)
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
        monitor = new(iface);
        
        #CLK_INIT_TIME
        clk_enable = 1;
    end


    /*
        Runs reading data from the file.
    */
    always @(posedge iface.reading_clk) begin
        readDriver.run();   
    end


    /*
        Runs data logging (console and file).
    */
    always @(posedge iface.writing_clk) begin
        writeDriver.run();
        monitor.print(); 
    end


endmodule
