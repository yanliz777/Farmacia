package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Customers = clientes
 * Acá van todo los método que permiten a java
 * interactuar con la base de datos
 */
public class CustomersDao {
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
    //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //registrar cliente:
    public boolean registerCustomerQuery(Customers customer){
        String query = "INSERT INTO customers (id, full_name, address, telephone, email, created, updated)"
                + "VALUES (?,?,?,?,?,?,?)";
        Timestamp dateTime = new Timestamp(new Date().getTime());//para las fechas de updated y created
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            //Settiamos los datos que vamos a registrar a la base de datos:
            pst.setInt(1,customer.getId());
            pst.setString(2,customer.getFull_name());
            pst.setString(3,customer.getAddress());
            pst.setString(4,customer.getTelephone());
            pst.setString(5,customer.getEmail());
            pst.setTimestamp(6, dateTime);
            pst.setTimestamp(7, dateTime);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar al empleado " + e);
            return false;
        }
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    //listar clientes:
    public List listCustomerQuery(String value)
    {
        List<Customers> list_customers = new ArrayList();
//Esta consulta me trae todos los registros de la tabla customers/clientes:
        String query = "SELECT * FROM customers";
/*Esta consulta me trae UN registro de la tabla empleados/customers donde el value sea igual al que el usuario ingrese en
el txt_search_customer de la pestaña clientes :*/       
        String query_search_customer = "SELECT * FROM customers WHERE id LIKE '%" + value + "%'";
        
        try {
            conn = cn.getConnection();//hacemos la conexión a la BD
//si NO ingesamos nada en el txt_search_customer mostramos todos los registros de la tabla clientes            
            if(value.equalsIgnoreCase(" "))
            {
               pst = conn.prepareStatement(query);//hacemos la consulta para listar todos los clientes
               rs = pst.executeQuery();//ejecutamos la consulta  y nos trae la lista de clientes es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query);   
            }
            else
            {  //Traemos el registro especifico de la tabla clinetes:
                pst = conn.prepareStatement(query_search_customer);
                rs = pst.executeQuery();//Ejecutamos la consulta y nos trae el cliente especifico, es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query_search_customer);
            }
            
        while(rs.next())//se ejecuta miestras hayan registros
        {
           Customers customer = new Customers();
           //settiamos todos los datos del modelo Customers:
           customer.setId(rs.getInt("id"));
           customer.setFull_name(rs.getString("full_name"));
           customer.setAddress(rs.getString("address"));
           customer.setTelephone(rs.getString("telephone"));
           customer.setEmail(rs.getString("email"));
           
           list_customers.add(customer);//pasamos el objeto a la lista
           
        }
            
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                if(rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        return list_customers;
    }
    
    //modificar cliente:
     public boolean updateCustomerQuery(Customers customer)
    {   
        String query = "UPDATE customers SET full_name = ?, address = ?, "
                + "telephone = ?, email = ?, updated = ? WHERE id = ?";//modificamos al emmpleado 
        //con el id que ingresa el ususario
        
        Timestamp dateTime = new Timestamp(new Date().getTime());
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            
            //accedemos a los metodos setter de employees para envías los datos que se registraran en la base de datos:
            pst.setString(1,customer.getFull_name());
            pst.setString(2,customer.getAddress());
            pst.setString(3,customer.getTelephone());
            pst.setString(4,customer.getEmail());
            pst.setTimestamp(5, dateTime);
            pst.setInt(6, customer.getId());
            //ejecutamos la sentencia/query SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar los datos del cliente " + e);
            return  false;
        }
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
     
     //eliminar cliente:
     public boolean deleteCustomerQuery(int id){
//elimina al cliente que se seleccione en la tabla de la pestaña cliente con el id que se captura:       
        String query = "DELETE FROM customers WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();//ejecutamos la consulta
            return true;
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,"No puede eliminar un cliente que tenga relación con otra tabla ");
            return false;
        }
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
