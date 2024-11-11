package com.litecommand.example.command;

import com.litecommand.example.Account;
import com.litecommand.example.Server;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import dev.rollczi.litecommands.strict.StrictMode;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.kookbc.impl.command.litecommands.KookLitePlatform;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * 基于{@link LiteCommand}实现的注册
 *
 * @see ExampleLiteCommand
 */
public class LiteExampleCommands {
    private LiteExampleCommands() {
    }

    public static LiteCommand<CommandSender> create() {
        return new LiteCommand<CommandSender>("tutorial", "tutor", "tutor2")
                .description("a simple command tutorial", "second description")// 根命令介绍
                .meta(KookLitePlatform.PREFIX, new HashSet<>(Arrays.asList("/", ".")))// 定义指令prefix
                .subcommands(
                        setMode(),
                        setServer(),
                        help("help")
                )// 添加子命令
                .withoutExecutor() // 标记根命令没有执行的内容，当输入`/tutorial`时会提示命令错误
                ;
    }

    private static LiteCommand<CommandSender> setMode() {
        return new LiteCommand<CommandSender>("set mode")
                .description("set user mode")
                .context("account", Account.class)// 定义该命令需要的context类型和名称
                .argument("mode", String.class) // 定义该命令的参数类型和名称
                .execute(context -> { // 执行
                    User user = context.invocation().context().get(User.class).orElseThrow(() -> new UnsupportedOperationException("User Only"));
                    Account account = context.context("account", Account.class);
                    // set mode
                });
    }

    private static LiteCommand<CommandSender> setServer() {
        return new LiteCommand<CommandSender>("set server")
                .context("account", Account.class)
                .argument("server", Server.class)
                .execute(context -> {
                    User user = context.invocation().context().get(User.class).orElseThrow(() -> new UnsupportedOperationException("User Only"));
                    Account account = context.context("account", Account.class);
                    Server server = context.argument("server", Server.class);
                    // set server
                });
    }

    // for Shortcut
    public static LiteCommand<CommandSender> help(String name) {
        return new LiteCommand<CommandSender>(name)
                .executorMeta(Meta.DESCRIPTION, Collections.singletonList("get help"))
                .strictExecutor(StrictMode.DISABLED)
                .executeReturn(context -> {
                    return "//help message//";
                });
    }
}
