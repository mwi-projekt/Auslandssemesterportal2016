/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.mwi.Auslandsemesterportal2016.test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

public class TestUser {
    
    private static String testUser_mail = "testusermwi@dhbw.de";
    private static String testUser_pw = "Password1234";
    private static int testUser_id = 368;
    private static int testUser_rolle = 3;
    
    private static String testAAMA_mail = "testaamamwi@dhbw.de";
    private static String testAAMA_pw = "Password1234";
    private static int testAAMA_id = 370;
    private static int testAAMA_rolle = 2;
    
    private static String testAdmin_mail = "testadminmwi@dhbw.de";
    private static String testAdmin_pw = "Password1234";
    private static int testAdmin_id = 371;
    private static int testAdmin_rolle = 1;
    
    static{
        for(int i=1; i<4;i++){
            checkUser(i);
        }
    }
    
    private static void checkUser(int role){
        if(role == 3){
            int uID = SQL_queries.getUserID(testUser_mail);
            
            if(uID == -1){
            String salt = Util.generateSalt();
            SQL_queries.userRegister("UnitTest", "User", Util.HashSha256(Util.HashSha256(testUser_pw) + salt), salt, 3, testUser_mail, "UnitTest",
		"UnitTest", 0003, "000", "000", "UnitTest", "1");
            }
            else{
                testUser_id = uID;
            }
           if(!SQL_queries.userLogin(testUser_mail, SQL_queries.getSalt(testUser_mail), testUser_pw)[0].equals("1")){
               SQL_queries.setPassword(SQL_queries.forgetPassword(testUser_mail), testUser_pw);
           }
        }
        else if(role == 2){
            int uID = SQL_queries.getUserID(testAAMA_mail);
            if(uID == -1){
            String salt = Util.generateSalt();
            SQL_queries.userRegister("UnitTest", "AuslandsMA", Util.HashSha256(Util.HashSha256(testAAMA_pw) + salt), salt, 2, testAAMA_mail, "UnitTest",
		"UnitTest", 0002, "000", "000", "UnitTest", "1");
            }
            else{
                testAAMA_id = uID;
            }
           if(!SQL_queries.userLogin(testAAMA_mail, SQL_queries.getSalt(testAAMA_mail), testAAMA_pw)[0].equals("1")){
               SQL_queries.setPassword(SQL_queries.forgetPassword(testAAMA_mail), testAAMA_pw);
           }
        }
        else if(role == 1){
            int uID = SQL_queries.getUserID(testAdmin_mail);
            
            if(uID == -1){
            String salt = Util.generateSalt();
            SQL_queries.userRegister("UnitTest", "Admin", Util.HashSha256(Util.HashSha256(testAdmin_pw) + salt), salt, 1, testAdmin_mail, "UnitTest",
		"UnitTest", 0001, "000", "000", "UnitTest", "1");
            }
            else{
                testAdmin_id = uID;
            }
           if(!SQL_queries.userLogin(testAdmin_mail, SQL_queries.getSalt(testAdmin_mail), testAdmin_pw)[0].equals("1")){
               SQL_queries.setPassword(SQL_queries.forgetPassword(testAdmin_mail), testAdmin_pw);
           }
        }
       
       
       

    }
    
    public static String[] getAdmin(){
        return new String[] {testAdmin_mail, testAdmin_pw, ""+testAdmin_id};
    }
    
     public static String[] getAAMA(){
        return new String[] {testAAMA_mail, testAAMA_pw, ""+testAAMA_id};
    }
     
      public static String[] getUser(){
        return new String[] {testUser_mail, testUser_pw, ""+testUser_id};
    }
    
    
}
