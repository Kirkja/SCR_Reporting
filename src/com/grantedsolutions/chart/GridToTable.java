/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.grantedsolutions.chart;

import com.learnerati.utilities.XMLBase;
import com.learnerati.datameme.DMemeGrid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;


/**
 *
 * @author User1
 */
public class GridToTable {
    
    private Document doc;
    private Element root;
    private XMLBase base;
    private DMemeGrid dataGrid;
    
    private Element footer;
    private boolean hasFooter = false;
    
    private Map<Integer, String> _colColors;

    public GridToTable(XMLBase base)
    {
        doc         = base.doc;
        root        = base.doc.getDocumentElement();
        this.base   = base;
        
        _colColors  = new HashMap<>();
    }

    
    
    public void Load(DMemeGrid data)
    {
        dataGrid = data;
    }
    
    public void Footer(List<String> data)
    {
         hasFooter  = true;         
         footer     = doc.createElement("data-table-footer");
         
         for (String str : data)
         {
            Element s2 = doc.createElement("footer-item");
            
            DocumentFragment frag = base.ImportFragmentString(str); 
            s2.appendChild(frag);
            footer.appendChild(s2);
         }                                         
    }
    
    public void RemoveFooter()
    {
        if (hasFooter)
        {
            root.removeChild(footer);
        }
    }
    
    public void Footer(String str)
    {
        hasFooter  = true;         
        footer     = doc.createElement("data-table-footer");

        Element s2 = doc.createElement("footer-item");
        DocumentFragment frag = base.ImportFragmentString(str); 
        s2.appendChild(frag);
        footer.appendChild(s2);                                      
    }    
    
    
    
    public void ColorColumnLabels(Map<Integer, String> colors)
    {
        _colColors = colors;
    }            
            
    
    
    public void Build()
    {
        Element title       = doc.createElement("title");
        Element caption     = doc.createElement("coldescriptor");
        Element colHeaders  = CreateColumnHeaders();
        Element rows        = CreateRows();
        
        title.appendChild(doc.createTextNode(dataGrid.getLabel()));
        caption.appendChild(doc.createTextNode(dataGrid.getColDescriptor()));
        
        root.appendChild(title);
        root.appendChild(caption);
        root.appendChild(colHeaders);
        root.appendChild(rows);

        if (hasFooter)
        {
            root.appendChild(footer);
        }
    }
    
    public void Index(boolean mode)
    {
        root.setAttribute("index", String.format("%s", mode));
    }
    
    
    
    private Element CreateColumnHeaders()
    {
        Element colHeaders  = doc.createElement("headers");
        Element header      = doc.createElement("col-header");
        header.appendChild(doc.createTextNode(dataGrid.getRowDescriptor()));
        
        colHeaders.appendChild(header);
        int cndx = 0;
        
        for (String colName : dataGrid.getColLabels())
        {
            Element item = doc.createElement("header");
            
            String text = SpaceHump(colName);
    
            List<String> tmplabels = HumpDivided(colName);
              
            Element gcolor = doc.createElement("color");
            gcolor.setAttribute("hue", "black");
            
            // Color the col labels appropiately 
            if (_colColors.containsKey(cndx))
            {
                gcolor.setAttribute("hue", _colColors.get(cndx));
            }
            
            // line break the words
            for (String s: tmplabels)
            {
               gcolor.appendChild(doc.createTextNode(s)); 
               gcolor.appendChild(doc.createElement("br"));
            }            
            
            item.appendChild(gcolor);
                       
            colHeaders.appendChild(item);
            
            cndx++;
        }
        
        
        // Apppend the row summary if present
        if (dataGrid.getRowSummarys().size() > 0)
        {
            Element item = doc.createElement("header");                        
            item.appendChild(doc.createTextNode(dataGrid.getRowSummaryLabel()));
            colHeaders.appendChild(item);            
        }
                
        return colHeaders;
    }
    
    
    
    private Element CreateRows()
    {
        Element rows = doc.createElement("rows");
        
        for (int rdx=0; rdx < dataGrid.Rows(); rdx++)
        {
            Element row     = doc.createElement("row");
            Element header  = doc.createElement("row-header");
            
            header.appendChild(doc.createTextNode(dataGrid.getRowLabel(rdx)));
            row.appendChild(header);
                        
            for (int cdx=0; cdx < dataGrid.Cols(); cdx++)
            {
                Element item = doc.createElement("col");
                String str = dataGrid.getItem(rdx, cdx).asText().equals("null") ? "*" : dataGrid.getItem(rdx, cdx).asText();
                item.appendChild(doc.createTextNode(str));                
                row.appendChild(item);
            }
              
            if (dataGrid.getRowSummarys().size() > 0)
            {
                Element item = doc.createElement("col");
                String str = dataGrid.getRowSummaryItem(rdx).asText().equals("null") ? "*" : dataGrid.getRowSummaryItem(rdx).asText();
                item.appendChild(doc.createTextNode(str));                
                row.appendChild(item);
            }
                        
            rows.appendChild(row);
        }
        
        
        
        if (dataGrid.getColSummarys().size() > 0)
        {      
            Element row     = doc.createElement("row-summary");
            Element header  = doc.createElement("summary-header");
            
            header.appendChild(doc.createTextNode(dataGrid.getColSummaryLabel()));
            row.appendChild(header);  
            
            Integer total = 0;
            
            for (int cndx=0; cndx < dataGrid.getColSummarys().size(); cndx++)
            {
                Element item = doc.createElement("summary-item");
                item.appendChild(doc.createTextNode(dataGrid.getColSummarys().get(cndx).asText()));                
                row.appendChild(item);  
                
                total += dataGrid.getColSummarys().get(cndx).asInteger();
            }
            
            if (dataGrid.getRowSummarys().size() > 0)
            {                       
                Element item = doc.createElement("summary-item");
                item.appendChild(doc.createTextNode(total.toString()));                
                row.appendChild(item);  
            }
            
            rows.appendChild(row);
        }
               
        return rows;
    }
    
    
    private String SpaceHump(String str)
    {
        StringBuilder sb = new StringBuilder();
        
        for (char c : str.toCharArray())
        {
            if (Character.isUpperCase(c))
            {
                sb.append(" ");  
                //sb.append("<?linebreak?>");
            }
            sb.append(c);
        }
        
        return sb.toString().trim();
    }
    
    
    private List<String> HumpDivided(String str)
    {        
        String[] tmp = SpaceHump(str).split(" ");
        
        List<String> items; 
        items = Arrays.asList(tmp);
        
        return items;
    }
    
    //=======================================================================
    
    public void ToFile(String path, String name) {
        //base.WriteToFile(path, name);
        base.AsFile(path, name);
    }

    
    
}
