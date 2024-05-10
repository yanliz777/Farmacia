package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import static models.EmployeesDao.rol_user;
import models.Suppliers;
import models.SuppliersDao;
import views.SystemView;

public class SuppliersController implements ActionListener
{
    private Suppliers supplier = new Suppliers();
    private SuppliersDao suppliersDao = new SuppliersDao();
    private SystemView views = new SystemView();
    String rol = rol_user;

    public SuppliersController(Suppliers supplier,SuppliersDao suppliersDao,SystemView views)
    {
        this.supplier = supplier;
        this.suppliersDao = suppliersDao;
        this.views = views;
        
        //Ponemos a la escucha al botón de registrar ante cualquiere acción en el botón:
        views.btn_register_supplier.addActionListener(this);//enviamos este controlador que es el que se comunica con el modelo
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_supplier)//si presiona el botón registrar:
        {
            if(views.txt_supplier_name.getText().equals("")
            || views.txt_supplier_description.getText().equals("")
            || views.txt_supplier_address.getText().equals("")    
            || views.txt_supplier_telephone.getText().equals("")
            || views.txt_supplier_email.getText().equals("")
            || views.cmb_supplier_city.getSelectedItem().toString().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios!!");
            }
            else{
                //realizamos la inserción de datos que el usuario ingresa en los txt:
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim()) ;
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString()) ;  
                
                //si todo ha salido bien hacemos el registro:
                if(suppliersDao.registerSupplierQuery(supplier)){
                   JOptionPane.showMessageDialog(null,"Proveedor registrado con exito!!"); 
                }
                else{
                    JOptionPane.showMessageDialog(null,"Ha ocurrido un error al registrar al proveedor!!");
                }
            }
        }
    }
    
    
}
