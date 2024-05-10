package models;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.List;

/**
 * Employees = empleados
 * Acá van todos los método que permiten a java
 * interactuar con la base de datos
 */
public class EmployeesDao {
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
    //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //Variables que nos van a permitir enviar datos entre interfaces:
    public static  int id_user = 0;
    public static  String full_name_user= "";
    public static  String username_user = "";
    public static  String address_user = "";
    public static  String rol_user = "";
    public static  String email_user = "";
    public static  String telephone_user = "";
    
    //método del login:
    public Employees loginQuery(String user,String password)
    {
        String query = "SELECT * FROM employees WHERE username = ? AND password = ?";//consulta
        Employees employee = new Employees();//instanciamos el objeto para settiarle los atributos con los datos de la BD
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//hacemos/llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            
            /*enviamos parametros: Estamos enviando la información que el usuario ingreso en user
            para compararlos con username y password para copararla con el password:
            */
            pst.setString(1, user);
            pst.setString(2, password);
            rs = pst.executeQuery();//Ejecutamos la consulta y nos trae el registro que concuerde 
            //con user y password de la consulta(con pst)
  
            if(rs.next()){
                employee.setId(rs.getInt("id"));//almacenamos lo que trae el rs(Resulset)
                id_user = employee.getId();//guardamos el dato que setiamos en la linea anterior
                employee.setFull_name(rs.getString("full_name"));
                full_name_user = employee.getFull_name();
                employee.setUsername(rs.getString("username"));
                username_user = employee.getUsername();
                employee.setAddress(rs.getString("address"));
                address_user = employee.getAddress();
                employee.setTelephone(rs.getString("telephone"));
                telephone_user = employee.getTelephone();
                employee.setEmail(rs.getString("email"));
                email_user = employee.getEmail();
                employee.setRol(rs.getString("rol"));
                rol_user = employee.getRol();
            }
            
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al obtener al empleado " + e);
        }  
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        return employee;
    }  
    
    //registrar empleado:
    public boolean registerEmployeeQuery(Employees employee)
    {   
        String query = "INSERT INTO employees(id, full_name, username, address, "
                + "telephone, email, password, rol, created, updated) VALUES(?,?,?,?,?,?,?,?,?,?)";//cada signo de 
        //interrogación hace referencia a los campos de la tabla employess poque no sabemos lo que ingresara el usuario
        Timestamp dateTime = new Timestamp(new Date().getTime());
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            
            //accedemos a los metodos setter de employees para envías los datos que se registraran en la base de datos:
            pst.setInt(1,employee.getId());
            pst.setString(2,employee.getFull_name());
            pst.setString(3,employee.getUsername());
            pst.setString(4,employee.getAddress());
            pst.setString(5,employee.getTelephone());
            pst.setString(6,employee.getEmail());
            pst.setString(7,employee.getPassword());
            pst.setString(8,employee.getRol());
            pst.setTimestamp(9, dateTime);
            pst.setTimestamp(10, dateTime);
            //ejecutamos la sentencia/query SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar al empleado " + e);
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
    
    //listar empleado:
    public List listEmployeesQuery(String value)
    {
        List<Employees> list_employees = new ArrayList();
        String query = "SELECT * FROM employees ORDER BY rol ASC ";//consulta para listar todos los empleados de la tabla employees
        String query_search_employee = "SELECT * FROM employees WHERE id LIKE '%" + value + "%'";//para listar el empleado con la
        //identificación que le pasamos en la GUI en la txtfield.
        try {
            conn = cn.getConnection();
            
            if(value.equalsIgnoreCase(" ")){
               pst = conn.prepareStatement(query);//hacemos la consulta
               rs = pst.executeQuery();//ejecutamos la consulta  y nos trale la lista de empleados es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query);   
            }
            else{
                pst = conn.prepareStatement(query_search_employee);//hacemos la consulta
                rs = pst.executeQuery();//Ejecutamos la consulta y nos trae el empleado especifico, es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query_search_employee);
            }
            
        while(rs.next())//se ejecuta miestras hayan registros
        {
           Employees employee = new Employees();
           employee.setId(rs.getInt("id"));
           employee.setFull_name(rs.getString("full_name"));
           employee.setUsername(rs.getString("username"));
           employee.setAddress(rs.getString("address"));
           employee.setTelephone(rs.getString("telephone"));
           employee.setEmail(rs.getString("email"));
           employee.setRol(rs.getString("rol"));
           
           list_employees.add(employee);//pasamos el objeto a la lista
           
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
        
        return list_employees;
    }

    //modificar empleado:
    public boolean updateEmployeeQuery(Employees employee)
    {   
        String query = "UPDATE employees SET full_name = ?, username = ?, address = ?, "
                + "telephone = ?, email = ?, rol = ?, updated = ? WHERE id = ?";//modificamos al empleado 
        //teniendo en cuenta el id que se selecciona en el registro(en la tabla de la GUI empleado)
        
        Timestamp dateTime = new Timestamp(new Date().getTime());
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            
            //accedemos a los metodos setter de employees para envías los datos que se registraran en la base de datos:
            pst.setString(1,employee.getFull_name());
            pst.setString(2,employee.getUsername());
            pst.setString(3,employee.getAddress());
            pst.setString(4,employee.getTelephone());
            pst.setString(5,employee.getEmail());
            pst.setString(6,employee.getRol());
            pst.setTimestamp(7, dateTime);
            pst.setInt(8, employee.getId());
            //ejecutamos la sentencia/query SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar los datos del empleado " + e);
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
    
    //eliminar empleado:
    public boolean deleteEmployeeQuery(int id){
        String query = "DELETE FROM employees WHERE id = " + id;//elimina al empleado que coincida con el id que ingresa el usuario
        //es decir, el id que se recibe por parámetros
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();//ejecutamos la consulta
            return true;
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,"No puede eliminar un empleado que tenga relación con otra tabla ");
            return false;
        }
    }
    
    //cambiar la contraseña:
    public boolean updateEmployeePassword(Employees employee){
        String query = "UPDATE employees SET password = ? WHERE username = '" + username_user + "'";//consulta
        //para actualizar el campo password solo si el usuario que esta intentando cambiar la contraseña 
        //coincide con el usuario que se encuantra logeado
        try {
           conn = cn.getConnection();
           pst = conn.prepareStatement(query);
           pst.setString(1, employee.getPassword());
           pst.executeUpdate();
           return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ha ocurrido un error al modificar la contraseña " + e);
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
}
