package com.pfe.project.service;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.List;

import com.pfe.project.models.Client;
import com.pfe.project.models.ClientSearchCriteria;
import com.pfe.project.repository.ClientCriteriaRepository;
import com.pfe.project.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pfe.project.helper.ExcelHelper;


@Service
public class ExcelService {
    @Autowired
    ClientRepository repository;

    @Autowired
    ClientCriteriaRepository clientCriteriaRepository;

    public ByteArrayInputStream load(ClientSearchCriteria clientSearchCriteria) throws ParseException {
        //List<Client> tutorials = repository.findAll();
        List<Object[]> objects = clientCriteriaRepository.Excela(clientSearchCriteria) ;

        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(objects);
        return in;
    }

}