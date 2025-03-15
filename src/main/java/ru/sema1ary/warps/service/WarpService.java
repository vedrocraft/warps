package ru.sema1ary.warps.service;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.sema1ary.vedrocraftapi.service.Service;
import ru.sema1ary.warps.model.Warp;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface WarpService extends Service {
    Warp save(@NonNull Warp warp);

    Optional<Warp> findById(Long id);

    Optional<Warp> findByName(@NonNull String name);

    List<Warp> findAll();

    void createWarp(@NonNull Player sender, @NonNull String name);

    void deleteWarp(@NonNull Player sender, @NonNull Warp warp);
}
