package com.litecommand.example;

import com.litecommand.example.command.ExampleCommand;
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
                .bind(AccountMapper.class, AccountMapper::new)//给需要AccountMapper的命令注入实例
                .context(Account.class, new AccountContextProvider(new AccountMapper()))//注册Account的上下文提供器
                .commands(ExampleCommand.class)
                .argument(String.class, ArgumentKey.of("mode"), new ModeResolver())
                .invalidUsage(new BasicInvalidUsageHandler())
                .selfProcessor((builder, internal) ->
                        builder.schematicGenerator(new DescSchematicGenerator(internal.getValidatorService(), internal.getWrapperRegistry())
                        ));
//                .schematicGenerator(new CustomSchematicGenerator());

    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
