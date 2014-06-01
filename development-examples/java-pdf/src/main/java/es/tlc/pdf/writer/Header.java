package es.tlc.pdf.writer;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class Header extends PdfPageEventHelper {

    public void onEndPage (PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        //Aquí definimos el encabezado de nuestro documento PDF
       //Únicamente le ponemos nuestro nombre
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("MESVIDA"),
                0, 0, 0);
        
        //Aquí definimos el pie de página de nuestro document PDF
        //lo que hace es poner el número de página a cada hoja del documento.
        /*ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(String.format("page %d otra cosa", writer.getPageNumber())),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);*/
    }
}