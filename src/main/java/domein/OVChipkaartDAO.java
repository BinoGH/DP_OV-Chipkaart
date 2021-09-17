package domein;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    void save(OVChipkaart ovChipkaart) throws SQLException;
    void update(OVChipkaart ovChipkaart) throws SQLException;
    void delete(OVChipkaart ovChipkaart) throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    List<OVChipkaart> findAll() throws SQLException;
}
