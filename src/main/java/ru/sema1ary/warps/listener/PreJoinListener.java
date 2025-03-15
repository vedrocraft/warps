package ru.sema1ary.warps.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import ru.sema1ary.warps.model.WarpUser;
import ru.sema1ary.warps.service.WarpUserService;

@RequiredArgsConstructor
public class PreJoinListener implements Listener {
    private final WarpUserService userService;

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        String username = event.getName();

        if(username.isEmpty()) {
            return;
        }

        if(userService.findByUsername(username).isEmpty()) {
            userService.save(WarpUser.builder()
                    .username(username)
                    .warpLimit(2)
                    .build());
        }
    }
}
