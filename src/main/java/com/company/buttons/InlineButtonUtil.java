package com.company.buttons;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InlineButtonUtil {

    public static InlineKeyboardButton button(String text, String callBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
        return button;
    }

    public static InlineKeyboardButton button(String text, String callBackData, String emoji) {
        String emojiText = EmojiParser.parseToUnicode(emoji + " " + text);
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(emojiText);
        button.setCallbackData(callBackData);
        return button;
    }

    public static List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons) {
        return new LinkedList<>(Arrays.asList(inlineKeyboardButtons));
    }

    @SafeVarargs
    public static List<List<InlineKeyboardButton>> rowList(List<InlineKeyboardButton>... rows) {
        return new LinkedList<>(Arrays.asList(rows));
    }

    public static InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> rowList) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowList);
        return keyboardMarkup;
    }

    /**
     * Utils Keyboards
     */
    public static InlineKeyboardMarkup singleKeyboard() {
        return InlineButtonUtil.keyboard(
                InlineButtonUtil.rowList(
                        InlineButtonUtil.row(
                                InlineButtonUtil.button("Let's go", "/menu", "\uD83D\uDCB8")
                        )));
    }

    public static InlineKeyboardMarkup chooseKeyboard() {
        return InlineButtonUtil.keyboard(
                InlineButtonUtil.rowList(
                        InlineButtonUtil.row(
                                InlineButtonUtil.button("So'm to another money type", "/money/sum", "\uD83E\uDDFB")
                        ),
                        InlineButtonUtil.row(
                                InlineButtonUtil.button("Another money type to so'm", "/money/type", "\uD83D\uDCB8")
                        )));
    }

    public static InlineKeyboardMarkup menuSingleKeyboard() {
        return InlineButtonUtil.keyboard(
                InlineButtonUtil.rowList(
                        InlineButtonUtil.row(
                                InlineButtonUtil.button("USD", "/money/usd", "\uD83D\uDCB5"),
                                InlineButtonUtil.button("EUR", "/money/eur", "\uD83D\uDCB6"),
                                InlineButtonUtil.button("JPY", "/money/jpy", "\uD83D\uDCB4")),
                        InlineButtonUtil.row(
                                InlineButtonUtil.button("Contact with admin", "/contact", "\uD83D\uDC64")
                        )
                ));
    }
}
