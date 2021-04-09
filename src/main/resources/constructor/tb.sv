//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	tb	 		       										                            //
//	Author(-s)		:	Alex Shepel								 	 			                            //
//	Company			:	Radiy													                            //
//                                                                                                          //
// 	Description		:	The testbench of the design_under_test module.										//
//                                                                                                          //
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


`include "classes/Environment.sv"
`include "classes/Interface.sv"


module tb ();
    

    /*
        Instance properties.
    */
	// The interface is based on the ports and parameters lists of the DUT.
    Interface iface();

    // The testing environment object.
    Environment #(
        .PARAMETER (iface.PARAMETER)
    ) env;

    // The fundamental frequency clock and its default period.
    bit clk;
    localparam CLK_PERIOD = 20;


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
        Creates different frequency clock signals.
        Fundamental frequency is represented by frequency of input signal.
        Output signals are created by dividing of input signal frequency.
    */
	clk_hub #()	clk_hub (
        // inputs
        .clk_50MHz              (iface.clk_50MHz),

        // outputs
        .clk_5MHz               (iface.clk_5MHz),
        .clk_1MHz               (iface.clk_1MHz),
        .clk_100kHz             (iface.clk_100kHz),
        .clk_10kHz              (iface.clk_10kHz),
        .clk_1kHz               (iface.clk_1kHz),
        .clk_100Hz              (iface.clk_100Hz),
        .clk_10Hz               (iface.clk_10Hz),
        .clk_1Hz                (iface.clk_1Hz)
    );


    /*
        Creates fundamental high frequency clock.
    */
    always begin
        #(CLK_PERIOD / 2)    	
        clk = ~clk;
    end


    /*
        Connects the fundamental clock signal to the clock hub.
    */
    always begin
        #(CLK_PERIOD / 2)
        iface.clk_50MHz = clk;
    end


    /*
        Creates the correspondence between created clock signals and DUT ports.
    */
    always begin
		#(CLK_PERIOD / 2)    
		iface.dut_clk = iface.hub_clk;
	end


    /*
        Initializes the Environment object.
    */
    initial begin
        env = new(iface);
    end


    /*
        Drives the testing environment.
    */
    always @(posedge iface.sampling_clk) begin
        if (iface.sampling_clk) begin
            env.run();
        end 
    end


endmodule