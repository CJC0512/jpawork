package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/* 설명.
 *   Service 계층: 비즈니스 로직 및 트랜잭션처리(@Transactional), DTO <-> Entity(modelmapper lib 활용)
 * */
@Service
public class MenuService {

    private final ModelMapper mapper;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public MenuService(ModelMapper mapper, MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    /* 설명. 1. findById 예제 */
    public MenuDTO findMenuByCode(int menuCode) {

        /* 필기. IllegalArgumentException 발생 시 new
        *   Optional<Menu> findById(Integer param){}*/
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);

        return mapper.map(menu, MenuDTO.class);
    }

    /* 설명. 2. findAll(페이징 처리 전) */
    public List<MenuDTO> findMenuList(){

        /* 필기. findAll을 통해 조회와 동시에 괄호안에 Sort.by를 입력(기준 속성도 입력)하여 정렬까지 할 수 있다. */
        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());

        return menuList.stream().map(menu -> mapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }


}
