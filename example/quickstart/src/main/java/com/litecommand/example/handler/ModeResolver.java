package com.litecommand.example.handler;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import snw.jkook.command.CommandSender;

import java.util.Arrays;

//针对Mode参数的解释器和建议提示。
//CommandSender对应其平台的CommandSender
//String代表着解析结果
//invocation表示命令的上下文
//argument 表示参数对象
//s 代表着原始输入(已分割好)
public class ModeResolver extends ArgumentResolver<CommandSender,String> {
    @Override
    protected ParseResult<String> parse(Invocation<CommandSender> invocation, Argument<String> argument, String s) {
        if(Arrays.asList("模式1","模式2").contains(s)){
            return ParseResult.success(s);
        }else{
            return ParseResult.failure("请输入正确的模式");
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<String> argument, SuggestionContext context) {
        return SuggestionResult.of("模式1","模式2");
    }
}
