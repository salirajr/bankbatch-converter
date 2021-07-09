package devjs.rnd.iagi.bankbatchconverter.usecase;

import devjs.rnd.iagi.bankbatchconverter.config.ApplicationConfig;
import devjs.rnd.iagi.bankbatchconverter.model.BankBatch;
import devjs.rnd.iagi.bankbatchconverter.model.BasilAparkost;
import devjs.rnd.iagi.bankbatchconverter.service.BankBatchService;
import devjs.rnd.iagi.bankbatchconverter.service.BasilAparkostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class BasilAparkostMergerUsecase {

    private static Logger LOG = LoggerFactory
            .getLogger(BasilAparkostMergerUsecase.class);

    BankBatchService bbs = new BankBatchService();
    BasilAparkostService bas = new BasilAparkostService();






    public Collection<BankBatch> merge(String fileAPath, String fileBPath, String beritaTransfer, String beritaTransferTambahan, String resultFileName, String jenisTransferNonMandiri){
        System.out.println("Mengabungkan file A dan B dimulai");
        List<BasilAparkost> lbaA = bas.readXLS(fileAPath);
        System.out.println("File A: "+lbaA.size()+" data terfilter");

        List<BasilAparkost> lbaB = new ArrayList<BasilAparkost>();
        if(fileBPath == null){
            System.out.println("File B tidak di proses");
        }else{

            lbaB = bas.readXLS(fileBPath);
            System.out.println("File B: "+lbaB.size()+" data terfilter");
        }


        List<BasilAparkost> lba0 = bas.merge(lbaA, lbaB);

        Map<String, BankBatch> mba = new HashMap<String,BankBatch>();
        // normalize
        BankBatch bbTemp;
        for(BasilAparkost baItr : lba0){
            //LOG.info(baItr.toString());
            bbTemp = new BankBatch(baItr);
            bbTemp.setBeritaTransfer(beritaTransfer);
            bbTemp.setBeritaTransferTambahan(beritaTransferTambahan);

            if(bbTemp.getNamaBank().equalsIgnoreCase("mandiri")){
                bbTemp.setJenisTransfer("IBU");
            }else{
                if(jenisTransferNonMandiri.equalsIgnoreCase("LBU")){
                    bbTemp.setJenisTransfer("LBU");
                    //bbTemp.subNetDeviden(2900);
                }else if(jenisTransferNonMandiri.equalsIgnoreCase("OBU")){
                    bbTemp.setJenisTransfer("LBU");
                    //bbTemp.subNetDeviden(6500);
                }


            }
            bbTemp.setChargeInstruction("OUR");
            bbTemp.setKewarganegaraanPemegangRekening("Y");

            if(bbTemp.getEmail() != null & !bbTemp.getEmail().isEmpty()){
                bbTemp.setIsBeritaEmail("Y");
            }else{
                bbTemp.setIsBeritaEmail("N");
            }

            bbTemp.setAlamatBank("Jakarta");
            bbTemp.setMataUang("IDR");

            bbTemp.setBeneficiaryType("1");

            bbTemp.setKodeKliringBank(ApplicationConfig.KLIRING_BANK.get(bbTemp.getNamaBank().toUpperCase()));

            if(mba.containsKey(baItr.getKodeInvestor())){
                mba.get(bbTemp.getKodeInvestor()).addNetDeviden(bbTemp.getNetDeviden());
                //LOG.info("accumulate of "+baItr.getKodeInvestor());
            }else{
                if(bbTemp.getJenisTransfer().equalsIgnoreCase("LBU")){
                    bbTemp.subNetDeviden(2900);
                }else if(bbTemp.getJenisTransfer().equalsIgnoreCase("OBU")){
                    bbTemp.subNetDeviden(6500);
                }
                mba.put(bbTemp.getKodeInvestor(), bbTemp);

            }
//            if(bbTemp.getKodeInvestor().equalsIgnoreCase("INV17246")){
//                LOG.info(mba.get("INV17246").toString());
//            }

        }

        Collection<BankBatch> cbb=mba.values();



        bbs.toXLS(cbb, ApplicationConfig.getOutputFolder()+"/"+resultFileName.toLowerCase()+".xls");
        bbs.toBatchFileCSV(cbb, ApplicationConfig.getOutputFolder()+"/"+resultFileName.toLowerCase()+".csv");
        return cbb;
    }

}
