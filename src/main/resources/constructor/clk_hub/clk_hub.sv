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
	input  logic hub_clk_50MHz,
	output logic hub_clk_10MHz, hub_clk_5MHz, hub_clk_1MHz, hub_clk_100kHz, hub_clk_10kHz, hub_clk_1kHz, hub_clk_100Hz, hub_clk_10Hz, hub_clk_1Hz
);
    
	localparam RATIO_2 = 2;
	localparam RATIO_5 = 5;
	localparam RATIO_10 = 10;

	/*
		The submodules connection.
	*/
    clk_divider #(RATIO_5)  transform_50MHz_to_10MHz  	(.clk_HF(hub_clk_50MHz),	.clk_LF(hub_clk_10MHz));
    clk_divider #(RATIO_2)  transform_10MHz_to_5MHz  	(.clk_HF(hub_clk_10MHz),	.clk_LF(hub_clk_5MHz));
	clk_divider #(RATIO_10) transform_5MHz_to_1MHz  	(.clk_HF(hub_clk_5MHz),		.clk_LF(hub_clk_1MHz));
	clk_divider #(RATIO_10) transform_1MHz_to_100kHz  	(.clk_HF(hub_clk_1MHz), 	.clk_LF(hub_clk_100kHz));
	clk_divider #(RATIO_10) transform_100kHz_to_10kHz 	(.clk_HF(hub_clk_100kHz),	.clk_LF(hub_clk_10kHz));
	clk_divider #(RATIO_10) transform_10kHz_to_1kHz   	(.clk_HF(hub_clk_10kHz), 	.clk_LF(hub_clk_1kHz));
	clk_divider #(RATIO_10) transform_1kHz_to_100Hz   	(.clk_HF(hub_clk_1kHz), 	.clk_LF(hub_clk_100Hz));
	clk_divider #(RATIO_10) transform_100Hz_to_10Hz   	(.clk_HF(hub_clk_100Hz), 	.clk_LF(hub_clk_10Hz));
	clk_divider #(RATIO_10) transform_10Hz_to_1Hz 	   	(.clk_HF(hub_clk_10Hz), 	.clk_LF(hub_clk_1Hz));


endmodule