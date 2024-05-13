package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//clase que me permite conectarme a la base de datos
public class ConnectionMySQL
{
    private final String database_name = "pharmacy_database";//nombre de mi base de datos
    private final String user = "root";
    private String password = "1999";
    private String url = "jdbc:mysql://localhost:3306/" + database_name;
    Connection conn = null;
    
    //Hacemos la conexión con la BD :
    public Connection getConnection()
    {  
        try {
            //obtener valor del driver:
            Class.forName("com.mysql.cj.jdbc.Driver");
            //obtener la conexión:
            conn = DriverManager.getConnection(url,user,password);
        } 
        catch (ClassNotFoundException e) {
            System.err.println("Ha ocurrido un ClassNotFoundException " + e.getMessage());
        }
        catch (SQLException e){
            System.err.println("Ha ocurrido un SQLException " + e.getMessage());
        }
        
        return conn;
    }   
}
