package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JOptionPane;
import models.Employees;
import models.EmployeesDao;
import views.LoginView;
import views.SystemView;

/*
SIEMPRE IMPLEMENTAMOS LA INTERFAZ ActionListener PARA QUE ESCUCHE 
CUANDO SE REALICE UNA ACCIÓN EN UN ELLMENTO DE LA INTERFAZ GRÁFICA
 */
public class LoginController implements ActionListener {

    //objetos que me permitiran trabajar con los datos que ingresen en el login:
    private Employees employee;
    private EmployeesDao employees_dao;
    private LoginView login_view;

    public LoginController(Employees employee, EmployeesDao employees_dao, LoginView login_view) {
        this.employee = employee;
        this.employees_dao = employees_dao;
        this.login_view = login_view;
        this.login_view.btn_enter.addActionListener(this);//lo agregamos en el constructor para que este a la 
        //escucha del boton ingresar.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //obtenemos los datos de la vista:
        String user = login_view.txt_username.getText().trim();//trim para que limpie los espacios en blanco
        String pass = String.valueOf(login_view.txt_password.getPassword());
        //verificamos si el usuario presiono click en el botón ingresar:
        if (e.getSource() == login_view.btn_enter) {
            //validamos que los campos no esten vacios:
            if (!user.equals("") || pass.equals("")) {
                /*
                pasamos los parametros al método login al método loginQuery que se 
                encuentra en EmployeesDao:
                 */
                employee = employees_dao.loginQuery(user, pass);

                /*VERIFICAMOS LA EXISTENCIA DEL USUARIO:
                //DE ESTA MANERA SE MUESTRA UNA VENTANA DEPENDIENDO DE EL TIPO DE
                USUARIO. ACÁ NO ES NECESARIO, PERO ES BUENO TENERLO EN CUENTA
                 */
                if (employee.getUsername() != null) {
                    if (employee.getRol().equals("Administrador"))//si es administrador le muestra una vista:
                    {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);
                    } else {
                        SystemView aux = new SystemView();
                        aux.setVisible(true);
                    }
                    //cerramos la ventana del login para dejar solo la del SystemView
                    this.login_view.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, " Usuario o contraseña incorrecta !!");
                }
            } else {
                JOptionPane.showMessageDialog(null, " los campos están vacíos !!");
            }
        }

    }

}
