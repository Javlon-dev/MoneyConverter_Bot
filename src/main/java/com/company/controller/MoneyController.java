package com.company.controller;

import com.company.buttons.InlineButtonUtil;
import com.company.container.ComponentContainer;
import com.company.enums.MoneyType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MoneyController {
    private JSONArray jsonUsd = new JSONArray(OKHTTPRequest(MoneyType.usd.name()));
    private JSONArray jsonEur = new JSONArray(OKHTTPRequest(MoneyType.eur.name()));
    private JSONArray jsonJpy = new JSONArray(OKHTTPRequest(MoneyType.jpy.name()));

    private JSONObject usdJson = jsonUsd.getJSONObject(0);
    private JSONObject eurJson = jsonEur.getJSONObject(0);
    private JSONObject jpyJson = jsonJpy.getJSONObject(0);

    private Double usd = Double.valueOf(String.valueOf(usdJson.get("Rate")));
    private Double eur = Double.valueOf(String.valueOf(eurJson.get("Rate")));
    private Double jpy = Double.valueOf(String.valueOf(jpyJson.get("Rate")));

    public void handleText(User user, Message message, MoneyType type) {
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        if (!checkNumber(text)) {
            sendMessage.setText("\uD83D\uDE41");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            sendMessage.setText("Wrong type!");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            return;
        }
        double money = Double.parseDouble(text);
        double summa;
        switch (type) {
            case usd -> {
                summa = money * usd;
                sendMessage.setText(text + " \uD83D\uDCB5 DOLLAR - " + (float) summa + " so'mga teng  \uD83E\uDDFB\n");
            }
            case eur -> {
                summa = money * eur;
                sendMessage.setText(text + " \uD83D\uDCB6 EURO - " + (float) summa + " so'mga teng  \uD83E\uDDFB\n");
            }
            case jpy -> {
                summa = money * jpy;
                sendMessage.setText(text + " \uD83D\uDCB4 YUAN - " + (float) summa + " so'mga teng  \uD83E\uDDFB\n");
            }
        }
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
    }


    public void convertSum(User user, Message message) {
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        if (!checkNumber(text)) {
            sendMessage.setText("\uD83D\uDE41");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            sendMessage.setText("Wrong type!");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            return;
        }
        double money = Double.parseDouble(text);
        double summa;

        summa = money / usd;
        sendMessage.setText((float) summa + "  \uD83D\uDCB5 " + " DOLLAR " + " ga teng\n");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);

        summa = money / eur;
        sendMessage.setText((float) summa + "  \uD83D\uDCB6 " + " EURO " + " ga teng\n");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);

        summa = money / jpy;
        sendMessage.setText((float) summa + "  \uD83D\uDCB4 " + " YUAN " + " ga teng\n");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);


    }

    public void convertMoney(User user, Message message) {
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        if (!checkNumber(text)) {
            sendMessage.setText("\uD83D\uDE41");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            sendMessage.setText("Wrong type!");
            ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            return;
        }
        double money = Double.parseDouble(text);
        double summa;

        summa = money * usd;
        sendMessage.setText(text + " DOLLAR - " + (float) summa + " so'mga teng  \uD83E\uDDFB\n");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);

        summa = money * eur;
        sendMessage.setText(text + " EURO - " + (float) summa + " so'mga teng  \uD83E\uDDFB\n");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);

        summa = money * jpy;
        sendMessage.setText(text + " YUAN - " + (float) summa + " so'mga teng  \uD83E\uDDFB\n");
        ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
    }

    private boolean checkNumber(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void showAverage(User user, MoneyType type) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        switch (type) {
            case usd -> {
                JSONObject jsonObject = usdJson;
                sendMessage.setText("\n1 " + jsonObject.get("CcyNm_UZ") + " - " + jsonObject.get("Rate") + " so'm  \uD83E\uDDFB\n");
                sendMessage.setReplyMarkup(InlineButtonUtil.chooseKeyboard());
                ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            }
            case eur -> {
                JSONObject jsonObject = eurJson;
                sendMessage.setText("\n1 " + jsonObject.get("CcyNm_UZ") + " - " + jsonObject.get("Rate") + " so'm  \uD83E\uDDFB\n");
                sendMessage.setReplyMarkup(InlineButtonUtil.chooseKeyboard());
                ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            }
            case jpy -> {
                JSONObject jsonObject = jpyJson;
                sendMessage.setText("\n1 " + jsonObject.get("CcyNm_UZ") + " - " + jsonObject.get("Rate") + " so'm  \uD83E\uDDFB\n");
                sendMessage.setReplyMarkup(InlineButtonUtil.chooseKeyboard());
                ComponentContainer.MONEY_CONVERTER_BOT.send(sendMessage);
            }
        }
    }


    private String OKHTTPRequest(String type) {
        Request request = new Request.Builder()
                .url("https://cbu.uz/oz/arkhiv-kursov-valyut/json/" + type + "/"
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
