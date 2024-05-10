package controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static models.EmployeesDao.email_user;
import static models.EmployeesDao.full_name_user;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.telephone_user;
import views.SystemView;

//AjustesControladores(SettingsController):
public class SettingsController implements MouseListener
{
    //Intanciamos La clase del paquete view llamada SystemView:
    private SystemView views;
    
    //Construrctor de clase:
    public SettingsController(SystemView views)
    {
        this.views = views;
        /*Ponemos a la escucha a los jlabels de la parte lateral izquierda
        de la clase SystemView, es decir, de la vista principal con el controlador SettingsController:
        */
        this.views.jLabelProducts.addMouseListener(this);
        this.views.jLabelPurchases.addMouseListener(this);
        this.views.jLabelCustomer.addMouseListener(this);
        this.views.jLabelEmployees.addMouseListener(this);
        this.views.jLabelSuppliers.addMouseListener(this);
        this.views.jLabelCategories.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);
        this.views.jLabelSettings.addMouseListener(this); 
        Profile();
    }
    
    /*Asignamos el perfil al usuario y utilizamos las variables estáticas de la clase EmployeesDao:
    De esta formas se finjan los datos del usuario que este usando el sistema
    */
    public void Profile(){
        this.views.txt_Id_profile.setText( " " + id_user);
        this.views.txt_name_profile.setText(full_name_user);
        this.views.txt_address_profile.setText(full_name_user);
        this.views.txt_phone_profile.setText(telephone_user);
        this.views.txt_email_profile.setText(email_user);  
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

   // Es cuando la flecha del maouse entra(Más no da click) a x icono de la interfaz:
    @Override
    public void mouseEntered(MouseEvent e) 
    {
        /*Si el mouse de la persona se encuentra ubicado encima del jlabelProducts el
          jpanelProducts se coloca de color verde(new Color(152,202,63)).
          getSource():Método que me permite  capturar el ORIGEN del evento, ya que es un método que pertenece
          A la clase ActioEvent. Detecta el origen del evento ocurrido(Quién gana el foco)
        */
        if(e.getSource() == views.jLabelProducts )
        {                                       //Verde
            views.jpanelProducts.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelPurchases)
        {
             views.jPanelPurchases.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelCategories)
        {
             views.jPanelCategories.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelEmployees)
        {
             views.jPanelEmployees.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelReports)
        {
             views.jPanelReports.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelSettings)
        {
             views.jPanelSettings.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelSuppliers)
        {
             views.jPanelSuppliers.setBackground(new Color(152,202,63));
        }
        else if(e.getSource() == views.jLabelCustomer)
        {
             views.jPanelCustomer.setBackground(new Color(152,202,63));
        } 
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
         /*Si el mouse de la persona deja de estar encima del jlabelProducts el
          jpanelProducts se coloca de color Azul(new Color(18,45,61)).
          getSource():Método que me permite  capturar el ORIGEN del evento, ya que es un método que pertenece
          A la clase ActioEvent. Dtecta el origen del evento ocurrido(Quién gana el foco)
        */
        if(e.getSource() == views.jLabelProducts )
        {                                     //Azul
            views.jpanelProducts.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelPurchases)
        {
             views.jPanelPurchases.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelCategories)
        {
             views.jPanelCategories.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelEmployees)
        {
             views.jPanelEmployees.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelReports)
        {
             views.jPanelReports.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelSettings)
        {
             views.jPanelSettings.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelSuppliers)
        {
             views.jPanelSuppliers.setBackground(new Color(18,45,61));
        }
        else if(e.getSource() == views.jLabelCustomer)
        {                                                //Azul
             views.jPanelCustomer.setBackground(new Color(18,45,61));
        } 
    }   
}
