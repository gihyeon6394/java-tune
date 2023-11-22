package org.tune.thirteen;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

public class ParseSAX extends DefaultHandler {
    public HashMap<String, Integer> elMap = new HashMap<String, Integer>();
    private StringBuffer returnData = new StringBuffer();

    public ParseSAX() {

    }

    @Override
    public void startDocument() throws SAXException {
        returnData.append("Start Document\n");
    }

    @Override
    public void endDocument() throws SAXException {
        returnData.append("End Document\n");
        setNodeCountData();
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        addNode(qName);
    }

    private void addNode(String qName) {
        if (!elMap.containsKey(qName)) {
            elMap.put(qName, 1);
        } else {
            int count = elMap.get(qName);
            elMap.put(qName, ++count);
        }
    }

    private void setNodeCountData() {
        elMap.keySet().stream().forEach(key -> {
            returnData.append("Element = ").append(key)
                    .append(" Count = ").append(elMap.get(key).toString())
                    .append("<br/>");
        });
    }
}
