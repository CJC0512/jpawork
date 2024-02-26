package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCURDTests {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){

        /* 필기. META-INF/persistence.xml 의 jpatest 사용*/
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
    public void closeManager(){
        entityManager.close();
    }

    @Test
    public void 메뉴코드로_메뉴_조회_테스트(){

        // given
        int menuCode = 2;

        // when
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // then
        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu = " + foundMenu);
    }

    @Test
    public void 새로운_메뉴_추가_텍스트(){

        // given
        Menu menu = new Menu();
        menu.setMenuName("꿀발린추어탕");
        menu.setMenuPrice(7000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.persist(menu);
            entityTransaction.commit();
        } catch(Exception e){
            entityTransaction.rollback();
        }

        // then
        Assertions.assertTrue(entityManager.contains(menu));     // menu 객체가 현재 영속 상태로 관리되는지 확인
    }

    @Test
    public void 메뉴_이름_수정_테스트(){     // 최초 수정 후, 중복된 값을 보낸다면 update는 일어나지 않는다.

        // given
        Menu menu = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menu);

        String menuNameToChange = "갈치스무디";

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            menu.setMenuName(menuNameToChange);
            entityTransaction.commit();
        } catch (Exception e){
            entityTransaction.rollback();
        }

        // then
        Assertions.assertEquals(menuNameToChange, entityManager.find(Menu.class,2).getMenuName());
    }

    @Test
    public void 메뉴_삭제하기_테스트(){

        // given
        Menu menuToRemove = entityManager.find(Menu.class, 1);

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.remove(menuToRemove);
            System.out.println("menuToRemove = " + menuToRemove);
            entityTransaction.commit();
        } catch (Exception e){
//            외래키 참조로 인한 삭제 에러가 일어날 수 있으니 주의할 것.
//            System.out.println("rollback 후 : ");
//            e.printStackTrace();
            entityTransaction.rollback();
        }

        // then
        Menu removeMenu = entityManager.find(Menu.class,1);
        Assertions.assertEquals(null, removeMenu);
    }
}
