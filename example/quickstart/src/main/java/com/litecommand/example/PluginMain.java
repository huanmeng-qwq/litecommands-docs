package com.litecommand.example;

import com.litecommand.example.command.ExampleCommand;
import com.litecommand.example.command.ExampleLiteCommand;
import com.litecommand.example.handler.AccountContextProvider;
import com.litecommand.example.handler.BasicInvalidUsageHandler;
import com.litecommand.example.handler.DescSchematicGenerator;
import com.litecommand.example.handler.ModeResolver;
import dev.rollczi.litecommands.argument.ArgumentKey;
import snw.jkook.plugin.BasePlugin;
import snw.kookbc.impl.command.litecommands.LiteKookFactory;

public class PluginMain extends BasePlugin {

    @Override
    public void onEnable() {
        LiteKookFactory
                .builder(this)
                .bind(AccountMapper.class, AccountMapper::new)// 给需要AccountMapper的命令注入实例
                .context(Account.class, new AccountContextProvider(new AccountMapper()))// 注册Account的上下文提供器
                .commands(ExampleCommand.class)
                // 两者2选1即可
//                .commands(LiteExampleCommands.create(), LiteExampleCommands.help("t-help me"))
//                .commands(new ExampleLiteCommand(), new ExampleLiteCommand.Help("t-help me"))
                .argument(String.class, ArgumentKey.of("mode"), new ModeResolver())
                .self((builder, internal) ->
                        builder.schematicGenerator(new DescSchematicGenerator(internal.getValidatorService(), internal.getParserRegistry()))
                )
                .invalidUsage(new BasicInvalidUsageHandler())
//                .schematicGenerator(new CustomSchematicGenerator());
                .build();

    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
