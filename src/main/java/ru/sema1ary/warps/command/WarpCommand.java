package ru.sema1ary.warps.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.serialization.LocationSerializer;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.warps.model.Warp;
import ru.sema1ary.warps.service.WarpService;

@RequiredArgsConstructor
@Command(name = "warp", aliases = {"warps", "points", "point"})
public class WarpCommand {
    private final WarpService warpService;
    private final ConfigService configService;

    @Async
    @Execute(name = "reload")
    @Permission("warps.reload")
    void reload(@Context CommandSender sender) {
        configService.reload();
        PlayerUtil.sendMessage(sender, (String) configService.get("reload-message"));
    }

    @Async
    @Execute
    void execute(@Context Player sender, @Arg("название") Warp warp) {
        sender.teleportAsync(LocationSerializer.deserialize(warp.getLocation()));
        PlayerUtil.sendMessage(sender, (String) configService.get("warp-teleport"));
    }

    @Async
    @Execute(name = "set", aliases = {"add", "create"})
    void set(@Context Player sender, @Arg("название") String name) {
        warpService.createWarp(sender, name);
    }

    @Async
    @Execute(name = "remove", aliases = {"delete", "del", "rem"})
    void remove(@Context CommandSender sender, @Arg("название") Warp warp) {
        warpService.deleteWarp((Player) sender, warp);
    }
}
