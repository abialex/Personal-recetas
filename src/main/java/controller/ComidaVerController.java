/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Plato;
import Entidades.Tipo;
import Pdf.HistoriaclinicaSimplepdf;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ComidaVerController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private JFXTextField jtfPlato;

    @FXML
    private TableView<Plato> tablePlato;

    @FXML
    private TableColumn<Plato, Integer> columnNumero;

    @FXML
    private TableColumn<Plato, String> columnNombre;

    @FXML
    private TableColumn<Plato, Integer> columnEstado;

    @FXML
    private TableColumn<Plato, Tipo> columnTipo;

    @FXML
    private JFXComboBox<Tipo> jcbTipo;

    ObservableList<Plato> listPlato = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Stage stagePrincipal;
    ComidaVerController odc = this;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTipo();
        updateListPlato();
        initTableView();
        tablePlato.setItems(listPlato);

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
    void updateListPlato() {
        List<Plato> olistPlato = App.jpa.createQuery("select p from Plato p where nombre like " + "'%" + jtfPlato.getText() + "%'" + " order by id DESC").setMaxResults(10).getResultList();
        listPlato.clear();
        for (Plato ocarta : olistPlato) {
            listPlato.add(ocarta);
        }
    }

    void initTableView() {
        columnNumero.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("id"));
        columnTipo.setCellValueFactory(new PropertyValueFactory<Plato, Tipo>("tipo"));
        columnNombre.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombre"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("id"));

        Callback<TableColumn<Plato, Integer>, TableCell<Plato, Integer>> cellFoctory = (TableColumn<Plato, Integer> param) -> {
            final TableCell<Plato, Integer> cell = new TableCell<Plato, Integer>() {

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

                        ImageView PrintIcon = newImage("printer-1.png", tamHightImag, tamWidthImag, item);
                        PrintIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarImprimir(event));
                        PrintIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagImprimirMoved(event));
                        PrintIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagImprimirFuera(event));

                        HBox managebtn = new HBox(PrintIcon, editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(PrintIcon, new Insets(0, 2.75, 0, 2.75));
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

                    ImageView buton = (ImageView) event.getSource();
                    for (Plato oplato : listPlato) {
                        if (oplato.getId() == (Integer) buton.getUserData()) {
                            ComidaModificarController oComidaModificarController = (ComidaModificarController) mostrarVentana(ComidaModificarController.class, "ComidaModificar");
                            oComidaModificarController.setPlato(oplato);
                            oComidaModificarController.setController(odc);
                            lockedPantalla();
                            break;
                        }
                    }
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

                void mostrarImprimir(MouseEvent event) {
                    /*
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPlato.size(); i++) {
                        if (listPlato.get(i).getIdpersona() == (Integer) imag.getUserData()) {
                            try {
                                Persona opersona = listPlato.get(i);

                                HistoriaclinicaSimplepdf.ImprimirHistoriaClinica(opersona);
                                File file = new File("pdf\\historia_clinica.pdf");
                                Desktop.getDesktop().open(file);

                                break;
                            } catch (IOException ex) {
                                Logger.getLogger(VerPacienteController.class.getName()).log(Level.SEVERE, null, ex);
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

    @FXML
    void imagAddMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/add-2.png").toExternalForm()));
    }

    @FXML
    void imagAddFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/add-1.png").toExternalForm()));
    }

    @FXML
    void imagInventoryMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/inventario-2.png").toExternalForm()));
    }

    @FXML
    void imagInventoryFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/inventario-1.png").toExternalForm()));
    }

    @FXML
    void mostrarAgregar() {
        ComidaAgregarController oComidaAgregarController = (ComidaAgregarController) mostrarVentana(ComidaAgregarController.class, "ComidaAgregar");
        oComidaAgregarController.setController(odc);
        lockedPantalla();

    }

    public Object mostrarVentana(Class generico, String nameFXML) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(generico.getResource(nameFXML + ".fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(generico.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(generico.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getX();
                y = event.getY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        });
        stage.show();
        return loader.getController();
    }

    void setStagePrincipall(Stage stage) {
        this.stagePrincipal = stage;
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

}
