package domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    Connection conn;
    ReizigerDAOPsql rDAOPsql;

    public OVChipkaartDAOPsql(Connection conn, ReizigerDAOPsql rDAOPsql){
        this.conn = conn;
        this.rDAOPsql = rDAOPsql;
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
    }

    public void delete(OVChipkaart ovChipkaart) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE" +
                " kaart_nummer = ?");
        ps.setInt(1, ovChipkaart.getKaart_id());
        ps.executeUpdate();
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
