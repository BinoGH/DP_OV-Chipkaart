package domein;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    Connection conn;
    ReizigerDAOPsql rDAOPsql;
    ProductDAOPsql pDAOPsql;

    public OVChipkaartDAOPsql(Connection conn, ReizigerDAOPsql rDAOPsql){
        this.conn = conn;
        this.rDAOPsql = rDAOPsql;
    }

    public void setpDAOPsql(ProductDAOPsql pDAOPsql) {
        this.pDAOPsql = pDAOPsql;
    }

    public void save(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer," +
                "geldig_tot, klasse, saldo, reiziger_id)" +
                "VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, ovChipkaart.getKaart_id());
        ps.setDate(2, ovChipkaart.getEinddatum());
        ps.setInt(3, ovChipkaart.getKlasse());
        ps.setDouble(4, ovChipkaart.getSaldo());
        ps.setInt(5, ovChipkaart.getReiziger().getId());
        ps.executeUpdate();
        if (ovChipkaart.getProductList() != null){
            for(Product product : ovChipkaart.getProductList()){
                pDAOPsql.save(product);
            }
        }
        saveCP(ovChipkaart);
    }

    public void saveCP(OVChipkaart ovChipkaart) throws SQLException{
        for(Product product : ovChipkaart.getProductList()){
            Date currentDate = new Date(Instant.now().toEpochMilli());
            PreparedStatement ps = conn.prepareStatement("INSERT INTO " +
                    "ov_chipkaart_product (kaart_nummer, product_nummer, status, " +
                    "last_update) VALUES (?, ?, ?, ?)");
            ps.setInt(1, ovChipkaart.getKaart_id());
            ps.setInt(2, product.getProduct_nummer());
            ps.setString(3, "gekocht");
            ps.setDate(4, currentDate);
            ps.executeUpdate();
        }
    }

    public void update(OVChipkaart ovChipkaart) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE ov_chipkaart SET " +
                "geldig_tot = ?, klasse = ?, saldo = ? , reiziger_id = ? WHERE kaart_nummer = ?");
        ps.setDate(1, ovChipkaart.getEinddatum());
        ps.setInt(2, ovChipkaart.getKlasse());
        ps.setDouble(3, ovChipkaart.getSaldo());
        ps.setInt(4, ovChipkaart.getReiziger().getId());
        ps.setInt(5, ovChipkaart.getKaart_id());
        ps.executeUpdate();
        updateCP(ovChipkaart);
    }

    public void updateCP(OVChipkaart ovChipkaart) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?"
        );
        ps.setInt(1, ovChipkaart.getKaart_id());
        ps.executeUpdate();
        saveCP(ovChipkaart);
    }

    public void delete(OVChipkaart ovChipkaart) throws SQLException{
        try {
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE" +
                    " kaart_nummer = ?");
            ps2.setInt(1, ovChipkaart.getKaart_id());
            ps2.executeUpdate();
        }
        catch(Exception e){}
        PreparedStatement ps = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE" +
                " kaart_nummer = ?");
        ps.setInt(1, ovChipkaart.getKaart_id());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM ov_chipkaart WHERE " +
                "reiziger_id = " + reiziger.getId());
        while (rs.next()){
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt(1),
                    rs.getDate(2),
                    rs.getInt(3),
                    rs.getDouble(4),
                    reiziger);
            ovChipkaartList.add(ovChipkaart);
        }
        rs.close();
        st.close();
        return ovChipkaartList;
    }

    public void OVCSaveUpdateCheck(OVChipkaart ovc) throws SQLException {
        if(findById(ovc.getKaart_id()) != null){
            update(ovc);
        }else{
            save(ovc);
        }
    }

    public OVChipkaart findById(int id) throws SQLException{
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM ov_chipkaart WHERE kaart_nummer = " + id);
        while(rs.next()){
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt(1),
                    rs.getDate(2),
                    rs.getInt(3),
                    rs.getDouble(4),
                    rDAOPsql.findById(rs.getInt(5)));
            return ovChipkaart;
        }
        return null;
    }

    public List<OVChipkaart> findAll() throws SQLException{
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM ov_chipkaart");
        while (rs.next()){
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt(1),
                    rs.getDate(2),
                    rs.getInt(3),
                    rs.getDouble(4),
                    rDAOPsql.findById(rs.getInt(5)));
            ovChipkaartList.add(ovChipkaart);
        }
        rs.close();
        st.close();
        return ovChipkaartList;
    }
}
