package com.litecommand.example.handler;

import com.litecommand.example.Account;
import com.litecommand.example.AccountMapper;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;

public class AccountContextProvider implements ContextProvider<CommandSender, Account> {

    private final AccountMapper accountMapper;

    public AccountContextProvider(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public ContextResult<Account> provide(Invocation<CommandSender> invocation) {
        // 判断sender是否为平台的User
        if (invocation.sender() instanceof User) {
            User user = (User) invocation.sender();
            // 进行正常的‘账号’查询
            Account account = accountMapper.getAccountById(Integer.parseInt(user.getId()));
            if (account == null) {
                return ContextResult.error("用户未找到");
            } else {
                return ContextResult.ok(() -> account);
            }
        } else {
            return ContextResult.error("请通过用户交互进行命令");
        }
    }
}
