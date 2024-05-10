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

public class PurchasesDao {
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
    //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //registrar compra:
    public boolean registerPurchaseQuery(int supplier_id,int employee_id,double total){
        String query = "INSERT INTO purchases(supplier_id, employe_id, total, created)"
                + " VALUES(?,?,?,?)";//no pasamos id porque es autoincremental. "posible error en employee_id"
        Timestamp dateTime = new java.sql.Timestamp(new Date().getTime());//para las fechas de updated y created
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta y le enviamos por parametros la query/consulta
            //accedemos a los metodos setter de Categories para envíar los datos que se 
            pst.setInt(1, supplier_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, dateTime);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar una compra " + e);
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
    
    //registrar detalles de la compra:
    public boolean registerPurchaseDetailQuery(int purchase_id, double purchase_price, int purchase_amount,
            double purchase_subtotal, int product_id)
         {
        String query = "INSERT INTO purchase_details(purchase_id, purchase_price, purchase_amount,\n" +
        "purchase_subtotal,purchase_date, product_id) VALUES(?,?,?,?,?,?)";//no pasamos id porque es autoincremental
        Timestamp dateTime = new java.sql.Timestamp(new Date().getTime());//para las fechas de updated y created
        
        //Cuando utilizamos la BD siempre lo encerramos en un try catch:
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta y le enviamos por parametros la query/consulta
            //accedemos a los metodos setter de Categories para envíar los datos que se 
            pst.setInt(1,purchase_id);
            pst.setDouble(2, purchase_price);
            pst.setInt(3,purchase_amount);
            pst.setDouble(4, purchase_subtotal);
            pst.setTimestamp(5, dateTime);
            pst.setInt(6, product_id);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
            return true;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar los detalles de la compra " + e);
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
    //obtener maximo id de la compra:
    public int purchaseId(){
        int id=0;
        String query = "SELECT MAX(id) AS id FROM purchases";
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();//rs captura los datos de la consulta pst
            
            if(rs.next()){
                id = rs.getInt("id");//almacenamos el MAX id de la compra que hacemos en la consulta
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
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
        return id;
    }
    
    //listar todas las compras realizadas:
    public List listAllPurchasesQuery(){
        List<Purchases> list_purchase = new ArrayList();
        String query = "SELECT pu.*, su.name AS supplier_name FROM purchases pu, suppliers su"
                + "WHERE pu.supplier_id = su.id ORDER BY pu.id ASC ";
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            
            while(rs.next()){
                Purchases purchase = new Purchases();
 //setiamos los datos obteniendolos de rs que es la que contiene los datos de la consulta:
                purchase.setId(rs.getInt("id"));
                purchase.setSupplier_name_product(rs.getString("supplier_name"));
                purchase.setTotal(rs.getDouble("total"));
                purchase.setCreated(rs.getString("created"));
                list_purchase.add(purchase);
            }
            
        } catch (SQLException e) {
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
        return list_purchase;
    }
    
    //listar compras para imprimir factura:
    public List listPurchaseDetailQuery(int id)
    {
        List<Purchases> list_purchases = new ArrayList();
        String query = "SELECT pu.created, pude.purchase_price, pude.purchase_amount, pude.purchase_subtotal, su.name AS supplier_name,\n" +
                        "pro.name AS product_name, em.full_name FROM purchases pu INNER JOIN purchase_details pude ON pu.id = pude.purchase_id\n" +
                        "INNER JOIN products pro ON pude.product_id = pro.id INNER JOIN suppliers su ON pu.supplier_id = su.id\n" +
                        "INNER JOIN employees em ON pu.employe_id = em.id WHERE pu.id = ?";
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            while(rs.next()){
                Purchases purchase = new Purchases();
                purchase.setProduct_name(rs.getString("product_name"));
                purchase.setPurchase_amount(rs.getInt("purchase_amount"));
                purchase.setPurchase_price(rs.getDouble("purchase_price"));
                purchase.setPurchase_subtotal(rs.getDouble("purchase_subtotal"));
                purchase.setSupplier_name_product(rs.getString("supplier_name"));
                purchase.setCreated(rs.getString("created"));
                purchase.setPurcharser(rs.getString("full_name"));
                list_purchases.add(purchase);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        finally {
            /**
             * cerramos las conexiones en este finally porque
             * en caso de haber una excepción no quede ninguna conexión abierta y pase
             * a este sector del código para cerrar las conexiones
             */
            try {
                if(rs != null)  rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        return list_purchases;
    }
    
}
