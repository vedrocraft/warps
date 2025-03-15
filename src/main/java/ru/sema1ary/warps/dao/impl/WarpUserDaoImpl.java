package ru.sema1ary.warps.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.sema1ary.warps.dao.WarpUserDao;
import ru.sema1ary.warps.model.WarpUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class WarpUserDaoImpl extends BaseDaoImpl<WarpUser, Long> implements WarpUserDao {
    public WarpUserDaoImpl(ConnectionSource connectionSource, Class<WarpUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public WarpUser save(@NonNull WarpUser user) throws SQLException {
        assignEmptyCollections(user);
        createOrUpdate(user);
        return user;
    }

    @Override
    public void saveAll(@NonNull List<WarpUser> users) throws SQLException {
        callBatchTasks((Callable<Void>) () -> {
            for (WarpUser user : users) {
                assignEmptyCollections(user);
                createOrUpdate(user);
            }
            return null;
        });
    }

    @Override
    public Optional<WarpUser> findById(Long id) throws SQLException {
        WarpUser result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<WarpUser> findByUsername(@NonNull String username) throws SQLException {
        QueryBuilder<WarpUser, Long> queryBuilder = queryBuilder();
        Where<WarpUser, Long> where = queryBuilder.where();
        String columnName = "username";

        SelectArg selectArg = new SelectArg(SqlType.STRING, username.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<WarpUser> findAll() throws SQLException {
        return queryForAll();
    }

    private void assignEmptyCollections(@NonNull WarpUser user) throws SQLException {
        if (user.getWarps() == null) {
            user.setWarps(getEmptyForeignCollection("warps"));
        }
    }
}
