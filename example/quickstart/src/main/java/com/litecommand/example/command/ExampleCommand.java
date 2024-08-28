package com.litecommand.example.command;

import com.litecommand.example.Server;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import snw.jkook.entity.User;import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "example",aliases = {"exp","exp2"})
@Prefix("/")
@Description(value = {"a simple command example","second description"})
public class ExampleCommand {

    @Execute(name = "set mode" )
    public void SetMode(
            @Context User user,
            @Arg("mode")@Key("mode") String mode
    ){}
    @Execute(name = "set server" )
    public void SetServer(
            @Context User user,
            @Arg("server") Server server
    ){}

    @Execute(name = "help")
    public void Help(){}
}
