package es.tlc.docx4j.element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.TextAlignment;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Text;

public class Header {

	public Relationship createHeaderPart(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) throws Exception {
		HeaderPart headerPart = new HeaderPart();
		headerPart.setPackage(wordMLPackage);

		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, convertImageToByteArray(new File("src/main/resources/images/mesvida_logo.png")));

		int docPrId = 1;
		int cNvPrId = 2;

		headerPart.setJaxbElement(createHeader("MESVIDA\n Centre d’entrenament personal\n C/ Anoia, núm. 33, baixos\n 08740 Sant Andreu de la Barca – (Barcelona)", imagePart.createImageInline("mesvida logo", "mesvida logo", docPrId, cNvPrId, false), factory));

		return wordMLPackage.getMainDocumentPart().addTargetPart(headerPart);
	}

	public void createHeaderReference(Relationship relationship, WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
		List<SectionWrapper> sections = wordMLPackage.getDocumentModel().getSections();

		SectPr sectionProperties = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectionProperties==null ) {
			sectionProperties = factory.createSectPr();
			wordMLPackage.getMainDocumentPart().addObject(sectionProperties);
			sections.get(sections.size() - 1).setSectPr(sectionProperties);
		}

		HeaderReference headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.DEFAULT);
		sectionProperties.getEGHdrFtrReferences().add(headerReference);
	}

	private Hdr createHeader(String content, Inline inline, ObjectFactory factory) {
		Hdr header = factory.createHdr();
		P paragraph = factory.createP();
		R run = factory.createR();
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		Text text = new Text();
		text.setValue(content);
		run.getContent().add(text);
		paragraph.getContent().add(run);
		
		RPr runProperties = factory.createRPr();
		PPr otherProperties = factory.createPPr();
		TextAlignment ta = new TextAlignment();
		ta.setVal("center");
		otherProperties.setTextAlignment(ta);
		paragraph.setPPr(otherProperties);
		run.setRPr(runProperties);
				
		header.getContent().add(paragraph);
		return header;
	}

	private byte[] convertImageToByteArray(File file) throws FileNotFoundException, IOException {
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
			System.out.println("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}
}
