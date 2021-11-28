package com.ll.vbc.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class XmlDocumentConversion {

    /**
    * Convert the document to an array of bytes.
    *
    * @param doc The XML document.
    * @param encoding The encoding of the output data.
    *
    * @return The XML document as an array of bytes.
    *
    * @throws TransformerException If there is an error transforming to text.
    **/
    public static byte[] toByteArray(Document doc, String encoding) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        return writer.getBuffer().toString().getBytes();
    }

    /**
     * Converts byte[] to xml Document
     * @param bytes - byte[]
     * @return xml Document
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document fromByteArray(byte[] bytes) throws ParserConfigurationException, SAXException, IOException {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(bytes));

    }

}
