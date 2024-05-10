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
import models.Employees;
import models.EmployeesDao;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;
import views.SystemView;
/**
 * Controlador que me controla los datos que ingresa el usuario en la GUI del empleado y me conecta 
 * con el modelo, es decir,con todos los métodos que tenermos en EmployeesDao. 
 * Para que todo funcione este controlador lo llamamos en la vista SystemView 
 * @author YAN FRANCO
 */
public class EmployeesController implements ActionListener, MouseListener,KeyListener
{
    private Employees employee;
    private EmployeesDao employeeDao;
    private SystemView views;//La gui principal
    //rol:
    String rol = rol_user;//variable estatica(rol_user) que tenemos en la clase employes Dao, es para hacer segumiento 
    //al usuario que este en el sistema actualmente
    
    //Para interactuar con la tabla que tenemos en la GUI/pestaña empleados:
    DefaultTableModel model = new DefaultTableModel();
    
    /*
    constructor: se encarga de fijar todos los datos que se encuentran en él, por lo tanto
    cada vez que se instancie la clase EmployeesController todo lo que esta dentro de él se pine en funcionamiento
    */
    public EmployeesController(Employees employee, EmployeesDao employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;
        //Escuchadores de acciones =
        /*Ponemos a la escucha al Boton de registrar empleado: se coloca acá ya que este es el 
        constructor del controlador encargado de los darle manejo a las acciones de la ventana de empleados*/
        this.views.btn_register_employee.addActionListener(this);
        /*Ponemos a la escucha al Boton de modificar empleado: se coloca acá ya que este es el 
        constructor del controlador encargado de los darle manejo a las acciones de la ventana de empleados*/
        this.views.btn_update_employee.addActionListener(this);
        /*Ponemos a la escucha al Boton de eliminar empleado: se coloca acá ya que este es el 
        constructor del controlador encargado de los darle manejo a las acciones de la ventana de empleados*/
        this.views.btn_delete_employee.addActionListener(this);
        //Ponemos a la escucha al Boton de cancelar de la ventana empleado:
        this.views.btn_cancel_employee.addActionListener(this);
        //ponemos a la escucha el botón de cambiar contraseña:
        this.views.btn_modify_data.addActionListener(this);
        //ponemos label de la GUI principal a la escucha para cuando se de clic con el mouse:
        this.views.jLabelEmployees.addMouseListener(this);
        //ponemos la tabla en escucha a cualquier acción del mouse:
        this.views.employees_table.addMouseListener(this);
        //ponemos a la escucha al txt de buscar en caso de que haya una acción en el teclado:
        this.views.txt_search_employee.addKeyListener(this);
    }
    
    //ActionEvent() interface que se utiliza para cuando hacemos clic en los botones:
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == views.btn_register_employee)//si el usuario presiona en el botón de registrar
        {
            //verificamos si los de employee campos están vacíos:
            if(views.txt_employee_Id.getText().equals("")
              || views.txt_employee_fullname.getText().equals("")
              || views.txt_employee_username.getText().equals("")
              || views.txt_employee_addres.getText().equals("")
              || views.txt_employee_telephone.getText().equals("")
              || views.txt_employee_email.getText().equals("")
              || views.cmb_Roll.getSelectedItem().toString().equals("")
              || String.valueOf( views.txt_employee_password.getPassword()).equals(""))
            {
                 JOptionPane.showMessageDialog(null," Todos los campos son obligatorios!!");
            }
            else{
                //realizar la inserrción en caso de que los campos no esten vacíos, es decir, settiamos los datos:
                employee.setId(Integer.parseInt( views.txt_employee_Id.getText().trim()));
                employee.setFull_name( views.txt_employee_fullname.getText().trim());
                employee.setUsername( views.txt_employee_username.getText().trim());
                employee.setAddress( views.txt_employee_addres.getText().trim());
                employee.setTelephone(views.txt_employee_telephone.getText().trim());
                employee.setEmail(views.txt_employee_email.getText().trim());
                employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                employee.setRol(views.cmb_Roll.getSelectedItem().toString());
                
                //verificamos el registro de empleado en la BD sea existoso en el método registerEmployeeQuery(employee):
                if(employeeDao.registerEmployeeQuery(employee)){
                   cleanTable();//limipia la tabla despues de cada registro
                   cleanFields();//limpiamos las cajas de texto
                   listAllEmpleyees();//listamos a los empleados despues de insertar a una persona
                   JOptionPane.showMessageDialog(null,"Empleado registrado con exito!!"); 
                }
                else{
                   JOptionPane.showMessageDialog(null,"Ha ocurrido un error al registrar al empleado!!");
                }
            }
        }
        else if(e.getSource()  == views.btn_update_employee)//si presiona el botón de actualizar/modificar
        {
            if(views.txt_employee_Id.getText().equals(""))//si no se detecta ninún id
            {
                JOptionPane.showMessageDialog(null,"Selecciona una fila para continuar!!");
            }
            else{
               //verificamos si los campos están vacíos:
               if(views.txt_employee_Id.getText().equals("")
               || views.txt_employee_fullname.getText().equals("") 
               || views.cmb_Roll.getSelectedItem().toString().equals(""))
               {
                  JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios!!"); 
               } 
               else{
                   //realizar la inserrción en caso de que los campos no esten vacíos, es decir, settiamos los datos:
                   employee.setId(Integer.parseInt(views.txt_employee_Id.getText().trim()));
                   employee.setFull_name(views.txt_employee_fullname.getText().trim());
                   employee.setUsername(views.txt_employee_username.getText().trim());
                   employee.setAddress(views.txt_employee_addres.getText().trim());
                   employee.setTelephone(views.txt_employee_telephone.getText().trim());
                   employee.setEmail(views.txt_employee_email.getText().trim());
                   employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                   employee.setRol(views.cmb_Roll.getSelectedItem().toString()); 
                   
                   if(employeeDao.updateEmployeeQuery(employee))//verifica la query en la base de datos
                   {
                       cleanTable();//limpiamos la tabla despues de presionar el botón de modificación
                       cleanFields();//limpia los campos despues de presionar el botón de modificación
                       listAllEmpleyees();//Listamos todo los empleados despues de presionar el botón de modificación
                       views.btn_register_employee.setEnabled(true);//habilitamos el botón de registrar
                       JOptionPane.showMessageDialog(null,"Datos del empleado modificados con exito!!"); 
                   }
                   else{
                       JOptionPane.showMessageDialog(null,"Ha ocurrido un error al modificar al empleado!!"); 
                   }
                   
               }
            }     
        }
        else if(e.getSource() == views.btn_delete_employee)
        {
            //variable que almacena la fila(row) seleccionada por el usuario:
            int row = views.employees_table.getSelectedRow();
            
            if(row == -1){
                JOptionPane.showMessageDialog(null,"Debes seleccionar un empleado para eliminar!!");
            }
            else if(views.employees_table.getValueAt(row,0).equals(id_user))//verificamos que no se selecciono a sí mismo
     //Esto lo verificamos con las variables estaticas que creamos en EmployessDao para enviar datos entre interfaces           
            {
                JOptionPane.showMessageDialog(null,"No puede eliminar al usuario autenticado!!");
            }
            else{
               //con esta variable capturamos el id(columna 0) de la fila(row) que el usuario seleccine de la tabla
                int id = Integer.parseInt(views.employees_table.getValueAt(row,0).toString()); 
                int question = JOptionPane.showConfirmDialog(null,"¿En realidad quieres eliminar a este empleado?");
                
                if(question == 0 && employeeDao.deleteEmployeeQuery(id)!= false)
                {
                    cleanTable();//limpiamos la tabla para que se actualice
                    cleanFields();//limpiamos los campos
                    views.btn_register_employee.setEnabled(true);//habilitamos el botón de registrar
                    views.txt_employee_password.setEnabled(true);//habilitamos el txt de la password
                    listAllEmpleyees();//listamos los empleados que quedan despues de la eliminación
                    JOptionPane.showMessageDialog(null,"Empleado eliminado con exito!!");
                }  
            }
        }
        else if(e.getSource() == views.btn_cancel_employee)
        {
            cleanFields();//limpiamos los campos
            views.btn_register_employee.setEnabled(true);//habilitamos el botón de registrar
            views.txt_employee_password.setEnabled(true);//habilitamos el txt de la password
            views.txt_employee_Id.setEnabled(true);//habilitamos el id
        }
        //tener en cuenta que se modifica la contraseña del usuario actual que se encuentre en el sistema
        //esto se realiza en la pestaña perfil
        else if(e.getSource() == views.btn_modify_data){
            //recolectamos la información de las cajas de password de la pestaña perfil:
            String password = String.valueOf(views.txt_password_modify.getPassword());
            String confirm_password = String.valueOf(views.txt_password_modify_confirm.getPassword());
            
            //verificamos que las cajas de texto están vacías:
            if(!password.equals("") && !confirm_password.equals(""))//si los campos no estan vacíos
            {
                //verificamos que las constraseñas sean iguales:
                if(password.equals(confirm_password))//si las contraseñas son iguales, le setiamos 
                //una a la contraseña del empleado
                {
                    employee.setPassword(String.valueOf(views.txt_password_modify.getPassword()));
                    
                    if(employeeDao.updateEmployeePassword(employee) != false){
                        JOptionPane.showMessageDialog(null,"Contraseña modificada con exito!!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Ha ocurrido un error al modificar la contraseña!!");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"La contraseñas no coinciden!!");
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios!!");
            }  
        }
    }
    
    /*Listar todos los empleados: Solo se listan si el usuario tiene el rol de Administrador
    este método se llama en SystemView que es la gui principal,para que se carguen los registros cuando 
    iniciemos el programa
    */
    public void listAllEmpleyees()
    {
        if(rol.equals("Administrador"))
        {//Le enviamos por parámetros el string que el usuario ingrese al txt que tenemos en la GUI de empleados en 
         //este caso si elusuario ingresa el id se muestra el empleado en específico
            List<Employees> list = employeeDao.listEmployeesQuery(views.txt_search_employee.getText());
            //tabla de la GUI empleado:
            model = (DefaultTableModel) views.employees_table.getModel();
            //filas:
            Object[] row = new Object[7];//el 7 nos indica el número de atributos que tiene la tabla en la GUI empleados
            
            //para llenar la tabla con los registros que tengamos:
            for(int i=0; i < list.size(); i++)
            {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFull_name();
                row[2] = list.get(i).getUsername();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                //pasamos todos los empleados que esten en la lista(list) a la tabla:
                model.addRow(row);
            }
        }
    }
    
    /*Métodos que funcionan según la actividad que se haga con el mouse: En este caso 
    Cuando el usuario haga clic sobre unos de los registros de la tabla de la GUI empleados
    se mostraran en las cajas de texto de la GUI los datos de este usuario en específico.
    */
    @Override
    public void mouseClicked(MouseEvent e) 
    {
        if(e.getSource() == views.employees_table)//si la persona interactua con la tabla
        {
            int row = views.employees_table.rowAtPoint(e.getPoint());//es para saber en cual fila se hizó clic.
            //Por parametro le enviamos la fila(row) y la columna a laque hacemos refecencia:Esto es para que se muestren
            //los datos en las cajas de texto de la GUI empleados cuando se daclic en un registro de la tabla
            views.txt_employee_Id.setText(views.employees_table.getValueAt(row,0).toString());
            views.txt_employee_fullname.setText(views.employees_table.getValueAt(row,1).toString());
            views.txt_employee_username.setText(views.employees_table.getValueAt(row,2).toString());
            views.txt_employee_addres.setText(views.employees_table.getValueAt(row,3).toString());
            views.txt_employee_telephone.setText(views.employees_table.getValueAt(row,4).toString());
            views.txt_employee_email.setText(views.employees_table.getValueAt(row,5).toString());
            views.cmb_Roll.setSelectedItem(views.employees_table.getValueAt(row,6).toString());
            
            
            //deshabilitamos algunas cajas de texto y algunos botones:
            views.txt_employee_Id.setEnabled(false);
            views.txt_employee_password.setEnabled(false);
            views.btn_register_employee.setEnabled(false); 
        }
        else if(e.getSource() == views.jLabelEmployees)//si hago clic en donde dice Empleados en la GUI en laparte izquierda
          //de lapantalla que es el jLabelEmployees nos habilita la pestaña empleador que esta en la columna 4
        {
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(4);//ponemos la posición de la pestaña
                //limpiamos la tabla:
                cleanTable();
                //limpiamos los campos:
                cleanFields();
                //listamos los empleados:
                listAllEmpleyees();
            }
            else{//si no es administrador
                views.jTabbedPane1.setEnabledAt(4,false);//desabilita la pestaña
                views.jLabelEmployees.setEnabled(false);//desabilita el label
                JOptionPane.showMessageDialog(null,"No tienes privilegios de administrador para acceder a esta vista!!");
            }
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
    
    //Métodos de la interface KeyListener(): Esto para saber que esta escribiendo la persona en el buscador
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }
    
    /**El evento keyReleased se desencadena cada vez que se 
     * suelta una tecla del teclado.
     * En caso cuando el empleado ecriba en el txt_search_employee(buscar, en la GUI empleado)
     * se listará, en caso de ser correcto el id que ingrese, el empleado en específico
     */
    @Override
    public void keyReleased(KeyEvent e) 
    {
        if(e.getSource() == views.txt_search_employee){
            cleanTable();
            listAllEmpleyees();//para listar un empleado en específico
        }
    }
    
    //Limpiar campos cajas de textos 
    public void cleanFields(){
        views.txt_employee_Id.setText("");//pone/settea el campo id como vacío
        views.txt_employee_Id.setEditable(true);//para habilitar el campo del id
        views.txt_employee_fullname.setText("");
        views.txt_employee_username.setText("");//pone/settea el campo id como vacío
        views.txt_employee_addres.setText("");//pone/settea el campo id como vacío
        views.txt_employee_telephone.setText("");//pone/settea el campo id como vacío
        views.txt_employee_email.setText("");//pone/settea el campo id como vacío
        views.txt_employee_password.setText("");//pone/settea el campo id como vacío
        views.cmb_Roll.setSelectedIndex(0);//pone/settea el indice por defecto que es Administrador
    }
    
    //método para limpiar la tabla
    public void cleanTable(){
        for(int i=0; i < model.getRowCount(); i++){
            model.removeRow(i);
            i = i - 1;// revierte el incremento de i en el bucle for 
        }
    }    
}