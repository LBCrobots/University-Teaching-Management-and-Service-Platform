package com.example.utils;

/**工具类*/
public class ManageUtil {

    /**
     * 随机生成六位数
     * @return
     */
   public  static int randomSixNums(){
       int code = (int) ((Math.random()*9+1)*100000);
       return code;
   }

    public static void main(String[] args) {
        System.out.println(ManageUtil.randomSixNums());
    }
}
