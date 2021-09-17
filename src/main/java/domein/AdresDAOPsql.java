package domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAO rdao){
        this.conn = conn;
        this.rdao = rdao;
    }

    public AdresDAOPsql(Connection conn){
        this.conn = conn;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public void save(Adres adres) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("INSERT INTO adres (adres_id, postcode, " +
                "huisnummer, straat, woonplaats, reiziger_id)" +
                "VALUES (?, ?, ?, ?, ?, ?)");
        ps.setInt(1, adres.getId());
        ps.setString(2, adres.getPostcode());
        ps.setString(3, adres.getHuisnummer());
        ps.setString(4, adres.getStraat());
        ps.setString(5, adres.getWoonplaats());
        ps.setInt(6, adres.getReiziger_id());
        ps.executeUpdate();
    }

    public void update(Adres adres) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("UPDATE adres SET " +
                "postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");
        ps.setString(1, adres.getPostcode());
        ps.setString(2, adres.getHuisnummer());
        ps.setString(3, adres.getStraat());
        ps.setString(4, adres.getWoonplaats());
        ps.setInt(5, adres.getReiziger_id());
        ps.setInt(6, adres.getId());
        ps.executeUpdate();
    }

    public void delete(Adres adres) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?");
        ps.setInt(1, adres.getId());
        ps.executeUpdate();
    }

    public Adres findByReiziger(Reiziger reiziger) throws SQLException{
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(conn);
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new Adres(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4),
                    rs.getString(5), reiziger);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Adres> findAll() throws SQLException{
        List<Adres> adressen = new ArrayList<>();
        Statement st = conn.createStatement();
        try{
            ResultSet rs = st.executeQuery("SELECT * FROM adres");
            ReizigerDAOPsql rdao = new ReizigerDAOPsql(conn);
            while (rs.next()){
                Adres adres = new Adres(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rdao.findById(rs.getInt(6)));
                adressen.add(adres);
            }
            rs.close();
            st.close();
            if (adressen.size() < 1){
                throw new IllegalArgumentException();
            }
            return adressen;
        }
        catch(Exception e){
            return null;
        }
    }
}
