package domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{
    Connection conn;
    AdresDAOPsql aDAOPsql;

    public ReizigerDAOPsql(Connection conn){
        this.conn = conn;
    }

    public ReizigerDAOPsql(Connection conn, AdresDAOPsql aDAOPsql){
        this.conn = conn;
        this.aDAOPsql = aDAOPsql;
    }

    public void setaDAOPsql(AdresDAOPsql aDAOPsql) {
        this.aDAOPsql = aDAOPsql;
    }

    @Override
    public void save(Reiziger reiziger) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                "VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, reiziger.getId());
        ps.setString(2, reiziger.getVoorletters());
        ps.setString(3, reiziger.getTussenvoegsel());
        ps.setString(4, reiziger.getAchternaam());
        ps.setDate(5, reiziger.getGeboortedatum());
        ps.executeUpdate();
        if (reiziger.getAdres() != null){
            AdresDAOPsql adaop = new AdresDAOPsql(conn);
            adaop.save(reiziger.getAdres());}
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");
        ps.setString(1, reiziger.getVoorletters());
        ps.setString(2, reiziger.getTussenvoegsel());
        ps.setString(3, reiziger.getAchternaam());
        ps.setDate(4, reiziger.getGeboortedatum());
        ps.setInt(5, reiziger.getId());
        ps.executeUpdate();
        if (reiziger.getAdres() != null){
            AdresDAOPsql adaop = new AdresDAOPsql(conn);
            adaop.update(reiziger.getAdres());}
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        if (reiziger.getAdres() != null){
            AdresDAOPsql adaop = new AdresDAOPsql(conn);
            adaop.delete(reiziger.getAdres());}
        PreparedStatement ps = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
        ps.setInt(1, reiziger.getId());
        ps.executeUpdate();
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        Statement st = conn.createStatement();
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger WHERE reiziger_id = " + id);
            rs.next();
            Reiziger reiziger = new Reiziger(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5));
            AdresDAOPsql adaop = new AdresDAOPsql(conn);
            Adres adres = adaop.findByReiziger(reiziger);
            reiziger.setAdres(adres);
            return reiziger;
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String date) throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM reiziger");
        try {
            while (rs.next()) {
                if (rs.getDate(5).equals(Date.valueOf(date))) {
                    Reiziger reiziger = new Reiziger(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDate(5));
                    reizigerList.add(reiziger);
                }
            }
            for(Reiziger reiziger : reizigerList){
                AdresDAOPsql adaop = new AdresDAOPsql(conn);
                reiziger.setAdres(adaop.findByReiziger(reiziger));
            }
            return reizigerList;
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();
        Statement st = conn.createStatement();
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger");
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5));
                reizigerList.add(reiziger);
            }
            rs.close();
            st.close();
            if (reizigerList.size() < 1){
                throw new IllegalArgumentException();
            }
            for(Reiziger reiziger : reizigerList){
                AdresDAOPsql adaop = new AdresDAOPsql(conn);
                reiziger.setAdres(adaop.findByReiziger(reiziger));
            }
            return reizigerList;
        }
        catch(Exception e){
            return null;
        }
    }
}
