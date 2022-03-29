/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Ingrediente;
import Entidades.Plato;
import Entidades.PlatoIngrediente;
import Entidades.Tipo;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ComidaAgregarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXComboBox<Ingrediente> jcbIngrediente;

    @FXML
    private JFXComboBox<Tipo> jcbTipo;

    @FXML
    private JFXTextArea jtfpreparacion;

    @FXML
    private TextField jtfnombre, jtfcantidad, jtfund;

    @FXML
    private TableView<PlatoIngrediente> tableIngrediente;

    @FXML
    private TableColumn<PlatoIngrediente, String> columnIngrediente, columnUnd;

    @FXML
    private TableColumn<PlatoIngrediente, Float> columnCantidad;

    @FXML
    private TableColumn<PlatoIngrediente, Integer> columnEstado;

    ObservableList<PlatoIngrediente> listPlatoIngrediente = FXCollections.observableArrayList();
    AutoCompletionBinding<Ingrediente> autoComplete;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarIngrediente();
        cargarTipo();
        initTableView();
        initRestricciones();
        tableIngrediente.setItems(listPlatoIngrediente);
    }

    @FXML
    void agregarIngrediente() {
        if (isCompletoIngrediente()) {
            PlatoIngrediente oplatoingrediente = new PlatoIngrediente(
                    new Plato(),
                    jcbIngrediente.getSelectionModel().getSelectedItem(),
                    Float.parseFloat(jtfcantidad.getText()),
                    jtfund.getText());
            listPlatoIngrediente.add(oplatoingrediente);
            limpiarIngrediente();
        }
    }

    @FXML
    void quitarIngrediente() {
        PlatoIngrediente oplatoingrediente = new PlatoIngrediente(
                new Plato(),
                jcbIngrediente.getSelectionModel().getSelectedItem(),
                Float.parseFloat(jtfcantidad.getText()),
                jtfund.getText());
        listPlatoIngrediente.add(oplatoingrediente);
    }

    void cargarIngrediente() {
        ObservableList<Ingrediente> INGREDIENTE = FXCollections.observableArrayList();
        List<Ingrediente> list = App.jpa.createQuery("select p from Ingrediente p order by nombre asc").getResultList();
        for (Ingrediente oIngrediente : list) {
            INGREDIENTE.add(oIngrediente);
        }
        jcbIngrediente.setItems(INGREDIENTE);
    }

    void cargarTipo() {
        ObservableList<Tipo> TIPO = FXCollections.observableArrayList();
        List<Tipo> list = App.jpa.createQuery("select p from Tipo p").getResultList();
        for (Tipo tipo : list) {
            TIPO.add(tipo);
        }
        jcbTipo.setItems(TIPO);
    }

    @FXML
    void Guardar() {
        if (isCompleto()) {
            if (!listPlatoIngrediente.isEmpty()) {
                Plato oplato = new Plato(
                        jcbTipo.getSelectionModel().getSelectedItem(),
                        jtfnombre.getText(),
                        jtfpreparacion.getText()
                );
                App.jpa.getTransaction().begin();
                App.jpa.persist(oplato);
                for (PlatoIngrediente platoIngrediente : listPlatoIngrediente) {
                    platoIngrediente.setPlato(oplato);
                    App.jpa.persist(platoIngrediente);
                }
                App.jpa.getTransaction().commit();
                limpiar();
            } else {

            }
        } else {

        }
    }

    void initRestricciones() {
        jtfnombre.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfcantidad.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros4(event));
        jtfund.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras3(event));
    }

    void SoloLetras(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (Character.isDigit(key)) {
            event.consume();
        }
    }

    void SoloLetras3(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (Character.isDigit(key)) {
            event.consume();
        }

        if (o.getText().length() >= 3) {
            event.consume();
        }
    }

    void SoloNumerosEnteros4(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (key != '.') {
            if (!Character.isDigit(key)) {
                event.consume();
            }
        }
        if (o.getText().length() >= 4) {
            event.consume();
        }
    }

    void initTableView() {
        columnIngrediente.setCellValueFactory(new PropertyValueFactory<PlatoIngrediente, String>("ingrediente"));
        columnUnd.setCellValueFactory(new PropertyValueFactory<PlatoIngrediente, String>("und"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory<PlatoIngrediente, Float>("cantidad"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<PlatoIngrediente, Integer>("id"));

        Callback<TableColumn<PlatoIngrediente, Integer>, TableCell<PlatoIngrediente, Integer>> cellFoctory = (TableColumn<PlatoIngrediente, Integer> param) -> {
            final TableCell<PlatoIngrediente, Integer> cell = new TableCell<PlatoIngrediente, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        int tamHightImag = 23;
                        int tamWidthImag = 23;
                        ImageView deleteIcon = newImage("delete-1.png", tamHightImag, tamWidthImag, item);
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarEliminar(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));

                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(0, 2.75, 0, 2.75));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                ImageView newImage(String nombreImagen, int hight, int width, int item) {
                    ImageView imag = new ImageView(new Image(getClass().getResource("/imagenes/" + nombreImagen).toExternalForm()));
                    imag.setFitHeight(hight);
                    imag.setFitWidth(width);
                    imag.setUserData(item);
                    imag.setStyle(
                            " -fx-cursor: hand;"
                    );
                    return imag;
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPlatoIngrediente.size(); i++) {
                        if (listPlatoIngrediente.get(i).getId() == (Integer) imag.getUserData()) {
                            PlatoIngrediente opin = listPlatoIngrediente.get(i);
                            listPlatoIngrediente.remove(opin);
                            break;
                        }
                    }
                }

                private void imagEliminarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-2.png").toExternalForm()));
                }

                private void imagEliminarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                }
            };
            return cell;
        };
        columnEstado.setCellFactory(cellFoctory);

    }

    boolean isCompleto() {
        boolean aux = true;
        if (jtfnombre.getText().trim().length() == 0) {
            jtfnombre.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfnombre.setStyle("");
        }

        if (jcbTipo.getSelectionModel().getSelectedItem() == null) {
            jcbTipo.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbTipo.setStyle("");
        }

        if (jtfpreparacion.getText().trim().length() == 0) {
            jtfpreparacion.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfpreparacion.setStyle("");
        }

        return aux;
    }

    boolean isCompletoIngrediente() {
        boolean aux = true;
        if (jtfcantidad.getText().trim().length() == 0) {
            jtfcantidad.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfcantidad.setStyle("");
        }

        if (jcbIngrediente.getSelectionModel().getSelectedItem() == null) {
            jcbIngrediente.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbIngrediente.setStyle("");
        }

        if (jtfund.getText().trim().length() == 0) {
            jtfund.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfund.setStyle("");
        }

        return aux;
    }

    void limpiar() {
        jtfnombre.setText("");
        jtfpreparacion.setText("");
        jcbIngrediente.getSelectionModel().select(null);
        jtfcantidad.setText("");
        jtfund.setText("");
        jcbTipo.getSelectionModel().select(null);
        listPlatoIngrediente.clear();
    }

    void limpiarIngrediente() {
        jcbIngrediente.getSelectionModel().select(null);
        jtfcantidad.setText("");
        jtfund.setText("");
    }

}
