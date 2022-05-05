package com.softlines.fastpos.sse.model;

public class EventType {
    public static final String CREATE_ORDER = "Create.Order";
    public static final String UPDATE_ORDER = "Update.Order";
    public static final String DELETE_ORDER = "Delete.Order";
    public static final String PAY_ORDER = "Pay.Order";
    public  static final String MODIFY_PAID_ORDER = "Modify.Paid.Order";
    public  static final String UNDO_MODIFY_PAID_ORDER = "Undo.Modify.Paid.Order";
    public  static final String UPDATE_MODIFIED_PAID_ORDER = "Update.PaidModified.Order";
    public static final String CANCEL_ORDER = "Cancel.Order";
    public static final String LOCK_ORDER = "Lock.Order";
}
