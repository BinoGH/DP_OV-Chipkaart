import domein.Adres;
import domein.AdresDAOPsql;
import domein.Reiziger;
import domein.ReizigerDAOPsql;

import java.sql.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql:ovchip";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","54321");
        Connection conn = DriverManager.getConnection(url, props);

        ReizigerDAOPsql rDAOPsql = new ReizigerDAOPsql(conn);
        System.out.println("Alle reizigers:\n" + (rDAOPsql.findAll()));

        System.out.println("\nReiziger vinden met GBD:\n" + (rDAOPsql.findByGbdatum("2002-09-17")));

        System.out.println("\nReiziger vinden met ID:\n" + (rDAOPsql.findById(3)));

        Reiziger r1 = new Reiziger(6, "D", null, "Brands", Date.valueOf("2002-03-23"));
        rDAOPsql.save(r1);
        System.out.println("\nAlle reizigers na de save:\n" + (rDAOPsql.findAll()) + "\n");

        r1.setAchternaam("Pietertje");
        rDAOPsql.update(r1);
        System.out.println("Alle reizigers na de update:\n" + (rDAOPsql.findAll()) + "\n");

        AdresDAOPsql aDAOPsql = new AdresDAOPsql(conn, rDAOPsql);

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
        System.out.println(adres.toString());

        System.out.println("\ntoString via Reiziger:");
        System.out.println(r1.toString());

        rDAOPsql.delete(r1);
        System.out.println("\nAlle reizigers na de delete van reiziger 6:\n" +
                (rDAOPsql.findAll()) + "\n");
        System.out.println("\nAlle adressen na delete:");
        System.out.println(aDAOPsql.findAll());
    }
}
