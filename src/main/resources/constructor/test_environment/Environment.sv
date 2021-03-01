//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	Environment			        				    									//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Stores the objects of the test environment. Connects them to the interface.         //
//																											//
//	Start design	:	00.00.0000												                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


`include "ReadDriver.sv"
`include "WriteDriver.sv"
`include "Monitor.sv"
`include "Interface.sv"


class Environment;


    /*
        Instance properties.
    */
    // Path to the files that will be read.
	const string READ_FILES = "./readFolder/";

    // Path to the files that will be written.
	const string WRITE_FILES = "./writeFolder/";

    // The interface is based on the port list of the DUT.
    virtual Interface iface;

    // The objects of the test environment.
    ReadDriver r;
    WriteDriver w;
    Monitor m;


    /* 
        The class constructor.
    */
    function new(virtual Interface iface);
        this.iface = iface;
        this.r = new(iface, READ_FILES);
        this.w = new(iface, WRITE_FILES);
        this.m  = new(iface);
    endfunction 


    /*
        Runs the objects of the test environment.
    */
    function run();
        r.run();
        w.run();
        m.print();
    endfunction


endclass