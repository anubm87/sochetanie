package pro.savichev.bot.commands;

import pro.savichev.bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractCommand implements Command{
    protected Bot bot;

    @Autowired
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
