package pro.savichev.bot.impl;

import io.reactivex.Maybe;
import pro.savichev.Config;
import pro.savichev.bot.commands.TextCommand;
import pro.savichev.bot.BotTextCommandRouter;
import pro.savichev.bot.commands.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class BotTextCommandRouterImpl implements BotTextCommandRouter {

    private static final Map<String, Class<? extends Command>> mapping = new HashMap<>();

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(TextCommand.class);
        beans.forEach((key, value) -> {
            TextCommand textCommand = AnnotationUtils.findAnnotation(value.getClass(), TextCommand.class);
            String mappingValue = textCommand.value();
            mapping.put(mappingValue, value.getClass().asSubclass(Command.class));
        });
    }
    @Override
    public Maybe<Command> getRoute(String text) {
        return Maybe.create(emitter -> {
            Class<? extends Command> clazz = mapping.get(getCommand(text));
            if (clazz != null) {
                emitter.onSuccess(applicationContext.getBean(clazz));
                return;
            }
            emitter.onComplete();
        });
    }

    private String getCommand(String text) {
        String first = text.split(" ")[0];
        first = first.replaceAll("/", "");
        if (first.endsWith(Config.BOT_USERNAME)){
            first = first.substring(0, first.length() - Config.BOT_USERNAME.length() - 1);
        }
        return first.toLowerCase();
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
