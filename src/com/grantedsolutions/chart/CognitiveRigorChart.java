/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.chart;

import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author User1
 */
public class CognitiveRigorChart {

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
    
    private final HashMap<String, Object> params = new HashMap<>();
    private final HashMap<String, Object> styleBase = new HashMap<>();
    

    public CognitiveRigorChart(SVGBase base) {
        doc = base.doc;
        root = base.doc.getDocumentElement();
        this.base = base;
    }

    public void Build() {
        
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
