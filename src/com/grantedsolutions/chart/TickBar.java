/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author User1
 */
public class TickBar {

    private static DocumentFragment frag;
    private static Document doc;
    private static Map<String, Object> _params;
    private static Map<String, Object> _attrs;
    private static Map<String, Object> _style;

    private boolean _useSegmentLabels = true;
    private boolean _useColumnLabels = true;

    private Double labelBackset = 10d;

    /**
     *
     * @param styles A HashMap containing styles and their values.
     */
    protected void SetStyles(Map<String, Object> styles) {
        _style = styles;
    }

    /**
     *
     * @param params A HashMap containing parameters and their values.
     */
    protected void SetParams(Map<String, Object> params) {
        _params = params;
    }

    /**
     *
     * @param doc The Document doc used for creating Elements against.
     */
    public TickBar(Document doc) {
        TickBar.doc = doc;
        TickBar.frag = doc.createDocumentFragment();

        _params = new HashMap<>();
        _attrs = new HashMap<>();
        _style = new HashMap<>();
    }

    public Node Create(double x,
            double y,
            double len) {

        return Create(x, y, len, "");
    }

    public Node Create(double x,
            double y,
            double len,
            Map<String, Object> params) {

        SetParams(params);

        return Create(x, y, len, "");
    }
    
    public Node Create(double x,
            double y,
            double len,
            Map<String, Object> params,
            String title) {

        SetParams(params);

        return Create(x, y, len, title);
    }    

    public Node Create(double x, double y, double len, String title) {

        double w = 10d;

        double cx = x - w / 2;
        double cy = y - len / 2;

        // default values only
        int segments = 5;
        int tickWidth = 10;
        double tickSpacing = len / segments;

        List<String> labels = new ArrayList<>();

        if (_params.containsKey("tickSpacing")) {
            tickSpacing = new Double(_params.get("tickSpacing").toString());
        }

        if (_params.containsKey("segments")) {
            segments = new Integer(_params.get("segments").toString());
        }

        if (_params.containsKey("labels")) {
            labels = (ArrayList) (_params.get("labels"));
        }

        if (_params.containsKey("useSegmentLabels")) {
            _useSegmentLabels = (boolean) (_params.get("useSegmentLabels"));
        }

        double cLength = segments * tickSpacing;

        if (_params != null) {
            if (_params.containsKey("vertical-anchor")) {
                switch (_params.get("vertical-anchor").toString()) {
                    case "top":
                        cy = y;
                        break;

                    case "bottom":
                        cy = cy - len;
                        break;

                    case "center":
                        // default case
                        break;
                }
            }

            if (_params.containsKey("horizontal-anchor")) {
                switch (_params.get("horizontal-anchor").toString()) {
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

        Element group = doc.createElement("g");

        if (_style != null) {
            for (Iterator iter = _style.keySet().iterator(); iter.hasNext();) {
                String name = iter.next().toString();
                Object value = _style.get(name);

                group.setAttributeNS(null, name, value.toString());
            }
        }

        Element elem = doc.createElement("line");
        elem.setAttribute("x1", String.format("%.4f", cx));
        elem.setAttribute("y1", String.format("%.4f", cy));
        elem.setAttribute("x2", String.format("%.4f", cx));
        elem.setAttribute("y2", String.format("%.4f", cy + len));

        // Vertical spine for the tick marks
        group.appendChild(elem);

        // Small tic marks off the spine
        for (int tdx = 0; tdx <= segments; tdx++) {
            Element tick = doc.createElement("line");
            tick.setAttribute("x1", String.format("%.4f", cx));
            tick.setAttribute("y1", String.format("%.4f", cy + (tdx * tickSpacing)));
            tick.setAttribute("x2", String.format("%.4f", cx + tickWidth));
            tick.setAttribute("y2", String.format("%.4f", cy + (tdx * tickSpacing)));

            group.appendChild(tick);
        }

        // Segment labels along the spine
        if (_useSegmentLabels == true) {
            if (labels.size() > 0 && labels.size() <= segments) {
                for (int tdx = 0; tdx < segments; tdx++) {

                    String cellLabel = labels.get(tdx);

                    Element label = doc.createElementNS(null, "text");
                    label.setAttributeNS(null, "text-anchor", "end");
                    label.setAttributeNS(null, "baseline-shift", "-33%");
                    label.setAttributeNS(null, "stroke", "none");
                    label.setAttributeNS(null, "fill", "black");
                    label.setAttributeNS(null, "font-size", "10pt");
                    label.setAttributeNS(null, "x", String.format("%.4f", cx - labelBackset));
                    label.setAttributeNS(null, "y", String.format("%.4f", 3 + cy + (tdx * tickSpacing) + tickSpacing / 2d) );
                    label.appendChild(doc.createTextNode(cellLabel));

                    group.appendChild(label);
                }
            }
        }
        
        if (_useColumnLabels == true) {
            String cellLabel = title;
            
            Double yy = cy + (segments * tickSpacing) +20;

            Element label = doc.createElementNS(null, "text");
            label.setAttributeNS(null, "text-anchor", "start");
            label.setAttributeNS(null, "baseline-shift", "-33%");
            label.setAttributeNS(null, "stroke", "none");
            label.setAttributeNS(null, "fill", "black");
            label.setAttributeNS(null, "font-size", "10pt");
            label.setAttributeNS(null, "x", String.format("%.4f", cx + 5));
            label.setAttributeNS(null, "y", String.format("%.4f", yy ));
            label.appendChild(doc.createTextNode(cellLabel));

            group.appendChild(label);            
        }

        // sets up up a tooltip for capable viewers
        //if (!title.isEmpty()) {
        //    Element tooltip = doc.createElement("title");
        //    tooltip.appendChild(doc.createTextNode(title));
        //    elem.appendChild(tooltip);
        //}
        frag.appendChild(group);

        return frag;
    }

}
