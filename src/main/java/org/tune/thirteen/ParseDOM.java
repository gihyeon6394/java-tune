package org.tune.thirteen;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class ParseDOM {
    HashMap<String, String> elMap = new HashMap<>();
    private StringBuffer returnData = new StringBuffer();

    public void parseDOM(String xmlName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlName);

            Node rootNode = document.getChildNodes().item(0);
            addNode(rootNode.getNodeName());
            readNodes(rootNode);
            setNodeCountData();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }


    }

    private void readNodes(Node rootNode) {
        NodeList childs = rootNode.getChildNodes();
        int childCnt = childs.getLength();
        for (int i = 0; i < childCnt; i++) {
            Node node = childs.item(i);

            if (node.hasChildNodes()) {
                readNodes(node);
            }

            String nodeName = node.getNodeName();
            if (!nodeName.equals("#comment") && !nodeName.equals("#text")) {
                addNode(nodeName);
            }
        }
    }

    private void addNode(String nodeName) {
        if (!elMap.containsKey(nodeName)) {
            elMap.put(nodeName, "1");
        } else {
            elMap.put(nodeName, String.valueOf(Integer.parseInt(elMap.get(nodeName)) + 1));
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
