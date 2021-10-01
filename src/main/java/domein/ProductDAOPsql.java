package domein;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{
    private Connection conn;
    private OVChipkaartDAOPsql ovcDAOPsql;

    public ProductDAOPsql(Connection conn){
        this.conn = conn;
    }

    public void setOvcDAOPsql(OVChipkaartDAOPsql ovcDAOPsql) {
        this.ovcDAOPsql = ovcDAOPsql;
    }

    public void save(Product product) throws SQLException{
        for(OVChipkaart ovc : product.getOvChipkaartList()){
            ovcDAOPsql.OVCSaveUpdateCheck(ovc);
        }
        PreparedStatement ps = conn.prepareStatement("INSERT INTO " +
                "product (product_nummer, naam, beschrijving, prijs) " +
                "VALUES (?, ?, ?, ?)");
        ps.setInt(1, product.getProduct_nummer());
        ps.setString(2, product.getNaam());
        ps.setString(3, product.getBeschrijving());
        ps.setDouble(4, product.getPrijs());
        ps.executeUpdate();
        for(OVChipkaart ovc : product.getOvChipkaartList()){
            ovcDAOPsql.OVCSaveUpdateCheck(ovc);
        }
        saveCP(product);
    }

    public void saveCP(Product product) throws SQLException {
        for(OVChipkaart ovc : product.getOvChipkaartList()){
            Date currentDate = new Date(Instant.now().toEpochMilli());
            PreparedStatement ps = conn.prepareStatement("INSERT INTO " +
                    "ov_chipkaart_product (kaart_nummer, product_nummer, status, " +
                    "last_update) VALUES (?, ?, ?, ?)");
            ps.setInt(1, ovc.getKaart_id());
            ps.setInt(2, product.getProduct_nummer());
            ps.setString(3, "gekocht");
            ps.setDate(4, currentDate);
            ps.executeUpdate();
        }
    }

    public void update(Product product) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("UPDATE product " +
                "SET naam = ?, beschrijving = ?, prijs = ? " +
                "WHERE product_nummer = ?");
        ps.setString(1, product.getNaam());
        ps.setString(2, product.getBeschrijving());
        ps.setDouble(3, product.getPrijs());
        ps.setInt(4, product.getProduct_nummer());
        ps.executeUpdate();
    }

    public void delete(Product product) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("DELETE FROM product " +
                "WHERE product_nummer = ?");
        ps.setInt(1, product.getProduct_nummer());
        ps.executeUpdate();
    }

    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException{
        List<Product> producten = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT product.* " +
                    "FROM ov_chipkaart_product " +
                    "INNER JOIN product ON ov_chipkaart_product.product_nummer = " +
                    "product.product_nummer " +
                    "WHERE kaart_nummer = ?");
            ps.setInt(1, ovChipkaart.getKaart_id());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Product product = new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                );
                producten.add(product);
            }
            return producten;
        }
        catch(Exception e){
            return producten;
        }
    }

    public List<Product> findAll() throws SQLException{
        List<Product> producten = new ArrayList<>();
        Statement st = conn.createStatement();
        try{
            ResultSet rs = st.executeQuery("SELECT * FROM product");
            while(rs.next()){
                Product product = new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                );
                producten.add(product);
            }
            rs.close();
            st.close();
            return producten;
        }
        catch (Exception e){
            return null;
        }
    }
}
