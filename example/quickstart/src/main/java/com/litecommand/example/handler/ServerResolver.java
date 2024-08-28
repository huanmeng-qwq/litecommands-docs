package com.litecommand.example.handler;

import com.litecommand.example.Server;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import snw.jkook.command.CommandSender;

import java.util.Arrays;

public class ServerResolver extends ArgumentResolver<CommandSender, Server> {
    @Override
    protected ParseResult<Server> parse(Invocation<CommandSender> invocation, Argument<Server> argument, String s) {
        if (Arrays.asList("server1", "server2").contains(s)) {
            return ParseResult.success(Server.valueOf(s));
        } else {
            return ParseResult.failure("错误的服务器类型");
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Server> argument, SuggestionContext context) {
        return SuggestionResult.of("server1", "server2");
    }
}
