package es.tlc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;

import es.tlc.docx4j.element.Header;

public class Main {

	public static void main(String[] args) {
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			Header header = new Header();
			
			Relationship relationship = header.createHeaderPart(wordMLPackage, factory);
			header.createHeaderReference(relationship, wordMLPackage, factory);
			
			wordMLPackage.save(new java.io.File("src/main/files/Baja.docx"));
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
		} catch (Docx4JException docx4je) {
			docx4je.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addImage() {
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			ObjectFactory factory = Context.getWmlObjectFactory();

			File file = new File("src/main/resources/images/mesvida_logo.png");
			byte[] bytes = convertImageToByteArray(file);
			addImageToPackage(wordMLPackage, bytes);
			
			Relationship relationshipF = AddingHeaderAndFooter.createFooterPart(wordMLPackage, factory);
			Relationship relationshipH = AddingHeaderAndFooter.createHeaderPart(wordMLPackage, factory);
			AddingHeaderAndFooter.createFooterReference(relationshipF, wordMLPackage, factory);
			AddingHeaderAndFooter.createHeaderReference(relationshipH, wordMLPackage, factory);

			wordMLPackage.save(new java.io.File("src/main/files/HelloWord7.docx"));
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Docx4JException doc4je) {
			doc4je.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert the image from the file into an array of bytes.
	 *
	 * @param file  the image file to be converted
	 * @return 		the byte array containing the bytes from the image
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static byte[] convertImageToByteArray(File file) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(file );
		long length = file.length();
		// You cannot create an array using a long, it needs to be an int.
		if (length > Integer.MAX_VALUE) {
			System.out.println("File too large!!");
		}
		byte[] bytes = new byte[(int)length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read
		if (offset < bytes.length) {
			System.out.println("Could not completely read file "
					+file.getName());
		}
		is.close();
		return bytes;
	}
	
	/**
	 *  Docx4j contains a utility method to create an image part from an array of
	 *  bytes and then adds it to the given package. In order to be able to add this
	 *  image to a paragraph, we have to convert it into an inline object. For this
	 *  there is also a method, which takes a filename hint, an alt-text, two ids
	 *  and an indication on whether it should be embedded or linked to.
	 *  One id is for the drawing object non-visual properties of the document, and
	 *  the second id is for the non visual drawing properties of the picture itself.
	 *  Finally we add this inline object to the paragraph and the paragraph to the
	 *  main document of the package.
	 *
	 *  @param wordMLPackage The package we want to add the image to
	 *  @param bytes         The bytes of the image
	 *  @throws Exception    Sadly the createImageInline method throws an Exception
	 *                       (and not a more specific exception type)
	 */
	private static void addImageToPackage(WordprocessingMLPackage wordMLPackage, byte[] bytes) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

		int docPrId = 1;
		int cNvPrId = 2;
		Inline inline = imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);

		P paragraph = addInlineImageToParagraph(inline);

		wordMLPackage.getMainDocumentPart().addObject(paragraph);
	}

	/**
	 *  We create an object factory and use it to create a paragraph and a run.
	 *  Then we add the run to the paragraph. Next we create a drawing and
	 *  add it to the run. Finally we add the inline object to the drawing and
	 *  return the paragraph.
	 *
	 * @param   inline The inline object containing the image.
	 * @return  the paragraph containing the image
	 */
	private static P addInlineImageToParagraph(Inline inline) {
		// Now add the in-line image to a paragraph
		ObjectFactory factory = new ObjectFactory();
		P paragraph = factory.createP();
		R run = factory.createR();
		paragraph.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return paragraph;
	}

	public static void createSimpleDocx() {
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "Hello Word!");
			wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle",
					"This is a subtitle!");
			wordMLPackage.save(new java.io.File("src/main/files/HelloWord2.docx"));
		} catch (InvalidFormatException ife) {
			// TODO Auto-generated catch block
			ife.printStackTrace();
		} catch (Docx4JException doc4je) {
			// TODO Auto-generated catch block
			doc4je.printStackTrace();
		}
	}

}
