/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pdf;

import Entidades.Plato;
import Entidades.PlatoIngrediente;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import Pdf.style.style1;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;
import controller.App;
import java.awt.Label;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author yalle
 */
public class HistoriaclinicaSimplepdf {

    public static void ImprimirReceta(Plato oplato) {
        List<PlatoIngrediente> olistPlatoIngrediente = App.jpa.createQuery("select p from PlatoIngrediente p where idplato = " + oplato.getId()).getResultList();

        int volumen = 105;
        PdfWriter writer = null;
        try {
            writer = new PdfWriter("Pdf\\receta.pdf");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Label(), "agregue la carpeta Pdf");
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        style1 evento = new style1(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(HistoriaclinicaSimplepdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
        Style styleTextJustified = new Style().setTextAlignment(TextAlignment.JUSTIFIED).setFontSize(10f);
        Style styleTextCenter15 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(15f);
        Style styleTextCenter20 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(20f);
        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 0, 0);
        Color colorAzul = Color.BLUE;
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = Color.ORANGE;
        Color colorBlanco = Color.WHITE;

        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(1f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlancoSubrayado = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(1f));
        //Paragraph nombreTutor = opersona.getTutorNombre().isEmpty() ? palabraEnBlanco : new Paragraph(opersona.getTutorNombre());
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRayas = new Table(new float[]{volumen * 5});
        TableRayas.addCell(new Cell().add(palabraEnBlancoSubrayado).addStyle(styleTextLeft).addStyle(styleCell));
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell("Receta N°: ", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell(oplato.getId() + "", styleTextCenter, styleCell, subrayado));

        Table Cabecera = new Table(new float[]{volumen * 5f});
        Cabecera.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));

        //Fin Cabecera
        // ANAMNESIS
        Table tableTitulo = new Table(new float[]{volumen * 5});
        tableTitulo.addCell(getCell(oplato.getNombre(), styleTextCenter20, styleCell, subrayadoNo));
        tableTitulo.setMarginBottom(20);

        Table TableSplit = new Table(new float[]{volumen * 3f, volumen * 2f});

        Table TableSplit1 = new Table(new float[]{volumen * 2.95f});
        TableSplit1.addCell(getCell(oplato.getPreparacion(), styleTextJustified, styleCell, subrayadoNo));

        Table TableSplit2 = new Table(new float[]{volumen * 1.2f, volumen * 0.5f, volumen * 0.3f});
        for (PlatoIngrediente platoIngrediente : olistPlatoIngrediente) {
            TableSplit2.addCell(getCell(platoIngrediente.getIngrediente().getNombre(), styleTextLeft, styleCell, subrayadoNo));
            TableSplit2.addCell(getCell(platoIngrediente.getCantidad() + "", styleTextCenter, styleCell, subrayadoNo));
            TableSplit2.addCell(getCell(platoIngrediente.getUnd(), styleTextCenter, styleCell, subrayadoNo));
        }

        Table TableTipo = new Table(new float[]{volumen * 0.3f, volumen * 4.7f});
        TableTipo.addCell(getCell("TIPO:", styleTextCenter, styleCell, subrayadoNo).setFont(bold));
        TableTipo.addCell(getCell(oplato.getTipo().getNombre(), styleTextLeft, styleCell, subrayadoNo));
        TableSplit.addCell(getCell("PREPARACIÒN", styleTextCenter15, styleTextCenter15, subrayadoNo).setFont(bold));
        TableSplit.addCell(getCell("INGREDIENTES", styleTextCenter15, styleTextCenter15, subrayadoNo).setFont(bold));
        TableSplit.addCell(new Cell().add(TableSplit1));
        TableSplit.addCell(new Cell().add(TableSplit2));
        TableSplit.addCell(new Cell(1, 2).add(TableTipo));

        //Fin ANAMNESIS
        /*----Fin Contenido del documento  página 1------*/
 /*--------Contenido del documento página 2--------*/
 /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(tableTitulo);
        document.add(TableSplit);
        document.close();
        /*----Fin Cuerpo del documentos-----*/
    }

    public static Cell getCell(String palabra, Style posicion, Style border, Border subrayado) {
        return new Cell().add(new Paragraph(palabra).setBorderBottom(subrayado)).addStyle(posicion).addStyle(border);
    }
}
