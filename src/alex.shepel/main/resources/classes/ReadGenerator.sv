//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	ReadGenerator																		//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Reads the specified testing file. Creates the vector from read values.              //
//                      Gives access to vector values by its indexes.           							//
//                                                                                                          //
//                      Testing file must have the format of column of hexadecimal values                   //
//                      that follow each other. For example, the sequence of six 4'h values:                //
//                          ADE3															    			//
//                          1234                                                                            //
//                          FFFF                                                                            //
//                          FA99                                                                            //
//                          0000                                                                            //
//                          1234                                                                            //
//																											//
//						-- Variable parameters: 															//
//						@param DATA_WIDTH  			    The width of the each value of the testing vector.  //
//                                                      It must be copied from DUT's parameters             //
//                                                      description.                                        //
//																											//
//						-- Constant parameters:																//
//						@const --                                               							//
//																											//
//	Start design	:	00.00.0000																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


class ReadGenerator #(
    // The width of the each value of the testing vector.
    parameter DATA_WIDTH = 16
);


    /*
        Instance properties.
    */
    // The path to the file, that contains the testing input data.
    local string filePath;

    // The file descriptor.
    local int fd;

    // The vector, that stores a testing values.
    // They are read from file with testing data.
    local logic signed [DATA_WIDTH - 1 : 0] vector [];

    // The current index of the testing vector point.
    local int index;

    // Provides access to the TB signals.
    local virtual Interface iface;

    /*
        The class constructor.
    */
    function new(virtual Interface iface);
        this.iface = iface;
        index = 0;
    endfunction


    /*
        Opens the file, that is located at a specified address.
        Reads the test input vector from the file.
    */
    function void open(string filePath);
        integer readFaults, scanFaults;
        string fileLine;    // The store box for the line of the file, that will be read below.
        this.filePath = filePath;
        fd = $fopen(filePath, "r");
        
        if (fd) begin
            $display("File was opened successfully: %s\n", filePath);

            while (!$feof(fd)) begin
                vector = new [vector.size() + 1] (vector);
                readFaults = $fgets(fileLine, fd);
                scanFaults = $sscanf(fileLine, "%h", vector[vector.size() - 1]);
                checkReadingScanning(readFaults, scanFaults);
            end

            $fclose(fd);
<<<<<<< Updated upstream:src/main/resources/constructor/test_environment/ReadGenerator.sv
        end
    endfunction


    /*
        Checks reading and scanning operations for the success.
        Prints warnings to the console when fail is found.
    */
    local function void checkReadingScanning(integer readFaults, integer scanFaults);
        if (readFaults == 0) begin
            $display("** Warning: Can't read file: %s\n", filePath);
            iface.test_passed = 0;
        end

        if (scanFaults == 0) begin
            $display("** Warning: Can't interpret input data: %s", filePath);
=======
        end else
            iface.test_passed = 0;
    endfunction


    /*
        Checks reading and scanning operations for the success.
        Prints warnings to the console when fail is found.
    */
    local function void checkReadingScanning(integer readFaults, integer scanFaults);
        if (scanFaults == 0 | readFaults == 0) begin
            $display("** Warning: Can't read/scan file: %s\n", filePath);
>>>>>>> Stashed changes:src/alex.shepel/main/resources/classes/ReadGenerator.sv
            $display("            Note that:");
            $display("            a) input files must NOT have empty lines.");
            $display("            b) input data must have hexadecimal format.\n");
            iface.test_passed = 0;
        end
    endfunction


    /*
        Switches to the next (or specified) simulation point and returns it.
    */
    function logic signed [DATA_WIDTH - 1 : 0] getPoint();
        return vector[index];
    endfunction


    /*
        Returns the actual point index of the testing vector.
    */
    function int getIndex();
        return index;
    endfunction


    /*
        Sets the point index of the testing vector.
    */
    function void setIndex(int index);
        if (index >= vector.size()) begin
            $display("ERROR: The index %0d is out of the vector size.", index);
            $display("       Vector indexes must be in range [0 : %0d].", vector.size() - 1);
            $display("       Input data file: %0s", filePath);
        end else
            this.index = index;
    endfunction


    /*
        Returns the size of the testing vector.
    */
    function int getSize();
        return vector.size();
    endfunction


endclass