/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learnerati.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author Ben
 */
public class DocBuilder {

    Document doc;
    public Element root;
    public XMLBase base;
    Element config;
    Element cover;
    Element coverabstract;
    
    public XMLBase Base() { return this.base; }

    public DocBuilder(XMLBase base) {
        doc = base.doc;
        root = base.doc.getDocumentElement();
        this.base = base;

        config = doc.createElement("config");
        cover = doc.createElement("cover");
        coverabstract = doc.createElement("abstract");

        root.appendChild(config);
        root.appendChild(cover);

        cover.appendChild(coverabstract);
    }

    public void SetConfig(Map<String, String> options) {
        for (Iterator<Map.Entry<String, String>> it = options.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> configItem = it.next();

            Element item = doc.createElement(configItem.getKey());
            item.appendChild(doc.createTextNode(configItem.getValue()));
            config.appendChild(item);
        }
    }

    public void SetCover(Map<String, String> options) {
        for (Iterator<Map.Entry<String, String>> it = options.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> configItem = it.next();

            Element item = doc.createElement(configItem.getKey());
            item.appendChild(doc.createTextNode(configItem.getValue()));
            cover.appendChild(item);
        }
    }

    public void SetAbstract(Map<String, String> options) {
        for (Iterator<Map.Entry<String, String>> it = options.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> configItem = it.next();

            //System.out.println(configItem.getKey() + " = " + configItem.getValue());

            Element item = doc.createElement(configItem.getKey());

            if (!configItem.getKey().equals("body")) {
                item.appendChild(doc.createTextNode(configItem.getValue()));
            } else {
                DocumentFragment frag = base.ImportFragmentString(configItem.getValue());
                item.appendChild(frag);
            }
            
            coverabstract.appendChild(item);
        }
    }

    public void AddElement(Element elem) {
        root.appendChild(elem);
    }

    public Element CreateSection(String str) {
        Element section = doc.createElement("section");
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(str));

        section.appendChild(title);

        return section;
    }
    
    public Element CreateSection(String str, Map<String, String> attrs) {
        Element section = doc.createElement("section");
        
        if (attrs != null)
        {
            for (Iterator iter = attrs.keySet().iterator(); iter.hasNext();)
            {
                String name = iter.next().toString();
                Object value = attrs.get(name);

                section.setAttributeNS(null, name, value.toString());                 
            }
        } 
        
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(str));

        section.appendChild(title);

        return section;
    }

    public DocumentFragment PullExternal(String fileName) {

        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");

        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append(NL);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocBuilder.class.getName()).log(Level.SEVERE, null, ex);
            
            text.append("ERROR: File not found = " + fileName);
        }

        return base.ImportFragmentString(text.toString());
    }
    
    
    
    public String loadAsString(String fileName) {

        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");

        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append(NL);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        String str = text.toString();
        str = str.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        
        return str;
    }    
    
    
    public void ToFile(String path, String name) {
        base.AsFile(path, name);
    }
}
