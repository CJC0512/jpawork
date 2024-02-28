package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /* 설명. findAll 메소드를 사용할 수 있지만 jpql 또는 native sql로 작성할 수도 있음을 확인한다. */
//    @Query(value = "SELECT m FROM Category m ORDER BY m.categoryCode ASC")
    @Query(value = "SELECT CATEGORY_CODE, CATEGORY_NAME, REF_CATEGORY_CODE FROM TBL_CATEGORY ORDER BY CATEGORY_CODE ASC"
            , nativeQuery = true)
    List<Category> findAllCategory();
}
