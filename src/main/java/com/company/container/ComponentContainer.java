package com.company.container;


import com.company.MoneyConverterBot;
import com.company.controller.MoneyController;
import com.company.enums.UserStatus;

import java.util.HashMap;
import java.util.Map;


public abstract class ComponentContainer {

    public static MoneyConverterBot MONEY_CONVERTER_BOT;
    public static MoneyController MONEY_CONTROLLER = new MoneyController();
    public static final String ADMIN_ID = "490541840";
    public static Map<Long, UserStatus> userStatusMap = new HashMap<>();


}
