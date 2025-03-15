package ru.sema1ary.warps.command.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.sema1ary.warps.model.Warp;
import ru.sema1ary.warps.service.WarpService;

import java.util.Optional;

@RequiredArgsConstructor
public class WarpArgument extends ArgumentResolver<CommandSender, Warp> {
    private final WarpService warpService;

    @Override
    protected ParseResult<Warp> parse(Invocation<CommandSender> invocation, Argument<Warp> argument, String s) {
        Optional<Warp> optionalWarp = warpService.findByName(s);

        return optionalWarp.map(ParseResult::success).orElseGet(() -> ParseResult.failure("Warp not found"));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Warp> argument,
                                    SuggestionContext context) {

        if(!invocation.sender().hasPermission("warps.delete.other")) {
            return SuggestionResult.of(warpService.findAll().stream()
                    .filter(warp -> warp.getOwner().getUsername().equals(invocation.sender().getName()))
                    .map(Warp::getName)
                    .toList());
        }

        return warpService.findAll().stream().map(Warp::getName).collect(SuggestionResult.collector());
    }
}
