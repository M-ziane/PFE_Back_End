package com.pfe.project.controllers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.pfe.project.models.Client;
import com.pfe.project.models.Voiture;
import com.pfe.project.repository.TutorialRepository;
import com.pfe.project.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;
    @Autowired
    VoitureRepository voitureRepository ;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @GetMapping("/tutorials")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")

    public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
            @RequestParam(required = false) String name,
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
            List<Client> clients = new ArrayList<Client>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<Client> pageTuts;
            if (name == null){
                pageTuts = tutorialRepository.findAll(pagingSort);
                System.out.println(11);
            }
            else {
                System.out.println(2);
                pageTuts = tutorialRepository.findByNameContaining(name, pagingSort);
                System.out.println("filter by  name");
            }
            clients = pageTuts.getContent();
            System.out.println(3);
            Map<String, Object> response = new HashMap<>();
            response.put("clients", clients);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalClients", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> findByPublished(
            @RequestParam(required = false) boolean sexe,
            @RequestParam(required = false) String typologie,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {

        try {
            List<Client> clients = new ArrayList<Client>();
            Pageable paging = PageRequest.of(page, size);

            Page<Client> pageTuts ;
                    //= tutorialRepository.findBySexeAndTypologie(sexe,typologie,paging);

            //fornt end ====> "".equals(typologie)
            if ("".equals(typologie)) pageTuts = tutorialRepository.findBySexe(sexe , paging);
            //else if ((sexe != true) && (sexe != false)) pageTuts = tutorialRepository.findByTypologie(typologie, paging);
            else pageTuts = tutorialRepository.findBySexeAndTypologie(sexe,typologie,paging);

            //System.out.println((sexe != true) && (sexe != false));
            System.out.println(sexe);  //sexe = false
            System.out.println(typologie);

            clients = pageTuts.getContent();
            System.out.println(clients);
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
    /*
    @GetMapping("/tutorials/vtr")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<Map<String, Object>> getTutorialById( @RequestParam(required = false) String marque,
                                                   @RequestParam(required = false) String modele,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "9") int size) {
        System.out.println(marque);
        System.out.println(modele);
        try {

            List<Client> tutorials = new ArrayList<Client>();
            Pageable paging = PageRequest.of(page, size);
            Page<Client> pageTuts ;

            // (I)
            List<Voiture> PageTutsV ;
            //List<Voiture> voiture = voitureRepository.findByMarqueAndModele(marque,modele);
            if ("".equals(modele))
                PageTutsV = voitureRepository.findByMarque(marque);
            else {
                PageTutsV=voitureRepository.findByMarqueAndModele(marque,modele);
            }
            // (II)


            tutorials = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            System.out.println(tutorials);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //---------------
       @GetMapping("/sortedtutorials")
    @PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
    public ResponseEntity<List<Client>> getAllTutorials(@RequestParam(defaultValue = "id,desc") String[] sort) {
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
            List<Client> tutorials = tutorialRepository.findAll(Sort.by(orders));
            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */
}
