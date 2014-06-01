package es.tlc.main;

import java.io.File;
import java.util.List;

import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Text;

public class AddingHeaderAndFooter {

	/**
	 *  First we create the package and the factory. Then we create the footer part,
	 *  which returns a relationship. This relationship is then used to create
	 *  a reference. Finally we add some text to the document and save it.
	 */
	public static void main (String[] args) throws Docx4JException {
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		ObjectFactory factory = Context.getWmlObjectFactory();

		Relationship relationshipF = createFooterPart(wordMLPackage, factory);
		Relationship relationshipH = createHeaderPart(wordMLPackage, factory);
		createFooterReference(relationshipF, wordMLPackage, factory);
		createHeaderReference(relationshipH, wordMLPackage, factory);

		wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");

		wordMLPackage.save(new File("src/main/files/HelloWord14.docx") );
	}

	/**
	 *  This method creates a footer part and set the package on it. Then we add some
	 *  text and add the footer part to the package. Finally we return the
	 *  corresponding relationship.
	 * @param wordMLPackage 
	 * @param factory 
	 *
	 *  @return
	 *  @throws InvalidFormatException
	 */
	public static Relationship createFooterPart(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) throws InvalidFormatException {
		FooterPart footerPart = new FooterPart();
		footerPart.setPackage(wordMLPackage);

		footerPart.setJaxbElement(createFooter("Text", factory));

		return wordMLPackage.getMainDocumentPart().addTargetPart(footerPart);
	}

	public static Relationship createHeaderPart(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) throws InvalidFormatException {
		HeaderPart headerPart = new HeaderPart();
		headerPart.setPackage(wordMLPackage);

		headerPart.setJaxbElement(createHeader("Text", factory));

		return wordMLPackage.getMainDocumentPart().addTargetPart(headerPart);
	}
	
	/**
	 *  First we create a footer, a paragraph, a run and a text. We add the given
	 *  given content to the text and add that to the run. The run is then added to
	 *  the paragraph, which is in turn added to the footer. Finally we return the
	 *  footer.
	 *
	 *  @param content
	 * @param factory 
	 *  @return
	 */
	public static Ftr createFooter(String content, ObjectFactory factory) {
		Ftr footer = factory.createFtr();
		P paragraph = factory.createP();
		R run = factory.createR();
		Text text = new Text();
		text.setValue(content);
		run.getContent().add(text);
		paragraph.getContent().add(run);
		footer.getContent().add(paragraph);
		return footer;
	}
	
	public static Hdr createHeader(String content, ObjectFactory factory) {
		Hdr header = factory.createHdr();
		P paragraph = factory.createP();
		R run = factory.createR();
		Text text = new Text();
		text.setValue(content);
		run.getContent().add(text);
		paragraph.getContent().add(run);
		header.getContent().add(paragraph);
		return header;
	}

	/**
	 *  First we retrieve the document sections from the package. As we want to add
	 *  a footer, we get the last section and take the section properties from it.
	 *  The section is always present, but it might not have properties, so we check
	 *  if they exist to see if we should create them. If they need to be created,
	 *  we do and add them to the main document part and the section.
	 *  Then we create a reference to the footer, give it the id of the relationship,
	 *  set the type to header/footer reference and add it to the collection of
	 *  references to headers and footers in the section properties.
	 *
	 * @param relationship
	 * @param wordMLPackage 
	 * @param factory 
	 */
	public static void createFooterReference(Relationship relationship, WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
		List<SectionWrapper> sections = wordMLPackage.getDocumentModel().getSections();

		SectPr sectionProperties = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectionProperties==null ) {
			sectionProperties = factory.createSectPr();
			wordMLPackage.getMainDocumentPart().addObject(sectionProperties);
			sections.get(sections.size() - 1).setSectPr(sectionProperties);
		}
		
		FooterReference footerReference = factory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.DEFAULT);
		sectionProperties.getEGHdrFtrReferences().add(footerReference);
	}
	
	public static void createHeaderReference(Relationship relationship, WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
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
}
