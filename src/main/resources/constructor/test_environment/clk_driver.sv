//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	clk_driver	       										                            //
//	Author(-s)		:	Alex Shepel								 	 			                            //
//	Company			:	Radiy													                            //
//                                                                                                          //
// 	Description		:	Creates clocks that drive simulation.        										//
//                                                                                                          //
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


module clk_driver #(
    DUT_CLK_FREQ = 50000,
    SAMPLE_FREQ = 50000 // in kHz
)(
    input bit enable,
    output bit dut_clk,
    output bit reading_clk, writing_clk
);
    

    /*  
        Creates different frequency clock signals.
    */
    // Creates clock that drives reading from input file.
    clk_generator #(
        .FREQ       (SAMPLE_FREQ),
        .PHASE      (0),
        .DUTY       (50)
    ) reading_clk_gen (
        .enable     (enable),
        .clk        (reading_clk)
    );

    // Place for generating of DUT clocks.

    // Creates clock that drives writing to the file and console.
    clk_generator #(
        .FREQ       (SAMPLE_FREQ),
        .PHASE      (300),
        .DUTY       (50)
    ) writing_clk_gen (
        .enable     (enable),
        .clk        (writing_clk)
    );


endmodule