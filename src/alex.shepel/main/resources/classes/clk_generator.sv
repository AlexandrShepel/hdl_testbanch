//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	clk_generator	       										                        //
//	Author(-s)		:	Alex Shepel								 	 			                            //
//	Company			:	Radiy													                            //
//                                                                                                          //
// 	Description		:	Creates clocks that drive simulation.        										//
//                      Src: view-source:https://www.chipverify.com/verilog/verilog-clock-generator         //
//                                                                                                          //
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

`timescale 1ns/1ps

module clk_generator (
    input bit enable,
    output bit clk
);
  
    parameter FREQ = 100000; // in kHZ
    parameter PHASE = 0; 	 // in degrees (in range 0 to 360 deg)
    parameter DUTY = 50;  	 // in percentage 
  
    real clk_pd = 1.0 / (FREQ * 1e3) * 1e9; 	// convert to ns
    real clk_on = DUTY / 100.0 * clk_pd;
    real clk_off = (100.0 - DUTY) / 100.0 * clk_pd;
    real start_dly = clk_pd * PHASE / 360;
  
    reg start_clk;
  
    initial begin   
        $display("FREQ      = %0d kHz", FREQ);
        $display("PHASE     = %0d deg", PHASE);
        $display("DUTY      = %0d %%",  DUTY);
        $display("PERIOD    = %0.3f ns", clk_pd);    
        $display("CLK_ON    = %0.3f ns", clk_on);
        $display("CLK_OFF   = %0.3f ns", clk_off);
        $display("START_DLY = %0.0f ns", start_dly);
        $display(); 
    end
  
    // Initialize variables to zero
    initial begin
        clk <= 0;
        start_clk <= 0;
    end
    
    // When clock is enabled, delay driving the clock to one in order
    // to achieve the phase effect. start_dly is configured to the 
    // correct delay for the configured phase. When enable is 0,
    // allow enough time to complete the current clock period
    always @(posedge enable or negedge enable) begin
        if (enable) begin
            #(start_dly)
            start_clk = enable; 
        end else 
            start_clk = 0;
    end
    
    // Achieve duty cycle by a skewed clock on/off time and let this
    // run as long as the clocks are turned on.
    always @(posedge start_clk) begin
        if (start_clk) begin
        	clk = 1;
        
        	while (start_clk) begin
        		#(clk_on)  clk = 0;
      		    #(clk_off) clk = 1;
            end
        
        	clk = 0;
        end
    end 

endmodule