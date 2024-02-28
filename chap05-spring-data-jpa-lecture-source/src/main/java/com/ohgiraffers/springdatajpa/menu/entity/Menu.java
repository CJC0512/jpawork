package com.ohgiraffers.springdatajpa.menu.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity     // Menu라는 이름의 엔티티로 생성됨
@Table(name="tbl_menu")
/* 필기. 엔티티는 Setter 빼고 구현 (DTO는 Getter&Setter 전부 구현) */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
// @Data    // 내부 class로 생성자를 비롯한 getter,setter가 다 만들어진다. 하지만 내부 class로 구현되기에 비효율적.
public class Menu {

    @Id
    @Column(name="menu_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuCode;

    @Column(name="menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "orderable_status")
    private String orderableStatus;
}
