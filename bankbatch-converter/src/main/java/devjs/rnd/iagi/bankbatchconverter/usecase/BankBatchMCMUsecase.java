package devjs.rnd.iagi.bankbatchconverter.usecase;

import devjs.rnd.iagi.bankbatchconverter.config.ApplicationConfig;
import devjs.rnd.iagi.bankbatchconverter.model.BankBatch;
import devjs.rnd.iagi.bankbatchconverter.service.BankBatchService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class BankBatchMCMUsecase {

    BankBatchService bbs = new BankBatchService();

    public void convertToBankBatch(String fileName,String filePath, String defaultJenisTransaksi){

        Collection<BankBatch> cbb = bbs.readBankBatchXLS(filePath);
        System.out.println("BankBatch file terproses, terdapat:"+cbb.size()+" data terfilter");

        for (BankBatch bb : cbb){
            if(bb.getEmail() == null || bb.getEmail().isEmpty()){

            }else if(bb.getNomorRekening() == null || bb.getNomorRekening().isEmpty()){

            }else if(bb.getNamaPemegangRekening() == null || bb.getNamaPemegangRekening().isEmpty()){

            }else{
                if(bb.getJenisTransfer().equalsIgnoreCase("LBU") && bb.getNamaBank().equalsIgnoreCase("MANDIRI")){
                    bb.addNetDeviden(2900);
                    bb.setJenisTransfer("IBU");
                }else if(bb.getJenisTransfer().equalsIgnoreCase("IBU") && !bb.getNamaBank().equalsIgnoreCase("MANDIRI") && defaultJenisTransaksi.equalsIgnoreCase("LBU")){
                    bb.subNetDeviden(2900);
                    bb.setJenisTransfer("LBU");
                } else if(bb.getJenisTransfer().equalsIgnoreCase("OBU") && bb.getNamaBank().equalsIgnoreCase("MANDIRI")){
                    bb.addNetDeviden(6500);
                    bb.setJenisTransfer("IBU");
                }else if(bb.getJenisTransfer().equalsIgnoreCase("IBU") && !bb.getNamaBank().equalsIgnoreCase("MANDIRI") && defaultJenisTransaksi.equalsIgnoreCase("OBU")){
                    bb.subNetDeviden(6500);
                    bb.setJenisTransfer("LBU");
                }
                bb.setKodeKliringBank(ApplicationConfig.KLIRING_BANK.get(bb.getNamaBank().toUpperCase()));
            }

        }


        bbs.toBatchFileCSV(cbb, ApplicationConfig.getOutputFolder()+"/"+fileName.toLowerCase());
        System.out.println("MCM file dibuat.");

    }

}
