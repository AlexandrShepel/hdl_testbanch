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

	// The clock signals that is available in the testbench.
	logic hub_clocks;

	// DUT inputs:
	logic dut_clocks;
	logic dut_inputs;

	// DUT outputs:
	logic dut_outputs;


endinterface