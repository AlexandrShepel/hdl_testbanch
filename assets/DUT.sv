//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Name File		:	DSP																					//
//	Autor(-s)		:	Alex Shepel											 								//
//	Company			:	Radiy																				//
//																											//
// 	Description		:	Выполняет ЦОС:																		//
//							-- выполняет ДПФ;																//
//							-- вычисляет амплитуду;															//
//							-- вычисляет угол между током и напряжением.									//
//																											//
//						Имеет подмодуль DSP_control, в котором формируются управляющие сигналы.				//
//						Здесь также формируется сигнал общей готовности ЦОС	по окончанию работы всех 		//
//						подмодулей.																			//
//																											//
//						-- Parameters:																		//
//						@param INPUT_WIDTH  			Ширина входящей шины.								//
//						@param NUM_OF_INPUTS 			Кол-во входящих шин.								//
//						@param POINTS_PER_PERIOD 		Кол-во отсчетов на период (дискретизация			//
// 														сигнала в АЦП).										//
//						@param ATAN2_OUTPUT_WIDTH  		Ширина выхода модуля atan2*.						//
//						@param NUM_OF_CONTROLLED_STAGES Кол-во подмодулей, в которых проводяться 			//
//														вычисления (ступени ЦОС).							//
//																											//
//						-- Constants:																		//
//						@const NUM_OF_PHASES			Кол-во обрабатываемых фаз.							//
//																											//
//						  *	Подробно описан и расчитан в подмодуле atan2. При изменении параметров модуля	//
//							atan2 этот параметр необходимо пересмотреть.									//
//																											//
//	Start design	:	10.06.2020																			//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


module DUT #(	
		// Parameters:
		INPUT_WIDTH = 16,
		NUM_OF_INPUTS = 8,
		POINTS_PER_PERIOD = 20,
		ATAN2_OUTPUT_WIDTH = 11,
		NUM_OF_CONTROLLED_STAGES = 6,

		// Constants:
		NUM_OF_PHASES = 3	
	)(
		input logic clk_LF, clk_HF, a_rst_n,
		input logic signed data_in_1 [NUM_OF_INPUTS - 1 : 0],
		input logic signed [INPUT_WIDTH - 1 : 0] data_in_2,
		input logic signed [INPUT_WIDTH - 1 : 0] data_in_3 [NUM_OF_INPUTS - 1 : 0],

		output logic ready,
		output logic 		[       INPUT_WIDTH - 1 : 0] magn [NUM_OF_INPUTS - 1 : 0],
		output logic signed [ATAN2_OUTPUT_WIDTH - 1 : 0] phase [NUM_OF_PHASES - 1 : 0]
	);
	
	// Внутренние уравляющие сигналы.
	logic submodule_enable [NUM_OF_CONTROLLED_STAGES - 1 : 0];
	logic submodule_s_rst  [NUM_OF_CONTROLLED_STAGES - 1 : 0];
	logic submodule_ready  [NUM_OF_CONTROLLED_STAGES - 1 : 0];
	
	// Связь между подключенными модулями.
	logic signed [INPUT_WIDTH        - 1 : 0] scaled_down_data    [NUM_OF_INPUTS - 1 : 0];
	logic signed [INPUT_WIDTH        - 1 : 0] shift_reg 	      [NUM_OF_INPUTS - 1 : 0][POINTS_PER_PERIOD - 1 : 0];
	logic signed [INPUT_WIDTH        - 1 : 0] first_harmonic_real [NUM_OF_INPUTS - 1 : 0];
	logic signed [INPUT_WIDTH        - 1 : 0] first_harmonic_imag [NUM_OF_INPUTS - 1 : 0];
	logic signed [ATAN2_OUTPUT_WIDTH - 1 : 0] angel 	   		  [NUM_OF_INPUTS - 1 : 0];


	/*
		Управляет системой ЦОС.
	*/
	DSP_control #( 
		.NUM_OF_CONTROLLED_STAGES 	(NUM_OF_CONTROLLED_STAGES) 
	) DSP_control (
		// inputs
		.clk				(clk_HF),
		.zero_control_event	(clk_LF),
		.a_rst_n			(a_rst_n),
		.submodule_ready	(submodule_ready),

		// outputs
		.submodule_s_rst	(submodule_s_rst),
		.submodule_enable	(submodule_enable),
		.all_ready			(ready)
	);


	/*
		Масштабирует значения пришедшие с АЦП.
	*/
	scaling_factor #(	
		.INPUT_WIDTH	(INPUT_WIDTH),
		.NUM_OF_INPUTS	(NUM_OF_INPUTS)				
	) scaling_factor (
		// inputs
		.clk				(clk_HF),
		.a_rst_n			(a_rst_n),
		.s_rst				(submodule_s_rst [0]),
		.enable				(submodule_enable[0]),
		.in					(data_in),

		// outputs	
		.ready				(submodule_ready[0]),
		.out				(scaled_down_data)
	);


	/*
		Подлкючает сдвиговый регистр на NUM_OF_INPUTS каналов.
	*/
	shift_register #(	
		.INPUT_WIDTH			(INPUT_WIDTH),
		.NUM_OF_INPUTS			(NUM_OF_INPUTS),
		.STORED_VALUES_PER_CH	(POINTS_PER_PERIOD)			
	) shift_register (
		// inputs
		.clk				(clk_HF),
		.a_rst_n			(a_rst_n),
		.s_rst				(submodule_s_rst [1]),
		.enable				(submodule_enable[1]),
		.in					(scaled_down_data),

		// outputs
		.ready				(submodule_ready[1]),
		.shift_reg			(shift_reg)
	);


	/*
		Выполняет ДПФ. 	
			Возвращает действительную и мнимую составляющие 
			основной гармоники сигнала, пришедшего с АЦП.
	*/
	DFT #(				
		.INPUT_WIDTH		(INPUT_WIDTH),
		.NUM_OF_INPUTS		(NUM_OF_INPUTS),
		.POINTS_PER_PERIOD	(POINTS_PER_PERIOD)			
	) DFT (
		// inputs
		.clk				(clk_HF),
		.a_rst_n			(a_rst_n),
		.s_rst				(submodule_s_rst [2]),
		.enable				(submodule_enable[2]),
		.shift_reg			(shift_reg),

		// outputs
		.ready				(submodule_ready[2]),
		.out_real			(first_harmonic_real),
		.out_imag			(first_harmonic_imag)
	);


	/*
		Вычисляет амплитуду сигнала.
	*/
	magnitude 	#(	
					.INPUT_WIDTH	(INPUT_WIDTH),
					.NUM_OF_INPUTS	(NUM_OF_INPUTS)
				)
	magnitude 	(
		// inputs
		.clk				(clk_HF),
		.a_rst_n			(a_rst_n),
		.s_rst				(submodule_s_rst [3]),
		.enable				(submodule_enable[3]),
		.in_real			(first_harmonic_real),
		.in_imag			(first_harmonic_imag),

		// outputs
		.ready				(submodule_ready[3]),
		.magn		   		(magn)
	);


	/*
		Определяет угол вектора входящего сигнала на комплексной плоскости
		по его действительной и мнимой составляющих. 
	*/
	atan2 #(			
				.INPUT_WIDTH	(INPUT_WIDTH),
				.NUM_OF_INPUTS	(NUM_OF_INPUTS)
			)
	atan2 	(
		// inputs
		.clk				(clk_HF),
		.a_rst_n			(a_rst_n),
		.s_rst				(submodule_s_rst [4]),
		.enable				(submodule_enable[4]),
		.data_real			(first_harmonic_real),
		.data_imag			(first_harmonic_imag),
	
		// outputs
		.ready				(submodule_ready[4]),
		.angel				(angel)
	);


	/*
		Вычисялет разницу фаз между током и напряжением.
	*/
	UI_phase_difference	#(
							.ANGEL_WIDTH	(ATAN2_OUTPUT_WIDTH)
						)
	UI_phase_difference (
		// inputs
		.clk				(clk_HF),
		.a_rst_n			(a_rst_n),
		.s_rst				(submodule_s_rst [5]),
		.enable				(submodule_enable[5]),
		.current_angel		(angel[2 : 0]),
		.voltage_angel		(angel[5 : 3]),

		// outputs
		.ready				(submodule_ready[5]),
		.phase				(phase)
	);


endmodule