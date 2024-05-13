package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import static models.EmployeesDao.rol_user;
import javax.swing.table.DefaultTableModel;
import models.Suppliers;
import models.SuppliersDao;
import views.SystemView;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;


public class SuppliersController implements ActionListener,  MouseListener, KeyListener
{
    private Suppliers supplier = new Suppliers();
    private SuppliersDao suppliersDao = new SuppliersDao();
    private SystemView views = new SystemView();
    private DefaultTableModel model;

    String rol = rol_user;

    public SuppliersController(Suppliers supplier, SuppliersDao suppliersDao, SystemView views) {
        this.suppliersDao = suppliersDao;
        this.views = views;
        this.views.btn_register_supplier.addActionListener(this);
        this.views.btn_update_supplier.addActionListener(this);
        this.views.btn_delete_supplier.addActionListener(this);
        this.views.supplier_table.addMouseListener(this);
        this.views.txt_search_supplier.addKeyListener(this);
        this.model = (DefaultTableModel) this.views.supplier_table.getModel();
        listAllSuppliers();

        this.views.btn_cancel_supplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelRegistration(); // Llamar al método cancelRegistration al hacer clic en el botón de cancelar
            }

            private void cancelRegistration() {
                clearFields(); // Limpiar todos los campos de entrada
                views.btn_register_supplier.setEnabled(true); // Habilitar el botón de registro
                views.btn_update_supplier.setEnabled(true); // Habilitar el botón de actualización

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == views.btn_register_supplier) {
        // Verificar si el usuario actual tiene rol de Administrador
        if (!rol.equals("Administrador")) {
            JOptionPane.showMessageDialog(null, "No tienes permiso para registrar clientes");
            return; // Salir del método sin realizar ninguna acción adicional
        }
        if (e.getSource() == views.btn_register_supplier) {
            registerSupplier();
        } }else if (e.getSource() == views.btn_update_supplier) {
            updateSupplier();
        } else if (e.getSource() == views.btn_delete_supplier) {
            deleteSuppliers();
        }

    }
    
    public void registerSupplier() {
        String name = views.txt_supplier_name.getText().trim();
        String description = views.txt_supplier_description.getText().trim();
        String address = views.txt_supplier_address.getText().trim();
        String telephone = views.txt_supplier_telephone.getText().trim();
        String email = views.txt_supplier_email.getText().trim();
        String city = views.cmb_supplier_city.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || address.isEmpty() || telephone.isEmpty() || email.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios!!");
        } else {
            try {
                Suppliers supplier = new Suppliers();
                supplier.setName(name);
                supplier.setDescription(description);
                supplier.setAddress(address);
                supplier.setTelephone(telephone);
                supplier.setEmail(email);
                supplier.setCity(city);

                if (suppliersDao.registerSupplierQuery(supplier)) {
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con éxito!!");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al proveedor!!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El número de teléfono debe ser numérico");
            }
        }
    }

    public void updateSupplier() {
        
        if (views.txt_supplier_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar!!");
        } else {
            String name = views.txt_supplier_name.getText().trim();
            String description = views.txt_supplier_description.getText().trim();
            String address = views.txt_supplier_address.getText().trim();
            String telephone = views.txt_supplier_telephone.getText().trim();
            String email = views.txt_supplier_email.getText().trim();
            String city = views.cmb_supplier_city.getSelectedItem().toString();

            if (name.isEmpty() || city.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios!!");
            } else {
                try {
                    Suppliers supplier = new Suppliers();
                    supplier.setId(Integer.parseInt(views.txt_supplier_id.getText().trim()));
                    supplier.setName(name);
                    supplier.setDescription(description);
                    supplier.setAddress(address);
                    supplier.setTelephone(telephone);
                    supplier.setEmail(email);
                    supplier.setCity(city);

                    if (suppliersDao.updateSupplierQuery(supplier)) {
                        clearTable();
                        listAllSuppliers();
                        views.btn_register_supplier.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con éxito!!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar al proveedor!!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error: El ID del proveedor debe ser un número entero");
                }
            }
        }
    }

   public void deleteSuppliers() {
        int selectedRow = views.supplier_table.getSelectedRow();
        if (selectedRow != -1) {
            int supplierId = (int) model.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (suppliersDao.deleteSupplierQuery(supplierId)) {
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el cliente");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un cliente para eliminar");
        }
    }
            public void listAllSuppliers() {
                if (rol.equals("Administrador")) {
                    // Limpiar la tabla antes de cargar los proveedores
                    clearTable();

                    // Obtener el texto de búsqueda
                    String searchText = views.txt_search_supplier.getText();

                    // Obtener la lista de proveedores que coinciden con el texto de búsqueda
                    List<Suppliers> list = suppliersDao.listSupplierQuery(searchText);

                    // Actualizar la tabla con los resultados de la búsqueda
                    model = (DefaultTableModel) views.supplier_table.getModel();
                    Object[] row = new Object[7];
                    for (int i = 0; i < list.size(); i++) {
                        row[0] = list.get(i).getId();
                        row[1] = list.get(i).getName();
                        row[2] = list.get(i).getDescription();
                        row[3] = list.get(i).getAddress();
                        row[4] = list.get(i).getTelephone();
                        row[5] = list.get(i).getEmail();
                        row[6] = list.get(i).getCity();
                        model.addRow(row);
                    }
                }
            }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.supplier_table) {
            int row = views.supplier_table.rowAtPoint(e.getPoint());
            views.txt_supplier_id.setText(views.supplier_table.getValueAt(row, 0).toString());
            views.txt_supplier_name.setText(views.supplier_table.getValueAt(row, 1).toString());
            views.txt_supplier_description.setText(views.supplier_table.getValueAt(row, 2).toString());
            views.txt_supplier_address.setText(views.supplier_table.getValueAt(row, 3).toString());
            views.txt_supplier_telephone.setText(views.supplier_table.getValueAt(row, 4).toString());
            views.txt_supplier_email.setText(views.supplier_table.getValueAt(row, 5).toString());
            views.cmb_supplier_city.setSelectedItem(views.supplier_table.getValueAt(row, 6).toString());
            views.txt_supplier_id.setEditable(false);
            views.btn_register_supplier.setEnabled(false);
        }
    }

     public void clearFields() {
        views.txt_supplier_id.setText("");
        views.txt_supplier_id.setEditable(true);
        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_address.setText("");
        views.txt_supplier_telephone.setText("");
        views.txt_supplier_email.setText("");
        views.cmb_supplier_city.setSelectedIndex(0);
    }

     @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_search_customer) {
            clearTable();
            listAllSuppliers();
        }
    }
    
    public void clearTable() {
        model.setRowCount(0); // Limpia la tabla
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    
    
}
