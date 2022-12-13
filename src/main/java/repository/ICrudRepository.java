package repository;

import java.sql.SQLException;

public interface ICrudRepository<ID, E> {

    void add(E entity) throws SQLException;

    void remove(ID id, ID id2) throws SQLException;

    void update(ID id, ID id2, E newEntity) throws SQLException;

    E findById(ID id, ID id2);


}
