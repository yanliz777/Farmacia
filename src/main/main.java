package main;

import views.LoginView;

public class main 
{
    public static void main(String[] args) 
    {
        //Instanciamos el Login en la clase principal que es la que permite que mi aplicación corra:
        LoginView login = new LoginView();//Primera ventana que se abre al ejecutar el proyecto
        login.setVisible(true);
    }
}
