package com.company;

import com.company.controller.MainController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

public class MoneyConverterBot extends TelegramLongPollingBot {

    private MainController mainController = new MainController();

    @Override
    public String getBotUsername() {
        return "UZB_MoneyConverterBot";
    }

    @Override
    public String getBotToken() {
        return "5120530004:AAGo7gIGeT8XXPb-gFIqwvejPi1FoG_3xUc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                User user = message.getFrom();
                if (message.hasText()) {
                    log(user, message.getText());
                    mainController.handleText(user, message);
                } else if (message.hasLocation()) {
                    mainController.handleLocation(user, message);
                }
            } else if (update.hasCallbackQuery()) {
                Message message = update.getCallbackQuery().getMessage();
                User user = update.getCallbackQuery().getFrom();
                String data = update.getCallbackQuery().getData();
                log(user, data);
                mainController.handleCallback(user, message, data);
            }
//            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    public void send(Object object) {
        try {
            if (object instanceof SendMessage) {
                execute((SendMessage) object);
            } else if (object instanceof EditMessageText) {
                execute((EditMessageText) object);
            } else if (object instanceof SendPhoto) {
                execute((SendPhoto) object);
            } else if (object instanceof SendVideo) {
                execute((SendVideo) object);
            } else if (object instanceof SendContact) {
                execute((SendContact) object);
            } else if (object instanceof SendLocation) {
                execute((SendLocation) object);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void log(User user, String text) {
        String str = String.format(LocalDateTime.now() + ", Userid: %d, Firstname: %s, Lastname: %s, Text: %s",
                user.getId(), user.getFirstName(), user.getLastName(), text);
        System.out.println(str);
    }
}
