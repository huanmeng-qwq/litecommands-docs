package com.litecommand.example.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import snw.jkook.command.CommandSender;

public class BasicInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {
    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        Schematic schematic = result.getSchematic();
        InvalidUsage.Cause cause = result.getCause();
        String reason;
        switch (cause) {
            // 例如输入了: example abc
            // 但是没注册abc这个子命令，这里就是UNKNOWN_COMMAND
            case UNKNOWN_COMMAND:
                reason = "未知指令";
                break;
            // 例如输入了: example print text
            // 但是text这个参数的类型是 <num:int> text不是int，这里就是INVALID_ARGUMENT
            // 该类型是由解析器传递的: ParseResult.failure(FailedReason.of(InvalidUsage.Cause.INVALID_ARGUMENT));
            case INVALID_ARGUMENT:
                reason = "错误的参数";
                break;
            // 例如注册指令: example <location>
            // location是一个需要接收3个参数的类型
            // 但是用户输入了: exmaple 0 100
            // 用户只输入了2个参数 缺一个，这里就是MISSING_ARGUMENT
            case MISSING_ARGUMENT:
                reason = "需要参数";
                break;
            // 例如注册指令: exmaple [text] [location]
            // 但是用户输入了: exmaple text 10 20
            // 这里就是MISSING_PART_OF_ARGUMENT
            case MISSING_PART_OF_ARGUMENT:
                reason = "参数缺失";
                break;
            // 例如输入了: example print text 1000
            // 1000就多出来的参数， 这里就是TOO_MANY_ARGUMENTS
            case TOO_MANY_ARGUMENTS:
                reason = "参数过多";
                break;
            default:
                reason = "未知错误";
                break;
        }
        StringBuilder example = new StringBuilder();
        example.append("错误原因:").append(reason).append("\n");
        schematic.all().forEach((it) -> {
            //添加每一条指令的概述
            example.append(it).append("\n");
        });
        example.deleteCharAt(example.length() - 1);//删除最后一个换行符
        chain.resolve(invocation, example);// 执行result的结果处理 例如sender.sendMessage(reason) 或者 message.reply(reason)
    }
}