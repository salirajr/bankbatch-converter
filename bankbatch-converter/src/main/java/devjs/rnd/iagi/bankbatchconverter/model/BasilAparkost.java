package devjs.rnd.iagi.bankbatchconverter.model;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.csv.CSVRecord;

@Getter
@ToString
public class BasilAparkost {



    private String namaPemegangRekening;

    private String nomorRekening;


    private String namaBank;


    private String kodeBank;


    private String kodeInvestor;


    private String namaInvestor;


    private int netDeviden;

    private int totalPropertiSudahBayar;


    private String email;

    public BasilAparkost(CSVRecord record) {//{2,7,8,9,10,18,19,20};
        setNamaPemegangRekening(record.get(2));
        setNomorRekening(record.get(3));
        setNamaBank(record.get(7));
        setKodeBank(record.get(8));
        setKodeInvestor(record.get(9));
        setNamaInvestor(record.get(10));
        setNetDeviden(record.get(18));
        setTotalPropertiSudahBayar(record.get(19));
        setEmail(record.get(20));

    }

    public void setNamaPemegangRekening(String namaPemegangRekening) {
        if(namaPemegangRekening.length()<3){
            this.namaPemegangRekening = "";
        }else{

            this.namaPemegangRekening = namaPemegangRekening;
        }
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening.substring(1);;
    }

    public void setNamaBank(String namaBank) {
        this.namaBank = namaBank;
    }

    public void setKodeBank(String kodeBank) {
        this.kodeBank = kodeBank.substring(1);
    }

    public void setKodeInvestor(String kodeInvestor) {
        this.kodeInvestor = kodeInvestor;
    }

    public void setNamaInvestor(String namaInvestor) {
        this.namaInvestor = namaInvestor;
    }

    public void setNetDeviden(String netDeviden) {
        this.netDeviden = Integer.parseInt(netDeviden.substring(1).replace(",", "").split("\\.")[0]);
    }


    public void setTotalPropertiSudahBayar(String totalProperiSudahBayar) {
        this.totalPropertiSudahBayar = Integer.parseInt(totalProperiSudahBayar.substring(1).replace(",", "").split("\\.")[0]);
        ;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
