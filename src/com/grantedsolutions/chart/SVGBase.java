/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.chart;

import com.learnerati.utilities.XMLBase;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** A base object for generating SVG charts which provides a set of centralized 
 * Methods for dealing with images being constructed from numerous fragments.
 *
 * @author Ben
 */
public class SVGBase {
    
    /** The DOM Document used by other classes to create elements. */
    public Document doc;
    
    /** The root Node used by other classes to append elements. */
    public Element root;
    
    
    public Double minX = Double.MAX_VALUE;
    public Double maxX = Double.MIN_VALUE;
    public Double minY = Double.MAX_VALUE;
    public Double maxY = Double.MIN_VALUE;
    
    
    
    /** Initializes the base object for use by other classes.
     * <p/><b>Usage Examples:</b><p/>
     * {@code SVGBase svgb = new SVGBase().Create(); }
     * @return A SVGBase object used for creating SVG images.
     */
    public SVGBase Create()
    {
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        doc = impl.createDocument(svgNS, "svg", null);
        
        root = doc.getDocumentElement();
        root.setAttributeNS(null, "xmlns", "http://www.w3.org/2000/svg");
        root.setAttributeNS(null, "xlink", "http://www.w3.org/1999/xlink");
        root.setAttributeNS(null, "xml:space", "preserve");
        root.setAttributeNS(null, "width", "400");
        root.setAttributeNS(null, "height", "400");                
        
        if (doc == null) {
            System.out.println("SVGBase: Doc is null");
        }
        
        return this;
    }

    /**
     * 
     * @param x The x portion of a point.
     * @param y The y portion of a point.
     */
    public void CheckPoint(Double x, Double y)
    {
        minX = minX < x ? minX : x;
        maxX = maxX > x ? maxX : x;
        minY = minY < y ? minY : y;
        maxY = maxY > y ? maxY : y;        
    }
    
    
    /**
     * 
     * @return The width of the SVG image.
     */
    public Double GetWidth()
    {
        return maxX - minX + 20;
    }
    
    
    /**
     * 
     * @return  The height of the SVG image.
     */
    public Double GetHeight()
    {
        return maxY - minY + 20;
    }    
    
    
    /**
     * 
     * @param fileName The output file name for the image when written to disk.
     */
    public void WriteToFile(String path, String fileName)
    {
        //String path = "C:/LEARNERATI_ROOT/images/svg";
        String fullPath = path + "/" + fileName;
        
        root.setAttributeNS(null, "width",  String.format("%.4f", GetWidth()));
        root.setAttributeNS(null, "height", String.format("%.4f", GetHeight()));
                        
        try {
            boolean exists = (new File(path)).exists();

            if (exists == false) {
                boolean success = (new File(path)).mkdirs();
            }

            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputCharStream(new java.io.FileWriter(fullPath));
            serializer.serialize(doc);

        } catch (Exception ex) {
            Logger.getLogger(SVGBase.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }    
    
    
    public String WritetoString()
    {
        //String path = "C:/LEARNERATI_ROOT/images/svg";

        root.setAttributeNS(null, "width",  String.format("%.4f", GetWidth()));
        root.setAttributeNS(null, "height", String.format("%.4f", GetHeight()));
                        
        try {
            OutputFormat format    = new OutputFormat (doc); 
            StringWriter stringOut = new StringWriter ();    
            XMLSerializer serial   = new XMLSerializer (stringOut, format);
            
            serial.serialize(doc);   
            
            return stringOut.toString();
            
        } catch (IOException ex) {
            Logger.getLogger(XMLBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new String();      
    }     
}
