package devjs.rnd.iagi.bankbatchconverter.service;

import devjs.rnd.iagi.bankbatchconverter.model.BasilAparkost;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasilAparkostService {

    private static Logger LOG = LoggerFactory
            .getLogger(BasilAparkostService.class);

    private  String[]   HEADERS     = {"TEMPLATEFIELD", "NO", "NAMA PEMEGANG REKENING", "NOMOR REKENING", "VIRTUAL ACCT. NO",
            "NAMA PENGIRIM", "NAMA PENERIMA", "NAMA BANK", "KODE BANK", "CODE", "INVESTOR", "LOKASI", "MENARA",
            "LANTAI", "RUANGAN", "KELUASAN", "TOTAL DIVIDEN", "TOTAL BIAYA MANAJEMEN", "NET DIVIDEN", "TOTAL PROPERTI SUDAH DIBAYAR(%)",
            "EMAIL"};

    public List<BasilAparkost> readXLS(String filePath) {
        List<BasilAparkost> lba= new ArrayList<BasilAparkost>();
        try{
            CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.TDF);
            List<CSVRecord> list = parser.getRecords();
            for (CSVRecord record : list) {
                if(record.get(0).trim().equals("-")){
                    BasilAparkost baTemp = new BasilAparkost(record);
                    if(baTemp.getTotalPropertiSudahBayar() == 100){
                        lba.add(baTemp);
                        //LOG.info(baTemp.toString());
                    }

                }

            }
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lba;
    }

    public List<BasilAparkost> merge(List<BasilAparkost> listA, List<BasilAparkost> listB){
        List<BasilAparkost> newList = Stream.concat(listA.stream(), listB.stream())
                .collect(Collectors.toList());

        return newList;
    }
}
