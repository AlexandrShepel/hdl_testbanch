//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	ReadDriver					       													//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Connects the generators to the DUT through the interface. Drives the generators.    //
//																											//
//	Start design	:	00.00.0000																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


`include "./ReadGenerator.sv"


class ReadDriver #(
    parameter PARAMETER
);


    /*
        Instance properties.
    */
    // The path to the file, that contains the testing input data.
    local string filePath;

    // The interface is based on the port list of the DUT.
    virtual Interface iface;

    // The ReadGenerator objects that are driven by the ReadDriver class.
    ReadGenerator #(DATA_WIDTH) gen_port_name [PORTS_NUM];


    /* 
        The class constructor.
    */ 
    function new(virtual Interface iface, string filePath);
        this.iface = iface;
        this.filePath = filePath;
        initGens();
    endfunction


    /* 
        Initializes the generators. 
    */
    local function void initGens();

        isOneSize();
    endfunction


    /*
        Starts to assign a data to the DUT ports. 
    */
    function void run();
    endfunction


    /*
        Checks the end of the test vector.
    */
    function bit isEnding();
        return gen_port_name.getIndex() >= gen_port_name.getSize() - 1;
    endfunction


    /*
        Returns the size of the test vector.
    */
    function int getSize();
        return gen_port_name.getSize();
    endfunction


    /*
        Checks that generators contains equal number of the simulation points.
    */
    local function void checkSize();
        bit isTrue = 1;
        
        if (!isTrue) begin
            $display ("WARNING: Input data vectors have unequal sizes.");
            $display ("         Check spaces in the source file.");
            $display ("         Notice that empty strings are read as zeros by default.");
            $stop();
        end
    endfunction


endclass