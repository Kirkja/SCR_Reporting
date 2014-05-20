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

    private final Double horzOffset = 20d;
    private final Double vertOffset = 1d;

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
        
        
        for (int blmIdx = 1; blmIdx <= 6; blmIdx++) {
            for (int dokIdx = 1; dokIdx <= 4; dokIdx++) {
                
                Double X1 = horzOffset + blmIdx * cellWidth;
                Double Y1 = vertOffset + dokIdx * cellHeight;
                Double op = 0d;
                
                if (grid.hasElement(dokIdx-1, blmIdx-1)) {
                    if (!grid.getItem(dokIdx-1, blmIdx-1).isNull()) {
                        op = grid.getItem(dokIdx-1, blmIdx-1).asDouble();
                    }
                }

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
                
                base.CheckPoint(X1, Y1);
                base.CheckPoint(X1 + cellWidth, Y1 + cellHeight);
            }
        }        
        
        //-- Append this container
        root.appendChild(cellgrid);        
  
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
