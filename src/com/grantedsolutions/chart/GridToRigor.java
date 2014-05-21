/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.grantedsolutions.chart;

import com.learnerati.datameme.DMemeGrid;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author User1
 */
public class GridToRigor {
    
    private final Document doc;
    private final Element root;
    private final SVGBase base;

    private Double _min = Double.MAX_VALUE;
    private Double _max = Double.MIN_VALUE;
    private Double _total = 0d;

    private final Double horzOffset = 75d;
    private final Double vertOffset = 20d;
    
    private boolean useRowDescriptor = true;
    private boolean useColDescriptor = true;
    

    private final int textDrop = 12;
    private final int textBack = 15;
    private final Double height = 15d;

    private final int cellWidth = 50;
    private final int cellHeight = 50;
    
    
    private final HashMap<String, Object> styleBase = new HashMap<>();
   
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Object> rules = new HashMap<>();
    private Map<String, Object> styles = new HashMap<>();    

    public GridToRigor(SVGBase base) {
        doc = base.doc;
        root = base.doc.getDocumentElement();
        this.base = base;
        
        // default values
        params.put("vertical-anchor", "top");
        params.put("horizontal-anchor", "center");

        // default values
        styles.put("fill", "#3D5C00");
        styles.put("stroke", "#000000");
        styles.put("stroke-width", "0.25");        
    }

    
    public void setRules(Map<String, Object> rulesMap) {
        rules = rulesMap;
    }

    public void setParams(Map<String, Object> paramMap) {
        params = paramMap;
    }

    public void setStyles(Map<String, Object> styleMap) {
        styles = styleMap;
    }
    
    
    
    public void Build(DMemeGrid grid) {
        
        //-- Create the Cell Grid container
        Element cellgrid = doc.createElementNS(null, "g");
        cellgrid.setAttributeNS(null, "id", "gridcell");
        cellgrid.setAttributeNS(null, "stroke", "black");
        cellgrid.setAttributeNS(null, "stroke-width", "0.5");
        cellgrid.setAttributeNS(null, "opacity", "1"); 
        cellgrid.setAttributeNS(null, "fill", "white"); 
        
        Element labelContainer = doc.createElementNS(null, "g");
        labelContainer.setAttributeNS(null, "id", "grid_labels");
        labelContainer.setAttributeNS(null, "stroke", "black");
        labelContainer.setAttributeNS(null, "stroke-width", "0.25");        
        labelContainer.setAttributeNS(null, "opacity", "1");        
        
        Double maxX     = 0d;
        Double maxY     = 0d;
        Double total    = grid.Total();
        Double max      = grid.Max();
        
        base.CheckPoint(maxX, maxY);
        
        for (int blmIdx = 0; blmIdx < 6; blmIdx++) {            
            for (int dokIdx = 0; dokIdx < 4; dokIdx++) {
                
                Double X1 = horzOffset + (blmIdx * cellWidth);
                Double Y1 = vertOffset + (dokIdx * cellHeight);
                
                maxX = X1 > maxX ? X1 : maxX;
                maxY = Y1 > maxY ? Y1 : maxY;
                
                Double op = 0d;
                
                if (grid.hasElement(dokIdx, blmIdx)) {
                    if (!grid.getItem(dokIdx, blmIdx).isNull()) {
                        
                        op = grid.getItem(dokIdx, blmIdx).asDouble();                        
                        op = op / max;
                    }
                }
                
                styleBase.put("fill", "red");
                styleBase.put("fill-opacity", String.format("%1.2f", op));
                
                cellgrid.appendChild(
                        new ShadedRect(doc).Create(
                                X1,
                                Y1,
                                cellWidth,
                                cellHeight,
                                params,
                                styleBase
                        )
                );   
            }                                 
        }  
        
        

        // place the row descriptor -------------------------------------------
        if (useRowDescriptor) {
            String str = grid.getRowDescriptor();

            Double x = horzOffset - 55;
            Double y = vertOffset + (grid.Rows() * cellHeight) / 2 ;

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "middle");
            label.setAttributeNS(null, "baseline-shift", "-33%");
            label.setAttributeNS(null, "transform", String.format("rotate(-90, %d, %d)", x.intValue(), y.intValue()));
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "12pt");
            label.setAttributeNS(null, "x", x.toString());
            label.setAttributeNS(null, "y", y.toString());
            label.appendChild(doc.createTextNode(String.format("%s", str)));
            labelContainer.appendChild(label);
        }        
             
        
        // place the coldescriptor --------------------------------------------
        if (useColDescriptor) {
            
            Double x = (horzOffset + (grid.Cols() * cellWidth)) / 2d;
            Double y = vertOffset + (grid.Rows() * cellHeight) + cellHeight*0.8d;

            String str = grid.getColDescriptor();

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "middle");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "12pt");
            label.setAttributeNS(null, "x", x.toString());
            label.setAttributeNS(null, "y", y.toString());
            label.appendChild(doc.createTextNode(String.format("%s", str)));
            labelContainer.appendChild(label);
            
            base.CheckPoint(x, y +15);
        }        
           
        
        for (Double blmIdx = 0d; blmIdx < 6d; blmIdx++) {            
            Double X1 = horzOffset + (blmIdx * cellWidth);;
            Double Y1 = vertOffset + (4d * cellHeight) +15d;

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "middle");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "12pt");
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.appendChild(doc.createTextNode(String.format("%s", String.format("%d", blmIdx.intValue()+1))));
            labelContainer.appendChild(label);             
        }
        
        
        for (Double dokIdx = 0d; dokIdx < 4d; dokIdx++) {

            Double X1 = horzOffset -35d;
            Double Y1 = vertOffset + ((4d -dokIdx) * cellHeight) - cellHeight/2d +3d;

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "end");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "12pt");
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.appendChild(doc.createTextNode(String.format("%s", String.format("%d", dokIdx.intValue()+1))));
            labelContainer.appendChild(label);                
        }
        
        
                
        base.CheckPoint(maxX + cellWidth, maxY + cellHeight);
        
        //-- Append this container
        root.appendChild(cellgrid);
        root.appendChild(labelContainer);
        
    }

    //=========================================================================
    //
    public void ToFile(String path, String name) {
        base.WriteToFile(path, name);
    }

    public String ToString() {
        return base.WritetoString();
    }
    
}
