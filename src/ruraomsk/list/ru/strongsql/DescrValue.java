/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruraomsk.list.ru.strongsql;

import com.tibbo.aggregate.common.Log;

/**
 * Структура для хранения описаний переменных
 * @author Yury Rusinov <ruraomsk@list.ru Automatics-A Omsk>
 */
public class DescrValue {

    private String name;
    private String description;
    private Integer type;

    public DescrValue(String name, String description, Integer type) {
        this.name = name;
        this.description=description;
        this.type = type;
    }
//
//    public DescrValue(String name, Integer id, Integer type, Integer lenght) {
//        this.name = name;
//        this.id = id;
//        this.type = type;
//        this.lenght = lenght;
//    }

    @Override
    public String toString() {
        return name + getType() + getDescription();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        switch (type) {
            case 0:
                return "boolean";
            case 1:
                return "integer";
            case 2:
                return "real";
            case 3:
                return "bigint";
            case 4:
                return "integer";
        }
        Log.CORE.info("Ошибка типа в DescrValue");
        return "integer";
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }


}
