package my_bot.givebot.serviceInterface;

import my_bot.givebot.model.Group;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public interface BotButtonService {

    ReplyKeyboardMarkup createMarkupButtons(List<String> buttons);

    ReplyKeyboardMarkup makeReplyMarkup();

    ReplyKeyboardMarkup inputFullName(String lang);

    InlineKeyboardMarkup createInlineKeyboardButton(List<String> buttons, int column);
    InlineKeyboardMarkup listChannelInlineKeyboardButton(List<Group> buttons, int column);

    InlineKeyboardMarkup makeInlineMarkup();

    InlineKeyboardMarkup chooseLanguage();

    InlineKeyboardMarkup chooseType(String lang);

    ReplyKeyboardMarkup back(String lang);
    ReplyKeyboardMarkup inputChannelName(String lang);
    ReplyKeyboardMarkup inputChannelUsername(String lang);
    ReplyKeyboardMarkup inputChannelChatId(String lang);

    InlineKeyboardMarkup listChannel();
    /************************************************InlineKeyboards***************************************************/

}