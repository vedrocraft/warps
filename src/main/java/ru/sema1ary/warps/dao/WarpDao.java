package ru.sema1ary.warps.dao;

import com.j256.ormlite.dao.Dao;
import lombok.NonNull;
import ru.sema1ary.warps.model.Warp;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface WarpDao extends Dao<Warp, Long> {
    Warp save(@NonNull Warp warp) throws SQLException;

    Optional<Warp> findById(Long id) throws SQLException;

    Optional<Warp> findByName(@NonNull String name) throws SQLException;

    List<Warp> findAll() throws SQLException;
}
