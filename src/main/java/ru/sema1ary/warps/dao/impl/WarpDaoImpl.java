package ru.sema1ary.warps.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.sema1ary.warps.dao.WarpDao;
import ru.sema1ary.warps.model.Warp;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WarpDaoImpl extends BaseDaoImpl<Warp, Long> implements WarpDao {
    public WarpDaoImpl(ConnectionSource connectionSource, Class<Warp> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public Warp save(@NonNull Warp warp) throws SQLException {
        createOrUpdate(warp);
        return warp;
    }

    @Override
    public Optional<Warp> findById(Long id) throws SQLException {
        Warp result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Warp> findByName(@NonNull String name) throws SQLException {
        QueryBuilder<Warp, Long> queryBuilder = queryBuilder();
        Where<Warp, Long> where = queryBuilder.where();
        String columnName = "name";

        SelectArg selectArg = new SelectArg(SqlType.STRING, name.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<Warp> findAll() throws SQLException {
        return queryForAll();
    }
}
