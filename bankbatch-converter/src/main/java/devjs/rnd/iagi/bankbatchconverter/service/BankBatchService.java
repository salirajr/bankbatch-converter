package devjs.rnd.iagi.bankbatchconverter.service;

import devjs.rnd.iagi.bankbatchconverter.config.ApplicationConfig;
import devjs.rnd.iagi.bankbatchconverter.model.BankBatch;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankBatchService {

    private static Logger LOG = LoggerFactory
            .getLogger(BankBatchService.class);

    private StringBuilder batchFileLog;

    public void toBatchFileCSV(Collection<BankBatch> lbb, String filePath) {

        List<BankBatch> filteredBB = new ArrayList<BankBatch>();
        batchFileLog = new StringBuilder();
        this.batchFileLog.append("\n\nBankBatch Log:");

        int totalTransferred = 0, nRawTotal=0;
        for (BankBatch tempBA : lbb) {
            nRawTotal++;
            if (tempBA.getNomorRekening() == null || tempBA.getNomorRekening().isEmpty()) {
                batchFileLog.append("\nInvestor["+tempBA.getNamaInvestor()+"] nomor rekening kosong.");
            } else if (tempBA.getEmail() == null || tempBA.getEmail().isEmpty()) {
                batchFileLog.append("\nInvestor["+tempBA.getNamaInvestor()+"] nomor email kosong.");

            } else if (tempBA.getNamaPemegangRekening() == null || tempBA.getNamaPemegangRekening().isEmpty()) {
                batchFileLog.append("\nInvestor["+tempBA.getNamaInvestor()+"] nomor nama-pemegang-rekening kosong.");

            } else if (tempBA.getNetDeviden() < ApplicationConfig.MINIMUM_TRANSFER) {
                batchFileLog.append("\nInvestor["+tempBA.getNamaInvestor()+"] net deviden kurang dari minimum transfer.");

            } else {
                filteredBB.add(tempBA);
                totalTransferred += tempBA.getNetDeviden();
            }
        }
        batchFileLog.append("\n\nTotal data terfilter "+filteredBB.size()+" dari "+nRawTotal);
        System.out.println(batchFileLog+"\n\n\n");


        String[] header = new String[44];
        Arrays.fill(header, "");
        header[0] = "P";
        header[1] = ApplicationConfig.DATE_FORMAT.format(Calendar.getInstance().getTime());
        header[2] = "1260007555633";
        header[3] = String.valueOf(filteredBB.size());
        header[4] = String.valueOf(totalTransferred);

        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));


                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        ) {
            csvPrinter.printRecord(header);
            for (BankBatch ba : filteredBB) {

                csvPrinter.printRecord(ba.asArrays());
            }

            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toXLS(Collection<BankBatch> lbb, String filePath) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("basil");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = null;
        int iHeaderCell = 0;
        for (String headerName : new String[]{"No", "Nama Investor", "Nomor Rekening", "Nama Pemegang Rekening ", "Kode Investor",
                "Mata Uang", "Net Deviden", "N Deviden", "Berita Transfer", "Jenis Transfer", "Kode Kliring Bank",
                "Nama Bank", "Alamat Bank", "Berita Tambahan", "Is Berita Email", "Email", "Kewarganegaraan Pemegang Rekening", "Pengurangan Nilai", "Charge Instruction", "Tipe Beneficiary"}) {
            headerCell = header.createCell(iHeaderCell++);
            headerCell.setCellValue(headerName);
            headerCell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        CellStyle coralStyle = workbook.createCellStyle();
        coralStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        coralStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle aquaStyle = workbook.createCellStyle();
        aquaStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        aquaStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int iRows = 2, i = 0;
        Row row;
        Cell tcell;
        for (BankBatch bb : lbb) {
            //LOG.info(bb.toString());
            row = sheet.createRow(iRows++);
            iHeaderCell = 0;

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(++i);
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getNamaInvestor());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getNomorRekening());
            tcell.setCellStyle(style);
            if(bb.getNomorRekening() == null || bb.getNomorRekening().isEmpty()){
                tcell.setCellStyle(aquaStyle);
            }else {
                tcell.setCellStyle(style);
            }


            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getNamaPemegangRekening());
            if(bb.getNamaPemegangRekening() == null || bb.getNamaPemegangRekening().isEmpty()){
                tcell.setCellStyle(aquaStyle);
            }else if (bb.getNamaPemegangRekening().matches("[a-zA-Z ]+")) {
                tcell.setCellStyle(style);
            } else  {
                tcell.setCellStyle(coralStyle);
            }


            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getKodeInvestor());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getMataUang());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getNetDeviden());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getNDeviden());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getBeritaTransfer());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getJenisTransfer());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getKodeKliringBank());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getNamaBank());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getAlamatBank());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getBeritaTransferTambahan());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getIsBeritaEmail());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getEmail());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getKewarganegaraanPemegangRekening());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getPenguranganNilai());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getChargeInstruction());
            tcell.setCellStyle(style);

            tcell = row.createCell(iHeaderCell++);
            tcell.setCellValue(bb.getBeneficiaryType());
            tcell.setCellStyle(style);


        }


        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        //path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Collection<BankBatch> readBankBatchXLS(String fileLocation) {

        Collection<BankBatch> result = new ArrayList<BankBatch>();

        FileInputStream file = null;
        Workbook workbook;
        try {
            file = new FileInputStream(new File(fileLocation));

            workbook = WorkbookFactory.create(file);

            Sheet sheet = workbook.getSheetAt(0);

            BankBatch bb;
            int i = 0;
            String cell1st;
            for (Row row : sheet) {
                cell1st = getValue(row.getCell(0));
                if (cell1st != null && !cell1st.equalsIgnoreCase("No") && !cell1st.isEmpty()) {
                    bb = new BankBatch();
                    bb.setNamaInvestor(getValue(row.getCell(1)));
                    bb.setNomorRekening(getValue(row.getCell(2)));
                    bb.setNamaPemegangRekening(getValue(row.getCell(3)));
                    bb.setKodeInvestor(getValue(row.getCell(4)));
                    bb.setMataUang(getValue(row.getCell(5)));
                    bb.setNetDeviden(getValue(row.getCell(6)), i);
                    bb.setNDeviden(getValue(row.getCell(7)), i);
                    bb.setBeritaTransfer(getValue(row.getCell(8)));
                    bb.setJenisTransfer(getValue(row.getCell(9)));
                    bb.setKodeKliringBank(getValue(row.getCell(10)));
                    bb.setNamaBank(getValue(row.getCell(11)));
                    bb.setAlamatBank(getValue(row.getCell(12)));
                    bb.setBeritaTransferTambahan(getValue(row.getCell(13)));
                    bb.setIsBeritaEmail(getValue(row.getCell(14)));
                    bb.setEmail(getValue(row.getCell(15)));
                    bb.setKewarganegaraanPemegangRekening(getValue(row.getCell(16)));
                    bb.setPenguranganNilai(getValue(row.getCell(17)), i);
                    bb.setChargeInstruction(getValue(row.getCell(18)));
                    bb.setBeneficiaryType(getValue(row.getCell(19)));
                    //System.out.println(bb.toString());
                    result.add(bb);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException f) {
            f.printStackTrace();
        } catch (InvalidFormatException g) {
            g.printStackTrace();
        }
        return result;
    }

    private String getValue(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
