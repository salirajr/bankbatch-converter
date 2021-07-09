package devjs.rnd.iagi.bankbatchconverter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Row;

import java.util.Arrays;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BankBatch {

    private String nomorRekening;
    private String namaPemegangRekening;
    private String kodeInvestor;
    private String mataUang;
    private int netDeviden;
    private int nDeviden;
    private String beritaTransfer;
    private String jenisTransfer;
    private String kodeKliringBank;
    private String namaBank;
    private String alamatBank;
    private String beritaTransferTambahan;
    private String isBeritaEmail;
    private String email;
    private int penguranganNilai;
    private String chargeInstruction;// OUR
    private String kewarganegaraanPemegangRekening;
    private String beneficiaryType;// 1: personal, 2: company, 3: government
    private String namaInvestor;


    public BankBatch(BasilAparkost ba){
        this.nomorRekening= ba.getNomorRekening();
        this.email = ba.getEmail();
        this.namaPemegangRekening= ba.getNamaPemegangRekening();
        this.namaBank=ba.getNamaBank();
        this.netDeviden=ba.getNetDeviden();
        this.kodeInvestor=ba.getKodeInvestor();
        this.nDeviden=1;
        this.penguranganNilai = 0;
        this.namaInvestor = ba.getNamaInvestor();
    }

    public void addNetDeviden(int dividen){
        this.netDeviden=this.netDeviden+dividen;
        this.nDeviden++;
    }

    public String[] asArrays() {
        String[] arr = new String[44];
        Arrays.fill(arr, "");
        arr[0] = this.nomorRekening;
        arr[1] = this.namaPemegangRekening;
        arr[5] = this.mataUang;
        arr[6] = String.valueOf(this.netDeviden);
        arr[7] = this.beritaTransfer;
        arr[9] = this.jenisTransfer;
        arr[10] = this.kodeKliringBank;
        arr[11] = this.namaBank;
        arr[12] = this.alamatBank;
        arr[16] = this.isBeritaEmail;
        arr[17] = this.email;
        arr[21] = this.kewarganegaraanPemegangRekening;
        arr[38] = this.chargeInstruction;
        arr[39] = this.beneficiaryType;
        arr[40] = this.beritaTransferTambahan;
        return arr;
    }


    public void subNetDeviden(int amount) {
        this.netDeviden -=amount;
        this.penguranganNilai += amount;
    }

    public void setNetDeviden(String value, int index){
        int val = 0;
        try{
            val= Integer.parseInt(value);
            this.netDeviden = val;
        }catch (NumberFormatException e){
            System.out.println("Data["+value+"] Error pada row ke-"+index+", Net Deviden seharusnya angka");
        }


    }

    public void setNDeviden(String value, int index){
        int val = 0;
        try{
            val= Integer.parseInt(value);
            this.nDeviden = val;
        }catch (NumberFormatException e){
            System.out.println("Data["+value+"] Error pada row ke-"+index+", N Deviden seharusnya angka");
        }

    }

    public void setPenguranganNilai(String value, int index){
        int val = 0;
        try{
            val= Integer.parseInt(value);
            this.penguranganNilai = val;
        }catch (NumberFormatException e){
            System.out.println("Data["+value+"] Error pada row ke-"+index+", Pengurangan Nilai seharusnya angka");
        }

    }
}
