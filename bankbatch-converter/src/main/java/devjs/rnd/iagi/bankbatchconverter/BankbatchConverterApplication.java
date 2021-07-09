package devjs.rnd.iagi.bankbatchconverter;

import devjs.rnd.iagi.bankbatchconverter.config.ApplicationConfig;
import devjs.rnd.iagi.bankbatchconverter.controller.ConvertingBankBatchMCMController;
import devjs.rnd.iagi.bankbatchconverter.controller.ConvertngBasilAparkostController;
import devjs.rnd.iagi.bankbatchconverter.service.InputTypeInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SpringBootApplication
public class BankbatchConverterApplication
        implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(BankbatchConverterApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(BankbatchConverterApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
		InputTypeInService.init();

		if(args.length > 0 ){
			String typeIn;
			do {
				typeIn = mainMenu();
			} while (!typeIn.equalsIgnoreCase("exit"));

			InputTypeInService.close();
		}


    }

    private String mainMenu(){
		System.out.println("\n\n\nSelamat datang di System Bank Batch Converter SCC, menu:");
		System.out.println("[0]. Set Up Directory");
		System.out.println("[1]. Filtrasi Basil");
		System.out.println("[2]. Konversi MCM CSV");
		System.out.println("[3]. Konfigurasi");
		System.out.println("[exit]");


		String typeIn = InputTypeInService.input();



		switch (typeIn){
			case "0":
				setUpDirectory();
				break;
			case "1":
				new ConvertngBasilAparkostController().convertingToBankBatchXLS();
				break;
			case "2":
				new ConvertingBankBatchMCMController().convertingToCSVBSI();
				break;

			default:
				System.out.println("Anda menginput [" + typeIn+"], kembali ke menu.");
		}

		return typeIn;
	}

	private void setUpDirectory(){
		File rootDir = new File("./scc");
		if (!rootDir.exists()){
			rootDir.mkdirs();
		}
		File inputDir = new File("./scc/input");
		if (!inputDir.exists()){
			inputDir.mkdirs();
		}

		SimpleDateFormat dateformat = new SimpleDateFormat("yyMMMdd");


		File outputDir = new File(ApplicationConfig.getOutputFolder());
		if (!outputDir.exists()){
			outputDir.mkdirs();
		}



	}



}
