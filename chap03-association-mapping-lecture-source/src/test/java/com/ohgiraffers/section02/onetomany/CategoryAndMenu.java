package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "category_and_menu")
@Table(name = "tbl_category")
public class CategoryAndMenu {

    @Id
    @Column(name="category_code")
    private int categoryCode;

    @Column(name="category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    @JoinColumn(name = "category_code")
    @OneToMany
    private List<Menu> menuList;
}
