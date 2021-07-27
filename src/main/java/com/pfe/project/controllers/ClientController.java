package com.pfe.project.controllers;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;


import com.pfe.project.models.*;
import com.pfe.project.repository.*;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    VoitureRepository voitureRepository;

    @Autowired
    KilometrageRepository kilometrageRepository;

    @Autowired
    ClientCriteriaRepository clientCriteriaRepository;

    @Autowired
    PredictedRepository predictedRepository;

    @PersistenceContext
    EntityManager em;

    @GetMapping("/clients")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> motlt1(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {
            List<Client> clients = new ArrayList<Client>();
            Pageable paging = PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize());


            Page<Client> slm = clientCriteriaRepository.findAllFiltered(clientPage, clientSearchCriteria);
            //System.out.println(clientSearchCriteria);
            clients = slm.getContent();
            Map<String, Object> response = new HashMap<>();

            response.put("clients", clients);
            response.put("currentPage", clientPage.getPageNumber());

            //response.put("totalClients", clientPage.TotalElements());
            response.put("totalPages", clientPage.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //test
    @GetMapping("/clients/test")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> test25(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {
            List<Object[]> clients = new ArrayList<Object[]>();
            Pageable paging = PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize());


            Page<Object[]> response1 = clientCriteriaRepository.test1(clientPage, clientSearchCriteria);
            //System.out.println(clientSearchCriteria);
             clients = response1.getContent();
            Map<String, Object> response = new HashMap<>();

            response.put("clients", clients);
            response.put("currentPage", clientPage.getPageNumber());

            //response.put("totalClients", clientPage.TotalElements());
            response.put("totalPages", clientPage.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //trouver un client par son id
    @GetMapping("/clients/{id}")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Client> getClientById(@PathVariable("id") long id) {
        System.out.println(" @GetMapping(/clients/{id}) "+ id);

        Optional<Client> clientData = clientRepository.findById(id);

        if (clientData.isPresent()) {
            return new ResponseEntity<>(clientData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //modifier un client
    @PutMapping("/clients/{id}")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Client> updateClient(@PathVariable("id") long id, @RequestBody Client client) {

        Optional<Client> clientData = clientRepository.findById(id);
        if (clientData.isPresent()) {
            Client _client = clientData.get();
            _client.setName(client.getName());
            _client.setCode(client.getCode());
            _client.setSexe(client.getSexe());
            _client.setNum_tel1(client.getNum_tel1());
            _client.setNum_tel2(client.getNum_tel2());
            _client.setNum_tel3(client.getNum_tel3());
            _client.setAdresse(client.getAdresse());
            _client.setEmail(client.getEmail());
            _client.setVille(client.getVille());
            _client.setCodePostal(client.getCodePostal());
            _client.setTypologie(client.getTypologie());
            _client.setCommentaire(client.getCommentaire());
            return new ResponseEntity<>(clientRepository.save(_client), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/clients/chart")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<List<Long>> chart(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            List<Long> response = clientCriteriaRepository.findAllFilteredChart(clientPage, clientSearchCriteria);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // something that should execute on 1st day every month @ 00:00
    @Scheduled(cron="0 0 0 1 1/1 *")
    public ResponseEntity<Map<String, Object>> predict1mounth(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {
            List<Object[]> clients = new ArrayList<Object[]>();
            Pageable paging = PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize());
            predictedRepository.deleteAllInBatch();
            List<Object[]> response1 = clientCriteriaRepository.predict(clientPage, clientSearchCriteria);
            //System.out.println("response 1 :"+response1 );

            Date startDate = new Date();
            startDate.setDate(01);
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnly = dateFormat.format(startDate);
            java.util.Date start = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnly);
            Date endDate = new Date();
            endDate.setDate(30);
            SimpleDateFormat dateFormatE= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnlyE = dateFormatE.format(endDate);
            java.util.Date end = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnlyE);

            long diffInMilliesS = Math.abs(start.getTime() - end.getTime());
            System.out.println("diff in millies :"+diffInMilliesS);
            long totmounth = TimeUnit.DAYS.convert(diffInMilliesS, TimeUnit.MILLISECONDS);
            System.out.println("totoal mounth :"+totmounth);

            Date today1 = new Date();
            //endDate.setDate(30);
            SimpleDateFormat dateFormatT= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnlyT = dateFormatT.format(endDate);
            java.util.Date today = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnlyT);

            //clients = response1.getContent();
            List<Kilometrage> kilometrages = new ArrayList<Kilometrage>();
            List<Object[]> clientsP = new ArrayList<>();
            List<Predicted> predicteds = new ArrayList<Predicted>();
            long totalElements = 0;
            //System.out.println(response1.get(1) );
            for(Object[] anObject : response1){
                Object[] fields =  anObject;
                System.out.println("date 1 :"+fields[7]);
                Date date1 = (Date) fields[7];
                if(date1==null) continue;
                String vin = (String) fields[8];
                kilometrages=kilometrageRepository.findByImmatriculation(vin);
                Predicted predicted = new Predicted();
                Kilometrage kilometrage1 =kilometrages.get(0);
                Date date2 = kilometrage1.getDatePrise();
                System.out.println("date 2 :"+date2);
                if(date2==null) continue;

                //Date date2 = (Date) fields[8];
                //Long kilometrage = (Long) fields[9];
                Long kilometrage = kilometrage1.getKilometrage() ;
                System.out.println("kilometrage :"+kilometrage);
                if(kilometrage==null) continue;
                long diffInMillies = Math.abs(date1.getTime() - date2.getTime());
                System.out.println("diff de temps entre achat et prise kilometrage en millisec :"+diffInMillies);
                long tot = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                System.out.println("en jours :"+tot);
//duree pour atteindre 10 000 km
                double Dm = ((double)tot*10000) / kilometrage;
                System.out.println("Dm,duree pour atteindre 10 000 km :"+Dm);
//duree depuis date contrat a aujourd'hui  end => today
                long diffInMillie = Math.abs(end.getTime() - date1.getTime());
                long a = TimeUnit.DAYS.convert(diffInMillie, TimeUnit.MILLISECONDS);
                System.out.println("a duree entre date achat et aujourd'hui :"+a);
                double x =a/Dm;
                System.out.println("a/DM :"+x);

                long c = (long)x;
                double roundDbl = Math.round(x*10000.0)/10000.0;
                System.out.println(roundDbl);
                double Dp = (1 -( roundDbl-c ));

                if(diffInMillies==0 || tot == 0 || Dm ==0.0) continue;
                double rkmli7 = c*Dm + Dp;
                System.out.println("rk mli7 :"+rkmli7);

                LocalDate date11 = date1.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
//add 5 days
                LocalDate datepred = date11.plusDays((long) rkmli7);
                System.out.println("Date Predicted : "+datepred);
                Date pred =java.sql.Date.valueOf(datepred);

                if(start.compareTo(pred) *pred.compareTo(end) >= 0) {
                    clientsP.add(anObject);
                    predicted.setId((Long) fields[0]);
                    predicted.setMarque((String) fields[3]);
                    predicted.setModele((String) fields[4]);
                    predicted.setTypologie((String) fields[2]);
                    predicted.setNom((String) fields[1]);
                    predicted.setDateComptabilisation(date1);
                    predicted.setSuccersale((String) fields[5]);
                    predicted.setVendeur((String) fields[6]);
                    predicteds.add(predicted);
                    totalElements ++;
                }
            }

            System.out.println("clientsP :"+clientsP);
            predictedRepository.saveAll(predicteds);
            //traitement 3la clients -->
            System.out.println("hrira slkt ");
            Map<String, Object> response = new HashMap<>();

            response.put("clients", clientsP);
            response.put("currentPage", clientPage.getPageNumber());
            response.put("totalPages", clientPage.getTotalElements());
            //response.put("totalPages",totalElements);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------------
    @GetMapping("/clients/predict")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> predict(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {
            List<Object[]> clients = new ArrayList<Object[]>();
            Pageable paging = PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize());
            predictedRepository.deleteAllInBatch();
            List<Object[]> response1 = clientCriteriaRepository.predict(clientPage, clientSearchCriteria);
            //System.out.println("response 1 :"+response1 );

            Date startDate = new Date();
            startDate.setDate(01);
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnly = dateFormat.format(startDate);
            java.util.Date start = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnly);
            Date endDate = new Date();
            endDate.setDate(30);
            SimpleDateFormat dateFormatE= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnlyE = dateFormatE.format(endDate);
            java.util.Date end = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnlyE);

            long diffInMilliesS = Math.abs(start.getTime() - end.getTime());
            System.out.println("diff in millies :"+diffInMilliesS);
            long totmounth = TimeUnit.DAYS.convert(diffInMilliesS, TimeUnit.MILLISECONDS);
            System.out.println("totoal mounth :"+totmounth);

            Date today1 = new Date();
            //endDate.setDate(30);
            SimpleDateFormat dateFormatT= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnlyT = dateFormatT.format(endDate);
            java.util.Date today = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnlyT);

            //clients = response1.getContent();
            List<Kilometrage> kilometrages = new ArrayList<Kilometrage>();
            List<Object[]> clientsP = new ArrayList<>();
            List<Predicted> predicteds = new ArrayList<Predicted>();
            long totalElements = 0;
            //System.out.println(response1.get(1) );
            for(Object[] anObject : response1){
                Object[] fields =  anObject;
                System.out.println("date 1 :"+fields[7]);
                Date date1 = (Date) fields[7];
                if(date1==null) continue;
                String vin = (String) fields[8];
                kilometrages=kilometrageRepository.findByImmatriculation(vin);
                Predicted predicted = new Predicted();
                Kilometrage kilometrage1 =kilometrages.get(0);
                Date date2 = kilometrage1.getDatePrise();
                System.out.println("date 2 :"+date2);
                if(date2==null) continue;

                //Date date2 = (Date) fields[8];
                //Long kilometrage = (Long) fields[9];
                Long kilometrage = kilometrage1.getKilometrage() ;
                System.out.println("kilometrage :"+kilometrage);
                if(kilometrage==null) continue;
                long diffInMillies = Math.abs(date1.getTime() - date2.getTime());
                System.out.println("diff de temps entre achat et prise kilometrage en millisec :"+diffInMillies);
                long tot = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                System.out.println("en jours :"+tot);
//duree pour atteindre 10 000 km
                double Dm = ((double)tot*10000) / kilometrage;
                System.out.println("Dm,duree pour atteindre 10 000 km :"+Dm);
//duree depuis date contrat a aujourd'hui  end => today
                long diffInMillie = Math.abs(end.getTime() - date1.getTime());
                long a = TimeUnit.DAYS.convert(diffInMillie, TimeUnit.MILLISECONDS);
                System.out.println("a duree entre date achat et aujourd'hui :"+a);
                double x =a/Dm;
                System.out.println("a/DM :"+x);
                //long a =  ChronoUnit.MONTHS.between(end,date1) % Dm;
                //long D = (long) (a*Dm);


                //double value = 12.3457652133

                //double value =Double.parseDouble(new DecimalFormat("##.####").format(x));
                long c = (long)x;
                double roundDbl = Math.round(x*10000.0)/10000.0;
                System.out.println(roundDbl);
                double Dp = (1 -( roundDbl-c ));

                /*DecimalFormat df2 = new DecimalFormat(".####");
                df2.setRoundingMode(RoundingMode.UP);
                System.out.println("double : " + df2.format(x));
*/
                if(diffInMillies==0 || tot == 0 || Dm ==0.0) continue;
                double rkmli7 = c*Dm + Dp;
                System.out.println("rk mli7 :"+rkmli7);

                LocalDate date11 = date1.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

//add 5 days
                LocalDate datepred = date11.plusDays((long) rkmli7);
                System.out.println("Date Predicted : "+datepred);
/*
                LocalDateTime ldt = LocalDateTime.from(date1.toInstant()).plusDays(D);
                System.out.println(ldt);
                Date pred = java.sql.Timestamp.valueOf(ldt);
*/
            Date pred =java.sql.Date.valueOf(datepred);

                if(start.compareTo(pred) *pred.compareTo(end) >= 0) {
                    clientsP.add(anObject);
                    predicted.setId((Long) fields[0]);
                    predicted.setMarque((String) fields[3]);
                    predicted.setModele((String) fields[4]);
                    predicted.setTypologie((String) fields[2]);
                    predicted.setNom((String) fields[1]);
//--------
                    SimpleDateFormat formatter27 = new SimpleDateFormat(
                            "dd/MM/yyyy");
                    Date newpredDate = formatter27.parse(formatter27.format(date1));
                    predicted.setDateComptabilisation(newpredDate);
                    predicted.setSuccersale((String) fields[5]);
                    predicted.setVendeur((String) fields[6]);
                    predicteds.add(predicted);
                    totalElements ++;
                }
            }

            System.out.println("clientsP :"+clientsP);

            predictedRepository.saveAll(predicteds);
            //traitement 3la clients -->
            System.out.println("hrira slkt ");
            Map<String, Object> response = new HashMap<>();

            response.put("clients", clientsP);
            response.put("currentPage", clientPage.getPageNumber());
            response.put("totalPages", clientPage.getTotalElements());
            //response.put("totalPages",totalElements);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //'----------------------------------
    @GetMapping("/clients/p")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> pred(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {
            List<Object[]> clients = new ArrayList<Object[]>();
            Pageable paging = PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize());
            List<Predicted> predicteds = predictedRepository.findAll();
            System.out.println(predicteds.size());
            Map<String, Object> response = new HashMap<>();
            System.out.println(predicteds.get(1).getId() +": date :" +predicteds.get(1).getDateComptabilisation());
            response.put("clients", predicteds);
            response.put("currentPage", clientPage.getPageNumber());
            response.put("totalPages", clientPage.getTotalElements());
            //response.put("totalPages",totalElements);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //------------------------------------
    @GetMapping("/clients/sexe")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<List<Object[]>> motalt(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            List<Object[]> response = clientCriteriaRepository.findAllSexeChart(clientPage, clientSearchCriteria);
            //System.out.println((response));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients/typologie")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<List<Object[]>> motalt1(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            List<Object[]> response = clientCriteriaRepository.findAllTypologieChart(clientPage, clientSearchCriteria);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/clients/marque")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<List<Object[]>> motalt2(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            List<Object[]> response = clientCriteriaRepository.findAllMarqueChart(clientPage, clientSearchCriteria);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/clients/modele")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<List<Object[]>> motalt3(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            List<Object[]> response = clientCriteriaRepository.findAllModeleChart(clientPage, clientSearchCriteria);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//-------->>>>>>>>

    //--------->>>>>>>>>>>>

    @GetMapping("/clients/modeleList")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<Long> motalt3tab(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            Long response = clientCriteriaRepository.findAllModeleLong(clientPage, clientSearchCriteria);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/clients/ptVente")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<List<Object[]>> motalt4(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {

            List<Object[]> response = clientCriteriaRepository.findAllPtVenteChart(clientPage, clientSearchCriteria);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//----------------------------------------------------------------
@GetMapping("/clients/mh")
@PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
public ResponseEntity<List<Object[]>> mh(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
    try {
    List<Kilometrage> kilometrages=new ArrayList<Kilometrage>();
    List<Voiture> voitures =voitureRepository.findAll();
    //Voiture voiture = voitures.get(91824);
        //System.out.println(voiture.getImmatriculation());
    for (Voiture voiture : voitures){
        Kilometrage kilometrage = new Kilometrage();
        Optional<Kilometrage> optKilo = kilometrageRepository.findById(voiture.getId());
        if(optKilo.isPresent()) continue;

            kilometrage.setImmatriculation(voiture.getImmatriculation());
            kilometrage.setId(voiture.getId());

        kilometrages.add(kilometrage);
        System.out.println("----------kilometrage--------:"+kilometrage.getKilometrage());
    }
        System.out.println(kilometrages.size());
        System.out.println(voitures.size());
        System.out.println("");
        kilometrageRepository.saveAll(kilometrages);
        List<Object[]> response = clientCriteriaRepository.findAllPtVenteChart(clientPage, clientSearchCriteria);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

//-------------------------
  /*  @GetMapping("/teeeest")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public HttpEntity<Long> teeeeest() {
        try {
            Client c ;
            List<Client> clientList = new ArrayList<Client>();
            clientList =clientRepository.findAll();
            Long response =clientList.get(clientList.size() - 1).getId();
            //Long response = clientRepository.count();
            return new ResponseEntity<Long>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
  public String printRow(Object[] row) {
      StringBuilder s = new StringBuilder();
      for (Object object : row) {
          System.out.println(object);
          s.append(object + "\n");
      }
      return s.toString();
  }

}

