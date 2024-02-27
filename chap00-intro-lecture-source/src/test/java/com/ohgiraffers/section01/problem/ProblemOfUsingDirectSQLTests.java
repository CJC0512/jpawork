package com.ohgiraffers.section01.problem;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemOfUsingDirectSQLTests {

    private Connection con;

    @BeforeEach
    void setConnection() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user = "swcamp";
        String password = "swcamp";

        Class.forName(driver);

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        con.rollback();
        con.close();
    }

    /* 설명. JDBC API를 이용해 직접 SQL을 다룰 때 발생할 수 있는 문제점
     *  1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성(개발 시간 증가, 유지보수성 저하)
     *  2. SQL에 의존하여 개발
     *  3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색, 방향성)
     *  4. 동일성 보장 문제
    * */

    /* 설명. 1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성(개발 시간 증가, 유지보수성 저하) */
    @DisplayName("직접 SQL을 작성하여 메뉴를 조회할 때 발생하는 문제 확인")
    @Test
    void testDirectSelectSql() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE, "
                      + "ORDERABLE_STATUS FROM TBL_MENU";

        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();
        while(rset.next()) {
            Menu menu = new Menu();
            menu.setMenuCode(rset.getInt("MENU_CODE"));
            menu.setMenuName(rset.getString("MENU_NAME"));
            menu.setMenuPrice(rset.getInt("MENU_PRICE"));
            menu.setCategoryCode(rset.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));

            menuList.add(menu);
        }

        // then
        Assertions.assertTrue(!menuList.isEmpty());
        menuList.forEach(System.out::println);

        rset.close();
        stmt.close();
    }

    @DisplayName("직접 SQL을 작성하여 신규 메뉴를 추가할 때 발생하는 문제 확인")
    @Test
    void testDirectInsertSQL() throws SQLException {

        // given
        Menu menu = new Menu();
        menu.setMenuName("민트초코짜장면");
        menu.setMenuPrice(12000);
        menu.setCategoryCode(1);
        menu.setOrderableStatus("Y");

        String query = "INSERT INTO TBL_MENU(MENU_NAME, MENU_PRICE, CATEGORY_CODE, " +
                "ORDERABLE_STATUS) VALUES (?, ?, ?, ?)";

        // when
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, menu.getMenuName());
        pstmt.setInt(2, menu.getMenuPrice());
        pstmt.setInt(3, menu.getCategoryCode());
        pstmt.setString(4, menu.getOrderableStatus());

        int result = pstmt.executeUpdate();

        // then
        Assertions.assertEquals(1, result);
        pstmt.close();
    }

    /* 설명. 2. SQL에 의존하여 개발 */
    /* 설명. 2-1. 조회 항목 변경에 따른 의존성 확인 */
    @DisplayName("조회 항목 변경에 따른 의존성 확인")
    @Test
    void testChangeSelectColumns() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME FROM TBL_MENU";

        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();
        while(rset.next()) {
            Menu menu = new Menu();
            menu.setMenuCode(rset.getInt("MENU_CODE"));
            menu.setMenuName(rset.getString("MENU_NAME"));
//            menu.setMenuPrice(rset.getInt("MENU_PRICE"));

            menuList.add(menu);
        }

        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);

        rset.close();
        stmt.close();

    }

    /* 설명. 2-2. 연관된 객체 문제 */
    @DisplayName("연관된 객체 문제 확인")
    @Test
    void testAssociationObject() throws SQLException {

        // given
        String query = "SELECT A.MENU_CODE, A.MENU_NAME, A.MENU_PRICE, B.CATEGORY_CODE, "
                + "B.CATEGORY_NAME, A.ORDERABLE_STATUS "
                + "FROM TBL_MENU A "
                + "JOIN TBL_CATEGORY B ON (A.CATEGORY_CODE = B.CATEGORY_CODE)";

        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        List<MenuAndCategory> menuAndCategories = new ArrayList<>();
        while(rset.next()) {
            MenuAndCategory menuAndCategory = new MenuAndCategory();
            menuAndCategory.setMenuCode(rset.getInt("MENU_CODE"));
            menuAndCategory.setMenuName(rset.getString("MENU_NAME"));
            menuAndCategory.setMenuPrice(rset.getInt("MENU_PRICE"));
            menuAndCategory.setCategory(new Category(rset.getInt("CATEGORY_CODE"),
                    rset.getString("CATEGORY_NAME")));
            menuAndCategory.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));

            menuAndCategories.add(menuAndCategory);
        }

        // then
        Assertions.assertTrue(!menuAndCategories.isEmpty());
        menuAndCategories.forEach(System.out::println);

        rset.close();
        stmt.close();

    }

    /* 설명. 3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색, 방향성) */
    /* 설명. 3-1. 상속 문제
     *  객체 지향 언어의 상속 개념과 유사한 것이 데이터베이스의 서브타입엔티티이다.(서브타입을 별도의 클래스로 나뉘었을 때)
     *  슈퍼타입의 모든 속성을 서브타입이 공유하지 못하여 물리적으로 다른 테이블로 분리가 된 형태이다.
     *  (설계에 따라서는 하나의 테이블로 속성이 추가되기도 한다.)
     *  하지만 객체지향의 상속은 슈퍼타입의 속성을 공유해서 사용하므로 여기에서 패러다임의 불일치가 발생한다.
    * */

    /* 설명. 3-2. 연관관계 문제, 객체 그래프 탐색 문제, 방향성 문제
     *  객체지향에서 말하는 가지고 있는(ASSOCIATION 연관관계 혹은 COLLECTION 연관관계) 경우 데이터베이스 저장 구조와
     *  다른 형태이다.
     *
     * 설명.
     *  - 데이터베이스 테이블에 맞춘 객체 모델
     *  public class Menu {
     *    private int menuCode;
     *    private String menuName;
     *    private int menuPrice;
     *    private int categoryCode;
     *    private String orderableStatus;
     *  }
     *  - 객체 지향 언어에 맞춘 객체 모델
     *  public class Menu {
     *    private int menuCode;
     *    private String menuName;
     *    private int menuPrice;
     *    private Category category;
     *    private String orderableStatus;
     *  }
     *
    * */

    /* 설명. 4. 동일성 보장 문제 */
    @DisplayName("조회한 두 개의 행을 담은 객체의 동일성 비교 테스트")
    @Test
    void testEquals() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME FROM TBL_MENU WHERE MENU_CODE = 12";

        // when
        Statement stmt1 = con.createStatement();
        ResultSet rset1 = stmt1.executeQuery(query);

        Menu menu1 = null;

        while(rset1.next()) {
            menu1 = new Menu();
            menu1.setMenuCode(rset1.getInt("MENU_CODE"));
            menu1.setMenuName(rset1.getString("MENU_NAME"));
        }

        Statement stmt2 = con.createStatement();
        ResultSet rset2 = stmt2.executeQuery(query);

        Menu menu2 = null;

        while(rset2.next()) {
            menu2 = new Menu();
            menu2.setMenuCode(rset2.getInt("MENU_CODE"));
            menu2.setMenuName(rset2.getString("MENU_NAME"));
        }

        // then
        Assertions.assertNotEquals(menu1, menu2);
    }

    /* 설명. JPA를 이용하면 동일 비교가 가능하다.
     *  Menu menu1 = entityManager.find(Menu.class, 12);
     *  Menu menu2 = entityManager.find(Menu.class, 12);
     *  System.out.println(menu1 == menu2); // true(동일성이 보장된다. 같은 객체)
    * */

}
