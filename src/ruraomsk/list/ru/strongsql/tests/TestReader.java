/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruraomsk.list.ru.strongsql.tests;

import java.sql.Timestamp;
import java.util.ArrayList;
import ruraomsk.list.ru.strongsql.DescrValue;
import ruraomsk.list.ru.strongsql.SetValue;
import ruraomsk.list.ru.strongsql.StrongSql;
import ruraomsk.list.ru.strongsql.Util;

/**
 *
 * @author Yury Rusinov <ruraomsk@list.ru Automatics-A Omsk>
 */
public class TestReader {
    public static void main(String[] args) throws InterruptedException {
        
        ArrayList<SetValue> arrayValues = new ArrayList<>();
        ArrayList<DescrValue> arraydesc = new ArrayList<>();

        StrongSql stSQL = new StrongSql("temp", "org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/testbase", "postgres", "162747");
        Long start=System.currentTimeMillis();
        Integer count=0;
        for (DescrValue dsv : stSQL.getNames()) {
            count++;
            ArrayList<Integer> reqst=new ArrayList<>();
            reqst.add(dsv.getId());

            Timestamp to=new Timestamp(System.currentTimeMillis()-5000L);
            
            Timestamp from=new Timestamp(System.currentTimeMillis()-360000L);
            arrayValues=stSQL.seekData(from, to, reqst);
//            for(SetValue sv:arrayValues){
//                System.out.println(sv.toString());
//            }
//            break;
        }
        Long times=(System.currentTimeMillis()-start)/count;
        System.out.println("time on one seek="+times.toString());
        stSQL.disconnect();
    }
    
}
