package com.pfe.project.controllers;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;


import com.pfe.project.models.*;
import com.pfe.project.repository.ClientCriteriaRepository;
import com.pfe.project.repository.ClientRepository;
import com.pfe.project.repository.ContratRepository;
import com.pfe.project.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ContratRepository contratRepository;

    @Autowired
    ClientCriteriaRepository clientCriteriaRepository;

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

    //------------------
    @GetMapping("/clients/predict")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING') or hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> predict(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
        try {
            List<Object[]> clients = new ArrayList<Object[]>();
           // Pageable paging = PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize());

            Page<Object[]> response1 = clientCriteriaRepository.predict(clientPage, clientSearchCriteria);
            //System.out.println(clientSearchCriteria);


            Date startDate = new Date();
            startDate.setDate(01);
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnly = dateFormat.format(startDate);
            java.util.Date start = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnly);

//----
            Date endDate = new Date();
            endDate.setDate(30);
            SimpleDateFormat dateFormatE= new SimpleDateFormat("yyyy-MM-dd");
            String dateOnlyE = dateFormatE.format(endDate);
            java.util.Date end = new SimpleDateFormat("yyyy-MM-dd").parse(dateOnlyE);

            System.out.println("hrira slkt ");
            clients = response1.getContent();
            List<Object[]> clientsP = new ArrayList<>();
            long totalElements = 0;
            for(Object[] anObject : clients){
                Object[] fields =  anObject;
                System.out.println(fields[7]);
                Date date1 = (Date) fields[7];
                System.out.println(date1);

                Date date2 = (Date) fields[8];
                Long kilometrage = (Long) fields[9];

                long diffInMillies = Math.abs(date1.getTime() - date2.getTime());
                long tot = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);


                long Dm = (tot*10000) / kilometrage;

                long diffInMillie = Math.abs(end.getTime() - date1.getTime());
                long a = TimeUnit.DAYS.convert(diffInMillie, TimeUnit.MILLISECONDS);

                //long a =  ChronoUnit.MONTHS.between(end,date1) % Dm;
                long D = a*Dm;

                LocalDateTime ldt = LocalDateTime.from(date1.toInstant()).plusDays(D);
                System.out.println(ldt);
                Date pred = java.sql.Timestamp.valueOf(ldt);


                if(start.compareTo(pred) *pred.compareTo(end) >= 0) {
                    clientsP.add(anObject);
                    totalElements ++;
                }
            }
            System.out.println(clientsP);
            //traitement 3la clients -->

            Map<String, Object> response = new HashMap<>();

            response.put("clients", clients);
            response.put("currentPage", clientPage.getPageNumber());
            response.put("totalPages", clientPage.getTotalElements());
            //response.put("totalPages",totalElements);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
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

