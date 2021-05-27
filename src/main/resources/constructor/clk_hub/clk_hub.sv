//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	clk_hub																				        //
//	Author(-s)		:	Alex Shepel											 								        //
//	Company			:	Radiy																				        //
//																											        //
// 	Description		:	Creates the clock signals of different frequencies.									        //
//																											        //
//						-- Variable parameters:																        //
//							@parameter	RATIO_2, RATIO_5, RATIO_10	Ratio* between input and output frequencies		//
//															        of clk_divider module. 							//
//																											        //
//						Constant parameters:																        //
//							@localparam	--																	        //
//																											        //
//						  * In order to optimize the algorithm, the value should lie within [0 : 10].		        //
//																											        //
//	Start design	:	30.06.2020																			        //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


module clk_hub (
	input  logic clk_50MHz,
	output logic clk_10MHz, clk_5MHz, clk_1MHz, clk_100kHz, clk_10kHz, clk_1kHz, clk_100Hz, clk_10Hz, clk_1Hz
);
    
	localparam RATIO_2 = 2;
	localparam RATIO_5 = 5;
	localparam RATIO_10 = 10;

	/*
		The submodules connection.
	*/
    clk_divider #(RATIO_5)  transform_50MHz_to_10MHz  	(.clk_HF(clk_50MHz),	.clk_LF(clk_10MHz));
    clk_divider #(RATIO_2)  transform_10MHz_to_5MHz  	(.clk_HF(clk_10MHz),	.clk_LF(clk_5MHz));
	clk_divider #(RATIO_10) transform_5MHz_to_1MHz  	(.clk_HF(clk_5MHz),		.clk_LF(clk_1MHz));
	clk_divider #(RATIO_10) transform_1MHz_to_100kHz  	(.clk_HF(clk_1MHz), 	.clk_LF(clk_100kHz));
	clk_divider #(RATIO_10) transform_100kHz_to_10kHz 	(.clk_HF(clk_100kHz),	.clk_LF(clk_10kHz));
	clk_divider #(RATIO_10) transform_10kHz_to_1kHz   	(.clk_HF(clk_10kHz), 	.clk_LF(clk_1kHz));
	clk_divider #(RATIO_10) transform_1kHz_to_100Hz   	(.clk_HF(clk_1kHz), 	.clk_LF(clk_100Hz));
	clk_divider #(RATIO_10) transform_100Hz_to_10Hz   	(.clk_HF(clk_100Hz), 	.clk_LF(clk_10Hz));
	clk_divider #(RATIO_10) transform_10Hz_to_1Hz 	   	(.clk_HF(clk_10Hz), 	.clk_LF(clk_1Hz));


endmodule