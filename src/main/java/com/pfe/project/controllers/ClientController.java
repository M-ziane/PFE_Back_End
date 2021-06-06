package com.pfe.project.controllers;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;


import com.pfe.project.models.*;
import com.pfe.project.repository.ClientCriteriaRepository;
import com.pfe.project.repository.ClientRepository;
import com.pfe.project.repository.ContratRepository;
import com.pfe.project.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
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
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

    @GetMapping("/clients/date")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> getCotratByDate(@RequestParam(required = false) String datestart,
                                                               @RequestParam(required = false) String dateend,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "6") int size,
                                                               @RequestParam(defaultValue = "id,desc") String[] sort) {
        try {
            java.util.Date start = new SimpleDateFormat("yyyy-MM-dd").parse(datestart);
            Date end =new SimpleDateFormat("yyyy-MM-dd").parse(dateend);

            List<Client> clients = new ArrayList<Client>();
            Pageable paging = PageRequest.of(page, size);

            List<Contrat> contrats = contratRepository.findByDateComptabilisationBetween(start, end);
            Page<Client> pageTuts = clientRepository.findByContratIn(contrats, paging);

            clients = pageTuts.getContent();

            System.out.println(clients);
            //--------
            Map<String, Object> response = new HashMap<>();
            response.put("clients", clients);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalClients", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/clients/test")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> testtest(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String marque,
                                                        @RequestParam(required = false) String modele,
                                                        @RequestParam(required = false) Boolean sexe,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size,
                                                        @RequestParam(defaultValue = "id,desc") String[] sort) {
        try {

            List<Client> clients = new ArrayList<Client>();
            Pageable paging = PageRequest.of(page, size);
            List<Voiture> vtrs = new ArrayList<Voiture>();
            List<Contrat> contrats = new ArrayList<Contrat>();
            Page<Client> pageTuts = new PageImpl<>(clients);
            if (name != null) {
                pageTuts = clientRepository.findByNameContaining(name, paging);
            } else {
                if (marque == null) {
                    if (sexe == null) pageTuts = clientRepository.findAll(paging);
                    else pageTuts = clientRepository.findBySexe(sexe, paging);
                } else {
                    if (sexe == null && modele == null) {
                        vtrs = voitureRepository.findByMarque(marque);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findByContratIn(contrats, paging);
                    } else if (sexe == null) {
                        vtrs = voitureRepository.findByMarqueAndModele(marque, modele);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findByContratIn(contrats, paging);
                    } else if (modele == null) {
                        vtrs = voitureRepository.findByMarque(marque);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findBySexeAndContratIn(sexe, contrats, paging);
                    } else {
                        vtrs = voitureRepository.findByMarqueAndModele(marque, modele);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findBySexeAndContratIn(sexe, contrats, paging);
                    }
                }
            }
            clients = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("clients", clients);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalClients", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/clients/motalt")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Long>> motlt2(@RequestParam(required = false) String param) {
        try {
            List<Voiture> vtrs = new ArrayList<Voiture>();
            List<Contrat> contrats = new ArrayList<Contrat>();

            List<Long> slm = new ArrayList<Long>();
            switch (param){
                case "0" :
                    slm.add(clientRepository.countBySexe("homme"));
                    slm.add(clientRepository.countBySexe("femme"));
                    slm.add(clientRepository.countBySexe("other"));
                    slm.add(clientRepository.countBySexe(null));
                    break;
                case "1" :
                    vtrs = voitureRepository.findByMarque("renault");
                    contrats = contratRepository.findByVoitureAIn(vtrs);
                    slm.add(clientRepository.countByContratIn(contrats));

                    vtrs = voitureRepository.findByMarque("dacia");
                    contrats = contratRepository.findByVoitureAIn(vtrs);
                    slm.add(clientRepository.countByContratIn(contrats));

                    vtrs = voitureRepository.findByMarque(null);
                    contrats = contratRepository.findByVoitureAIn(vtrs);
                    slm.add(clientRepository.countByContratIn(contrats));
                    break;
                default: break;
            }


            System.out.println(slm);
            return new ResponseEntity<>(slm, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

/*
    @GetMapping("/clients")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> getSexeandMarque(@RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String marque,
                                                                @RequestParam(required = false) String modele,
                                                                @RequestParam(required = false) Boolean sexe,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "6") int size,
                                                                @RequestParam(defaultValue = "id,desc") String[] sort) {
        try {

            List<Client> clients = new ArrayList<Client>();
            Pageable paging = PageRequest.of(page, size);
            List<Voiture> vtrs = new ArrayList<Voiture>();
            List<Contrat> contrats = new ArrayList<Contrat>();
            Page<Client> pageTuts = new PageImpl<>(clients);
            if (name != null) {
                pageTuts = clientRepository.findByNameContaining(name, paging);
            } else {
                if (marque == null && modele == null && sexe == null) {  //("".equals(marque) && "".equals(modele) && sexe == null)
                    pageTuts = clientRepository.findAll(paging);
                } else if (marque == null && modele == null) {
                    pageTuts = clientRepository.findBySexe(sexe, paging);
                    System.out.println(pageTuts);
                }
                //working
                else if (marque == null) { //"".equals(marque)
                    if (sexe == null) {
                        vtrs = voitureRepository.findByModele(modele);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findByContratIn(contrats, paging);
                    } else {
                        vtrs = voitureRepository.findByModele(modele);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findBySexeAndContratIn(sexe, contrats, paging);
                    }
                }//working
                else if (modele == null) {    //"".equals(modele)
                    System.out.println(modele == null);
                    System.out.println(sexe == null);
                    if (sexe == null) {
                        System.out.println("modele==null & sexe = null");
                        vtrs = voitureRepository.findByMarque(marque);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        System.out.println("sexe=" + sexe);
                        System.out.println("modele=" + modele);
                        pageTuts = clientRepository.findByContratIn(contrats, paging);
                        System.out.println(pageTuts);
                    } else {
                        System.out.println("modele==null & sexe =!null");
                        vtrs = voitureRepository.findByMarque(marque);
                        System.out.println("sexe=" + sexe);
                        System.out.println("modele=" + modele);
                        contrats = contratRepository.findByVoitureAIn(vtrs);
                        pageTuts = clientRepository.findBySexeAndContratIn(sexe, contrats, paging);
                        System.out.println(pageTuts);
                    }
                } //working
                else if (sexe == null) {
                    System.out.println("sexe == null");
                    vtrs = voitureRepository.findByMarqueAndModele(marque, modele);
                    contrats = contratRepository.findByVoitureAIn(vtrs);
                    pageTuts = clientRepository.findByContratIn(contrats, paging);
                    System.out.println(pageTuts);
                }//working
                else {
                    System.out.println("else");
                    vtrs = voitureRepository.findByMarqueAndModele(marque, modele);
                    contrats = contratRepository.findByVoitureAIn(vtrs);
                    pageTuts = clientRepository.findBySexeAndContratIn(sexe, contrats, paging);
                }
            }
            clients = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("clients", clients);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalClients", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


  */