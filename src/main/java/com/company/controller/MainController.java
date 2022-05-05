package com.company.controller;

import com.company.buttons.InlineButtonUtil;
import com.company.container.ComponentContainer;
import com.company.enums.MoneyType;
import com.company.enums.UserStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    private Map<Long, User> userMap = new HashMap<>();

    public void handleText(User user, Message message) {
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        if (text.equals("/start") || text.equals("start")) {
            StringBuilder builder = new StringBuilder();
            builder.append("Welcome <b>");
            builder.append(user.getFirstName());
            builder.append("</b>\n");
            builder.append("<a href=\"https://t.me/UZB_MoneyConverterBot\">This bot</a>");
            builder.append(" is ready to work!");
            sendMessage.setParseMode("HTML");
            sendMessage.setDisableWebPagePreview(true);
            sendMessage.setText(builder.toString());
            sendMessage.setReplyMarkup(InlineButtonUtil.singleKeyboard());
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            if (!userMap.containsKey(user.getId())) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(ComponentContainer.ADMIN_ID);
                builder = new StringBuilder();
                builder.append("<a href=\"https://t.me/" + user.getUserName() + "\">" + user.getFirstName() + "</a>");
                builder.append(" joined the bot");
                sendMessage.setParseMode("HTML");
                sendMessage.setDisableWebPagePreview(true);
                sendMessage.setText(builder.toString());
                userMap.put(user.getId(), user);
                ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            }
        } else if (ComponentContainer.userStatusMap.containsKey(user.getId())) {
            switch (ComponentContainer.userStatusMap.get(user.getId())) {
                case SEND_MESSAGE -> {
                    adminMessage(user, message);
                    ComponentContainer.userStatusMap.remove(user.getId());
                }
                case USD -> ComponentContainer.MONEY_CONTROLLER.handleText(user, message, MoneyType.usd);
                case EUR -> ComponentContainer.MONEY_CONTROLLER.handleText(user, message, MoneyType.eur);
                case JPY -> ComponentContainer.MONEY_CONTROLLER.handleText(user, message, MoneyType.jpy);
                case SUM_TO_TYPE -> ComponentContainer.MONEY_CONTROLLER.convertSum(user, message);
                case TYPE_TO_SUM -> ComponentContainer.MONEY_CONTROLLER.convertMoney(user, message);
            }
        } else {
            sendMessage.setText("\uD83D\uDE41");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            sendMessage.setText("Wrong command!");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
        }

    }


    public void handleCallback(User user, Message message, String callback) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(String.valueOf(user.getId()));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        if (callback.equals("/menu")) {
            sendMessage.setText("Where do we start  <b>" + user.getFirstName() + "</b>");
            sendMessage.setParseMode("HTML");
            sendMessage.setReplyMarkup(InlineButtonUtil.menuSingleKeyboard());
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
        } else if (callback.equals("/contact")) {
            sendMessage.setText("Enter your text: ");
            ComponentContainer.userStatusMap.put(user.getId(), UserStatus.SEND_MESSAGE);
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
        } else if (callback.startsWith("/money")) {
            switch (callback) {
                case "/money/usd" -> {
                    ComponentContainer.userStatusMap.put(user.getId(), UserStatus.USD);
                    ComponentContainer.MONEY_CONTROLLER.showAverage(user, MoneyType.usd);
                    sendMessage.setText("\uD83D\uDCB2");
                    ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
                }
                case "/money/eur" -> {
                    ComponentContainer.userStatusMap.put(user.getId(), UserStatus.EUR);
                    ComponentContainer.MONEY_CONTROLLER.showAverage(user, MoneyType.eur);
                    sendMessage.setText("\uD83D\uDCB2");
                    ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
                }
                case "/money/jpy" -> {
                    ComponentContainer.userStatusMap.put(user.getId(), UserStatus.JPY);
                    ComponentContainer.MONEY_CONTROLLER.showAverage(user, MoneyType.jpy);
                    sendMessage.setText("\uD83D\uDCB2");
                    ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
                }
                case "/money/sum" -> {
                    ComponentContainer.userStatusMap.put(user.getId(), UserStatus.SUM_TO_TYPE);
                    sendMessage.setText("Enter so'm : ");
                    ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
                }
                case "/money/type" -> {
                    ComponentContainer.userStatusMap.put(user.getId(), UserStatus.TYPE_TO_SUM);
                    sendMessage.setText("Enter amount : ");
                    ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
                }
            }
        }
    }

    public void handleLocation(User user, Message message) {
        Location location = message.getLocation();
        System.out.println(user.getFirstName() + ": " + location.getLatitude() + " " + location.getLongitude());

        SendMessage toAdmin = new SendMessage();
        toAdmin.setChatId(ComponentContainer.ADMIN_ID);
        toAdmin.setText("User: " + "@" + user.getUserName() + " :  " + user.getFirstName() +
                "\n<a href=\"https://maps.google.com/?q=" + location.getLatitude() + "," +
                location.getLongitude() + "\">Location</a>");
        toAdmin.setParseMode("HTML");
        toAdmin.setDisableWebPagePreview(true);
        ComponentContainer.MONEY_CONVERTER_BOT.send(toAdmin);
        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(ComponentContainer.ADMIN_ID);
        sendLocation.setLatitude(location.getLatitude());
        sendLocation.setLongitude(location.getLongitude());
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendLocation);
    }


    private void adminMessage(User user, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText("\uD83D\uDE01");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
        sendMessage.setText("Accepted");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
        sendMessage.setText("We are back again  <b>" + user.getFirstName() + "</b>");
        sendMessage.setParseMode("HTML");
        sendMessage.setReplyMarkup(InlineButtonUtil.menuSingleKeyboard());
        messageToAdmin(user, message);
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
    }

    private void messageToAdmin(User user, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ComponentContainer.ADMIN_ID);
        sendMessage.setText("User:  " + "<a href=\"https://t.me/" + user.getUserName() + "\">" + user.getFirstName() +
                "</a>" + "\nID: " + user.getId() +
                "\nMessage: " + message.getText());
        sendMessage.setDisableWebPagePreview(true);
        sendMessage.setParseMode("HTML");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
    }
}
