/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruraomsk.list.ru.strongsql;

import com.tibbo.aggregate.common.Log;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Библиотека для работы с сохраненными значениями переменных
 *
 * @author Yury Rusinov <ruraomsk@list.ru Automatics-A Omsk>
 */
public class StrongSql {

    private Long SleepTime = 1000L;
    private Connection con;
    private ConcurrentLinkedQueue<SetData> outdata;
    private Statement stmt;
    private boolean errorSQL;
    private ParamSQL paramSQL;
    private WriteterSql wrSQL = null;

    /**
     * Начинаем работать с существующей базой данных
     *
     */
    public StrongSql(ParamSQL param) {
        startWork(param);
    }

    public StrongSql(ParamSQL param, Long SleepTime) {
        this.SleepTime = SleepTime;
        startWork(param);
    }

    private void startWork(ParamSQL param) {
        paramSQL = param;
        errorSQL = true;
        outdata = new ConcurrentLinkedQueue<>();
        if (connectDB()) {
            errorSQL = false;
            wrSQL = new WriteterSql();
        }

    }

    /**
     * Создаем базу данных на основе массива описаний полей
     *
     * @param arraydesc - массив описаний переменных
     * @param DBType - тип хранения данных 0-одна временная метка для всех.
     * 1-каждое значение имеет временную метку
     * @param maxSize - максимальное кол-во записей в циклическом буфере
     * @param description - название таблицы в читаемом виде
     */
    public StrongSql(ParamSQL param, ArrayList<DescrValue> arraydesc, String description) {
        try {
            paramSQL = param;
            Class.forName(paramSQL.JDBCDriver);
            con = DriverManager.getConnection(paramSQL.url, paramSQL.user, paramSQL.password);
            stmt = con.createStatement();
            stmt.executeUpdate("drop table if exists " + param.myDB + ";");
            String s = "create table " + param.myDB + " (tm timestamp primary key not null";
            for (DescrValue dsv : arraydesc) {
                s += "," + dsv.getName() + " " + dsv.getType();
            }
            s += ");";
            stmt.executeUpdate(s);
            stmt.executeUpdate("comment on table " + param.myDB + " is '" + description + "';");
            for (DescrValue dsv : arraydesc) {
                stmt.executeUpdate("comment on column "+param.myDB+"."+dsv.getName()+" is '"+dsv.getDescription()+"';");
            }
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Log.CORE.info("Error for create DataBase " + ex.getMessage());
        }

    }

    /**
     * Поиск в базе данных по переменных
     *
     * @param from - время от
     * @param to - время до
     * @param name - переменная для поиска
     * @return - возвращает массив значений переменных с метками времени
     */
    public synchronized ArrayList<SetValue> seekData(Timestamp from, Timestamp to, String name) {
        try {
            ArrayList<SetValue> result = new ArrayList<>();
            HashMap<Long, SetValue> map = new HashMap<>(32000);
            String rez = "SELECT tm," + name + " FROM " + paramSQL.myDB + " WHERE  tm<='" + to.toString() + "' and tm>='" + from.toString() + "' ORDER BY tm ";
            ResultSet rs = stmt.executeQuery(rez);
            while (rs.next()) {
                Long tm = rs.getTimestamp("tm").getTime();
                result.add(new SetValue(name, tm, rs.getObject(name)));
            }
            rs.close();
            return result;
        } catch (SQLException ex) {
            Log.CORE.info("Ошибка SQL " + ex.getMessage());
            return null;
        }
    }

    /**
     * Установить период записи значений в БД
     *
     * @param SleepTime
     */
    public void setSleepTime(Long SleepTime) {
        this.SleepTime = SleepTime;
    }

    /**
     * Закрывает соединение с базой данных
     */
    public void disconnect() {
        try {
            if (wrSQL == null) {
                return;
            }

            wrSQL.interrupt();
            wrSQL.join(SleepTime * 2);

            con.commit();
            stmt.close();
            con.close();
        } catch (SQLException | InterruptedException ex) {
        }
    }

    private boolean connectUrl() throws ClassNotFoundException, SQLException {
        Class.forName(paramSQL.JDBCDriver);
        con = DriverManager.getConnection(paramSQL.url, paramSQL.user, paramSQL.password);
        stmt = con.createStatement();
        return true;
    }

    private boolean connectDB() {
        try {
            connectUrl();
        } catch (ClassNotFoundException | SQLException ex) {
            Log.CORE.info("Connected " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Есть ли соединение
     *
     * @return true - база данных дотупна
     */
    public boolean isconnected() {
        return !errorSQL;
    }

    public void addValues(SetData setdata) {
//        System.out.println(setdata.toString());
        outdata.add(setdata);

    }

    class WriteterSql extends Thread {

        public WriteterSql() {
            start();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(SleepTime);
                } catch (InterruptedException ex) {
                    break;
                }
                SetData value = null;
                while ((value = outdata.poll()) != null) {
//                    System.out.println(value.toString());
                    try {
                        String sl = "INSERT INTO " + paramSQL.myDB + "(tm ";
                        String sr = " VALUES( '" + value.getTs().toString() + "'";
                        for (SetValue v : value.datas) {
                            sl += "," + v.getName();
                            sr += "," + v.getValue().toString();
                        }
                        sl += ")";
                        sr += ");";

                        ResultSet rs = stmt.executeQuery(sl + sr);
                        rs.close();
                    } catch (SQLException ex) {
                        System.out.println("Error writer " + ex.getMessage());
                        outdata.add(value);
                        errorSQL = true;
                        continue;
                    }

                }
            }
        }

    }
}
