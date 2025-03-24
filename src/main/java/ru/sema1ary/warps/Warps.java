package ru.sema1ary.warps;

import org.bukkit.plugin.java.JavaPlugin;
import ru.sema1ary.vedrocraftapi.BaseCommons;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.ormlite.DatabaseUtil;
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

public final class Warps extends JavaPlugin implements BaseCommons {

    @Override
    public void onEnable() {
        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        DatabaseUtil.initConnectionSource(this, WarpUser.class, Warp.class);

        ServiceManager.registerService(WarpUserService.class, new WarpUserServiceImpl(
                getDao(WarpUser.class)
        ));

        ServiceManager.registerService(WarpService.class, new WarpServiceImpl(
                getDao(Warp.class),
                getService(ConfigService.class),
                getService(WarpUserService.class)
        ));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                getService(WarpUserService.class)
        ), this);

        LiteCommandBuilder.builder()
                .commands(new WarpCommand(
                        getService(WarpService.class),
                        getService(ConfigService.class)
                ))
                .argument(Warp.class, new WarpArgument(
                        getService(WarpService.class)
                ))
                .build();
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);
    }
}
