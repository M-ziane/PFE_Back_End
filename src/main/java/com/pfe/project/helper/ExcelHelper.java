package com.pfe.project.helper;


        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.util.Date;
        import java.util.List;

        import com.pfe.project.models.Client;
        import org.apache.poi.ss.usermodel.*;
        import org.apache.poi.xssf.usermodel.XSSFCellStyle;
        import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
        import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Nom", "Code CLient", "E-mail", "Typlogie", "Code Vendeur", "Ville", "Adresse", "Code Postal", "Numéro de téléphone 1", "Numéro de téléphone 2", "Numéro de téléphone 3","Marque" ,"Modele" ,"Immatriculation","Deparetement","Etablissement","Date Comptabilisation","modalite de paiement","sexe"};
    static String SHEET = "Tutorials";

    public static ByteArrayInputStream tutorialsToExcel(List<Object[]> tutorials) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            //1. Create the date cell style
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle cellStyle         = workbook.createCellStyle();
            cellStyle.setDataFormat(
                    createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            //         0              1               2                    3              4                      5                       6                                  7                                  8                              9             10                      11                  12                    13                       14           15                       16                    17
//root.get("id"),root.get("name"),root.get("typologie"),voiture.get("marque"),voiture.get("modele"),details.get("pointVente"), details.get("nomVendeur"),details.get("dateComptabilisation"),voiture.get("immatriculation"),details.get("dpt"),root.get("num_tel1"),root.get("num_tel2"),root.get("num_tel13"),root.get("email"),root.get("ville"),root.get("codePostal"),root.get("adresse"),root.get("code")
     //add modalite paiement     //   sexe
            int rowIdx = 1;
            for(Object[] anObject : tutorials) {
                Object[] fields = anObject;
                Row row = sheet.createRow(rowIdx++);
                if(fields[1]!=null) row.createCell(0).setCellValue((String) fields[1]);
                if(fields[17]!=null) row.createCell(1).setCellValue((String) fields[17]);
                if(fields[13]!=null) row.createCell(2).setCellValue((String) fields[13]);
                if(fields[2]!=null) row.createCell(3).setCellValue((String) fields[2]);
                if( fields[6]!=null) row.createCell(4).setCellValue((String) fields[6]);
                if( fields[14]!=null) row.createCell(5).setCellValue((String) fields[14]);
                if(fields[16]!=null) row.createCell(6).setCellValue((String) fields[16]);
                if( fields[15]!=null) row.createCell(7).setCellValue((String) fields[15]);
                if( fields[10]!=null) row.createCell(8).setCellValue(String.valueOf( fields[10]));
                if( fields[11]!=null) row.createCell(9).setCellValue(String.valueOf(fields[11]));
                if(fields[12]!=null) row.createCell(10).setCellValue(String.valueOf(fields[12]));
                if( fields[3]!=null) row.createCell(11).setCellValue((String) fields[3]);
                if( fields[4]!=null) row.createCell(12).setCellValue((String) fields[4]);

                if( fields[8]!=null) row.createCell(13).setCellValue((String) fields[8]);
                if( fields[9]!=null) row.createCell(14).setCellValue((String) fields[9]);
                if( fields[5]!=null) row.createCell(15).setCellValue((String) fields[5]);
                if( fields[7]!=null){
                    System.out.println("fields[7]"+fields[7]);
                    Date date1 = (Date) fields[7];
                    System.out.println("date1"+date1);
                    Cell cell = row.createCell(16);
                    cell.setCellValue(date1);
                    cell.setCellStyle(cellStyle);
                }
//a modifier
                row.createCell(17).setCellValue("NAN");
                row.createCell(18).setCellValue(("NAN"));
            }
            /*int rowIdx = 1;
            for (Client client : tutorials) {
                Row row = sheet.createRow(rowIdx++);

                if(client.getName()!=null) row.createCell(0).setCellValue(client.getName());
                if(client.getCode()!=null) row.createCell(1).setCellValue(client.getCode());
                if(client.getEmail()!=null) row.createCell(2).setCellValue(client.getEmail());
                if(client.getTypologie()!=null) row.createCell(3).setCellValue(client.getTypologie());
                if(client.getSexe()!=null) row.createCell(4).setCellValue(client.getSexe());
                if(client.getVille()!=null) row.createCell(5).setCellValue(client.getVille());
                if(client.getAdresse()!=null) row.createCell(6).setCellValue(client.getAdresse());
                if(client.getCodePostal()!=null) row.createCell(7).setCellValue(client.getCodePostal());
                if(client.getNum_tel1()!=null) {row.createCell(8).setCellValue(client.getNum_tel1().toString());
                System.out.println(client.getNum_tel1().toString());}
                if(client.getNum_tel2()!=null) row.createCell(9).setCellValue(client.getNum_tel2().toString());
                if(client.getNum_tel3()!=null) row.createCell(10).setCellValue(client.getNum_tel3().toString());

            }*/

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}