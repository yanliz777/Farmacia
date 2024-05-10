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

public class SalesDao 
{
    //instanciamos la conexión ConnectionMySQL():
    ConnectionMySQL cn = new ConnectionMySQL();
    //variable para conectarnos a la base de datos:
    Connection conn = null;
    //variable para hacer las consultas:
    PreparedStatement pst = null;
    //variable para obtener datos de las consultas y poder operar con estos datos:
    ResultSet rs = null;
    
    //Registrar venta:
    public boolean registerSaleQuery(int customer_id, int employee_id, double total)
    {
        String query = "INSERT INTO sales (customer_id, employee_id, total, sale_date)"
                + "VALUES (?,?,?,?)";
        
        Timestamp dateTime = new Timestamp(new Date().getTime());//para las fechas de updated y created
        
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            pst.setInt(1, customer_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, dateTime);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
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
    
    //registro detalle venta:
    public boolean registerSaleDetailQuery(int product_id, double sale_id,
            int sale_quantity, double sale_price, double sale_subtotal)
    {
        String query = "INSERT INTO sale_details (product_id, sale_id, sale_quantity, sale_price, sale_subtotal)"
                + "VALUES (?,?,?,?,?)";
        
        Timestamp dateTime = new Timestamp(new Date().getTime());//para las fechas de updated y created
        
        try {
            conn = cn.getConnection();//llamamos la conexión
            pst = conn.prepareStatement(query);//hacemos la consulta
            pst.setInt(1, product_id);
            pst.setDouble(2, sale_id);
            pst.setInt(3, sale_quantity);
            pst.setDouble(4, sale_price);
            pst.setDouble(5, sale_subtotal);
            //ejecutamos la sentencia/query  pst SQL donde insertamos los datos :
            pst.execute();
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
                if(rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    //Obtener el ID máximo de la venta:
    public int saleId()
    {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM sales";
        
        try {
           conn = cn.getConnection();//llamamos la conexión
           pst = conn.prepareStatement(query);//hacemos la consulta
           rs = pst.executeQuery();//Almacena los datos de la consulta que se ejecutó(pst)
           
           if(rs.next()){
               id = rs.getInt("id");//almacenamos el máximo id
           }
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
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
    
    //listar ventas:
    public List listAllSalesQuery(String value)
    {
        List<Sales> list_sales = new ArrayList();
        //factura = invoice
        String query = "SELECT s.id AS invoice, c.full_name AS customer, e.full_name AS employee, s.total, s.sale_date \n" +
       "FROM sales s INNER JOIN customers c ON s.customer_id = c.id INNER JOIN employees e ON \n" +
       "s.employee_id = e.id ORDER BY s.id ASC";
        
        
        try {
            conn = cn.getConnection();//hacemos la conexión a la BD           
            pst = conn.prepareStatement(query);//hacemos la consulta para listar todos los clientes
            rs = pst.executeQuery();//ejecutamos la consulta  y nos trae la lista de clientes es decir, 
            //nos devuelve todos los reslutados que hacemos en pst = conn.prepareStatement(query);   
          
        //recorrer lo que almacena rs:    
        while(rs.next())//se ejecuta miestras hayan registros
        {
           Sales sale = new Sales();
           sale.setId(rs.getInt("invoice"));//factura
           sale.setCustomer_name(rs.getString("customer"));
           sale.setEmployee_name(rs.getString("employee"));
           sale.setTotal_to_pay(rs.getDouble("total"));
           sale.setSale_date(rs.getString("sale_date"));      
           
           list_sales.add(sale);//pasamos el objeto a la lista           
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
        return list_sales;
    }
    
}
