/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author xxxxxxxxxxxxxx
 * 
 */
@Named(value = "login")
@SessionScoped
public class Login implements Serializable {

    /**
     * Creates a new instance of Login
     */
    private String emailid;
    private String pwd;
    private String lastlogin;
    private String errmsg;
    public static String loggedinid;
  
  
    
    
    public String login() {
       
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        loggedinid=emailid;

        Database db=new Database();
           try
        {
          
            ResultSet rs = db.dbresultset("Select * from DAM_USER where emailid='"+emailid+"'");
                      
            if(rs.next())
            {
               
                //check if password is correct
                if(rs.getString("CONFIRM").equals("false"))
                {
                    errmsg="Account not yet veirified";
                    return "login";
                }
                if(rs.getString("password_").equals(pwd))
                {
               //get the lastlogin value
              lastlogin=rs.getString("lastseen");
              
              db.dbstatement("update DAM_USER set lastseen='"+dateFormat.format(date)+"' where emailid='"+emailid+"'");
           
         
                     return "Welcome";//go to welcome page login successfull
                }
                else//password is incorrect
                {
                     
                  
                 errmsg="Password Incorrect";
                 
                    return "login";
                }
               
               
            }else//userid is not found or userid is wrong
            {
             
              
                errmsg="User ID  is incorrecct";
                return "login";
            }
            
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return ("internalError");
        }
        finally
        {
            db.dbclose();
        }

        
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public static String getLoggedinid() {
        return loggedinid;
    }

    public static void setLoggedinid(String loggedinid) {
        Login.loggedinid = loggedinid;
    }

     
    
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

  
   
    
}
