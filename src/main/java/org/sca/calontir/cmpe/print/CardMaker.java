package org.sca.calontir.cmpe.print;

import com.itextpdf.text.BadElementException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.utils.MarshalUtils;

public class CardMaker {

    private static Font largFont = new Font(Font.FontFamily.TIMES_ROMAN, 24f, Font.NORMAL);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.NORMAL);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 8f, Font.NORMAL);
    private static Font smallerFont = new Font(Font.FontFamily.TIMES_ROMAN, 6f, Font.NORMAL);

    public void build(final OutputStream os, final List<Fighter> data) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        addMetaData(document);
        for (Iterator<Fighter> it = data.iterator(); it.hasNext();) {
            Fighter f = it.next();
            writeBackground(writer, document);
            writeToPage(document, f);
            tearOff(writer, document, f);
            if (it.hasNext()) {
                document.newPage();
            }
        }
        document.close();
        baos.writeTo(os);
    }

    private void addMetaData(Document document) {
        document.addTitle("Fighter Card");
        document.addAuthor("Deputy Earl Marshal");
        document.addCreator("Calontir Marshal Project");
    }

    public void writeBackground(PdfWriter writer, Document document) throws BadElementException, DocumentException, IOException {
        Rectangle size = document.getPageSize();
        PdfContentByte cbunder = writer.getDirectContentUnder();
        Image img = loadBackground();
        if (img != null) {
            img.setAbsolutePosition(0, 0);
            img.scaleAbsoluteWidth(size.getRight());
            img.scaleAbsoluteHeight(size.getTop());
            cbunder.addImage(img);
        }

    }

    private Image loadBackground() throws BadElementException, IOException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String name = "border.gif";

        if (loader != null) {
            URL url = loader.getResource(name);
            if (url == null) {
                url = loader.getResource("/" + name);
            }
            if (url != null) {
                Toolkit tk = Toolkit.getDefaultToolkit();
                java.awt.Image img = tk.getImage(url);
                return Image.getInstance(img, null);
            }
        }
        return null;
    }

    private void writeToPage(Document document, Fighter fighter) throws DocumentException {
        Rectangle size = document.getPageSize();
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;

        table.setTotalWidth(size.getRight());
        cell = new PdfPCell(new Phrase("\n\n\n", largFont));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("The Society for Creative Anachronism, Inc\nKingdom of Calontir\n", largFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Combat Authorizations", normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.format("%s is a fighter in good standing and authorized in the following:\n", fighter.getScaName()), normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(MarshalUtils.getAuthDescriptionAsString(fighter.getAuthorization()), normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        document.add(table);
    }

    private void tearOff(PdfWriter writer, Document document, Fighter fighter) throws BadElementException, DocumentException {
        PdfContentByte cb = writer.getDirectContent();
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(200);
        PdfPCell cell;

        // Back of card
        Paragraph p = new Paragraph();
        p.add(new Phrase("          Kingdom Specific Authorizations      \n", smallFont));
        p.add(new Phrase(String.format("  %s All      %s WSH     %s PA\n",
                MarshalUtils.hasAll(fighter) ? "X" : "",
                MarshalUtils.hasAuth("WSH", fighter) ? "X" : "",
                MarshalUtils.hasAuth("PA", fighter) ? "X" : ""), smallFont));
        p.add(new Phrase(String.format("  %s THS      %s TW      %s SP\n",
                MarshalUtils.hasAuth("THS", fighter) ? "X" : "",
                MarshalUtils.hasAuth("TW", fighter) ? "X" : "",
                MarshalUtils.hasAuth("SP", fighter) ? "X" : ""), smallFont));
        p.add(new Phrase(String.format("                         %s Marshal\n",
                MarshalUtils.hasAuth("Marshal", fighter) ? "X" : ""), smallFont));
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.TOP + Rectangle.LEFT + Rectangle.RIGHT);
        cell.setRotation(180);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        p = new Paragraph();
        p.add(new Phrase(String.format("Group: %20s  Minor %s\n", fighter.getScaGroup().getGroupName(),
                MarshalUtils.isMinor(fighter) ? "X" : ""), smallFont));
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
        cell.setRotation(180);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        p = new Paragraph();
        if (StringUtils.isBlank(fighter.getScaName())) {
            p.add(new Phrase("SCA Name _________________________\n", smallFont));
        } else {
            p.add(new Phrase(String.format("SCA Name %s\n", fighter.getScaName()), smallFont));
        }
        p.add(new Phrase(String.format("Modern Name %s\n", fighter.getModernName()), smallFont));
        p.add(new Phrase(String.format("Group: %s  Minor %s\n", fighter.getScaGroup().getGroupName(),
                MarshalUtils.isMinor(fighter) ? "X" : ""), smallFont));
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
        cell.setRotation(180);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("The Society for Creative Anachronism, Inc\nKingdom of Calontir\nCombat Authorization Card", smallFont));
        cell.setBorder(Rectangle.BOTTOM + Rectangle.LEFT + Rectangle.RIGHT);
        cell.setRotation(180);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        // Front of card
        cell = new PdfPCell(new Phrase("Kingdom of Calontir\nFighter Authorizatin Card\n\n", smallFont));
        cell.setBorder(Rectangle.TOP + Rectangle.LEFT + Rectangle.RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        p = new Paragraph();
        if (StringUtils.isBlank(fighter.getScaName())) {
            p.add(new Phrase("SCA Name _________________________\n", smallFont));
        } else {
            p.add(new Phrase(String.format("SCA Name %s\n", fighter.getScaName()), smallFont));
        }
        p.add(new Phrase(String.format("Date Issued %s\n", new Date().toString()), smallFont));
        p.add(new Phrase(String.format("Issuing Official: %s\n", "Sir Ashir"), smallFont));
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("This car is your authorization to participate on the field at SCA activities.  It must be presented to the list officials at all SCA events to register for participation in any martial activity. You may be requested to show this card to any marshal and/or list official at any time.", smallerFont));
        cell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Signature: _________________________\n", smallFont));
        cell.setBorder(Rectangle.BOTTOM + Rectangle.LEFT + Rectangle.RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.writeSelectedRows(0, -1, 520, 300, cb);

        StringBuilder sb = new StringBuilder(fighter.getScaName());
        sb.append(" - ");
        for (Iterator<Authorization> it = fighter.getAuthorization().iterator(); it.hasNext();) {
            sb.append(it.next().getCode());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        BarcodeQRCode qrcode = new BarcodeQRCode(sb.toString(), 1, 1, null);
        Image img = qrcode.getImage();
        img.setAlignment(Image.RIGHT | Image.TEXTWRAP);
        img.setAbsolutePosition(670, 160);
//        img.scalePercent(20.0f);
        document.add(img);
    }
}
