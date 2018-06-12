/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Chandu
 */
@Named(value = "searchprofile")
@RequestScoped
public class searchprofile {

    /**
     * Creates a new instance of searchprofile
     */
    private String gender;
    private int minage;
    private int maxage;
    private String city;
    private String interest;
    public static List<String> items = new ArrayList<String>();
    public static List<String> userprofile = new ArrayList<String>();
    private String message;
    public static List<String> friend=new ArrayList<String>();
    public static List<String> dfriend=new ArrayList<String>();

    public static List<String> getDfriend() {
        return dfriend;
    }

    public static void setDfriend(List<String> dfriend) {
        searchprofile.dfriend = dfriend;
    }
    

    public static List<String> getFriend() {
        return friend;
    }

    public static void setFriend(List<String> friend) {
        searchprofile.friend = friend;
    }
    
    
    
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMinage() {
        return minage;
    }

    public void setMinage(int minage) {
        this.minage = minage;
    }

    public int getMaxage() {
        return maxage;
    }

    public void setMaxage(int maxage) {
        this.maxage = maxage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public static List<String> getItems() {
        return items;
    }

    public static void setItems(List<String> items) {
        searchprofile.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    public List<String> profiles()
    {
        return items;
    }
    
     
    
     public List<String> users()
    {
        return userprofile;
    }
     public List<String> flist()
     {
         return friend;
     }
     public List<String> dflist()
     {
         return dfriend;
     }
    public static List<String> getUserprofile() {
        return userprofile;
    }

    public static void setUserprofile(List<String> userprofile) {
        searchprofile.userprofile = userprofile;
    }
    
    
    
    
    
    public String searchprofile() {
         
  
          boolean ageverify=false;
       String sql="SELECT emailid,firstname,lastname,gender,age,city,interest1,interest2,interest3 FROM DAM_USER WHERE ";
  
       //gender
       if(gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"))
       {
           sql=sql+" gender='"+gender+"' and ";
       }
       
       //age
           sql=sql+"(age >="+minage+" and age <="+maxage+") and ";
       
      
       //city
       if(city.equalsIgnoreCase("any"))
       {
           ;
       }else
       {
           sql=sql+"city like '%"+city+"%' and ";
       }
       
       
     //Interest
     
       if(interest.equalsIgnoreCase("any"))
       {
           ;
       }else
       {
           sql=sql+" (interest1 like '%"+interest+"%' or "
                    +"interest2 like '%"+interest+"%' or "
                    +"interest3 like '%"+interest+"%') and ";
       }
     
       sql=sql+"emailid!='"+Login.getLoggedinid()+"'";
       //we have collected all input required to searchprofile people
       //System.out.println(sql);
       //database part to searchprofile using above values
   
       Database db_search=new Database();
       
       try{
      
            
        ResultSet   rs=db_search.dbresultset(sql);
            
            int count=0;
            items.removeAll(items);
            while (rs.next()) {
            items.add(rs.getString("emailid"));
            count=1;
                }
            
            
           if(count ==0)
            {
                message="No Matching Profiles Found";
                return "Profilesearch";
            }
             //close result set and database connection
             
             
             return "Profiles";
          

             
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return "error";
        }
        finally
        {
            
          db_search.dbclose();
        }
      
    }
    //search method completed

     
    public String viewporfile(String str)
    {
        
        String sql="select * from DAM_USER where emailid='"+str+"'";
        
        
        try{
        int view_count=0;  
        Database db_profile=new Database();
        ResultSet rs=db_profile.dbresultset(sql);
        userprofile.removeAll(userprofile);
        while(rs.next())
        {
            userprofile.add(rs.getString("Emailid"));
            userprofile.add(rs.getString("Firstname"));
            userprofile.add(rs.getString("LastName"));
            userprofile.add(rs.getString("Age"));
            userprofile.add(rs.getString("Gender"));
            userprofile.add(rs.getString("Interest1"));
            userprofile.add(rs.getString("Interest2"));
            userprofile.add(rs.getString("Interest3"));
            userprofile.add(rs.getString("viewcount"));
            view_count=Integer.parseInt(rs.getString("viewcount"));
            break;         
        }
        
        String s="update DAM_USER set viewcount='"+(view_count+1) +"' where emailid='"+str+"'" ;
        db_profile.dbstatement(s);
        return "Profiles";
        }catch(SQLException e)
        {
            e.printStackTrace();
            return "Internal error";
        }
         
    }
        //send friend request
        
        public String sendreqeust(String userid)
        {
            
           Database dr=new Database();
               // System.out.println(loginID +"  "+uid);
                //query to check if he is in your friend list
                String sql_rq="Select * from DAM_USER_FRIENDS where (sendID='"+Login.getLoggedinid()+"' and resid='"+userid+"') or (sendID='"+userid+"' and resID='"+Login.getLoggedinid()+"')";
               try{
                ResultSet rs_rq=dr.dbresultset(sql_rq);
               
                boolean friend_flag=false;
                
                while(rs_rq.next())
                {
                    friend_flag=true;
                   // System.out.println(friend_flag);
                }
                rs_rq.close();
                
              if(friend_flag)
              {
                  message="User is present in your friends list";
                  return "Profiles";
              }else{
                String sql="insert into DAM_USER_FRIENDS (sendid,resid,status) values('"+Login.getLoggedinid()+"' , '"+userid+"','pending')" ;
                dr.dbstatement(sql);
                message="Friend request sent";
                return "Profiles";
              }
            
               }catch(SQLException e)
               {
                   e.printStackTrace();
                   return "internal error";
               }finally
               {
              //close the database
              dr.dbclose();
               }
            
        }//send reqeust complleted
        
        
        
        
       //frieds method
        
        public void friendslist()
        {
            Database dr1=new Database();
            
            
            String s1 = "select * from DAM_USER_FRIENDS where sendid='"+Login.getLoggedinid()+"' or resid='"+Login.getLoggedinid()+"' and status= 'accepted'";
            String fr = "";
            try{
            ResultSet r1 = dr1.dbresultset(s1);
            friend.removeAll(friend);
            while(r1.next())
            {
                if(r1.getString("sendid").equals(Login.getLoggedinid()))
                {
                    fr =r1.getString("resid");
                    friend.add(fr);
                }
                
                else if(r1.getString("resid").equals(Login.getLoggedinid()))
                {
                    fr =r1.getString("sendid");
                    friend.add(fr);
                }
                
            }
            }catch(SQLException e)
            {
                e.printStackTrace();
            }
            
        }
        
        public void pendinglist()
        {
            Database dr=new Database();
            
            String sql="select * from DAM_USER_FRIENDS where resid='"+Login.getLoggedinid()+"' and status= 'pending'";
            
            ResultSet rs=dr.dbresultset(sql);
            
            try{
                
                
                friend.removeAll(friend);
                while(rs.next())
                {
                    
                    friend.add(rs.getString("sendid"));
                    
                }
                
                rs.close();
                
            }catch(SQLException e)
            {
                e.printStackTrace();
            }
            
        }
        
        public void Deniedlist()
        {
            Database dr=new Database();
            
            String sql="select * from DAM_USER_FRIENDS where resid='"+Login.getLoggedinid()+"' and status= 'denied'";
            
            ResultSet rs=dr.dbresultset(sql);
            try{
                
                
                dfriend.removeAll(dfriend);
                while(rs.next())
                {
                    
                    dfriend.add(rs.getString("sendid"));
                    
                }
                
                rs.close();
                
            }catch(SQLException e)
            {
                e.printStackTrace();
            }
            
        }
        
        public void Acceptfriend(String str)
        {
            Database dr=new Database();
            
            String sql = "update DAM_USER_FRIENDS set status ='accepted' where resid ='"+Login.getLoggedinid()+"' and sendid = '"+str+"';";
            
            dr.dbstatement(sql);
            
            
            
        }
        
        public void Denyfriend(String str)
        {
            Database dr=new Database();
            
            String sql = "update DAM_USER_FRIENDS set status ='denied' where resid ='"+Login.getLoggedinid()+"' and sendid = '"+str+"';";
            
            dr.dbstatement(sql);
        }
    
    
   
}
