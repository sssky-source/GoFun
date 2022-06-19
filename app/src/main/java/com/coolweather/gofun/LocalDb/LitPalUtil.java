package com.coolweather.gofun.LocalDb;

import android.util.Log;

import com.coolweather.gofun.fragment.Mine.bean.Person;

import org.litepal.LitePal;

public class LitPalUtil {
    public static void setUserInfo(Person person,String token,String password) {
        LitePal.deleteAll(PersonLitePal.class);
        PersonLitePal person_LitePal = new PersonLitePal();

        //要保证已经保存完成在查询数据库
        //因为LitePal的原因 会将ID 默认作为主键，导致用户id不能保存下来
        //所以另外建立了一个Bean id作为主键 用户ID写为userID
                /*
                    LitePal不支持自定义主键，默认的主键为id,不管一个实体类对象有没有设置id字段，
                    数据库的表中都会创建一个id的主键，而这个id的值会在新记录插入时被自动置为表中的Id，
                    也即是唯一值。如果你里面定义了个String id，运行会报错的。
                 */
        if (person != null) {
            person_LitePal.setUserID(person.getId());
            person_LitePal.setEmail(person.getEmail());
            person_LitePal.setUsername(person.getUsername());
            person_LitePal.setImage(person.getImage());
            person_LitePal.setSex(person.getSex());
            person_LitePal.setAge(person.getAge());
            person_LitePal.setBrief(person.getBrief());
            person_LitePal.setX(person.getX());
            person_LitePal.setY(person.getY());
            person_LitePal.setHobby(person.getHobby());
            person_LitePal.setLocation(person.getLocation());
            person_LitePal.setToken(token);
            person_LitePal.setPassword(password);
            Log.d("111", "Ok request");
            //存入数据库
            person_LitePal.save();
        }
    }

    public static PersonLitePal getPersonInfo(){
        PersonLitePal personLitePal  = LitePal.findFirst(PersonLitePal.class);
        return personLitePal;
    }

//    public static String getUserImage(){
//        PersonLitePal personLitePal  = LitePal.findFirst(PersonLitePal.class);
//
//        return personLitePal.getImage();
//    }
//
//    public static String getUserToken(){
//        PersonLitePal personLitePal  = LitePal.findFirst(PersonLitePal.class);
//        return personLitePal.getToken();
//    }
//
//    public static String getUserName(){
//        PersonLitePal personLitePal  = LitePal.findFirst(PersonLitePal.class);
//        return personLitePal.getUsername();
//    }
}
