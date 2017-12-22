/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruraomsk.list.ru.strongsql;

import java.sql.Date;

/**
 * Структура для хранения значений переменных
 *
 * @author Yury Rusinov <ruraomsk@list.ru Automatics-A Omsk>
 */
public class SetValue {

    private String name;
    private Long time;
    private Object value;
    private byte good;

    public SetValue(String name, Long time, Object value) {
        this.name = name;
        this.time = time;
        this.value = value;
        good = 0;
    }

    public SetValue(String name, Long time, Object value, byte good) {
        this.name = name;
        this.time = time;
        this.value = value;
        this.good = good;
    }

    public SetValue(String name, Object value) {
        this.name = name;
        time = 0L;
        this.value = value;
        good = 0;
    }

    @Override
    public String toString() {
        return "=" + name + " [" + value.toString() + "] " + (time != 0L ? new Date(time).toString() : "") + (good != 0 ? "!" : ""); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the id
     */
    public String getName() {
        return name;
    }

    /**
     * @return the time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    public void setGood(byte good) {
        this.good = good;
    }

    public byte getGood() {
        return good;
    }

    public float getFloatValue() {
        if (value.getClass().getName().equals("java.lang.Boolean")) {
            if ((boolean) value) {
                return 1.0f;
            }
            return 0.0f;
        }
        if (value.getClass().getName().equals("java.lang.Integer")) {
            Integer val = (int) value;
            return (float) (val & 0xffffffff);
        }
        if (value.getClass().getName().equals("java.lang.Long")) {
            Long val = (long) value;
            return (float) (val & 0xffffffff);
        }
        return (float) value;
    }
}
