/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruraomsk.list.ru.strongsql;

import java.sql.Date;

/**
 * Структура для хранения значений переменных
 * @author Yury Rusinov <ruraomsk@list.ru Automatics-A Omsk>
 */
public class SetValue {
    private Integer id;
    private Long time;
    private Object value;

    public SetValue(Integer id, Long time, Object value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }
    public SetValue(Integer id,  Object value) {
        this.id = id;
        this.time = 0L;
        this.value = value;
    }

    @Override
    public String toString() {
        return "="+id.toString()+" ["+value.toString()+"] "+(time!=0L?new Date(time).toString():""); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
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
   
    
}