package ru.sema1ary.warps.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.sema1ary.economy.Economy;
import ru.sema1ary.economy.service.EconomyService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.serialization.LocationSerializer;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.warps.dao.WarpDao;
import ru.sema1ary.warps.model.Warp;
import ru.sema1ary.warps.model.WarpUser;
import ru.sema1ary.warps.service.WarpService;
import ru.sema1ary.warps.service.WarpUserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WarpServiceImpl implements WarpService {
    private final WarpDao warpDao;
    private final ConfigService configService;
    private final WarpUserService userService;

    @Override
    public Warp save(@NonNull Warp warp) {
        try {
            return warpDao.save(warp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Warp> findById(Long id) {
        try {
            return warpDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Warp> findByName(@NonNull String name) {
        try {
            return warpDao.findByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Warp> findAll() {
        try {
            return warpDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createWarp(@NonNull Player sender, @NonNull String name) {
        WarpUser user = userService.getUser(sender.getName());
        if(user.getWarps().size() >= user.getWarpLimit()) {
            PlayerUtil.sendMessage(sender,
                    ((String) configService.get("warp-limit-error-message"))
                            .replace("{warps}", String.valueOf(user.getWarps().size()))
                            .replace("{limit}", String.valueOf(user.getWarpLimit()))
            );
            return;
        }

        if(findByName(name).isPresent()) {
            PlayerUtil.sendMessage(sender, (String) configService.get("warp-already-exists-message"));
            return;
        }

        save(Warp.builder()
                .name(name)
                .owner(userService.getUser(sender.getName()))
                .location(LocationSerializer.serialize(sender.getLocation()))
                .build());

        PlayerUtil.sendMessage(sender, (String) configService.get("warp-successful-created"));
    }

    @Override
    public void deleteWarp(@NonNull Player sender, @NonNull Warp warp) {
        if(!sender.hasPermission("warps.delete.other") &&
                !warp.getOwner().getUsername().equals(sender.getName())) {
            PlayerUtil.sendMessage(sender, (String) configService.get("other-warp-delete-error-message"));
            return;
        }

        try {
            warpDao.delete(warp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PlayerUtil.sendMessage(sender, (String) configService.get("successful-warp-deletion-message"));
    }

    @Override
    public void increaseWarpLimit(@NonNull Player sender, int amount) {
        WarpUser user = userService.getUser(sender.getName());
        Economy economy = (Economy) Bukkit.getPluginManager().getPlugin("economy");

        assert economy != null;
        int price = (int) configService.get("limit-up-price") * amount;

        if(!economy.getService(EconomyService.class).withdrawWithMoneyCheck(sender, price)) {
            PlayerUtil.sendMessage(sender,
                    ((String) configService.get("dont-have-money-message"))
                            .replace("{price}", String.valueOf(price))
            );
            return;
        }

        PlayerUtil.sendMessage(sender, (String) configService.get("successful-limit-upgrade"));
        user.setWarpLimit(user.getWarpLimit() + price);
        userService.save(user);
    }
}
