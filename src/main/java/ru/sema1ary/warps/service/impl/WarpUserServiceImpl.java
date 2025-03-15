package ru.sema1ary.warps.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.sema1ary.warps.dao.WarpUserDao;
import ru.sema1ary.warps.model.WarpUser;
import ru.sema1ary.warps.service.WarpUserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WarpUserServiceImpl implements WarpUserService {
    private final WarpUserDao userDao;

    @Override
    public WarpUser save(@NonNull WarpUser warpUser) {
        try {
            return userDao.save(warpUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(@NonNull List<WarpUser> list) {
        try {
            userDao.saveAll(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<WarpUser> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<WarpUser> findByUsername(@NonNull String s) {
        try {
            return userDao.findByUsername(s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<WarpUser> findAll() {
        try {
            return userDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WarpUser getUser(@NonNull String s) {
        return findByUsername(s).orElseGet(() -> save(WarpUser.builder()
                .username(s)
                .warpLimit(2)
                .build()));
    }
}
