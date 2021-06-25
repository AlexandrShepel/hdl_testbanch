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

    // The WriteGenerator objects declaration. They are driven by the WriteDriver class.

    // Writes log file.
    WriteGenerator gen_log;


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
    endfunction


    /*
        Starts to write a data to the resulting file. 
    */
    function void run();
    endfunction


    /*
        Saves testbench results to the log.txt file.
    */
    function void createLog(string filePath, int vectorSize);
        gen_log = new();
        gen_log.open({$sformatf("%s", filePath), "/log.txt"}, "a");

        if (iface.test_passed)
            gen_log.write("PASS");
        else
            gen_log.write("FAIL");

        gen_log.write($sformatf("\t\tDate: %s", getTime()));
        gen_log.write($sformatf("\t\tSamples: %0d", vectorSize));
        gen_log.write("\t\tMismatches:");

        gen_log.write("");
    endfunction


    /*
        Returns the system time and date.
    */
    local function string getTime();
        int temp_fd;
        string localdate;
        string localtime;

        void'($system("date /t > temp.txt"));
        void'($system("time /t >> temp.txt"));

        temp_fd = $fopen("temp.txt", "r");
        void'($fscanf(fd, "%s", localdate));
        void'($fscanf(fd, "%s", localtime));

        $fclose(temp_fd);
        void'($system("del time.txt"));

        return {localtime, " ", localdate};
    endfunction


    /*
        Closes writing streams.
    */
    function void close();
    endfunction


endclass