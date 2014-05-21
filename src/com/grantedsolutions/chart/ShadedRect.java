/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.chart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Ben
 */
public class ShadedRect {

    
    private static DocumentFragment frag;
    private static Document doc;         
    private static Map<String, Object> _params;
    private static Map<String, Object> _attrs;    
    private static Map<String, Object> _style;
    

    /**
     * 
     * @param styles A HashMap containing styles and their values.
     */
    protected void SetStyles(Map<String, Object> styles)
    {
        _style = styles;
    }    
    
    
    /**
     * 
     * @param params A HashMap containing parameters and their values.
     */
    protected void SetParams(Map<String, Object> params)
    {
        _params = params;
    }      
    
    
    /**
     * 
     * @param doc The Document doc used for creating Elements against.
     */
    public ShadedRect(Document doc)
    {
        ShadedRect.doc  = doc;
        ShadedRect.frag = doc.createDocumentFragment();
        
        _params = new HashMap<>();
        _attrs  = new HashMap<>();
        _style  = new HashMap<>();
    }

    
    /**
     * 
     * @param x An double defining the x coordinate of the ShadedRect.
     * @param y An double defining the y coordinate of the ShadedRect.
     * @param w An double defining the ShadedRect width.
     * @param h An double defining the ShadedRect height.
     * @param params A HashMap containing parameters and their values.
     * @param styles A HashMap containing styles and their values.
     * @return A DOM Node.
     */
    public Node Create(double x, double y, double w, double h, 
            Map<String, Object> params, 
            Map<String, Object> styles)
    {
        SetParams(params);
        SetStyles(styles);
        
        return Create(x, y, w, h, "");
    }
  
    public Node Create(double x, double y, double w, double h, 
            Map<String, Object> params, 
            Map<String, Object> styles, 
            String title)
    {
        SetParams(params);
        SetStyles(styles);
        
        return Create(x, y, w, h, title);
    }    
    
    /**
     * 
     * @param x An double defining the x coordinate of the ShadedRect.
     * @param y An double defining the y coordinate of the ShadedRect.
     * @param w An double defining the ShadedRect width.
     * @param h An double defining the ShadedRect height.
     * @return A DOM Node.
     */
    public Node Create(double x, double y, double w, double h, String title)
    {                
        double cx = x - w/2;
        double cy = y - h/2;
                        
        if (_params != null)
        {
            if (_params.containsKey("vertical-anchor"))
            {
                switch ( _params.get("vertical-anchor").toString() ) 
                {
                    case "top":
                        cy = y;
                        break;
                    
                    case "bottom":
                        cy = cy - h;
                        break;
                    
                    case "center":
                        // default case
                        break;
                }                
            }
            
            if (_params.containsKey("horizontal-anchor"))
            {
                switch (_params.get("horizontal-anchor").toString())
                {
                    case "left":
                        cx = x;
                        break;
                    
                    case "right":
                        cx = x - w;
                        break;
                    
                    case "center":
                        // default case
                        break;
                }
            }                        
        }

        Element elem =  doc.createElement("rect");  
        elem.setAttribute("x", String.format("%.2f", cx));
        elem.setAttribute("y", String.format("%.2f", cy));
        elem.setAttribute("width", String.format("%.2f", w));
        elem.setAttribute("height", String.format("%.2f", h)); 

        if (_style != null)
        {
            for (Iterator iter = _style.keySet().iterator(); iter.hasNext();)
            {
                String name = iter.next().toString();
                Object value = _style.get(name);

                elem.setAttributeNS(null, name, value.toString());                 
            }
        } 
        
        // sets up up a tooltip for capable viewers
        if (!title.isEmpty()) {
            Element tooltip =  doc.createElement("title"); 
            tooltip.appendChild(doc.createTextNode(title)); 
            elem.appendChild(tooltip);
        }

        frag.appendChild(elem);

        return frag;
    }

    

    
    

}
