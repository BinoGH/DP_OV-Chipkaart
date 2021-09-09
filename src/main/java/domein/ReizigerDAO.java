package domein;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    void save(Reiziger reiziger) throws SQLException;
    void update(Reiziger reiziger) throws SQLException;
    void delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbdatum(String date) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}
