package my_bot.givebot.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my_bot.givebot.constants.UserMessages;
import my_bot.givebot.model.Group;
import my_bot.givebot.model.User;
import my_bot.givebot.serviceInterface.BotButtonService;
import my_bot.givebot.serviceInterface.BotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainBotService extends TelegramLongPollingBot {
    private final User user;

    private final Group group;
    private final UserMessages userMessages;
    private final BotButtonService buttonService;
    private final BotService service;
    private final GroupService groupService;
    private final UserService userService;
    private final Map<Long, Integer> back = new HashMap<>();
    private final Map<Long, String> lang = new HashMap<>();
    private final Map<Long, String> round = new HashMap<>();
    private final HashMap<Long, HashMap<String, String>> globalMap = new HashMap<Long, HashMap<String, String>>();
    private final HashMap<String, String> map = new HashMap<String, String>();
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        user.setChatId(service.getUserChatId(update));
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = service.getUserChatId(update);

            if ("/start".equals(message.getText())) {
//                userService.saveUser(update);
                round.put(chatId, "0");
                executeButtons(null, buttonService.chooseLanguage(), "uz");
                round.put(chatId, "1");


            }


            if (update.hasMessage() && message.hasText()) {
                if (message.getText().equals("Main menu")) {
                    message.setText("/start");
                    onUpdateReceived(update);
                }
                if (message.getText().equals("Ortga")) {
                    switch (round.get(chatId)) {
                        case "2":
                            round.put(chatId, "1");
                            onUpdateReceived(update);
                            break;
                    }
                }
                switch (round.get(chatId)) {
                    case "2":
                        shareContact(lang.get(user.getChatId()));
                        round.put(user.getChatId(), "4.1");
                        group.setName(update.getMessage().getText());
                        break;
                    case "3.1":
                        executeButtons(buttonService.inputChannelName(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "4.1");
                        group.setName(update.getMessage().getText());
                        break;
                    case "4.1":
                        executeButtons(buttonService.inputChannelUsername(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "5.1");
                        group.setUsername(update.getMessage().getText());
                        break;
                    case "5.1":
                        executeButtons(buttonService.inputChannelChatId(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "6.1");
                        group.setChatId(update.getMessage().getText());
                        groupService.saveGroup(group);
                        break;
                    case "6":
                        break;

                    case "12":
                        executeGroup(update);
                        message.setText("/start");
                        execute();
                        executeButtons(buttonService.back(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.remove(chatId);
                        onUpdateReceived(update);
                        break;
                    case "13":
                        shareContact(lang.get(chatId));
                        break;

                    default:
                        return;
                }

                if (update.getMessage().hasContact()) {
                    message.setText("/start");
                    round.put(chatId, "14");
                    executeGroupContact(update);
                    round.remove(chatId);
                    onUpdateReceived(update);
                }
            }

            if (update.getMessage().hasContact()) {
                round.put(update.getMessage().getChatId(), "3");
                executeButtons(null, buttonService.listChannel(), lang.get(user.getChatId()));
            }


        }
        /*************************************************InlineKeyboards**********************************************/
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long chatId = callbackQuery.getMessage().getChatId();
            if (round.get(user.getChatId()).equals("1")) {
                switch (callbackQuery.getData()) {
                    case "\uD83C\uDDFA\uD83C\uDDFF O'zbekcha":

                        if (chatId == 1) {
                            lang.put(user.getChatId(), "uz");
                            executeDeleteMessage(update);
                            round.put(chatId, "1.1");
                            executeButtons(null, buttonService.chooseType("uz"), "uz");
                            round.put(chatId, "2.1");

                        } else {
                            lang.put(user.getChatId(), "uz");
                            executeDeleteMessage(update);
                            executeButtons(buttonService.inputFullName(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                            round.put(user.getChatId(), "2");
                            globalMap.put(user.getChatId(), map);
                        }
                        break;
                    case "\uD83C\uDDF7\uD83C\uDDFA Русский":
//                        shareContact(null);
                        lang.put(user.getChatId(), "ru");
                        executeDeleteMessage(update);
                        executeButtons(buttonService.inputFullName(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "2");
                        globalMap.put(user.getChatId(), map);
                        break;
                    default:
                        return;

                }

            }
            if (round.get(user.getChatId()).equals("2.1")) {
                switch (callbackQuery.getData()) {
                    case "Add channel, group, bot":
                        executeButtons(buttonService.inputChannelName(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "3.1");
                        break;
                    case "Связь":
//                        executeButtons(null, buttonService.chooseConnect(lang.get(user.getChatId())), lang.get(user.getChatId()));
                        round.put(user.getChatId(), "11");
                        break;
                }
            }
            if (round.get(user.getChatId()).equals("11")) {
                switch (callbackQuery.getData()) {
                    case "Operatorga yozish":
                        executeButtons(buttonService.back(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "12");
                        break;
                    case "Menga qo'ng'iroq qiling":
                        round.put(user.getChatId(), "13");
                        shareContact(lang.get(user.getChatId()));
                        break;
                    case "Ortga":
//                        executeButtons(null, buttonService.chooseConnect(lang.get(user.getChatId())), lang.get(user.getChatId()));
                        round.put(user.getChatId(), "11");
                        break;

                    case "Напишите оператору":
                        executeButtons(buttonService.back(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "12");
                        break;
                    case "Позвони мне":
                        executeButtons(buttonService.back(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "13");
                        break;
                    case "Назад":
//                        executeButtons(null, buttonService.chooseConnect(lang.get(user.getChatId())), lang.get(user.getChatId()));
                        round.put(user.getChatId(), "11");
                        break;
                }

            }
        }
    }
    private void executeDeleteMessage(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        deleteMessage.setChatId(String.valueOf(user.getChatId()));
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void execute() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getChatId()));
        sendMessage.setText(userMessages.response(round.get(user.getChatId()).toString(), lang.get(user.getChatId())));
        sendMessage.enableHtml(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeGroup(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("");
        sendMessage.setText(
                "First name: " + update.getMessage().getFrom().getFirstName() +
                        "\nLast name: " + update.getMessage().getFrom().getLastName() +
                        "\nUsername: " + update.getMessage().getFrom().getUserName() +
                        "\nDescription: " + update.getMessage().getText());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeGroupContact(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("groupId");
        sendMessage.setText("Contact: " + update.getMessage().getContact().getPhoneNumber());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



    private void executeButtons(ReplyKeyboardMarkup replyKeyboardMarkup, InlineKeyboardMarkup inlineKeyboardMarkup, String lang) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getChatId()));
        sendMessage.setText(userMessages.response(round.get(user.getChatId()).toString(), lang));
        sendMessage.enableHtml(true);
        if (replyKeyboardMarkup != null)
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        else if (inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void shareContact(String lang) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getChatId()));
        sendMessage.setText(userMessages.response(round.get(user.getChatId()).toString(), lang));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(userMessages.response(String.valueOf(round.get(user.getChatId())), lang));
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
