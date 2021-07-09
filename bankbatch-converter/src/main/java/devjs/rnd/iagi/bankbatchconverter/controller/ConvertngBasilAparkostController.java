package devjs.rnd.iagi.bankbatchconverter.controller;

import devjs.rnd.iagi.bankbatchconverter.model.BankBatch;
import devjs.rnd.iagi.bankbatchconverter.model.BasilAparkost;
import devjs.rnd.iagi.bankbatchconverter.service.BasilAparkostService;
import devjs.rnd.iagi.bankbatchconverter.service.InputTypeInService;
import devjs.rnd.iagi.bankbatchconverter.usecase.BasilAparkostMergerUsecase;

import javax.xml.xpath.XPath;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ConvertngBasilAparkostController {

    private InputTypeInService in;

    private BasilAparkostMergerUsecase bamc;

    private File fileA, fileB;
    private String fileNameA, fileNameB, beritaTransfer, beritaTransferTambahan, jenisTransfer;

    private Collection<BankBatch> cbb;

    private final String PATH = "./scc/input/";

    public ConvertngBasilAparkostController() {
        bamc = new BasilAparkostMergerUsecase();
        fileA = null;
        fileB = null;
        fileNameA = "";
        fileNameB = "";
        beritaTransfer = "";
        beritaTransferTambahan = "";
        jenisTransfer = "LBU";
    }


    public void convertingToBankBatchXLS() {
        String typeIn;
        do {
            System.out.println("\n\n\nMenu: Converting to CSV BSI, step:");
            System.out.println("[1]. Input Basil Aparkost file A/" + this.fileNameA);
            System.out.println("[2]. Input Basil Aparkost file B/" + this.fileNameB);
            System.out.println("[3]. Proses Basil Aparkost (A+B)");
            System.out.println("[4]. Input Berita Transfer/"+this.beritaTransfer);
            System.out.println("[5]. Input Berita Transfer Tambahan/"+this.beritaTransferTambahan);
            System.out.println("[6]. Proses Basil Aparkost (A)");
            System.out.println("[7]. Transfer Option for Non Mandiri/"+jenisTransfer);
            System.out.println("[menu]");

            typeIn = InputTypeInService.input();


            switch (typeIn) {
                case "1":
                    inputFileA();
                    break;
                case "2":
                    inputFileB();
                    break;
                case "3":
                    mergeBasil();
                    break;
                case "4":
                    inputBeritaTransfer();
                    break;
                case "5":
                    inputBeritaTransferTambahan();
                    break;
                case "6":
                    prosesABasilBankBatch();
                    break;
                case "7":
                    transferOption();
                    break;
                case "99":
                    mergeBasilDummy();
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
            System.out.println("[1]. LBU Rp. 2900" + (this.jenisTransfer.equalsIgnoreCase("LBU")?"(current)":""));
            System.out.println("[2]. OBU Rp. 6500" + (this.jenisTransfer.equalsIgnoreCase("OBU")?"(current)":""));

            typeIn = InputTypeInService.input();
            if(typeIn.equalsIgnoreCase("1") || typeIn.equalsIgnoreCase("2")){
                if(typeIn.equalsIgnoreCase("1")){

                    this.jenisTransfer="LBU";
                }else{

                    this.jenisTransfer="OBU";
                }
                typeIn = "menu";
            }
        } while (!typeIn.equalsIgnoreCase("menu"));
    }

    private void inputFileA() {
        System.out.println("Silahkan input file B: Basil Aparkost");
        File fileA;
        String fileNameA;
        do {
            fileNameA = in.input();
            fileA = new File(PATH + fileNameA + ".xls");
            if (!fileA.exists() || fileA.isDirectory()) {
                System.out.println("file A tidak ditemukan: " + PATH + fileNameA + ".xls, silahkan input kembali.");
            }

        } while ((!fileA.exists() || fileA.isDirectory()) && !fileNameA.equalsIgnoreCase("menu"));

        if(!fileNameA.equalsIgnoreCase("menu")){
            System.out.println("file A ditemukan: " + PATH + fileNameA + ".xls");

            if (this.fileA != null) {
                System.out.println("ketik Y untuk menganti file sebelumnya");
                if (in.input().equalsIgnoreCase("Y")) {
                    this.fileA = fileA;
                    this.fileNameA = fileNameA;
                    System.out.println("file A sudah diupdate.");
                }
            } else {
                this.fileA = fileA;
                this.fileNameA = fileNameA;
                System.out.println("file A sudah diupdate.");
            }

        }




    }

    private void inputFileB() {
        System.out.println("Silahkan input file A: Basil Aparkost");
        File fileB;
        String fileNameB;
        do {
            fileNameB = in.input();
            fileB = new File(PATH + fileNameB + ".xls");
            if (!fileB.exists() || fileB.isDirectory()) {
                System.out.println("file B tidak ditemukan: " + PATH + fileNameB + ".xls, silahkan input kembali.");
            }

        } while ((!fileB.exists() || fileB.isDirectory())&& !fileNameB.equalsIgnoreCase("menu"));


        if(!fileNameB.equalsIgnoreCase("menu")){
            System.out.println("file B ditemukan: " + PATH + fileNameB + ".xls");

            if (this.fileB != null) {
                System.out.println("ketik Y untuk menganti file sebelumnya");
                if (in.input().equalsIgnoreCase("Y")) {
                    this.fileB = fileB;
                    this.fileNameB = fileNameB;
                    System.out.println("file B sudah diupdate.");
                }
            } else {
                this.fileB = fileB;
                this.fileNameB = fileNameB;
                System.out.println("file B sudah diupdate.");
            }
        }



    }

    private void mergeBasil() {
        if (this.fileA == null) {
            System.out.println("file A belum disimpan. Kembali ke menu sebelumnya");
        } else if (this.fileB == null) {
            System.out.println("file B belum disimpan. Kembali ke menu sebelumnya");

        } else {
            this.cbb = bamc.merge(this.fileA.getAbsolutePath(), this.fileB.getAbsolutePath(), beritaTransfer, beritaTransferTambahan,fileNameA+"_"+fileNameB, jenisTransfer);
        }

    }

    private void prosesABasilBankBatch() {
        if (this.fileA == null) {
            System.out.println("file A belum disimpan. Kembali ke menu sebelumnya");
        }  else {
            this.cbb = bamc.merge(this.fileA.getAbsolutePath(), null, beritaTransfer, beritaTransferTambahan,fileNameA+"_"+fileNameB, jenisTransfer);
        }

    }

    private void mergeBasilDummy() {
        this.cbb = bamc.merge("/Users/jovi/Documents/devjs/prj/bankbatch-converter/scc/input/pgrapr21.xls",
                "/Users/jovi/Documents/devjs/prj/bankbatch-converter/scc/input/pgrmar21.xls",
                beritaTransfer, beritaTransferTambahan, fileNameA+"_"+fileNameB,jenisTransfer);
    }

    private void inputBeritaTransfer() {
        if(beritaTransfer.equals(""))
        {
            System.out.println("Silahkan input Berita Transfer:");

            String input = in.input();
            if(!input.equalsIgnoreCase("menu")){
                this.beritaTransfer = input;
            }


        }else{
            System.out.println("Berita Transfer: ["+beritaTransfer+"], update?");
            if(in.input().equalsIgnoreCase("Y")){
                System.out.println("Silahkan input Berita Transfer:");
                String berita = in.input();
                if(!berita.equalsIgnoreCase("menu")){
                    System.out.println("Update berita Transfer: ["+this.beritaTransfer+"], menjadi ["+berita+"]");
                    if(in.input().equalsIgnoreCase("Y")){
                        this.beritaTransfer = berita;
                    }
                }

            }
        }
    }

    private void inputBeritaTransferTambahan() {
        if(this.beritaTransferTambahan.equals(""))
        {
            System.out.println("Silahkan input Berita Transfer Tambahan:");


            String input = in.input();
            if(!input.equalsIgnoreCase("menu")){
                this.beritaTransferTambahan = input;
            }

        }else{
            System.out.println("Berita Transfer Tambahan: ["+beritaTransfer+"], update?");
            if(in.input().equalsIgnoreCase("Y")){
                System.out.println("Silahkan input Berita Transfer Tambahan:");
                String berita = in.input();
                if(!berita.equalsIgnoreCase("menu")){
                    System.out.println("Update berita Transfer Tambahan: ["+this.beritaTransferTambahan+"], menjadi ["+berita+"]");
                    if(in.input().equalsIgnoreCase("Y")){
                        this.beritaTransferTambahan = berita;
                    }
                }

            }
        }
    }
}
