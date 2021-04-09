//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	WriteDriver					        												//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Connects the output ports of the DUT with the WriteGenerator objects                //
//                      through the specified interface.                                                    //
//																											//
//	Start design	:	00.00.0000																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


`include "./WriteGenerator.sv"


class WriteDriver #(
    parameter PARAMETER
);


    /*
        Instance properties.
    */
    // The interface is based on the port list of the DUT.
    virtual Interface iface;

    // The path to the files, that will be created or overwritten during the simulation.
    local string filePath;

    // The file descriptor.
    local int fd;

    // The WriteGenerator objects that are driven by the driver class.
    WriteGenerator #(DATA_WIDTH) gen_port_name [PORTS_NUM];


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
    local task initGens();
    endtask


    /*
        Starts to write a data to the resulting file. 
    */
    function run();
    endfunction


endclass