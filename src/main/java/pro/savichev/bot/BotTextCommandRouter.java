package pro.savichev.bot;

import io.reactivex.Maybe;
import pro.savichev.bot.commands.Command;

public interface BotTextCommandRouter {
    Maybe<Command> getRoute(String text);
}
