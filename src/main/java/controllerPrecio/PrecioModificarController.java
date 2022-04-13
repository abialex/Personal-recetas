/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerPrecio;

import Entidades.Ingrediente;
import Entidades.TipoIngrediente;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class PrecioModificarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfprecio;

    @FXML
    private JFXComboBox<String> jcbEscala;

    @FXML
    private JFXComboBox<TipoIngrediente> jcbTipo;

    @FXML
    private JFXTextField jtfNombre;

    @FXML
    private JFXTextField jtfMarca, jtfCaracteristica;

    PrecioVerController oPrecioVerController;
    Ingrediente oIngrediente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEscala();
        cargarTipo();
    }
    
    void cargarEscala() {
        ObservableList<String> escala = FXCollections.observableArrayList("Kg.", "Und.", "Lt.");
        jcbEscala.setItems(escala);
    }

    void cargarTipo() {
        ObservableList<TipoIngrediente> TIPO = FXCollections.observableArrayList();
        List<TipoIngrediente> list = App.jpa.createQuery("select p from TipoIngrediente p").getResultList();
        for (TipoIngrediente tipo : list) {
            TIPO.add(tipo);
        }
        jcbTipo.setItems(TIPO);
    }

    void setController(PrecioVerController odc) {
        this.oPrecioVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }
    

    void setIngrediente(Ingrediente ingrediente) {
        this.oIngrediente=ingrediente;
        jtfNombre.setText(ingrediente.getNombre());
        jtfprecio.setText(ingrediente.getPrecioactual()+"");
        jcbEscala.getSelectionModel().select(ingrediente.getEscala());
        jtfMarca.setText(ingrediente.getMarca());
        jcbTipo.getSelectionModel().select(ingrediente.getTipoingrediente());
    }
    
    @FXML
    void actualizarIngrediente() {
        if (isCompleto()) {
            Ingrediente ingrediente = oIngrediente;
            ingrediente.setNombre(jtfNombre.getText());
            ingrediente.setPrecioactual(Float.parseFloat(jtfprecio.getText()));
            ingrediente.setEscala(jcbEscala.getSelectionModel().getSelectedItem());
            ingrediente.setMarca(jtfMarca.getText());
            ingrediente.setTipoingrediente(jcbTipo.getSelectionModel().getSelectedItem());
            ingrediente.setCaracteristica(jtfCaracteristica.getText());
            
            App.jpa.getTransaction().begin();
            App.jpa.persist(ingrediente);
            App.jpa.getTransaction().commit();
            this.oPrecioVerController.updateListPrecio();
            cerrar();
        }
    }

    @FXML
    void cerrar() {
        oPrecioVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

    private boolean isCompleto() {
        boolean aux = true;
        if (jtfNombre.getText().length() == 0) {
            jtfNombre.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfNombre.setStyle("");
        }

        if (jtfprecio.getText().length() == 0) {
            jtfprecio.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfprecio.setStyle("");
        }

        if (jcbEscala.getSelectionModel().getSelectedItem() == null) {
            jcbEscala.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbEscala.setStyle("");
        }

        if (jcbTipo.getSelectionModel().getSelectedItem() == null) {
            jcbTipo.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbTipo.setStyle("");
        }

        return aux;
    }

}
