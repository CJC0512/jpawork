package com.ohgiraffers.section01.simple;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

public class SimpleJPQLTests {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @Test
    public void TypeQuery를_이용한_단일행_단일열_조회_테스트(){

        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode = 7";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);

        String resultMenuName = query.getSingleResult();

        System.out.println("resultMenuName = " + resultMenuName);

        Assertions.assertEquals("민트미역국", resultMenuName);

    }
}
