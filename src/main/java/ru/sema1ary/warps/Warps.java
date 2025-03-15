package ru.sema1ary.warps;

import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sema1ary.vedrocraftapi.BaseCommons;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;
import ru.sema1ary.warps.command.WarpCommand;
import ru.sema1ary.warps.command.argument.WarpArgument;
import ru.sema1ary.warps.listener.PreJoinListener;
import ru.sema1ary.warps.model.Warp;
import ru.sema1ary.warps.model.WarpUser;
import ru.sema1ary.warps.service.WarpService;
import ru.sema1ary.warps.service.WarpUserService;
import ru.sema1ary.warps.service.impl.WarpServiceImpl;
import ru.sema1ary.warps.service.impl.WarpUserServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Warps extends JavaPlugin implements BaseCommons {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        initConnectionSource();

        ServiceManager.registerService(WarpUserService.class, new WarpUserServiceImpl(
                getDao(WarpUser.class)
        ));

        ServiceManager.registerService(WarpService.class, new WarpServiceImpl(
                getDao(Warp.class),
                ServiceManager.getService(ConfigService.class),
                ServiceManager.getService(WarpUserService.class)
        ));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                ServiceManager.getService(WarpUserService.class)
        ), this);

        LiteCommandBuilder.builder()
                .commands(new WarpCommand(
                        ServiceManager.getService(WarpService.class),
                        ServiceManager.getService(ConfigService.class)
                ))
                .argument(Warp.class, new WarpArgument(
                        ServiceManager.getService(WarpService.class)
                ))
                .build();
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);
    }

    @SneakyThrows
    private void initConnectionSource() {
        Path databaseFilePath = Paths.get("plugins/warps/database.sqlite");
        if(!Files.exists(databaseFilePath) && !databaseFilePath.toFile().createNewFile()) {
            return;
        }

        ConnectionSourceUtil.connectNoSQLDatabase(databaseFilePath.toString(), WarpUser.class,
                Warp.class);
    }
}
