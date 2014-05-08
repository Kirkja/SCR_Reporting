/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.chart;

import com.learnerati.datameme.DMemeGrid;
import com.learnerati.datameme.DataMeme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author User1
 */
public class GridToChart {

    private final Document doc;
    private final Element root;
    private final SVGBase base;

    private final int cellWidth = 50;
    private final int cellHeight = 50;
    private int gridInset = 0;

    private final Double horzOffset = 38d;
    private final Double vertOffset = 30d;

    private boolean useRowDescriptor = true;
    private boolean useColDescriptor = true;

    //private boolean _useCountData = true;
    //private boolean _useValueData = false;
    
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Object> rules = new HashMap<>();
    private Map<String, Object> styles = new HashMap<>();

    public GridToChart(SVGBase base) {
        doc = base.doc;
        root = base.doc.getDocumentElement();
        this.base = base;
        
        // default values
        params.put("vertical-anchor", "center");
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

        useRowDescriptor = rules.containsKey("UseRowDescriptor") 
                ? rules.get("UseRowDescriptor").equals("true") ? true : false
                : true;
        
        useColDescriptor = rules.containsKey("UseColDescriptor")
                ? rules.get("UseColDescriptor").equals("true") ? true : false
                : true;
        
        base.CheckPoint(0.0, 0.0);

        gridInset = cellWidth / 2 + 12;


        Double X1 = 0d;
        Double Y1 = 0d;

        int rowCount = grid.Rows();
        int colCount = grid.Cols();

        //  place the row labels ----------------------------------------------
        Element labelGroup = doc.createElementNS(null, "g");
        labelGroup.setAttributeNS(null, "id", "label_group");
        labelGroup.setAttributeNS(null, "fill", "black");
        labelGroup.setAttributeNS(null, "stroke", "none");
        labelGroup.setAttributeNS(null, "stroke-width", "0.5");
        labelGroup.setAttributeNS(null, "opacity", "1");

        for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
            String str = grid.getRowLabel(rowIdx);

            X1 = horzOffset;
            Y1 = vertOffset + rowIdx * cellHeight;

            base.CheckPoint(X1, Y1);
            base.CheckPoint(X1 + cellWidth / 2, Y1 + cellHeight / 2);

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "end");
            label.setAttributeNS(null, "baseline-shift", "-33%");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "14pt");
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.appendChild(doc.createTextNode(String.format("%s", str)));

            labelGroup.appendChild(label);
        }

        // place the row descriptor -------------------------------------------
        if (useRowDescriptor) {
            String str = grid.getRowDescriptor();

            X1 = horzOffset - 22;
            Y1 = vertOffset + (rowCount * cellHeight) / 2 - cellHeight / 2;

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "middle");
            label.setAttributeNS(null, "baseline-shift", "-33%");
            label.setAttributeNS(null, "transform", String.format("rotate(-90, %d, %d)", X1.intValue(), Y1.intValue()));
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "12pt");
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.appendChild(doc.createTextNode(String.format("%s", str)));
            labelGroup.appendChild(label);
        }

        //  place the col labels ----------------------------------------------
        Y1 = vertOffset + rowCount * cellHeight;

        for (int colIdx = 0; colIdx < colCount; colIdx++) {
            String str = grid.getColLabel(colIdx);

            X1 = horzOffset + colIdx * cellWidth + gridInset;

            base.CheckPoint(X1, Y1);
            base.CheckPoint(X1 + cellWidth / 2, Y1 + cellHeight / 2);

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "middle");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "14pt");
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.appendChild(doc.createTextNode(String.format("%s", str)));

            labelGroup.appendChild(label);
        }

        // place the coldescriptor --------------------------------------------
        X1 = horzOffset + (colCount * cellWidth) / 2 + 12;
        Y1 += cellHeight / 2;
        if (useColDescriptor) {
            String str = grid.getColDescriptor();

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "middle");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "12pt");
            label.setAttributeNS(null, "x", X1.toString());
            label.setAttributeNS(null, "y", Y1.toString());
            label.appendChild(doc.createTextNode(String.format("%s", str)));
            labelGroup.appendChild(label);
        }

        // create the grid container -----------------------------------------
        Element gridContainer = doc.createElementNS(null, "g");
        gridContainer.setAttributeNS(null, "id", "grid_lines");
        gridContainer.setAttributeNS(null, "stroke", "black");
        gridContainer.setAttributeNS(null, "stroke-width", "0.5");
        gridContainer.setAttributeNS(null, "opacity", "1");

        // create the grid value label container ------------------------------
        Element gridContainer2 = doc.createElementNS(null, "g");
        gridContainer2.setAttributeNS(null, "id", "grid_lines");
        gridContainer2.setAttributeNS(null, "stroke", "black");
        gridContainer2.setAttributeNS(null, "stroke-width", "0.5");
        gridContainer2.setAttributeNS(null, "opacity", "1");

        for (int colIdx = 0; colIdx < colCount; colIdx++) {
            for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {

                X1 = horzOffset + colIdx * cellWidth + gridInset;
                Y1 = vertOffset + rowIdx * cellHeight;

                Double cellValue = 0d;
                Double cellCount = 0d;
                Double value = 0d;

                DataMeme dm = grid.getItem(rowIdx, colIdx);
                if (dm.asDouble() != null) {
                    cellValue = dm.asDouble();
                }
                if (dm.getCount() != null) {
                    cellCount = dm.getCount().doubleValue();
                }

                Double opacity = 0d;

                if (rules.containsKey("UseValueData")) {
                    if (rules.get("UseValueData").equals("true")) {
                        opacity = cellValue / grid.Total();
                        value = cellValue;
                    }
                } 
                if (rules.containsKey("UseCountData")) {
                    if (rules.get("UseCountData").equals("true")) {
                        opacity = cellCount / grid.Count();
                        value = cellCount;
                    }
                }

                opacity = opacity > 0d ? opacity + 0.075d : 0d;

                styles.put("fill-opacity", opacity.toString());

                gridContainer.appendChild(
                        new ShadedRect(doc).Create(
                                X1,
                                Y1,
                                cellWidth,
                                cellHeight,
                                params,
                                styles
                        )
                );

                base.CheckPoint(X1, Y1);
                base.CheckPoint(X1 + cellWidth, Y1 + cellHeight);

                String color = opacity > 0.5d ? "white" : "black";

                String cellLabel = "";

                Double d = 100 * (opacity - 0.075d);

                if (d > 0d) {
                    cellLabel = String.format("%.0f", d);
                    Element label = doc.createElementNS(null, "text");
                    label.setAttributeNS(null, "text-anchor", "middle");
                    label.setAttributeNS(null, "baseline-shift", "-33%");
                    label.setAttributeNS(null, "stroke", "none");
                    label.setAttributeNS(null, "fill", color);
                    label.setAttributeNS(null, "font-size", "10pt");
                    label.setAttributeNS(null, "x", X1.toString());
                    label.setAttributeNS(null, "y", Y1.toString());
                    label.appendChild(doc.createTextNode(
                            cellValue > 0d
                            ? String.format("%s", cellLabel) + "%"
                            : String.format("%s", cellLabel)
                        )
                    );
                    gridContainer2.appendChild(label);
                }
            }
        }

        root.appendChild(gridContainer);
        root.appendChild(gridContainer2);
        root.appendChild(labelGroup);
    }

//=============================================================================
    public void ToFile(String path, String name) {
        base.WriteToFile(path, name);
    }

    public String ToString() {
        return base.WritetoString();
    }

}
