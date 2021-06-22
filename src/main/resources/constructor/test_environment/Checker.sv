//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	Checker					        													//
//	Author(-s)		:	Alex Shepel, Max Kubkin										     					//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Checks the data output.                                                             //
//																											//
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


class Checker #(
	parameter PARAMETER
);


    /*
        Instance properties.
    */
    // The interface is based on the port list of the DUT.
    virtual Interface iface;


    /*
        The class constructor.
    */
    function new(virtual Interface iface);
        this.iface = iface;
        iface.port_name_errors = 0;
    endfunction


    /*
        Checks actual and expected DUT output data
    */
    function void mismatch();
    endfunction


    /*
        Counts mismatches between actual and expected data.
    */
    function void countError();
    endfunction


    /*
        Displays the total count of mismatches for each port.
    */
    function void displayMismatches();
        $display("Total mismatches:");
    endfunction


endclass
