/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pdf.style;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.StyleConstants;

/**
 *
 * @author david
 */
public class style1 implements IEventHandler {

    private final Document documento;
    private int pag = 0;

    public style1(Document doc) {
        documento = doc;
    }

    /**
     * Crea el rectangulo donde pondremos el encabezado
     *
     * @param docEvent Evento de documento
     * @return Area donde colocaremos el encabezado
     */
    private Rectangle crearRectanguloEncabezado(PdfDocumentEvent docEvent) {
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();

        float xEncabezado = pdfDoc.getDefaultPageSize().getX() + documento.getLeftMargin();
        float yEncabezado = pdfDoc.getDefaultPageSize().getTop() - documento.getTopMargin();
        float anchoEncabezado = page.getPageSize().getWidth() - 72;
        float altoEncabezado = 50F;

        Rectangle rectanguloEncabezado = new Rectangle(xEncabezado, yEncabezado, anchoEncabezado, altoEncabezado);

        return rectanguloEncabezado;
    }

    /**
     * Crea el rectangulo donde pondremos el pie de pagina
     *
     * @param docEvent Evento del documento
     * @return Area donde colocaremos el pie de pagina
     */
    private Rectangle crearRectanguloPie(PdfDocumentEvent docEvent) {
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();

        float xPie = pdfDoc.getDefaultPageSize().getX() + documento.getLeftMargin();
        float yPie = pdfDoc.getDefaultPageSize().getBottom();
        float anchoPie = page.getPageSize().getWidth() - 72;
        float altoPie = 50F;

        Rectangle rectanguloPie = new Rectangle(xPie, yPie, anchoPie, altoPie);

        return rectanguloPie;
    }

    /**
     * Crea la tabla que contendra el mensaje del encabezado
     *
     * @param mensaje Mensaje que desplegaremos
     * @return Tabla con el mensaje de encabezado
     */
    private Table crearTablaEncabezado(String mensaje) {
        float[] anchos = {1F};
        Table tablaEncabezado = new Table(anchos);
        tablaEncabezado.setWidth(527F);
        tablaEncabezado.addCell(mensaje);
        return tablaEncabezado;
    }

    /**
     * Crea la tabla de pie de pagina, con el numero de pagina
     *
     * @param docEvent Evento del documento
     * @return Pie de pagina con el numero de pagina
     */
    private Table crearTablaPie(PdfDocumentEvent docEvent) {
        PdfPage page = docEvent.getPage();
        float[] anchos = {2F};
        Table tablaPie = new Table(anchos);
        tablaPie.setWidth(527F);
        Integer pageNum = docEvent.getDocument().getPageNumber(page);

        tablaPie.addCell("Pagina " + pageNum);

        return tablaPie;
    }

    /**
     * Manejador del evento de cambio de pagina, agrega el encabezado y pie de
     * pagina
     *
     * @param event Evento de pagina
     */
    @Override
    public void handleEvent(Event event) {
        //AL MODIFICAR Este encabezado , MODIFICA TODOS LOS ARCHIVOS 
        //LO mejor sería hacer un EventoPagina2;
        int volumen = 105;
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

        //Table tablaEncabezado = this.crearTablaEncabezado("Departamento de Recursos Humanos");
        Rectangle rectanguloEncabezado = this.crearRectanguloEncabezado(docEvent);
        Canvas canvasEncabezado = new Canvas(canvas, pdfDoc, rectanguloEncabezado);
        Paragraph just_serv = new Paragraph("San luis Géminis");
        //styles
        Style styleText = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Color colorNegro = Color.BLACK;
        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
        /*--- tabla en cabezado ---*/
        Image img = null;
        try {
            img = new Image(ImageDataFactory.create("images\\sanluisimag.jpg"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(style1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table tabla_encabezado = new Table(new float[]{volumen * 1, volumen * 4});

        Cell cell = new Cell().add(img.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /*new SolidBorder(Color.BLACK,1*/
        tabla_encabezado.addCell(cell).addStyle(styleCell).setTextAlignment(TextAlignment.LEFT);
        PdfFont bold = null;
        try {
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(style1.class.getName()).log(Level.SEVERE, null, ex);
        }
        cell = new Cell().add(new Paragraph("Centro Odontologico Especializado").setFont(bold))
                .add(new Paragraph("San Luis Géminis").setFont(bold))
                // .add(new Paragraph(new Date().toString()))
                .addStyle(styleText).addStyle(styleCell)
                .setBorder(Border.NO_BORDER);
        tabla_encabezado.addCell(cell);
        //canvasEncabezado.add(tabla_encabezado);

        Table tablaNumeracion = this.crearTablaPie(docEvent);
        Rectangle rectanguloPie = this.crearRectanguloPie(docEvent);
        Canvas canvasPie = new Canvas(canvas, pdfDoc, rectanguloPie);
        Table TablePie = new Table(new float[]{volumen * 2.5f, volumen * 2.5f});
        Table TablePie1 = new Table(new float[]{volumen * 1.4f});
        Table TablePie2 = new Table(new float[]{volumen * 1.4f});
        Cell cellPie = new Cell().add(new Paragraph("Firma del operador").setBorderTop(new SolidBorder(1f)).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextCenter);
        TablePie1.addCell(cellPie);
        cellPie = new Cell().add(new Paragraph("Firma del paciente").setBorderTop(new SolidBorder(1f)).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextCenter);
        TablePie2.addCell(cellPie);
        TablePie.addCell(new Cell().add(TablePie1).setMarginLeft(70).addStyle(styleCell));
        TablePie.addCell(new Cell().add(TablePie2).setMarginLeft(70).addStyle(styleCell));
        pag++;
        if (pag == 2) {
            canvasPie.add(TablePie);
        } else {
            canvasPie.add(new Paragraph(pag + "").setTextAlignment(TextAlignment.RIGHT));
        }
    }

}
