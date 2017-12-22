/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruraomsk.list.ru.strongsql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Хранение данных для записи в БД
 * @author Yury Rusinov <ruraomsk@list.ru Automatics-A Omsk>
 */
public class SetData {
    private Timestamp ts;
    public ArrayList<SetValue> datas=null;

    public SetData(Timestamp ts) {
        this.ts = ts;
        this.datas = new ArrayList<>(); 
    }
    public void AddValue(SetValue value ){
        datas.add(value);
    }

    /**
     * @return the ts
     */
    public Timestamp getTs() {
        return ts;
    }

}
