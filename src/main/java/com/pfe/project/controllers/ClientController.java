package com.pfe.project.controllers;
import java.util.*;

import com.pfe.project.models.Client;
import com.pfe.project.models.Contrat;
import com.pfe.project.models.Voiture;
import com.pfe.project.repository.ClientRepository;
import com.pfe.project.repository.ContratRepository;
import com.pfe.project.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ClientController {
    //List<Client> findByFilter(String typologie,boolean sexe);*

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    VoitureRepository voitureRepository ;

    @Autowired
    ContratRepository contratRepository ;

//liste de tous les clients
    @GetMapping("/clients")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Client>> getAllClients(@RequestParam(required = false) String name) {
        try {
            List<Client> clients = new ArrayList<Client>();
            if (name == null){
                //clientRepository.findBySexe(false ).forEach(clients::add);
                //clientRepository.findBySexe(true ).forEach(clients::add);
                clientRepository.findAll().forEach(clients::add);
                }
            else{
                clientRepository.findByNameContaining(name).forEach(clients::add);
                }
            if (clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-------------------------------------

/*
    @GetMapping("/clientsT")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<ClientSummary>> getAllClientsT(@RequestParam(required = false) String name) {
        try {
            List<ClientSummary> clients = new ArrayList<ClientSummary>();

                clientRepository.getAll().forEach(clients::add);

            if (clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

*/
    //-------------------------------------
    //filtrer les clients
    @GetMapping("/clientsf")
    @PreAuthorize("hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Client>> getAllClientsF(@RequestParam(required = false) Boolean sexe){
        try {
            List<Client> clients = new ArrayList<Client>();
            if (sexe == null){
                clientRepository.findAll().forEach(clients::add);
            }
            else{
                clients = clientRepository.findBySexe(sexe);
            }
            if (clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
    //trouver les clients apres filtrage
    @GetMapping("/clients/filter")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Client>> filterClient(@RequestParam(required = false) String typologie) {

        try {
            List<Client> clients = clientRepository.findByTypologie(typologie);
            System.out.println(clients);
            //clientRepository= (ClientRepository) clientRepository.findByTypologie(typologie);
            //clients=clientRepository.findBySexe(false);
            System.out.println(clientRepository);
            System.out.println(clients);
            if (clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//créer un client // à enlever
    @PostMapping("/clients")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        try {
            Client _client = clientRepository
                    .save(new Client(client.getName(), client.getCode(), false));
            return new ResponseEntity<>(_client, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
            _client.setSexe(client.isSexe());
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
//supprimer un client

    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") long id) {
        try {
            clientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//supprimer tous les clients

    @DeleteMapping("/clients")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<HttpStatus> deleteAllClients() {
        try {
            clientRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
//
    @GetMapping("/clients/bouti")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> getCotratByVoiture(@RequestParam(required = false) String marque,
                                                     @RequestParam(required = false) String modele, @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size,
                                                     @RequestParam(defaultValue = "id,desc") String[] sort
                                                     )
    {         try{

                    List<Client> clients = new ArrayList<Client>();
                    Pageable paging = PageRequest.of(page, size);

                    /*
                    if( ("".equals(marque)) and ("".equals(modele)) ) clientRepository.findAll()
                    else if("".equals(modele)) clientRepository.findByMarque(marque)
                    else if ("".equals(marque)) client repository.findByModele(modele)
                    else{}
                    */


                    List<Voiture> vtrs = voitureRepository.findByMarqueAndModele(marque , modele);

                    List<Contrat> contrats = contratRepository.findByVoitureAIn(vtrs);

                    Page<Client> pageTuts = clientRepository.findByContratIn(contrats,paging);

                    clients = pageTuts.getContent();

                    //System.out.println(clients);
                    //--------
                    Map<String, Object> response = new HashMap<>();
                    response.put("clients", clients);
                    response.put("currentPage", pageTuts.getNumber());
                    response.put("totalClients", pageTuts.getTotalElements());
                    response.put("totalPages", pageTuts.getTotalPages());


                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

    }

    @GetMapping("/clients/bouti/date")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> getCotratByDate(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd")  Date datestart,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd")  Date dateend,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "6") int size,
                                                               @RequestParam(defaultValue = "id,desc") String[] sort
    )
    {         try{

        List<Client> clients = new ArrayList<Client>();
        Pageable paging = PageRequest.of(page, size);

                    /*
                    if( ("".equals(marque)) and ("".equals(modele)) ) clientRepository.findAll()
                    else if("".equals(modele)) clientRepository.findByMarque(marque)
                    else if ("".equals(marque)) client repository.findByModele(modele)
                    else{}
                    */

        List<Contrat> contrats = contratRepository.findByDateComptabilisationBetween(datestart,dateend);
        Page<Client> pageTuts = clientRepository.findByContratIn(contrats,paging);

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
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }

   // */
}