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
 * suppliers = proveedores
 * Acá van todo los método que permiten a java
 * interactuar con la base de datos
 */
public class SuppliersDao
{
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
   //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //registrar proveedor:
     public boolean registerSupplierQuery(Suppliers supplier){
        String query = "INSERT INTO suppliers (name, description, telephone , address, email, city, created, updated)"
                + "VALUES (?,?,?,?,?,?,?,?)";
        Timestamp dateTime = new Timestamp(new Date().getTime());//para las fechas de updated y created
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            //accedemos a los metodos setter de Customers para envíar los datos que se registraran en la base de datos:
            pst.setString(1,supplier.getName());
            pst.setString(2,supplier.getDescription());
            pst.setString(3,supplier.getTelephone());
            pst.setString(4, supplier.getAddress());
            pst.setString(5,supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, dateTime);
            pst.setTimestamp(8, dateTime);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar al proveedor " + e);
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
     
     //listar proveedores:
     public List listSupplierQuery(String value)
    {
        List<Suppliers> list_suppliers = new ArrayList();
        String query = "SELECT * FROM suppliers";//consulta para listar todos los proveedores de la tabla employees
        String query_search_supplier = "SELECT * FROM suppliers WHERE name LIKE '%" + value + "%'";//para listar el cliente con la
        //identificación que le pasamos en la GUI en la txtfield.
        try {
            conn = cn.getConnection();//hacemos la conexión a la BD
            
            if(value.equalsIgnoreCase(" "))
            {
               pst = conn.prepareStatement(query);//hacemos la consulta para listar todos los clientes
               rs = pst.executeQuery();//ejecutamos la consulta  y nos trae la lista de clientes es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query);   
            }
            else
            {
                pst = conn.prepareStatement(query_search_supplier);//hacemos la consulta
                rs = pst.executeQuery();//Ejecutamos la consulta y nos trae el proveedor especifico, es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query_search_customer);
            }
            
        while(rs.next())//se ejecuta miestras hayan registros
        {
           Suppliers supplier = new Suppliers();
           supplier.setId(rs.getInt("id"));
           supplier.setName(rs.getString("name"));
           supplier.setDescription(rs.getString("description"));
           supplier.setTelephone(rs.getString("telephone"));
           supplier.setAddress(rs.getString("address"));          
           supplier.setEmail(rs.getString("email"));
           supplier.setCity(rs.getString("city"));
           
           list_suppliers.add(supplier);//pasamos el objeto a la lista
           
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
        return list_suppliers;
    }
     
     //modificar proveedores
     public boolean updateSupplierQuery(Suppliers supplier)
    {   
        String query = "UPDATE suppliers SET name = ?, description = ?, telephone = ?, address = ?, "
                + "email = ?, city = ?, updated = ? WHERE id = ?";//modificamos al proveedor
        //con el id que ingresa el ususario
        
        Timestamp dateTime = new Timestamp(new Date().getTime());
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            
            //accedemos a los metodos setter de employees para envías los datos que se registraran en la base de datos:
            pst.setString(1,supplier.getName());
            pst.setString(2,supplier.getDescription());
             pst.setString(3,supplier.getTelephone());
            pst.setString(4,supplier.getAddress());         
            pst.setString(5,supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, dateTime);
            pst.setInt(8, supplier.getId());
            //ejecutamos la sentencia/query SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar los datos del proveedor " + e);
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
     
     //eliminar proveedor:
     public boolean deleteSupplierQuery(int id){
        String query = "DELETE FROM suppliers WHERE id = " + id;//elimina al proveedor que coincida con el id que ingresa el usuario
        //es decir, el id que se recibe por parámetros
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);
            pst.execute();//ejecutamos la consulta
            return true;
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,"No puede eliminar un proveedor que tenga relación con otra tabla ");
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
