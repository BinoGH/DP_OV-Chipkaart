import domein.*;

import java.sql.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {
        testPrints(getConnection());
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql:ovchip";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","54321");
        return DriverManager.getConnection(url, props);
    }

    public static void testPrints(Connection conn) throws SQLException {
        ReizigerDAOPsql rDAOPsql = new ReizigerDAOPsql(conn);
        AdresDAOPsql aDAOPsql = new AdresDAOPsql(conn, rDAOPsql);
        OVChipkaartDAOPsql ovcDAOPsql = new OVChipkaartDAOPsql(conn, rDAOPsql);
        rDAOPsql.setAdresDAOPsql(aDAOPsql);
        rDAOPsql.setOvcDAOPsql(ovcDAOPsql);
        ProductDAOPsql pDAOPsql = new ProductDAOPsql(conn);
        pDAOPsql.setOvcDAOPsql(ovcDAOPsql);
        ovcDAOPsql.setpDAOPsql(pDAOPsql);

        // REIZIGER PRINTS

        System.out.println("Alle reizigers:\n" + (rDAOPsql.findAll()));

        System.out.println("\nReiziger vinden met GBD:\n" + (rDAOPsql.findByGbdatum("2002-09-17")));

        System.out.println("\nReiziger vinden met ID:\n" + (rDAOPsql.findById(3)));

        Reiziger r1 = new Reiziger(6, "D", null, "Brands", Date.valueOf("2002-03-23"));
        rDAOPsql.save(r1);
        System.out.println("\nAlle reizigers na de save:\n" + (rDAOPsql.findAll()) + "\n");

        r1.setAchternaam("Pietertje");
        rDAOPsql.update(r1);
        System.out.println("Alle reizigers na de update:\n" + (rDAOPsql.findAll()));

        // ADRES PRINTS

        System.out.println("\nAdres vinden met reiziger_id 2:");
        System.out.println(aDAOPsql.findByReiziger(rDAOPsql.findById(2)));

        System.out.println("\nAlle adressen:");
        System.out.println(aDAOPsql.findAll());

        Adres adres = new Adres(6, "1234ZX", "12k",
                "Dorpstraat", "Utrecht", r1);
        r1.setAdres(adres);
        aDAOPsql.save(adres);

        System.out.println("\nAlle adressen na save:");
        System.out.println(aDAOPsql.findAll());

        adres.setStraat("Parklaan");
        aDAOPsql.update(adres);

        System.out.println("\nAlle adressen na update:");
        System.out.println(aDAOPsql.findAll());

        System.out.println("\ntoString via Adres:");
        System.out.println(adres);

        System.out.println("\ntoString via Reiziger:");
        System.out.println(r1.toString());

        // OV PRINTS

        System.out.println("\nAlle ovchipkaarten voor de nieuwe kaart:");
        System.out.println(ovcDAOPsql.findAll());

        OVChipkaart ovChipkaart = new OVChipkaart(8, Date.valueOf("2030-04-08"), 2, 25.50, r1);
        r1.addOVChipkaart(ovChipkaart);
        ovcDAOPsql.save(ovChipkaart);

        OVChipkaart ovChipkaart2 = new OVChipkaart(9, Date.valueOf("2025-05-07"), 1, 10.20, r1);
        r1.addOVChipkaart(ovChipkaart2);
        ovcDAOPsql.save(ovChipkaart2);

        System.out.println("\nAlle ovchipkaarten na de nieuwe kaarten:");
        System.out.println(ovcDAOPsql.findAll());

        ovChipkaart.setKlasse(3);
        ovcDAOPsql.update(ovChipkaart);

        System.out.println("\nAlle ovchipkaarten na de update:");
        System.out.println(ovcDAOPsql.findAll());

        System.out.println("\nAlle ovchipkaarten van reiziger 6 (findByReiziger):");
        System.out.println(ovcDAOPsql.findByReiziger(r1));

        System.out.println("\ntoString van ovchipkaart:");
        System.out.println(ovChipkaart);

        System.out.println("\ntoString van Reiziger:");
        System.out.println(r1);

        // PRODUCT PRINTS

        System.out.println("\nAlle Producten voor het nieuwe product:");
        System.out.println(pDAOPsql.findAll());

        Product product = new Product(7, "Weekkaart 2e klas",
                "Een hele week onbeperkt reizen met de trein.", 150.00);

        pDAOPsql.save(product);

        ovChipkaart.addProduct(product);

        System.out.println("\nAlle producten na het nieuwe product:");
        System.out.println(pDAOPsql.findAll());

        product.setBeschrijving("Twee weken onbeperkt reizen met het OV.");
        pDAOPsql.update(product);

        System.out.println("\nAlle producten na de update:");
        System.out.println(pDAOPsql.findAll());

        pDAOPsql.delete(product);

        System.out.println("\nAlle producten na de delete:");
        System.out.println(pDAOPsql.findAll());

        System.out.println("\nAlle producten van kaart_id 35283:");
        System.out.println(pDAOPsql.findByOVChipkaart(ovcDAOPsql.findById(35283)));

        // PRODUCT_OVCHIP TESTS

        OVChipkaart ovChipkaart3 = new OVChipkaart(10, Date.valueOf("2031-04-08"), 1,
                30.60, r1);
        ovChipkaart3.addProduct(product);
        ovcDAOPsql.save(ovChipkaart3);

        ovcDAOPsql.delete(ovChipkaart3);

        // EIND PRINTS

        rDAOPsql.delete(r1);
        pDAOPsql.delete(product);
        System.out.println("\nAlle reizigers na de delete van reiziger 6:\n" +
                (rDAOPsql.findAll()) + "\n");
        System.out.println("Alle adressen na delete:");
        System.out.println(aDAOPsql.findAll());
        System.out.println("\nAlle ovchipkaarten na delete:");
        System.out.println(ovcDAOPsql.findAll());

        // DELETE COPY

//        DELETE FROM ov_chipkaart_product WHERE kaart_nummer < 100;
//        DELETE FROM product WHERE product_nummer = 7;
//        DELETE FROM ov_chipkaart WHERE kaart_nummer < 100;
//        DELETE FROM adres WHERE reiziger_id = 6;
//        DELETE FROM reiziger WHERE reiziger_id = 6;

    }
}
