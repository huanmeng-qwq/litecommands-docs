package com.litecommand.example;

import com.litecommand.example.command.ExampleCommand;
import com.litecommand.example.handler.BasicInvalidUsageHandler;
import com.litecommand.example.handler.CustomSchematicGenerator;
import com.litecommand.example.handler.DescSchematicGenerator;
import com.litecommand.example.handler.ModeResolver;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import snw.jkook.plugin.BasePlugin;
import snw.kookbc.impl.command.litecommands.LiteKookFactory;

public class PluginMain extends BasePlugin {

    @Override
    public void onEnable() {
        LiteKookFactory
                .builder(this)
                .commands(new ExampleCommand())
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
