package my_bot.givebot.config;


import my_bot.givebot.service.MainBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;


@Component
public class BotConfig{

    private final MainBotService botService;
    public BotConfig(MainBotService botService) {
        this.botService = botService;
    }

    @PostConstruct
    protected void init(){

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(botService);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
