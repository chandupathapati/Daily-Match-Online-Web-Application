/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Chandu
 */
@Named(value = "forgotPassword")
@RequestScoped
public class ForgotPassword {

   

    /**
     * Creates a new instance of ForgotPassword
     */
    private String otp;
    private String userID;
    private String errmsg;
    private int sentotp;
    private String newpassword;
    private boolean disable;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    
    

        
    
    
    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public int getSentotp() {
        return sentotp;
    }

    public void setSentotp(int sentotp) {
        this.sentotp = sentotp;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    public void ForgotPassword()
    {
        disable=true;
    }

    
    
    
    public String updatepassword()
    {
         try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (Exception e)
        {
            return ("Internal Error, Please try again later");
        }
             final String URL = "jdbc:oracle:thin:@localhost:1521:CHANDU"; 
            Connection conn = null;
            Statement stat = null;
            ResultSet rs = null;
            try
            {
                conn = DriverManager.getConnection(URL,"scott", "chandu"); 
          stat = conn.createStatement();
                rs=stat.executeQuery("select * from DAM_USER where emailid='"+userID+"'");
                if(rs.next())         
                {
                    
                String value = ""+rs.getString("otp");
       if(value.equals(otp))
       {
          
                conn = DriverManager.getConnection(URL,"scott","chandu");
                stat = conn.createStatement();
                int r = stat.executeUpdate("update DAM_USER set password_='"+newpassword+"' where emailid='"+userID+"'");
            }
        else
       {
           errmsg="Incorrect otp please try again";
           return "forgotPassword";
       }
       }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
          return "login";
       
      
    }
    public String SendOtp()
    {  
          try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (Exception e)
        {
            return ("Internal Error, Please try again later");
        }
        final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:CHANDU";
        Connection conn = null;  //a connection to the database
        Statement stat = null;    //execution of a statement
        ResultSet rs = null;   //set of results
        //to validate userID
     
        
           try
        {
            //connect to the database
          conn = DriverManager.getConnection(DATABASE_URL,"scott","chandu"); 
          stat = conn.createStatement();
                rs= stat.executeQuery("select * from DAM_USER where emailid='"+ userID+"'");
                if(rs.next())
                {
                     int Low = 10000;
                     int High = 100000;
                     Random r = new Random();
                     int Result = r.nextInt(High-Low) + Low;
                     int a = stat.executeUpdate("update DAM_USER set otp='"+Result+"' where emailid='"+userID+"'");
                      Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
                 props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
                props.setProperty("mail.debug", "true");

		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("rsingetham@gmail.com","rohith123");
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("rsingetham@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(userID));
			message.setSubject("Password recovery");
			message.setText("Dear User please enter this number to get your password," +
					Result);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
               this.sentotp =Result;
               
                }
                else
                {
                    userID="";
                    errmsg="incorrect username";
                }
                
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
                            return "ForgotPassword";

    }
    

}
