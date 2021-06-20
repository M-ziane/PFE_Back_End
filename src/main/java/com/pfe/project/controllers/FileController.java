package com.pfe.project.controllers;
import com.pfe.project.models.Client;
import com.pfe.project.models.Contrat;
import com.pfe.project.models.Voiture;
import com.pfe.project.repository.ClientRepository;
import com.pfe.project.repository.ContratRepository;
import com.pfe.project.repository.VoitureRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ContratRepository contratRepository;
    @Autowired
    VoitureRepository voitureRepository;
    // Upload the file interface

    @PostMapping(value = "/excel/import")
    //@PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ArrayList<Map<String,String>> excelImport(@RequestParam(value = "excelFile") MultipartFile excelFile ,@RequestParam(required = false)Integer progressBarUseless) throws Exception{
        if(excelFile==null){
            System.out.println("Please select file");
        }

        InputStream fis = excelFile.getInputStream(); //Read input stream
        ArrayList<Map<String,String>> data = parseExcel(fis);
        return data;
    }

    // Analyze excel
    private ArrayList<Map<String,String>> parseExcel(InputStream fis) throws Exception{
        ArrayList<Map<String, String>> data;
        data = new ArrayList<Map<String,String>>();
        Workbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);
        //HSSFWorkbook workbook = new HSSFWorkbook(fis);
        //HSSFSheet sheet = workbook.getSheetAt(0);

        int firstRow = sheet.getFirstRowNum();//first line number
        int lastRow = sheet.getLastRowNum();//last line number

        XSSFRow titleRow = (XSSFRow) sheet.getRow(firstRow);// header row

        ArrayList<Client> clients = new ArrayList<Client>();
        ArrayList<Contrat> contrats = new ArrayList<Contrat>();
        ArrayList<Voiture> voitures = new ArrayList<Voiture>();
        LongAdder numL = new LongAdder();
        numL.add(clientRepository.count());
        Long id=numL.longValue();
        for(int i=firstRow+1;i<lastRow+1;i++){

            Client client = new Client();
            Voiture voiture = new Voiture();
            Contrat contrat = new Contrat();

            XSSFRow row = (XSSFRow) sheet.getRow(i); //Get the first line
            Map map = getRowData(row,titleRow);//Get the i-th row of data
//create entity from excel sheet



            Set<String> keySet = map.keySet();
            //Long id=clientRepository.count();


            //String myKey = null;
            for ( String mykey : keySet) {
                System.out.println(id);
                String slm= (String) map.get(mykey);
                //System.out.println(myKey);
                id= numL.longValue();
                //Client --> Done
                client.setId(id);
                if (mykey.equals("Nom")) client.setName(slm);
                if (mykey.equals("Typologie Client  VN")) client.setTypologie(slm);
                if (mykey.equals("addresseC")) client.setAdresse(slm);
                if (mykey.equals("Code Postal")) client.setCodePostal(slm);
                if (mykey.equals("Ville")) client.setVille(slm);
                //if (mykey.equals("Nom"))if (mykey == "Nom") client.setCode(slm);
                if (mykey.equals("E-Mail")) client.setEmail(slm);
                if (mykey.equals("N° Tél Mobile Privé") && !slm.equals("ELMANSOUR,") && (slm.length()==10) ) {
                    //Integer lbs = Integer.parseInt(slm);
                    Long num = Long.valueOf(slm);
                    client.setNum_tel1(num);
                }
                if (mykey.equals("N° Téléphone" ) && !slm.equals("ELMANSOUR,") && (slm.length()==10)) {
                    //Integer lbs = Integer.parseInt(slm);
                    Long num = Long.valueOf(slm);
                    client.setNum_tel2(num);
                }
                if (mykey.equals("N°Tel Mobile") && !slm.equals("ELMANSOUR,") && (slm.length()==10)){
                    Long num = Long.valueOf(slm);
                    //Integer lbs = Integer.parseInt(slm);
                    client.setNum_tel3(num);
                }

                //Voiture  --> Done
                voiture.setId(id);
                if (mykey.equals("Code marque")) voiture.setMarque(slm);
                if (mykey.equals("Code model") && slm.length()>=3) {
                    String modele =slm.substring(0,slm.indexOf("/") );
                    voiture.setModele(modele);
                }
                if (mykey.equals("VIN")) voiture.setImmatriculation(slm);
//                if (mykey == "") voiture.setMarque();//id??
                //Contrat
                contrat.setId(id);

                if (mykey.equals("Date de creation") && slm.contains("/")) {
                    System.out.println(slm);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    Date date = formatter.parse(slm);
                    contrat.setDate_comptabilisation(date);
                }
                if(mykey.equals("Etablisement")) contrat.setPointVente(slm);
                if(mykey.equals("Deparetemnt")) contrat.setDpt(slm);
                if(mykey.equals("Code uitlisateur")) contrat.setNomVendeur(slm);
                //System.out.println(id);
            }

            contrat.setClient(client);
            System.out.println("voitureA"+voiture.getId());
            //problem  Unable to find com.pfe.project.models.Voiture with id 0
            contrat.setVoiture(voiture);
            System.out.println("contratid"+contrat.getId());
            System.out.println("clientid"+client.getId());
            System.out.println("voitureid"+voiture.getId());
            numL.increment();
            System.out.println(id);
//add entity to array
            clients.add(client);
            contrats.add(contrat);
            voitures.add(voiture);
            //System.out.println(clients);
            data.add(map);
        }
        //add to data base
        System.out.println(clients.size());
        System.out.println(contrats.size());
        System.out.println(voitures.size());


        clientRepository.saveAll(clients);
        voitureRepository.saveAll(voitures);
        contratRepository.saveAll(contrats);


        return data;
    }

    // Get the data in a row in excel
    private Map<String,String> getRowData(XSSFRow row,XSSFRow titleRow){
        Map<String,String> map = new HashMap<String,String>();
        int firstCell = row.getFirstCellNum();//first column number
        int lastCell = row.getLastCellNum();//last column number


        for(int j=firstCell+1;j<lastCell;j++){

            String key = titleRow.getCell(j).getStringCellValue();//current column header
            XSSFCell cell = row.getCell(j);//current cell

            // When the cell is a number format, you need to convert its cell type into a String, otherwise it will be wrong
            if(cell.getCellType()==NUMERIC){
                cell.setCellType(STRING);
            }


            String val =cell.getStringCellValue();//The value of the current cell
            map.put(key,val);
        }
//return client
        return map;
    }

}

/*usefull or not
            cell.getNumericCellValue();
            cell.getStringCellValue();
            cell.getBooleanCellValue();
*/