/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learnerati.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Ben
 */
public class XMLBase {

    /**
     * The DOM Document used by other classes to create elements.
     */
    public Document doc;
    /**
     * The root Node used by other classes to append elements.
     */
    public Element root;
    
       
    private DocumentBuilderFactory factory;
    

    public XMLBase Create(String rootElement)
    {
        doc     = null;
        root    = null;
        
        try {

            factory = DocumentBuilderFactory.newInstance();

            // set the factory to be namespace aware
            factory.setNamespaceAware(true);

            // create the xml document builder object
            // and get the DOMImplementation object
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation domImpl = builder.getDOMImplementation();

            doc = domImpl.createDocument(null, rootElement, null);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        root = doc.getDocumentElement();
        //root.setAttribute("xmlns", "http://www.w3.org/2000/xmlns/");
        //root.setAttribute("xlink", "http://www.w3.org/1999/xlink");
        //root.setAttribute("xml:space", "preserve");
        
        if (doc == null) {
            System.out.println("XMLBase: Doc is null");
        }

        return this;
    }

    /**
     *
     * @param fileName The output file name for the image when written to disk.
     */
    public void WriteXML(String fileName)
    {                
        Properties outputProps = new ReadProperties("output.properties").Fetch();
                
        String path     = outputProps.getProperty("file.outbase"); //"E:/LEARNERATI_ROOT/raw/xml";
        String location = outputProps.getProperty("file.xml");
        
        String fullPath = path + location + "/" + fileName;

        System.out.println("\nWriting: " + fullPath);
        
        if (doc == null) {
            System.out.println("DOC is NULL");
        }
        
        /*
         * try { boolean exists = (new File(path)).exists();
         *
         * if (exists == false) { boolean success = (new File(path)).mkdirs(); }
         *
         * XMLSerializer serializer = new XMLSerializer();
         *
         * serializer.setOutputCharStream(new java.io.FileWriter(fullPath));
         * serializer.serialize(doc);
         *
         * } catch (Exception ex) {
         * Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex); }          *
         */

        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            //Setup indenting to "pretty print"
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            serializer.transform(new DOMSource(doc)
                    , new StreamResult(new java.io.FileWriter(fullPath))
            );

        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String AsString()
    {
        try {
            OutputFormat format    = new OutputFormat (doc);
            format.setIndent(2);
            format.setIndenting(true);
            StringWriter stringOut = new StringWriter ();    
            XMLSerializer serial   = new XMLSerializer (stringOut, format);
            
            serial.serialize(doc);   
            
            return stringOut.toString();
            
        } catch (IOException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new String();
    }
    
    
    public void AsFile(String path, String name)
    {
        String fullPath  = String.format("%s%s%s", path, File.separatorChar, name);
        
        //System.out.println("Writing: " + fullPath);
        
        try {
            OutputFormat format    = new OutputFormat (doc);
            format.setIndent(2);
            format.setIndenting(true);
            StringWriter stringOut = new StringWriter ();    
            XMLSerializer serial   = new XMLSerializer (stringOut, format);
            
            serial.serialize(doc);   
            
            FileWriter outFile = new FileWriter(fullPath);  
            BufferedWriter bWriter = new BufferedWriter(outFile);
            bWriter.write(stringOut.toString());  
             
            bWriter.flush();
            bWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public String Stripped()
    {
        try {
            OutputFormat format    = new OutputFormat (doc); 
            StringWriter stringOut = new StringWriter ();    
            XMLSerializer serial   = new XMLSerializer (stringOut, format);
            
            serial.serialize(doc);   
                        
            String str = stringOut.toString();
           
            str = str.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

            return str.trim();
                                           
        } catch (IOException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new String();
    }    
    
    
    public DocumentFragment ImportFragmentString(String str)
    {
        DocumentFragment docfrag = doc.createDocumentFragment();

        Document d;
        
        String strFrag = "<voot>" + str + "</voot>";
        
        try {
               
            d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(strFrag)));
            
            Node node = doc.importNode(d.getDocumentElement(), true);  
            
            while (node.hasChildNodes()) {
                docfrag.appendChild(node.removeChild(node.getFirstChild()));
            }            

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        
        }            

       
        return docfrag;
    }
}
