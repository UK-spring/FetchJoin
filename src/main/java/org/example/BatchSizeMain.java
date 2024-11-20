package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.entity.Company;
import org.example.entity.Tutor;

import java.util.List;

public class BatchSizeMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("entity");

        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        try {

            Company sparta = new Company("sparta");
            Company etc = new Company("etc");

            em.persist(sparta);
            em.persist(etc);

            Tutor tutor1 = new Tutor("tutor1" );
            Tutor tutor2 = new Tutor("tutor2" );
            Tutor tutor3 = new Tutor("tutor3" );

            tutor1.setCompany(sparta);
            tutor2.setCompany(etc);
            tutor3.setCompany(sparta);

            em.persist(tutor1);
            em.persist(tutor2);
            em.persist(tutor3);

            em.flush();
            em.clear();

            String query = "select c from Company c";

            List<Company> companyList = em.createQuery(query, Company.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("companyList.size() = " + companyList.size());

            for (Company company : companyList) {
                System.out.println("company.getName() = " + company.getName());
                for (Tutor tutor : company.getTutorList()) {
                    System.out.println("tutor.getName(): " + tutor.getName());
                }
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
