package devjs.rnd.iagi.bankbatchconverter.controller;

import devjs.rnd.iagi.bankbatchconverter.config.ApplicationConfig;
import devjs.rnd.iagi.bankbatchconverter.service.InputTypeInService;
import devjs.rnd.iagi.bankbatchconverter.usecase.BankBatchMCMUsecase;

import java.io.File;

public class ConvertingBankBatchMCMController {

    private InputTypeInService in;

    private File file;

    private String fileName, defaultJenisTransfer;

    private BankBatchMCMUsecase bbmu;

    public ConvertingBankBatchMCMController() {
        bbmu   = new BankBatchMCMUsecase();
        file = null;
        fileName = "";
        defaultJenisTransfer = "LBU";
    }

    public void convertingToCSVBSI() {
        String typeIn;
        do {
            System.out.println("\n\n\nMenu: Converting to Bank Batch to CSV, step:");
            System.out.println("[1]. Update Default Jenis Trasfer/" + this.defaultJenisTransfer);
            System.out.println("[2]. Input Bank Batch file/" + this.fileName);

            typeIn = InputTypeInService.input();


            switch (typeIn) {
                case "1":
                    transferOption();
                    break;
                case "2":
                    convertToMCM();
                    break;
                default:
                    System.out.println("Anda menginput [" + typeIn + "], kembali ke menu.");
                    typeIn = "menu";
            }
        } while (!typeIn.equalsIgnoreCase("menu"));

    }

    private void transferOption() {

        String typeIn;
        do {
            System.out.println("\n\n\nMenu: Jenis Transaksi:");
            System.out.println("[1]. LBU Rp. 2900" + (this.defaultJenisTransfer.equalsIgnoreCase("LBU")?"(current)":""));
            System.out.println("[2]. OBU Rp. 6500" + (this.defaultJenisTransfer.equalsIgnoreCase("OBU")?"(current)":""));

            typeIn = InputTypeInService.input();
            if(typeIn.equalsIgnoreCase("1") || typeIn.equalsIgnoreCase("2")){
                if(typeIn.equalsIgnoreCase("1")){

                    this.defaultJenisTransfer="LBU";
                }else{

                    this.defaultJenisTransfer="OBU";
                }
                typeIn = "menu";
            }
        } while (!typeIn.equalsIgnoreCase("menu"));
    }

    public void convertToMCM() {
        System.out.println("Silahkan input file Bank Batch:");
        File file;
        do {
            this.fileName = in.input();
            file = new File(ApplicationConfig.PATH + fileName + ".xls");
            if (!file.exists() ||              file.isDirectory()) {
                System.out.println("file tidak ditemukan: " + ApplicationConfig.PATH + fileName + ".xls, silahkan input kembali.");
            }

        } while ((!file.exists() || file.isDirectory()) && !fileName.equalsIgnoreCase("menu"));

        System.out.println("file ditemukan: " + ApplicationConfig.PATH + fileName + ".xls");

        bbmu.convertToBankBatch("m"+fileName+".csv", file.getAbsolutePath(), defaultJenisTransfer);


    }
}
