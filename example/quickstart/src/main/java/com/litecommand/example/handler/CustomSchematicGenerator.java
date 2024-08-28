package com.litecommand.example.handler;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.schematic.SchematicInput;
import snw.jkook.command.CommandSender;

import java.util.List;

public class CustomSchematicGenerator implements SchematicGenerator<CommandSender> {

    // 简单获取命令的DESCRIPTION作为schema
    @Override
    public Schematic generate(SchematicInput<CommandSender> schematicInput) {
        List<String> desc = schematicInput.getLastRoute().meta().get(Meta.DESCRIPTION);
        return new Schematic(desc);
    }
}
