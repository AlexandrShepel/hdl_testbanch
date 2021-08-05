//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Author      	:	Alex Shepel											 								//
//	Company			:	RPC Radiy																			//
//																											//
// 	Description		:	Provides smoke testing of the application.                                          //
//                      Connects input ports to the output ports.										    //
//																											//
//						-- Parameters:																		//
//						@param DATA_WIDTH  			    The width of the data.						        //
//						@param BUS_WIDTH 			    The width of the data bus.							//
//																											//
//	Start design	:	05.02.1997																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


module DUT #(	
    // Parameters:
    DATA_WIDTH = 4,
    BUS_WIDTH = 2
)(
    input logic clk1, clk2,
    input logic in1,
    input logic in2 [BUS_WIDTH - 1 : 0],
    input logic signed [DATA_WIDTH - 1 : 0] in3,
    input logic signed [DATA_WIDTH - 1 : 0] in4 [BUS_WIDTH - 1 : 0],

    output logic out1,
    output logic out2 [BUS_WIDTH - 1 : 0],
    output logic signed [DATA_WIDTH - 1 : 0] out3,
    output logic signed [DATA_WIDTH - 1 : 0] out4 [BUS_WIDTH - 1 : 0]
);


    assign out1 = in1;
    assign out2 = in2;
    assign out3 = in3;
    assign out4 = in4;


endmodule