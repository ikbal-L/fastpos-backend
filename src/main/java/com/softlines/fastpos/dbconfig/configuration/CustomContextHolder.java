package com.softlines.fastpos.dbconfig.configuration;


public class CustomContextHolder {
    private static final ThreadLocal<Long> contextHolder =
            new ThreadLocal<>();
    public static Long getId(){
        return contextHolder.get();
    }

    public static void setId(Long id){
        contextHolder.set(id);
    }

    public static void clear() {
        contextHolder.remove();
    }

}
