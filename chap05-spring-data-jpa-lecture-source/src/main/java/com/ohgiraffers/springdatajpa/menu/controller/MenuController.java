package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/menu")
@Slf4j          // lombok의 기능 (로그)
public class MenuController {

    private final MenuService menuService;

    /* 설명. 로그 생성해 보기 (feat. slf4j.Logger) */
//    Logger logger = LoggerFactory.getLogger(MenuController.class);
//    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /* 필기. 경로상에 있는 것을 바로 받아쓰는 것을 PathVariable이라 부른다.*/
    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model){
//        logger.info("menuCode: {}", menuCode);
        log.info("menuCode: {}", menuCode);

        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    /* 설명. 페이징 처리 전 */
    @GetMapping("/list")
    public String findMenuList(Model model){

        List<MenuDTO> menuList = menuService.findMenuList();
        model.addAttribute("menuList", menuList);

        return "menu/list";
    }


}











