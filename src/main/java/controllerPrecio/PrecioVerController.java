/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerPrecio;

import Entidades.Ingrediente;
import Entidades.Plato;
import Entidades.PlatoIngrediente;
import Entidades.Tipo;
import Entidades.TipoIngrediente;
import Pdf.HistoriaclinicaSimplepdf;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import controller.ComidaModificarController;
import controllerPrecio.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class PrecioVerController implements Initializable {

    @FXML
    private TableView<Ingrediente> TablePrecio;

    @FXML
    private TableColumn<Ingrediente, TipoIngrediente> columnTipo;

    @FXML
    private TableColumn<Ingrediente, String> columnIngrediente;

    @FXML
    private TableColumn<Ingrediente, Ingrediente> columnPrecio;

    @FXML
    private TableColumn<Ingrediente, Integer> columnEstado;

    @FXML
    private JFXComboBox<TipoIngrediente> jcbTipo;

    @FXML
    private JFXComboBox<String> jcbEscala;

    @FXML
    private JFXComboBox<TipoIngrediente> jcbTipoingrediente;

    @FXML
    private JFXTextField jtfIngrediente, jtfNombre, jtfprecio, jtfMarca;

    ObservableList<Ingrediente> listPrecio = FXCollections.observableArrayList();
    List<Ingrediente> listPrecios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEscala();
        cargarTipo();
        updateListPrecio();
        initTableView();
        TablePrecio.setItems(listPrecio);
    }

    void cargarEscala() {
        ObservableList<String> escala = FXCollections.observableArrayList("Kg.", "Und.", "Lt.");
        jcbEscala.setItems(escala);
    }

    void cargarTipo() {
        ObservableList<TipoIngrediente> TIPO = FXCollections.observableArrayList();
        ObservableList<TipoIngrediente> TIPO2 = FXCollections.observableArrayList();
        List<TipoIngrediente> list = App.jpa.createQuery("select p from TipoIngrediente p").getResultList();
        TipoIngrediente otipo = new TipoIngrediente("NINGUNO");
        TIPO.add(otipo);
        jcbTipoingrediente.getSelectionModel().select(otipo);
        for (TipoIngrediente tipo : list) {
            TIPO.add(tipo);
            TIPO2.add(tipo);
        }
        jcbTipoingrediente.setItems(TIPO);
        jcbTipo.setItems(TIPO2);

    }

    @FXML
    void updateListPrecio() {
        String tipo = jcbTipoingrediente.getSelectionModel().getSelectedItem().getNombre().equals("NINGUNO") ? "" : "and idtipoingrediente = " + jcbTipoingrediente.getSelectionModel().getSelectedItem().getId();
        listPrecios = App.jpa.createQuery("select p from Ingrediente p where nombre like " + "'%" + jtfIngrediente.getText() + "%' " + tipo + " order by id DESC").setMaxResults(9).getResultList();
        listPrecio.clear();
        for (Ingrediente oIngrediente : listPrecios) {
            listPrecio.add(oIngrediente);
        }
    }

    @FXML
    void agregarIngrediente() {
        if (isCompleto()) {
            Ingrediente oIngrediente = new Ingrediente();
            oIngrediente.setNombre(jtfNombre.getText());
            oIngrediente.setPrecioactual(Float.parseFloat(jtfprecio.getText()));
            oIngrediente.setEscala(jcbEscala.getSelectionModel().getSelectedItem());
            oIngrediente.setMarca(jtfMarca.getText());
            oIngrediente.setTipoingrediente(jcbTipo.getSelectionModel().getSelectedItem());

            App.jpa.getTransaction().begin();
            App.jpa.persist(oIngrediente);
            App.jpa.getTransaction().commit();
            updateListPrecio();
            limpiar();
        }
    }

    void initTableView() {
        columnTipo.setCellValueFactory(new PropertyValueFactory<Ingrediente, TipoIngrediente>("tipoingrediente"));
        columnIngrediente.setCellValueFactory(new PropertyValueFactory<Ingrediente, String>("nombre"));
        columnPrecio.setCellValueFactory(new PropertyValueFactory<Ingrediente, Ingrediente>("ingrediente"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Ingrediente, Integer>("id"));

        columnPrecio.setCellFactory(column -> {
            TableCell<Ingrediente, Ingrediente> cell = new TableCell<Ingrediente, Ingrediente>() {
                @Override
                protected void updateItem(Ingrediente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        for (Ingrediente ingrediente : listPrecio) {

                        }
                        setText(item.getPrecioactual() == 0 ? "Desc." : item.getPrecioactual() + " " + item.getEscala());
                    }
                }
            };

            return cell;
        });

        Callback<TableColumn<Ingrediente, Integer>, TableCell<Ingrediente, Integer>> cellFoctory = (TableColumn<Ingrediente, Integer> param) -> {
            final TableCell<Ingrediente, Integer> cell = new TableCell<Ingrediente, Integer>() {

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

                        ImageView editIcon = newImage("modify-1.png", tamHightImag, tamWidthImag, item);
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarModificar(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(0, 2.75, 0, 2.75));
                        HBox.setMargin(deleteIcon, new Insets(0, 2.75, 0, 2.75));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                ImageView newImage(String nombreImagen, int hight, int width, Integer item) {
                    ImageView imag = new ImageView(new Image(getClass().getResource("/imagenes/" + nombreImagen).toExternalForm()));
                    imag.setFitHeight(hight);
                    imag.setFitWidth(width);
                    imag.setUserData(item);
                    imag.setStyle(
                            " -fx-cursor: hand;"
                    );
                    return imag;
                }

                void mostrarModificar(MouseEvent event) {
                    /*
                    ImageView buton = (ImageView) event.getSource();
                    for (Plato oplato : listPlato) {
                        if (oplato.getId() == (Integer) buton.getUserData()) {
                            ComidaModificarController oComidaModificarController = (ComidaModificarController) mostrarVentana(ComidaModificarController.class, "ComidaModificar");
                            oComidaModificarController.setPlato(oplato);
                            oComidaModificarController.setController(odc);
                            lockedPantalla();
                            break;
                        }
                    }*/
                }

                void mostrarEliminar(MouseEvent event) {
                    /*
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPlato.size(); i++) {
                        if (listPlato.get(i).getIdpersona() == (Integer) imag.getUserData()) {
                            oPersonaEliminar = listPlato.get(i);
                            indexEliminar = i;
                            oAlertConfimarController = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                            oAlertConfimarController.setController(odc);
                            oAlertConfimarController.setMensaje(" ¿Está seguro de eliminar al \n paciente? \n \n" + " " + oPersonaEliminar.getNombres_apellidos());
                            lockedPantalla();
                            break;
                        }
                    }*/
                }

                void mostrarImprimir(MouseEvent event) {/*
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPlato.size(); i++) {
                        if (listPlato.get(i).getId() == (Integer) imag.getUserData()) {
                            try {
                                Plato oplato = listPlato.get(i);
                                HistoriaclinicaSimplepdf.ImprimirReceta(oplato);
                                File file = new File("pdf\\receta.pdf");
                                Desktop.getDesktop().open(file);
                                break;
                            } catch (IOException ex) {
                                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }*/
                }

                private void imagEliminarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-2.png").toExternalForm()));
                }

                private void imagEliminarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                }

                private void imagModificarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/modify-2.png").toExternalForm()));
                }

                private void imagModificarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/modify-1.png").toExternalForm()));
                }

                private void imagImprimirMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/printer-2.png").toExternalForm()));
                }

                private void imagImprimirFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/printer-1.png").toExternalForm()));
                }

                private void imagMoneyMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/money-2.png").toExternalForm()));
                }

                private void imagMoneyFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/money-1.png").toExternalForm()));
                }
            };
            return cell;
        };
        columnEstado.setCellFactory(cellFoctory);

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

    void limpiar() {
        jtfNombre.setText("");
        jtfprecio.setText("");
        jcbEscala.getSelectionModel().select(null);
        jcbTipo.getSelectionModel().select(null);
        jtfMarca.setText("");
        //jcbIngrediente se limpia cada vez que cargaIngrediente

    }
}
