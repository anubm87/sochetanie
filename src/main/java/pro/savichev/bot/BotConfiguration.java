package pro.savichev.bot;

import com.google.gson.Gson;
import pro.savichev.Config;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pro.savichev.bot.impl.TelegramBot;


@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "pro.savichev")
public class BotConfiguration {

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    @Bean
    public TelegramBot getTelegramBot() {
        return new TelegramBot(Config.BOT_TOKEN);
    }
}
