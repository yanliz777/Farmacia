package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Customers;
import models.CustomersDao;
import views.SystemView;
/*
Este controlador me controla las acciones de la pestaña de la vista cliente, este controlador se debe 
instanciar en la vista principal, SystemView y  se debe de poner en el constructor de la
GUI principal(SystemView) que es la que tiene las pestañas, entre ellas, la pestaña cliente/customer
*/
public class CustomersController implements ActionListener, MouseListener,KeyListener
{
    private Customers customer;
    private CustomersDao customersDao;
    private SystemView views;
    //Para interactuar con la tabla que tenemos en la GUI/pestaña clientes:
    DefaultTableModel model = new DefaultTableModel();
    

    public CustomersController(Customers customer, CustomersDao customersDao, SystemView views) {
        this.customer = customer;
        this.customersDao = customersDao;
        this.views = views;
        
        //Ponemos a la escucha al botón de resgistrar un cliente:
        this.views.btn_register_customer.addActionListener(this);//le enviamos este controlador 
        //Ponemos a la escucha al botón de de modificar un cliente ante una acción en el botón:
        this.views.btn_update_customer.addActionListener(this);
        //Ponemos a la escucha al botón de de eliminar un cliente ante una acción en el botón:
        this.views.btn_delete_customer.addActionListener(this);
        //ponemos a la escucha al botón de cancelar:
        this.views.btn_cancel_customer.addActionListener(this);
        //Ponemos a la escucha a la tabla ante cualquier acción con el Mouse:
        this.views.customers_table.addMouseListener(this);
        //ponemos a la escucha al buscador de la pestaña clientes ante cualquier acción del teclado:
        this.views.txt_search_customer.addKeyListener(this);
        //ponemos a la escucha al jlable de empleados
        this.views.jLabelCustomer.addMouseListener(this);     
        //ponemos a la escucha a la tbla ante cualquier acción con el mouse
        this.views.customers_table.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_customer)//si el usuario presiona en el botón de registrar
        {
            //verificamos si los de  campos customer están vacíos:
            if(views.txt_customer_Id.getText().equals("")
              || views.txt_customer_fullname.getText().equals("")
              || views.txt_customer_address.getText().equals("")
              || views.txt_customer_telephone.getText().equals("")
              || views.txt_customer_email.getText().equals(""))
            {
                 JOptionPane.showMessageDialog(null," Todos los campos son obligatorios!!");
            }
            else{
                //realizar la inserrción en caso de que los campos no esten vacíos, es decir, settiamos los datos:
               customer.setId(Integer.parseInt( views.txt_customer_Id.getText().trim()));
               customer.setFull_name( views.txt_customer_fullname.getText().trim());
               customer.setAddress( views.txt_customer_address.getText().trim());
               customer.setTelephone(views.txt_customer_telephone.getText().trim());
               customer.setEmail(views.txt_customer_email.getText().trim());
                
                //verificamos el registro de empleado en la BD sea existoso en el método registerEmployeeQuery(employee):
                if(customersDao.registerCustomerQuery(customer)){
                   cleanFields();//Para que limpie los campos después de hacer el registro
                   listALLCustomers();//para que liste el usuario que se registro despues de presionar el boton registrar
                   JOptionPane.showMessageDialog(null,"Cliente registrado con exito!!"); 
                }
                else{
                   JOptionPane.showMessageDialog(null,"Ha ocurrido un error al registrar al empleado!!");
                }
            }
        }
        else if(e.getSource()  == views.btn_update_customer)//si presiona el botón de actualizar/modificar
        {
            if(views.txt_customer_Id.getText().equals(""))//si no se detecta ningún id, es decri, si no se selecciono una fila
            {
                JOptionPane.showMessageDialog(null,"Selecciona una fila para continuar!!");
            }
            else{
               //verificamos si los campos están vacíos:
               if(views.txt_customer_Id.getText().equals("")
               || views.txt_customer_fullname.getText().equals("")
               || views.txt_customer_address.getText().equals("")
               || views.txt_customer_telephone.getText().equals("")  
               || views.txt_customer_email.getText().equals(""))
               {
                  JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios!!"); 
               } 
               else{
                   //realizar la inserrción en caso de que los campos no esten vacíos, es decir, settiamos los datos:
                   customer.setId(Integer.parseInt(views.txt_customer_Id.getText().trim()));
                   customer.setFull_name(views.txt_customer_fullname.getText().trim());                
                   customer.setAddress(views.txt_customer_address.getText().trim());
                   customer.setTelephone(views.txt_customer_telephone.getText().trim());
                   customer.setEmail(views.txt_customer_email.getText().trim());
                   
                   if(customersDao.updateCustomerQuery(customer))//verifica la query en la base de datos
                   {
                       cleanTable();//limpiamos la tabla despues de presionar el botón de modificación
                       cleanFields();//limpia los campos despues de presionar el botón de modificación
                       listALLCustomers();//Listamos todo los empleados despues de presionar el botón de modificación
                       views.btn_register_customer.setEnabled(true);//habilitamos el botón de registrar
                       JOptionPane.showMessageDialog(null,"Datos del cliente modificados con exito!!"); 
                   }
                   else{
                       JOptionPane.showMessageDialog(null,"Ha ocurrido un error al modificar los datos del cliente!!"); 
                   }
                   
               }
            }     
        }
        else if(e.getSource() == views.btn_delete_customer)
        {
            //variable que almacena la fila(row) seleccionada por el usuario:
            int row = views.customers_table.getSelectedRow();
            
            if(row == -1)//si o ha seleccionado nada
            {
                JOptionPane.showMessageDialog(null,"Debes seleccionar un cliente para eliminar!!");
            }
            else{
               //con esta variable capturamos el id(columna 0) de la fila(row) que el usuario seleccine de la tabla
                int id = Integer.parseInt(views.customers_table.getValueAt(row,0).toString());//capturamos el id del cliente 
                int question = JOptionPane.showConfirmDialog(null,"¿En realidad quieres eliminar a este cliente?");
                
                if(question == 0 && customersDao.deleteCustomerQuery(id) != false)
                {
                    cleanTable();//limpiamos la tabla para que se actualice
                    cleanFields();//limpiamos los campos
                    views.btn_register_customer.setEnabled(true);//habilitamos el botón de registrar
                    listALLCustomers();//listamos los empleados que quedan despues de la eliminación
                    JOptionPane.showMessageDialog(null,"cliente eliminado con exito!!");
                }  
            }
        }
        else if(e.getSource() == views.btn_cancel_customer)
        {
            cleanFields();//limpiamos los campos
            views.txt_customer_Id.setEnabled(true);//habilitamos el id
            views.btn_register_customer.setEnabled(true);//habilitamos el botón de registrar
            
        }
    }
    
    //Listar  clientes:
    public void listALLCustomers(){
        List<Customers> list = customersDao.listCustomerQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customers_table.getModel();
        //filas:
            Object[] row = new Object[5];//el 7 nos indica el número de atributos que tiene la tabla en la GUI empleados
            
            //para llenar la tabla con los registros que tengamos en la BD:
            for(int i=0; i < list.size(); i++)
            {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFull_name();               
                row[2] = list.get(i).getAddress();
                row[3] = list.get(i).getTelephone();
                row[4] = list.get(i).getEmail();
                //pasamos todos los empleados que esten en la lista(list) a la tabla:
                model.addRow(row);
            }
            
            views.customers_table.setModel(model);
            
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.customers_table)//si hay una acción de clic con el mouse en la tabla
        {
            //Para obtener las coordenadas donde el usuario dio clic:
            int row = views.customers_table.rowAtPoint(e.getPoint());
            //Ahora llenamos los txtField con el cliente qie el usuario presiono n la tabla:
            views.txt_customer_Id.setText(views.customers_table.getValueAt(row,0).toString());
            views.txt_customer_fullname.setText(views.customers_table.getValueAt(row,1).toString());
            views.txt_customer_address.setText(views.customers_table.getValueAt(row,2).toString());
            views.txt_customer_telephone.setText(views.customers_table.getValueAt(row,3).toString());
            views.txt_customer_email.setText(views.customers_table.getValueAt(row,4).toString());
            
            //deshabilitamos algunos botones:
            views.btn_register_customer.setEnabled(false);
            views.txt_customer_Id.setEditable(false);//para que no se pueda editar el id     
        }
        else if(e.getSource() == views.jLabelCustomer)//si presiona el lable d¿qeu esta a la izq de clientes
        {   //se activa la pestaña clientes
            views.jTabbedPane1.setSelectedIndex(3);
             //limpiamos la tabla:
             cleanTable();
             //limpiamos los campos:
             cleanFields();
             //listamos los empleados:
             listALLCustomers();
            
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_search_customer){
            //limpiar la tabla:
            cleanTable();
            //listar los clinetes:
            listALLCustomers();
            
        }
    }
    
    
    //método para limpiar la tabla
    public void cleanTable(){
        for(int i=0; i < model.getRowCount(); i++){
            model.removeRow(i);
            i = i - 1;// revierte el incremento de i en el bucle for 
        }
    }    
         
    //Limpiar campos cajas de textos 
    public void cleanFields(){
        views.txt_customer_Id.setText("");//pone/settea el campo id como vacío
        views.txt_customer_Id.setEditable(true);//para habilitar el campo del id
        views.txt_employee_Id.setEditable(true);//para habilitar el campo del id
        views.txt_customer_fullname.setText("");
        views.txt_customer_address.setText("");//pone/settea el campo id como vacío
        views.txt_customer_telephone.setText("");//pone/settea el campo id como vacío
        views.txt_customer_email.setText("");//pone/settea el campo id como vacío
    }
}
