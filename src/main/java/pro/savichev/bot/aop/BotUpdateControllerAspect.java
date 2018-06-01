package pro.savichev.bot.aop;

import com.pengrad.telegrambot.model.Update;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class BotUpdateControllerAspect {

    @Pointcut("execution(* pro.savichev.bot.BotUpdateController.onUpdate(..)) && args(update,..)")
    private void executionOnUpdate(Update update) {}

    @Around(value = "executionOnUpdate(update)", argNames = "joinPoint, update")
    private Object aroundOnUpdate(ProceedingJoinPoint joinPoint, Update update) throws Throwable{
        Logger.getLogger(getClass().getSimpleName()).log(Level.INFO, "NEW UPDATE!");
        return joinPoint.proceed();
    }

}
