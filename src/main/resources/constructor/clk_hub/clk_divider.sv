//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	clk_divider																			//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Divides input frequency by specified ratio.											//
//																											//
//						-- Variable parameters:																//
//							@parameter 	RATIO	Ratio* between input and output frequencies of 				//
//												clk_divider module. 										//
//																											//
//						Constant parameters:																//
//							@localparam	--																	//
//																											//
//						  * In order to optimize the algorithm, the value should lie within [0 : 10].		//
//																											//
//	Start design	:	30.06.2020																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


module clk_divider #( 
	// Variable parameters:
	parameter RATIO = 5 
)(
	input logic clk_HF, 
	output logic clk_LF = 0
);


    /*
        The module properties.
    */
    // Registers for the temporary intermediate values.
	logic clk_pos_LF = 0;
	logic clk_neg_LF = 0;

	// Counters of clock edges.
    logic [3:0] pos_cnt = 0;
    logic [3:0] neg_cnt = 0;


    /*
		The structure of the sequential logic.
	*/
	generate
		/*
			Divides input frequency by specified ratio,
			when ratio is EVEN.
		*/
		if (RATIO % 2 == 0) begin
			always_ff @(posedge clk_HF) begin
				pos_cnt ++;
				if ( pos_cnt == RATIO / 2 ) begin
					clk_LF <= !clk_LF;
					pos_cnt <= 0;
				end
			end
		end
		
		
		/*
			Divides input frequency by specified ratio,
			when ratio is ODD.
		*/
		if (RATIO % 2 == 1) begin
			// Divider that works by positive edge of clock.
			always_ff @(posedge clk_HF) begin
				pos_cnt++;
				if (pos_cnt == RATIO / 2) begin
					clk_pos_LF <= 1;
				end
				if (clk_pos_LF && (pos_cnt == RATIO)) begin
					clk_pos_LF <= 0;
					pos_cnt <= 0;
				end
			end

			// Divider that works by negative edge of clock.
			always_ff @(negedge clk_HF) begin
				neg_cnt++;
				if (neg_cnt == RATIO / 2) begin
					clk_neg_LF <= 1;
				end
				if (clk_neg_LF && (neg_cnt == RATIO)) begin
					clk_neg_LF <= 0;
					neg_cnt <= 0;
				end
			end
			
			// Resulting low frequency clock.
			assign clk_LF = clk_pos_LF && clk_neg_LF;		
		end	
	endgenerate


endmodule