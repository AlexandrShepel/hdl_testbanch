//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	Monitor					        													//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Prints the data that stores in the interface to the console.                        //
//																											//
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


class Monitor;


    /*
        Instance properties.
    */
    // Parameters that are copied from the DUT.
    localparam PARAMETER = 0000;

    // The interface is based on the port list of the DUT.
    virtual Interface iface;


    /* 
        The class constructor.
    */ 
    function new(virtual Interface iface);
        this.iface = iface;
    endfunction


    /* 
        Prints contents of the interface data to the console.
    */
    function void print();
        // Boolean value. Uses to stop the simulation when incorrect value is found.
        bit isIncorrect = 0;

        // Separating line
        $display ("");
        $display ("*** Monitor beginning ***");
        $display ("***         |         ***");
        $display ("***         v         ***");
        $display ("");

        // Simulation time
        $display ("Simulation time: %0t", $time);

		// Ports monitors declaration
		isIncorrect = setPortsMonitors();

        // Separating line
        $display ("");
        $display ("***         ^        ***");
        $display ("***         |        ***");
        $display ("***  Monitor ending  ***");
        $display ("");

        // Stops if undefined value is found.
        if (isIncorrect == 1) begin
            $display ("WARNING: Undefined value is found.");
            $display ("         Check ongoing monitored values.");
        end
    endfunction


	/*
		Sets ports monitors.
	*/
	local function bit setPortsMonitors();
		bit isIncorrect = 0;

		return isIncorrect;
	endfunction


endclass