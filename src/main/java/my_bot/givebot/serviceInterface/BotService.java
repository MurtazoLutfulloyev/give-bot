package my_bot.givebot.serviceInterface;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotService {

    Long getUserChatId(Update update);

    String getUserResponse(Update update);

}
