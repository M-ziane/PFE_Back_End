package com.pfe.project.repository;


import com.pfe.project.models.*;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class ClientCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ClientCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Client> findAllFiltered(ClientPage employeePage,
                                           ClientSearchCriteria employeeSearchCriteria) throws ParseException {

        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> fromClients = criteriaQuery.from(Client.class);
        Join<Client, Contrat> details = fromClients.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");
        List<Predicate> conditions = new ArrayList();

        if(Objects.nonNull(employeeSearchCriteria.getMarque())){
            conditions.add(criteriaBuilder.equal(voiture.get("marque"), employeeSearchCriteria.getMarque()));
        }
        if(Objects.nonNull(employeeSearchCriteria.getModele())){
            conditions.add(criteriaBuilder.equal(voiture.get("modele"), employeeSearchCriteria.getModele()));
        }
        if(Objects.nonNull(employeeSearchCriteria.getName())){
            System.out.println(employeeSearchCriteria.getName());
            conditions.add(
                    criteriaBuilder.like(fromClients.get("name"),
                            "%" + employeeSearchCriteria.getName() + "%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getTypologie())){
            System.out.println(employeeSearchCriteria.getTypologie());
            conditions.add(
                    criteriaBuilder.like(fromClients.get("typologie"),
                            "%" + employeeSearchCriteria.getTypologie() + "%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getSexe())){
            System.out.println(employeeSearchCriteria.getSexe());
            System.out.println(fromClients.get("sexe"));
            conditions.add(
                    criteriaBuilder.equal(fromClients.get("sexe"),employeeSearchCriteria.getSexe())
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getstartDate())&&Objects.nonNull(employeeSearchCriteria.getEndDate())){

            java.util.Date start = new SimpleDateFormat("dd/MM/yyyy").parse(employeeSearchCriteria.getstartDate());
            System.out.println(start);
            Date end =new SimpleDateFormat("yyyy-MM-dd").parse(employeeSearchCriteria.getEndDate());
            System.out.println(end);
            conditions.add(
                    criteriaBuilder.between(details.get("dateComptabilisation"), start,end)
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.isMdt())) {
            System.out.println(employeeSearchCriteria.isMdt());
            conditions.add(
                    criteriaBuilder.equal(details.get("modalitePaiement"),employeeSearchCriteria.isMdt())
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getPtVente())){
            System.out.println(employeeSearchCriteria.getPtVente());
            conditions.add(
                    criteriaBuilder.equal(details.get("pointVente"),employeeSearchCriteria.getPtVente())
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getCommerciale())){
            System.out.println(employeeSearchCriteria.getCommerciale());
            conditions.add(
                    criteriaBuilder.equal(details.get("nomVendeur"),employeeSearchCriteria.getCommerciale())
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getUser())){
            conditions.add(
                    criteriaBuilder.equal(details.get("nomVendeur"),employeeSearchCriteria.getUser())
            );
            System.out.println("slm ?");
        }
        setOrder(employeePage, criteriaQuery, fromClients);
        TypedQuery<Client> typedQuery = entityManager.createQuery(criteriaQuery
                .select(fromClients)
                .where(conditions.toArray(new Predicate[] {}))
        );
 //***
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Client> entity_ = countQuery.from(Client.class);
        entity_.alias("entitySub");
        countQuery.select(criteriaBuilder.count(entity_));
        Predicate restriction = this.getPredicate(employeeSearchCriteria,entity_);

        if (restriction != null) {
            countQuery.where(restriction); // Copy restrictions
        }
        System.out.println(restriction);
        Long totalCount=entityManager.createQuery(countQuery).getSingleResult();
        //System.out.println(totalCount);
        Long totalPages = totalCount/employeePage.getPageSize();
        employeePage.setTotalElements(totalPages);
        typedQuery.setFirstResult(employeePage.getPageNumber() * employeePage.getPageSize());
        typedQuery.setMaxResults(employeePage.getPageSize());

        Pageable pageable = getPageable(employeePage);
        List<Client> result = typedQuery.getResultList();
        return PageableExecutionUtils.getPage(result,pageable, () -> totalCount);

    }
    //-----------Test---------------
    public Page<Object[]> test1(ClientPage clientPage,
                                             ClientSearchCriteria employeeSearchCriteria) throws ParseException {


        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        Join<Client, Contrat> details = root.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");
/***/
        CriteriaQuery<Long> countQuery1 = criteriaBuilder.createQuery(Long.class);
        Root<Client> entity1_ = countQuery1.from(Client.class);
        entity1_.alias("entitySub");
        countQuery1.select(criteriaBuilder.count(entity1_));
/***/
        criteriaQuerytest.multiselect(root.get("id"),root.get("name"),root.get("typologie"),voiture.get("marque"),voiture.get("modele"),details.get("pointVente"), details.get("nomVendeur"),details.get("dateComptabilisation"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }
        //******************paging test
        Predicate restriction1 = this.getPredicate(employeeSearchCriteria,entity1_);
        if (restriction1 != null) {
            countQuery1.where(restriction1); // Copy restrictions
            //System.out.println("restriction1:"+restriction1);
        }
        Long totalCount=entityManager.createQuery(countQuery1).getSingleResult();
        System.out.println("total count :"+totalCount);
        Long totalPages = totalCount/clientPage.getPageSize();
        clientPage.setTotalElements(totalPages);
        System.out.println("total pages :" +totalPages);
        //List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();

        List<Object[]> results =  entityManager.createQuery(criteriaQuerytest).setFirstResult(clientPage.getPageNumber() * clientPage.getPageSize()).setMaxResults(clientPage.getPageSize()).getResultList();
        Pageable pageable = getPageable(clientPage);

        Page<Object[]> PageC= PageableExecutionUtils.getPage(results,pageable, () -> totalCount);
        System.out.println(PageC);
        return PageC ;
        //List<Client> results = criteriaQuerytest.getResultList();
    }
    //-----------Test Predict---------------
    public Page<Object[]> predict(ClientPage clientPage,
                                ClientSearchCriteria employeeSearchCriteria) throws ParseException {
        // nzid kilometrage o date prise f multiselect

        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        Join<Client, Contrat> details = root.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");
        Join<Contrat, Kilometrage> kilometrage = details.join("kilometrageC");
        /***/
        CriteriaQuery<Long> countQuery1 = criteriaBuilder.createQuery(Long.class);
        Root<Client> entity1_ = countQuery1.from(Client.class);
        entity1_.alias("entitySub");
        countQuery1.select(criteriaBuilder.count(entity1_));
/***/
        criteriaQuerytest.multiselect(root.get("id"),root.get("name"),root.get("typologie"),voiture.get("marque"),voiture.get("modele"),details.get("pointVente"), details.get("nomVendeur"),details.get("dateComptabilisation"),kilometrage.get("datePrise"),kilometrage.get("kilometrage"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }
        //******************paging test
        Predicate restriction1 = this.getPredicate(employeeSearchCriteria,entity1_);
        if (restriction1 != null) {
            countQuery1.where(restriction1); // Copy restrictions
            //System.out.println("restriction1:"+restriction1);
        }
        Long totalCount=entityManager.createQuery(countQuery1).getSingleResult();
        System.out.println("total count :"+totalCount);
        Long totalPages = totalCount/clientPage.getPageSize();
        clientPage.setTotalElements(totalPages);
        System.out.println("total pages :" +totalPages);
        //List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();

        List<Object[]> results =  entityManager.createQuery(criteriaQuerytest).setFirstResult(clientPage.getPageNumber() * clientPage.getPageSize()).setMaxResults(clientPage.getPageSize()).getResultList();
        Pageable pageable = getPageable(clientPage);

        Page<Object[]> PageC= PageableExecutionUtils.getPage(results,pageable, () -> totalCount);
        System.out.println(PageC);
        return PageC ;
        //List<Client> results = criteriaQuerytest.getResultList();
    }
        //----------------------testChart----
    public List<Long> findAllFilteredChart(ClientPage employeePage,
                                        ClientSearchCriteria employeeSearchCriteria) throws ParseException {

        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> fromClients = criteriaQuery.from(Client.class);
        Join<Client, Contrat> details = fromClients.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");

        //***count Total clients
        CriteriaQuery<Long> countQueryT = criteriaBuilder.createQuery(Long.class);
        Root<Client> entity_ = countQueryT.from(Client.class);
        entity_.alias("entitySub");
        countQueryT.select(criteriaBuilder.count(entity_));
       // List<Long> result1 = entityManager.createQuery(countQueryT).getResultList();
        //System.out.println(result1);
        Long totalCount=entityManager.createQuery(countQueryT).getSingleResult();
        System.out.println(totalCount);

        //*** count clients filtred
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Client> entity1_ = countQuery.from(Client.class);
        entity1_.alias("entitySub");
        countQuery.select(criteriaBuilder.count(entity1_));
        Predicate restriction = this.getPredicate(employeeSearchCriteria,entity1_);

        System.out.println(countQuery.getResultType());

        if (restriction != null) {
            countQuery.where(restriction);
            //countQuery.groupBy(restriction);
        }
        //

        Long Count=entityManager.createQuery(countQuery).getSingleResult();
        System.out.println(Count);
        //Long totalCount=0L;

        List<Long> result = new ArrayList<>();
        result.add(Count);
        result.add(totalCount);
        System.out.println(result);
        return result ;
    }
    //----Usefull fucntions
    private Predicate getPredicate(ClientSearchCriteria employeeSearchCriteria,
                                   Root<Client> employeeRoot) throws ParseException {

        Join<Client, Contrat> details = employeeRoot.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");

        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(employeeSearchCriteria.getMarque())){
            predicates.add(criteriaBuilder.equal(voiture.get("marque"), employeeSearchCriteria.getMarque()));
        }
        if(Objects.nonNull(employeeSearchCriteria.getModele())){
            predicates.add(criteriaBuilder.equal(voiture.get("modele"), employeeSearchCriteria.getModele()));
        }
        if(Objects.nonNull(employeeSearchCriteria.getName())){
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("name"),
                            "%" + employeeSearchCriteria.getName() + "%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getTypologie())){
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("typologie"),
                            "%" + employeeSearchCriteria.getTypologie() + "%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getSexe())){
            predicates.add(
                    criteriaBuilder.equal(employeeRoot.get("sexe"),employeeSearchCriteria.getSexe())
            );
        }
        if (Objects.nonNull(employeeSearchCriteria.isMdt())) {
            predicates.add(
                    criteriaBuilder.equal(details.get("modalitePaiement"),employeeSearchCriteria.isMdt())
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getPtVente())){
            predicates.add(
                    criteriaBuilder.equal(details.get("pointVente"),employeeSearchCriteria.getPtVente())
            );
        }if(Objects.nonNull(employeeSearchCriteria.getCommerciale())){
            predicates.add(
                    criteriaBuilder.equal(details.get("nomVendeur"),employeeSearchCriteria.getCommerciale())
            );
        }
        //à régler
        if(Objects.nonNull(employeeSearchCriteria.getstartDate())&&Objects.nonNull(employeeSearchCriteria.getEndDate())){

            LocalDate dates = LocalDate.parse(employeeSearchCriteria.getstartDate());
            java.util.Date start =java.sql.Date.valueOf(dates);
            LocalDate dateE = LocalDate.parse(employeeSearchCriteria.getEndDate());
            java.util.Date end =java.sql.Date.valueOf(dateE);
            predicates.add(
                    criteriaBuilder.between(details.get("dateComptabilisation"), start,end)
            );

        }
        if(employeeSearchCriteria.getSlm() == 1){
            predicates.add(
                    criteriaBuilder.equal(details.get("nomVendeur"),"AKKARAMOU")
            );
        }/*
        if(Objects.nonNull(employeeSearchCriteria.getPredict())){
            predicates.add(criteriaBuilder.between(details.get("dateComptabilisation"), startDate,endDate));

        }*/
        //System.out.println("slm"+predicates.toArray(new Predicate[] {}).getClass());
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(ClientPage employeePage,
                          CriteriaQuery<Client> criteriaQuery,
                          Root<Client> employeeRoot) {
        if(employeePage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(employeePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(employeePage.getSortBy())));
        }
    }

    private Pageable getPageable(ClientPage employeePage) {
        Sort sort = Sort.by(employeePage.getSortDirection(), employeePage.getSortBy());

        return PageRequest.of(employeePage.getPageNumber(),employeePage.getPageSize(), sort);
    }

    private long getEmployeesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Client> countRoot = countQuery.from(Client.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    //------------Charts

    public List<Object[]> findAllSexeChart(ClientPage clientPage,
                                       ClientSearchCriteria employeeSearchCriteria) throws ParseException {



        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        criteriaQuerytest.multiselect(root.get("sexe"), criteriaBuilder.count(root));
        criteriaQuerytest.groupBy(root.get("sexe"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }
        List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();
        System.out.println(result);
        return result ;
    }
    public List<Object[]> findAllTypologieChart(ClientPage clientPage,
                                           ClientSearchCriteria employeeSearchCriteria) throws ParseException {



        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        criteriaQuerytest.multiselect(root.get("typologie"), criteriaBuilder.count(root));
        criteriaQuerytest.groupBy(root.get("typologie"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }
        List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();
        System.out.println(result);
        return result ;
    }
    public List<Object[]> findAllMarqueChart(ClientPage clientPage,
                                                ClientSearchCriteria employeeSearchCriteria) throws ParseException {



        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        Join<Client, Contrat> details = root.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");
        criteriaQuerytest.multiselect(voiture.get("marque"), criteriaBuilder.count(root));
        criteriaQuerytest.groupBy(voiture.get("marque"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }
        List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();
        System.out.println(result);
        return result ;
    }
    public List<Object[]> findAllModeleChart(ClientPage clientPage,
                                             ClientSearchCriteria employeeSearchCriteria) throws ParseException {



        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        Join<Client, Contrat> details = root.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");
        criteriaQuerytest.multiselect(voiture.get("modele"), criteriaBuilder.count(root));
        criteriaQuerytest.groupBy(voiture.get("modele"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }


        List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();
        System.out.println(result);
        return result ;
    }

    public List<Object[]> findAllPtVenteChart(ClientPage clientPage,
                                             ClientSearchCriteria employeeSearchCriteria) throws ParseException {



        CriteriaQuery<Object[]> criteriaQuerytest = criteriaBuilder.createQuery(Object[].class);
        Root<Client> root = criteriaQuerytest.from(Client.class);
        Join<Client, Contrat> details = root.join("contrat");
        criteriaQuerytest.multiselect(details.get("pointVente"), criteriaBuilder.count(root));
        criteriaQuerytest.groupBy(details.get("pointVente"));

        Predicate restriction = this.getPredicate(employeeSearchCriteria,root);  //where
        if (restriction != null) {
            criteriaQuerytest.where(restriction);
        }
        List<Object[]> result =  entityManager.createQuery(criteriaQuerytest).getResultList();
        System.out.println(result);
        return result ;
    }
    //--------------------------
    public Long findAllModeleLong(ClientPage employeePage,
                                  ClientSearchCriteria employeeSearchCriteria) throws ParseException {

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Voiture> entity_ = countQuery.from(Voiture.class);
        entity_.alias("entitySub");
        Join<Client, Contrat> details =  entity_.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");


        countQuery.select(criteriaBuilder.count(entity_));

        Predicate restriction = this.getPredicateV(employeeSearchCriteria,entity_);

        if (restriction != null) {
            countQuery.where(restriction); // Copy restrictions
        }
        Long totalCount=entityManager.createQuery(countQuery).getSingleResult();

        return totalCount ;
    }

    private Predicate getPredicateV(ClientSearchCriteria employeeSearchCriteria,
                                    Root<Voiture> employeeRoot) throws ParseException {

        Join<Client, Contrat> details = employeeRoot.join("contrat");
        Join<Contrat, Voiture> voiture = details.join("voitureA");

        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(employeeSearchCriteria.getMarque())){
            predicates.add(criteriaBuilder.equal(voiture.get("marque"), employeeSearchCriteria.getMarque()));
        }
        if(Objects.nonNull(employeeSearchCriteria.getModele())){
            predicates.add(criteriaBuilder.equal(voiture.get("modele"), employeeSearchCriteria.getModele()));
        }
        if(Objects.nonNull(employeeSearchCriteria.getName())){
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("name"),
                            "%" + employeeSearchCriteria.getName() + "%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getTypologie())){
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("typologie"),
                            "%" + employeeSearchCriteria.getTypologie() + "%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getSexe())){
            predicates.add(
                    criteriaBuilder.equal(employeeRoot.get("sexe"),employeeSearchCriteria.getSexe())
            );
        }
        if (Objects.nonNull(employeeSearchCriteria.isMdt())) {
            predicates.add(
                    criteriaBuilder.equal(details.get("modalitePaiement"),employeeSearchCriteria.isMdt())
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getPtVente())){
            predicates.add(
                    criteriaBuilder.equal(details.get("pointVente"),employeeSearchCriteria.getPtVente())
            );
        }if(Objects.nonNull(employeeSearchCriteria.getCommerciale())){
            predicates.add(
                    criteriaBuilder.equal(details.get("nomVendeur"),employeeSearchCriteria.getCommerciale())
            );
        }
        //à régler
        if(Objects.nonNull(employeeSearchCriteria.getstartDate())&&Objects.nonNull(employeeSearchCriteria.getEndDate())){
            java.util.Date start = new SimpleDateFormat("yyyy-MM-dd").parse(employeeSearchCriteria.getstartDate());
            Date end =new SimpleDateFormat("yyyy-MM-dd").parse(employeeSearchCriteria.getEndDate());
            predicates.add(
                    criteriaBuilder.between(details.get("dateComptabilisation"), start,end)
            );
        }
        if(employeeSearchCriteria.getSlm() == 1){
            predicates.add(
                    criteriaBuilder.equal(details.get("nomVendeur"),"AKKARAMOU")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}