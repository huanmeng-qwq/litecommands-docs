package com.litecommand.example.command;

import com.litecommand.example.Account;
import com.litecommand.example.Server;
import com.litecommand.example.handler.AccountContextProvider;
import com.litecommand.example.handler.ModeResolver;
import dev.rollczi.litecommands.command.executor.LiteContext;
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
 * 直接继承{@link LiteCommand}类实现 <br/>
 * {@link LiteCommand}是推荐继承写法的，它的所有成员变量都是protected修饰的
 */
public class ExampleLiteCommand extends LiteCommand<CommandSender> {
    public ExampleLiteCommand() {
        super("tutorial", "tutor", "tutor2");
        register();
    }

    void register() {
        // 根命令介绍
        description("a simple command tutorial", "second description");
        // 标记根命令没有执行的内容，当输入`/tutorial`时会提示命令错误
        withoutExecutor();
        // 定义指令prefix
        meta.put(KookLitePlatform.PREFIX, new HashSet<>(Arrays.asList("/", ".")));
        // 添加子命令
        subcommands(new SetMode(), new SetServer(), new Help("help"));
    }

    private static class SetMode extends LiteCommand<CommandSender> {
        /**
         * @see AccountContextProvider
         */
        private static final String CTX_ACCOUNT = "account";
        /**
         * @see ModeResolver
         */
        private static final String ARG_MODE = "mode";

        public SetMode() {
            super("set mode");
            register();
        }

        void register() {
            description("set user mode");

            // 定义该命令需要的context类型和名称
            context(CTX_ACCOUNT, Account.class);
            // 定义该命令的参数类型和名称
            argument(ARG_MODE, String.class);
            execute(context -> { // 执行
                User user = context.invocation().context().get(User.class).orElseThrow(() -> new UnsupportedOperationException("User Only"));
                Account account = context.context(CTX_ACCOUNT, Account.class);
                String mode = context.argument(ARG_MODE, String.class);
                // set mode
            });
        }
    }

    private static class SetServer extends LiteCommand<CommandSender> {
        private static final String CTX_ACCOUNT = "account";
        private static final String ARG_SERVER = "server";

        public SetServer() {
            super("set server");
            register();
        }

        void register() {
            context(CTX_ACCOUNT, Account.class);
            argument(ARG_SERVER, Server.class);

            execute(this::setServer);
        }

        void setServer(LiteContext<CommandSender> context) {
            User user = context.invocation().context().get(User.class).orElseThrow(() -> new UnsupportedOperationException("User Only"));
            Account account = context.context(CTX_ACCOUNT, Account.class);
            Server server = context.argument(ARG_SERVER, Server.class);
            // set server
        }
    }

    public static class Help extends LiteCommand<CommandSender> {

        public Help(String name) {
            super(name);
            register();
        }

        void register() {
            executorMeta.put(Meta.DESCRIPTION, Collections.singletonList("get help"));
            strictExecutor(StrictMode.DISABLED);
            executeReturn(context -> "//help message//");
        }
    }
}
