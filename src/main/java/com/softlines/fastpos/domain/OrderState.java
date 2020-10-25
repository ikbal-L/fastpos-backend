package com.softlines.fastpos.domain;
public enum OrderState {
    Ordered, //passed
    Prepared, //
    Ready,
    Delivered, //
    Payed,
    Splitted,
    Canceled, //passe a la cuisine puis annule
    Removed, // annule avant de le passer a la cuisine
    Served // servi a la table
}