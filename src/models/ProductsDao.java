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
 * Products = productos
 * Acá van todo los método que permiten a java
 * interactuar con la base de datos
 */
public class ProductsDao 
{
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
   //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //registrar producto:
    public boolean registerProductQuery(Products product){
        //no insertamos product_cuantity porque lo hacemos en otro método
        String query = "INSERT INTO products(code,name,description,unit_price,created,updated,category_id)"
                + " VALUES(?,?,?,?,?,?,?)";//no pasamos id porque es autoincremental
        Timestamp dateTime = new java.sql.Timestamp(new Date().getTime());//para las fechas de updated y created
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta y le enviamos por parametros la query/consulta
            //accedemos a los metodos setter de Categories para envíar los datos que se 
            //registraran en la base de datos, NO setiamos el id porque es autoincremental:
            pst.setInt(1, product.getCode());
            pst.setString(2,product.getName());
            pst.setString(3,product.getDescription());
            pst.setDouble(4,product.getUnit_price());
            pst.setTimestamp(5, dateTime);
            pst.setTimestamp(6, dateTime);
            pst.setInt(7,product.getCategory_id());
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar un producto " + e);
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
    
    //listar productos:
    public List listProductsQuery(String value){
        List<Products> list_products = new ArrayList<>();
/* voy a traer todos los campos de la tabla productos y además que me traiga el nombre
  de la categoría relacionada con ese producto y esto lo podemos hacer 
  porque en la tabla productos tenemos categoty_id que es la relacion entre
  productos y categorias, para esto ponemos lo siguiente: pro:alisa, * todos los campos,ca:alias.
*/
        //consulta para listar todos los proveedores de la tabla employees:
        String query = "SELECT pro.*, ca.name AS categoy_name FROM products pro, categories ca WHERE pro.category_id = ca.id";
        
        /*La siguiente consulat también trae la relación entre un producto y su categoría: retorna el nombre y no el ID
        ON para relacionar: 
        para listar el producto con el nombre que le pasamos en la GUI en la txtfield: Cuando busque 
        //un producto con su nombre me va traer todos los campos de productos y también la categoria relacionada 
        // con ese producto, pero no me va a trae el id sino el nombr:
        */
        String query_search_product = "SELECT pro.*, ca.name AS category_name FROM products pro INNER JOIN categories ca"
                + "ON pro.category_id = ca.id WHERE pro.name LIKE '%" + value + " %'";
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
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
                pst = conn.prepareStatement(query_search_product);//hacemos la consulta
                rs = pst.executeQuery();//Ejecutamos la consulta y nos trae el producto especifico, es decir, 
                //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query_search_customer);
            }
            
        while(rs.next())//se ejecuta miestras hayan registros
        {
           Products product = new Products();
           product.setId(rs.getInt("id"));
           product.setCode(rs.getInt("code"));
           product.setName(rs.getString("name"));
           product.setDescription(rs.getString("description"));
           product.setUnit_price(rs.getDouble("unit_price"));
           product.setProduct_cuantity(rs.getInt("product_cuantity"));
           product.setCategory_name(rs.getString("category_name"));// category_name la obtenemos  de las consultas
           
           list_products.add(product);//pasamos el objeto a la lista
           
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
        
        return list_products;
    }
    
    //modificar producto:
    public boolean updateProductQuery(Products product)
    {   
        String query = "UPDATE products SET code = ?, name = ?, description = ?, unit_price = ?,"
                + " updated = ?, category_id = ? WHERE id = ?";//modificamos el producto
        //con el id que ingresa el ususario
        Timestamp dateTime = new Timestamp(new Date().getTime());
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta y le enviamos por parametros la query/consulta
            //accedemos a los metodos setter de Categories para envíar los datos que se 
            //registraran en la base de datos, NO setiamos el id porque es autoincremental:
            pst.setInt(1, product.getCode());
            pst.setString(2,product.getName());
            pst.setString(3,product.getDescription());
            pst.setDouble(4,product.getUnit_price());
            pst.setTimestamp(5, dateTime);
            pst.setInt(6,product.getCategory_id());
            pst.setInt(7,product.getId());
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar los datos del producto" + e);
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
    
    //eliminar producto
    public boolean deleteProductQuery(int id){
        String query = "DELETE FROM products WHERE id = " + id;//elimina la categoria que coincida con el id que ingresa el usuario
        //es decir, el id que se recibe por parámetros
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);
            pst.execute();//ejecutamos la consulta
            return true;
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,"No puede eliminar un producto que tenga relación con otra tabla ");
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
    
    //buscar productos:
    public Products searchProduct(int id){
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro INNER JOIN categories ca"
                + "ON pro.category_id = ca.id WHERE pro.id = ? ";
        Products product = new Products();
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//ejecuta la consulta
            pst.setInt(1, id);
            rs = pst.executeQuery();//almacena los datos que se obtienen al ejecutar la consula
            
            if(rs.next()){
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setCategory_name(rs.getString("category_name"));// category_name la obtenemos  de las consultas
                
            }   
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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
        return product;
    }
    
    //buscar producto por código:
     public Products searchCode(int code){
        String query = "SELECT pro.id, pro.name FROM products pro WHERE pro.code = ? ";
        Products product = new Products();
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//ejecuta la consulta
            pst.setInt(1, code);
            rs = pst.executeQuery();//almacena los datos que se obtienen al ejecutar la consula
            
            if(rs.next())//Caá usamos if en vez de while porque es solo un registro
            {
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
            }   
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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
        return product;
    }
     
     //Traer la cantidad de productos:
     public Products searchId(int id){
         String query = "SELECT pro.product_quantity FROM products pro WHERE pro.id = ? ";
         Products product = new Products();
         
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//ejecuta la consulta
            pst.setInt(1, id);
            rs = pst.executeQuery();//rs almacena los datos que se obtienen al ejecutar la consula con pst
            
            if(rs.next()){
                product.setProduct_cuantity(rs.getInt("product_quantity"));
                
            }   
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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
        return product;
     }
     
     //Actualizar stock:
    public boolean updateStockQuery(int amount,int produc_id)
    {
         String query = "UPDATE products SET product_quantity = ? WHERE id = ?";
         try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//ejecuta la consulta
            pst.setInt(1, amount);
            pst.setInt(2,produc_id);
            pst.execute();//pst para ejecutar la consulta
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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