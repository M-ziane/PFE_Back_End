package com.bezkoder.springjwt.controllers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.bezkoder.springjwt.models.Client;
import com.bezkoder.springjwt.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;
//liste de tous les clients
    @GetMapping("/clients")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Client>> getAllClients(@RequestParam(required = false) String name) {
        try {
            List<Client> clients = new ArrayList<Client>();

            if (name == null)
                clientRepository.findAll().forEach(clients::add);
            else
                clientRepository.findByNameContaining(name).forEach(clients::add);
                //Name ??
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
//créer un client
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
            _client.setPublished(client.isPublished());
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
//les clients published
    @GetMapping("/clients/published")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Client>> findByPublished() {
        try {
            List<Client> clients = clientRepository.findByPublished(true);

            if (clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}