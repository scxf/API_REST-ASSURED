package com.test.utils;

import java.util.Random;

/**
 * @Description: 手机号码随机生成类
 * @ClassName: PhoneRandom
 * @Author: Ming         // 创建者
 * @Date: 2020/12/26 17:13   // 时间
 * @Version: 1.0     // 版本
 */
public class PhoneRandom {
    /**
     *  随机生成手机号码
     * @author Ming
     * @return 随机生成的手机号码 String类型
     * @date 2020/12/26 17:14
     */
    public static String setPhone(){
        //定义号段
        String phoneNumber="189";
        Random random = new Random();
        random.nextInt(9);
        //循环8次，拼接号码
        for(int i =0;i<8;i++){
            int num= random.nextInt(9);
            phoneNumber+=num;
        }
       return  phoneNumber;
    }
    /**
     *  生成数据库中未被注册的手机号
     * @author Ming
     * @return  手机号 用String接受
     * @date 2020/12/26 17:36
     */
    public static String getRandomPhone(){
        while (true){
            String mobile_phone = setPhone();
            Object result = JDBCUtils.querySinger("select count(*) from member where mobile_phone=" + mobile_phone);
            if ((Long)result==1){
                System.out.println("该手机号码已经存在");
            }else {
                return  mobile_phone;
            }
        }
    }
}
