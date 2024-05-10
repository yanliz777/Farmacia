package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 * categories = categorias
 * Acá van todo los método que permiten a java
 * interactuar con la base de datos
 */
public class CategoriesDao {
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
    //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //Registrar categoria:
    public boolean registerCategoryQuery(Categories category){
        String query = "INSERT INTO categories(name,created,updated) VALUES(?,?,?)";//no pasamos id porque es autoincremental
       
        Timestamp dateTime = new java.sql.Timestamp(new Date().getTime());//para las fechas de updated y created
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta y le enviamos por parametros la query/consulta
            //accedemos a los metodos setter de Categories para envíar los datos que se 
            //registraran en la base de datos, no setiamos el id porque es autoincremental:
            pst.setString(1,category.getName());
            pst.setTimestamp(2, dateTime);
            pst.setTimestamp(3, dateTime);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar una categoría " + e);
            return false;
        }
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
               
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    //listar categoria:
     public List listCategoriesQuery(String value)
    {
        List<Categories> list_Categories = new ArrayList();
        String query = "SELECT * FROM categories";//consulta para listar todos los proveedores de la tabla employees
        String query_search_Category = "SELECT * FROM suppliers WHERE name LIKE '%" + value + "%'";//para listar la categoria con
        //el nombre que le pasamos en la GUI en la txtfield.
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
                pst = conn.prepareStatement(query_search_Category );//hacemos la consulta
                rs = pst.executeQuery();//Ejecutamos la consulta y nos trae el proveedor especifico, es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query_search_customer);
            }
            
        while(rs.next())//se ejecuta miestras hayan registros
        {
           Categories category = new Categories();
           category.setId(rs.getInt("id"));
           category.setName(rs.getString("name"));
           list_Categories.add(category);//pasamos el objeto a la lista
           
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
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        return list_Categories;
    }
     
     //modificar categoria
     public boolean updateCategoryQuery(Categories category)
    {   
        String query = "UPDATE categories SET name = ?, updated = ? WHERE id = ?";//modificamos la categoria
        //con el id que ingresa el ususario
        Timestamp dateTime = new Timestamp(new Date().getTime());
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            
            //accedemos a los metodos setter de Categories para envías los datos que se registraran en la base de datos:
            pst.setString(1,category.getName());
            pst.setTimestamp(2, dateTime);
            pst.setInt(3, category.getId());
            //ejecutamos la sentencia/query SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar los datos de la categotia " + e);
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
     
     //eliminar categorias:
     public boolean deleteCategoryQuery(int id){
        String query = "DELETE FROM categories WHERE id = " + id;//elimina la categoria que coincida con el id que ingresa el usuario
        //es decir, el id que se recibe por parámetros
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);
            pst.execute();//ejecutamos la consulta
            return true;
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,"No puede eliminar una categoria que tenga relación con otra tabla ");
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