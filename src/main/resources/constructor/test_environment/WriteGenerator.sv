//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	WriteGenerator																		//
//	Author(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Writes the values, that are processed by DUT, to the specified file.                //
//                                                                                                          //
//                      Created/overwritten file has the format of column of hexadecimal values             //
//                      that follow each other.                                                             //
//                                                                                                          //
//                      For example, the sequence of six 4'h values is shown below:                         //
//                          ADE3															    			//
//                          1234                                                                            //
//                          FFFF                                                                            //
//                          FA99                                                                            //
//                          0000                                                                            //
//                          1234                                                                            //
//																											//
//						-- Variable parameters: 															//
//						@param DATA_WIDTH  			    The width of an input data.                         //
//                                                      It must be copied from DUT's parameters             //
//                                                      description.                                        //
//																											//
//						-- Constant parameters:																//
//						@const --                                               							//
//																											//
//	Start design	:	15.09.2020																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


class WriteGenerator #(
    // The width of the values, that are processed by DUT.
    parameter DATA_WIDTH = 16
);


    /*
        Instance properties.
    */
    // The file descriptor.
    local int fd;


    /* 
        The class constructor.
    */ 
    function new();
    endfunction

    
    /*
        Creates the specified file for writing.
        Overwrites if it exists.
    */
    function void open(string filePath);
        fd = $fopen(filePath, "w");
    endfunction


    /*
        Writes the data, that is processed by DUT, to a file.
    */
    function void write(logic [DATA_WIDTH - 1 : 0] dutResult);
        $fdisplay(fd, "%0d", dutResult);
    endfunction


endclass