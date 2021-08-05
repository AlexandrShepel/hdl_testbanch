//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	Iface         																		//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Stores the values that transfers through the testbench system.                      //
//                      Allows to exchange the data between objects and design modules.                     //
//																											//
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


interface Interface;


	// Parameters that are copied from the DUT.
    localparam PARAMETER = 0000;

    // Simulation clocks configuration.
    localparam SAMPLE_FREQ = 0000;
    localparam DUT_CLK_FREQ = 0000;

	// Simulation clocks.
	bit reading_clk;
	bit writing_clk;

	// DUT inputs:

	// DUT outputs:

    // TB internal signals:

    bit test_passed = 1;

endinterface
