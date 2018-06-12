/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Chandu
 */
public class Database {
    private Connection connection;
    private Statement statement;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Database() {
        
        try{
                //load the Driver
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
                      
        }
       String DATABASE_URL="jdbc:oracle:thin:@localhost:1521:CHANDU";
       
       
        //connect to the database
         connection = DriverManager.getConnection(DATABASE_URL, 
                  "scott", "chandu");
            //create statement
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
 
        //type of result set completed.
        }catch (SQLException e)
            {
                e.printStackTrace();
                
            }
        
    }
public void dbstatement(String sql)
{
    try{
      statement.executeUpdate(sql); 
      statement.execute("commit;");
      
    }catch (SQLException e)
            {
                e.printStackTrace();
            }
    
    
}
public ResultSet dbresultset(String sql) {
     ResultSet resultSet = null;
    try{
                    
            resultSet=statement.executeQuery(sql);
         
        
    }
    catch (SQLException e)
            {
                e.printStackTrace();
            }
    
    return resultSet;  
  }

public void dbclose(){
    //close the database
            try
            {
               
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
}
        
  
    }
       

