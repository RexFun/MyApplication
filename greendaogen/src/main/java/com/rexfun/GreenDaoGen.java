package com.rexfun;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGen {
    public static void main(String args[]) throws Exception {
        //创建一个用于添加实体（Entity) 的模式（Schema）对象
        Schema schema = new Schema(1, "com.rexfun.greendaodemo.dao.user");

        //关联实体表的实体类
        Entity userBean = schema.addEntity("User");

        //添加字段
        userBean.addIdProperty();
        userBean.addStringProperty("tc_code");
        userBean.addStringProperty("tc_password");

        //生成DAO
        new DaoGenerator().generateAll(schema, "greendaodemo/src/main/java-gen");
    }
}
