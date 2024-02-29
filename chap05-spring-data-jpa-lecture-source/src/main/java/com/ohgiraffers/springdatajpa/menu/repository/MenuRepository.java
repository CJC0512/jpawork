package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/* 필기. JpaRepository<다룰 엔티티, 엔티티의 PK의 데이터 타입(객체로)> (복합키 시, PK 자리에 복합키 class를 위치시키면 된다.) */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findByMenuPriceGreaterThan(int menuPrice);
}
