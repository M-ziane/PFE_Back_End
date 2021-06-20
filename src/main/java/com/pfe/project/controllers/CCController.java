package com.pfe.project.controllers;
import com.pfe.project.models.CC;
import com.pfe.project.models.Client;
import com.pfe.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CCController {

    @Autowired
    CCRepository ccRepository;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @GetMapping("/cc")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
//Map<String, Object>
    public ResponseEntity<Map<String, Object>> getAllSucc(
            @RequestParam(required = false) String ax,
            //@RequestParam(required = false) int numEquipe,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<CC> clients = new ArrayList<CC>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<CC> pageTuts;


            //if(numEquipe == 0) {

                if (ax == null) {
                    System.out.println("madkhlch ");
                    pageTuts = ccRepository.findAll(pagingSort);

                } else {
                    pageTuts = ccRepository.findByAxContaining(ax, pagingSort);


                }
           /* }
            else{
                pageTuts=ccRepository.findByNumEquipe(numEquipe,pagingSort);
            }

            */
            //List<CC> pageTuts = ccRepository.findBySuccNull();
            //Page<CC> pageTuts = ccRepository.findAll(pagingSort);


            clients = pageTuts.getContent();
            //List<CC> response = pageTuts;
            Map<String, Object> response = new HashMap<>();
            response.put("cc", clients);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalClients", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cc")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<CC> addCC(@RequestBody CC cc) {
        try {
            CC _cc = ccRepository
                    .save(new CC(cc.getNom(), cc.getAx(),cc.getSucc(),cc.getNumEquipe(),cc.getChef()));
            return new ResponseEntity<>(_cc, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cc/Succ")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> getAllSuccNull(
            @RequestParam(defaultValue = "5" ) Integer count ,
            @RequestParam(defaultValue = "0") Integer numEquipe,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }
            List<CC> clients = new ArrayList<CC>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<CC> pageTuts = ccRepository.findAll(pagingSort);
            if(numEquipe == 0) {

                if(count == 1 ){
                    pageTuts = ccRepository.findBySucc("  \r",pagingSort);
                }
                else if(count ==2 ){
                    pageTuts = ccRepository.findByNom("  ",pagingSort);
                }
                else if(count == 3){
                     pageTuts = ccRepository.findBySuccAndNom("  \r","  ",pagingSort);
                }
                else{
                    ccRepository.findAll(pagingSort);
                }
            }
            else{                pageTuts=ccRepository.findByNumEquipe(numEquipe,pagingSort);}
            System.out.println(pageTuts);
            clients = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("cc", clients);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalClients", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cc/{id}")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<CC> getCCById(@PathVariable("id") Integer id) {
        Long myid = Long.valueOf(id);
        Optional<CC> clientData = ccRepository.findById(myid);

        if (clientData.isPresent()) {
            return new ResponseEntity<>(clientData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cc/{id}")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<CC> updateCC(@PathVariable("id") Integer id, @RequestBody CC cc) {
        Long myid = Long.valueOf(id);
        Optional<CC> clientData = ccRepository.findById(myid);
        System.out.println(cc.getNumEquipe());
        System.out.println(cc.getChef());
        if (clientData.isPresent()) {
            CC _cc = clientData.get();
            _cc.setNom(cc.getNom());
            _cc.setAx(cc.getAx());
            _cc.setSucc(cc.getSucc());
            _cc.setNumEquipe(cc.getNumEquipe());
            _cc.setChef(cc.getChef());


            return new ResponseEntity<>(ccRepository.save(_cc), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}