package com.litecommand.example.command;

import com.litecommand.example.Account;
import com.litecommand.example.AccountMapper;
import com.litecommand.example.Server;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import snw.jkook.entity.User;import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "example",aliases = {"exp","exp2"})
@Prefix("/")
@Description(value = {"a simple command example","second description"})
public class ExampleCommand {

    private final AccountMapper mapper;

    @Inject
    public ExampleCommand(AccountMapper mapper){
        this.mapper = mapper;
    }


    @Execute(name = "set mode" )
    @Description("set user mode")
    public void SetMode(
            @Context User user,
            @Context Account account,
            @Arg("mode")@Key("mode") String mode
    ){}
    @Execute(name = "set server" )
    @Description("set user server")
    public void SetServer(
            @Context User user,
            @Arg("server") @Key("server") Server server
    ){}

    @Execute(name = "help")
    @Description("get help")
    @Shortcut("help me") //对于命令的‘快捷键’ 支持使用空格进行子命令划分。此时命令的快捷命令为/help me。
    public void Help(){}
}
