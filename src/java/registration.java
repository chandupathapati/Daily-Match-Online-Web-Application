/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author xxxxxxxxxxxxxx
 */
@Named(value = "registration")
@RequestScoped
public class registration implements Serializable{

    
    /**
     * Creates a new instance of registration
     */
    private String emailid;
    private String password;
    private String FirstName;
    private String LastName;
    private String Gender;
    private String Age;
    private String City;
    private String i1;
    private String i2;
    private String i3;
    private String message;

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getI1() {
        return i1;
    }

    public void setI1(String i1) {
        this.i1 = i1;
    }

    public String getI2() {
        return i2;
    }

    public void setI2(String i2) {
        this.i2 = i2;
    }

    public String getI3() {
        return i3;
    }

    public void setI3(String i3) {
        this.i3 = i3;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
       
    
    
    public String registration() {
        
        
        
        //to get the current date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        
        //to check if userid is unique
        boolean unique=false;
         try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
                      
        }
        //database string
        final String DATABASE_URL="jdbc:oracle:thin:@localhost:1521:CHANDU";
       
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
   
        
        while(!unique){
   
                
           try
        {
            //connect to the database
          connection = DriverManager.getConnection(DATABASE_URL, 
                  "scott", "chandu");
            //create statement
            statement = connection.createStatement();
            //search the user accounts with the userID
            resultSet = statement.executeQuery("Select * from DAM_USER where emailid='"+emailid+"'");
            //use the found records to create bank account objects
            if(resultSet.next())
            {
               message="Email id is already registered";
               return "register";
               
            }else
            {
                unique=true;
            }
            
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //close the database
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
        
        
        }//end of while loop
        
          
        
           try
        {
            //connect to the database
          connection = DriverManager.getConnection(DATABASE_URL, 
                 "scott", "chandu");
            //create statement
            statement = connection.createStatement();
           
             
                Database dr=new Database();
          
               
              String sql="insert into DAM_USER values"
                        + "('" + emailid + "', '" +
                      password+ "', '" +
                      FirstName + "', '" +
                      LastName + "', '" +
                      Gender + "'," +
                      Age + ", '" +
                      City + "', '" +
                      i1 + "', '" +
                      i2 + "', '" +
                      i3 + "', '" +
                      dateFormat.format(date)+"'," +
                      0 +",utl_raw.cast_to_raw('NULL'),0,'true')";
              
               statement.executeUpdate(sql);
                    message="Account creation successful!";
              
              
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
					return new PasswordAuthentication("pchandrashakervarmav@gmail.com","******");
				}
			});
                try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("pchandrashakervarmav@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailid));
			message.setSubject("Email confirmation");
			message.setText("Please click the link to confirm your mail id," +
					"http://localhost:8080/Dating/faces/VerifiedAccount.xhtml");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
                
                return "EmailConformation";
                
                
                
                
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return "internalError";
        }
        finally
        {
            //close the database
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
  
    }
    
    public String accountconform()
    {
        
        try
        {
                       
            Database db=new Database();
            db.dbstatement("update DAM_USER set confirm='true' where emailid='"+emailid+"'");
            
            db.dbclose();
            return "login";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "internal error";
        }
    }
}
