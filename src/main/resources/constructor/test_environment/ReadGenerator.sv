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
//	Start design	:	03.09.2020																			//
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
    local bit signed [DATA_WIDTH - 1 : 0] vector [];

    // The current index of the testing vector point.
    local int index;


    /*
        The class constructor.
    */
    function new();
        index = 0;
    endfunction


    /*
        Opens the file, that is located at a specified address.
        Reads the test input vector from the file.
    */
    function void open(string filePath);
        integer getFaults, scanFaults;
        string fileLine;    // The store box for the line of the file, that will be read below.
        this.filePath = filePath;
        fd = $fopen(filePath, "r");
        
        if (fd) begin
            $display("File was opened successfully: %s", filePath);

            while (!$feof(fd)) begin
                vector = new [vector.size() + 1] (vector);
                getFaults = $fgets(fileLine, fd);
                scanFaults = $sscanf(fileLine, "%h", vector[vector.size() - 1]);

                if (getFaults == 0 || scanFaults == 0)
                    $display("** Warning: Can't read/scan file: %s", filePath);
            end

            $fclose(fd);
        end

    endfunction


    /*
        Switches to the next (or specified) simulation point and returns it.
    */
    function bit signed [DATA_WIDTH - 1 : 0] getPoint();
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