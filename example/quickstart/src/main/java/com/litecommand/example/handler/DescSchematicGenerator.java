package com.litecommand.example.handler;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicInput;
import dev.rollczi.litecommands.schematic.SimpleSchematicGenerator;
import dev.rollczi.litecommands.validator.ValidatorService;
import snw.jkook.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DescSchematicGenerator extends SimpleSchematicGenerator<CommandSender> {

    public DescSchematicGenerator(ValidatorService<CommandSender> validatorService, ParserRegistry<CommandSender> parserRegistry) {
        super(SchematicFormat.angleBrackets(), validatorService, parserRegistry);
    }

    // 这里返回Schematic的原始内容。通过去重平铺后,new Schematic(schematics)来创建Schematic
    @Override
    protected Stream<String> generateRaw(SchematicInput<CommandSender> schematicInput) {
        CommandExecutor<CommandSender> executor = schematicInput.getExecutor();
        // 此处获得命令的route内容，例如/example set mode,则为 set mode
        String base = schematicInput.collectRoutes().stream()
                              .skip(1)// 这里去除掉第一个元素 代表去除掉指令一级名称，下面用label替换
                              .map(CommandRoute::getName)
                              .collect(Collectors.joining(" "))
                      + " ";
        if (base.trim().isEmpty()) {
            base = schematicInput.getInvocation().label() + " ";
        } else {
            base = schematicInput.getInvocation().label() + " " + base;
        }
        // 这里获取命令类下的所有子命令描述
        Stream<String> routeScheme = generateRoute(schematicInput, schematicInput.getLastRoute(), base);
        // 如果有executor则和上面的结果合并
        if (executor != null) {
            Stream<String> executorScheme = Stream.of(base + generateExecutor(schematicInput, executor));
            return Stream.concat(routeScheme, executorScheme);
        }
        return routeScheme;
    }

    @Override
    protected Stream<String> generateRoute(SchematicInput<CommandSender> input, CommandRoute<CommandSender> route, String base) {
        // 为每个子router生成RouterSchema(递归)
        Stream<String> children = route.getChildren().stream()
                .flatMap(subRoute -> generateRoute(input, subRoute, base + subRoute.getName() + " "));
        // 为每个Route下的executors生成schema
        Stream<String> executors = route.getExecutors().stream()
                .filter(executor -> isVisible(input, executor))
                .map(executor -> {
                    String prefix = base;
                    if (executor.getArguments().isEmpty()) {
                        prefix = base.substring(0, base.length() - 1);// 如果没有参数 那就应该移除掉最后的一个空格字符
                    }
                    return prefix + generateExecutor(input, executor);
                });
        // 合并schema
        return Stream.concat(executors, children);
    }

    // 为具体的executor生成schema
    @Override
    protected String generateExecutor(SchematicInput<CommandSender> input, CommandExecutor<CommandSender> executor) {
        String string = executor.getArguments().stream()
                .map(argument -> String.format(generateArgumentFormat(input, argument), generateArgumentName(input, argument)))
                .collect(Collectors.joining(" "));
        StringBuilder sb = new StringBuilder(string);
        List<String> desc = executor.meta().get(Meta.DESCRIPTION);// 获取该指令执行器的介绍信息
        // 合并描述
        if (!desc.isEmpty()) {
            sb.append(" # ");
            sb.append(String.join(", ", desc));
        }
        return sb.toString();// 返回例如: example set mode <mode> # 该指令的简介信息
    }
}