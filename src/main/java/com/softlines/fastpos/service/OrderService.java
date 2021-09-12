package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import org.springframework.stereotype.Service;


public class OrderService {

    public static boolean IsActionPayment(Order source, Order incoming){
        return source.getState()!= OrderState.Payed&& incoming.getState() == OrderState.Payed;
    }

    public static boolean IsActionCancel(Order source, Order incoming){
        return source.getState()!= OrderState.Canceled&& incoming.getState() == OrderState.Canceled;
    }
}
