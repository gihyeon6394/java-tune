# 13. XML과 JSON도 잘 쓰자

- 자바에서 사용하는 XML 파서의 종류는?
- SAX 파서는 어떻게 사용할까?
- DOM 파서는 어떻게 사용할까?
- XML 파서가 문제가 된 사례
- JSON과 파서들
- 데이터 전송을 빠르게 하는 라이브러리 소개

---

## 자바에서 사용하는 XML 파서의 종류는?

- XML : eXtensible Markup Language
- XML은 parsing 과정이 필요
- parsing : XML 문서를 읽어서 의미를 분석하는 과정

| 이름   | 의미                                             | 패키지                   | 특징                                                                        |
|------|------------------------------------------------|-----------------------|---------------------------------------------------------------------------|
| JAXP | Java API for XML Processing                    | `javax.xml.parsers`   | SAX, DOM, XSLT를 표준화한 API <br/>SAXParserFactory, DocumentBuilderFactory 제공 |
| SAX  | Simple API for XML                             | `org.xml.sax`         | 순차적으로 XML 처리                                                              |
| DOM  | Document Object Model                          | `org.w3c.dom`         | 모든 XML 문서를 메모리에 로딩하고, 트리 구조로 관리, 처리                                       |                                           
| XSLT | eXtensible Stylesheet Language Transformations | `javax.xml.transform` | XML이 화면에 잘 보이도록 처리                                                        |

## SAX 파서는 어떻게 사용할까?

- 순차적 이벤트 처리
- `SAXParserFactory` : SAX 파서 객체를 생성하는 추상 클래스
- `SAXParser` : 여러 종류의 `parse()` 메소드를 제공
- `DefaultHandler` : `ConentHandler`, `DTDHandler`, `EntityResolver`, `ErrorHandler` 구현체
- `ContentHandler` : XML 문서를 읽어서 이벤트를 발생시키는 인터페이스
    - `startDocument()`, `endDocument()`, `startElement()`, `endElement()`, `characters()`
- `ErrorHandler` : XML 문서를 읽는 도중에 에러가 발생하면 호출되는 인터페이스
- `DTDHandler` : DTD를 처리하는 인터페이스
- `EntityResolver` : URI를 통해 식별

```java

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class XMLParser {


    @Benchmark
    public void withSAXParse100() throws ParserConfigurationException, SAXException, IOException {
        ParseSAX handler = new ParseSAX();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse("src/jmh/resources/100.xml", handler);
    }

    @Benchmark
    public void withSAXParse1000() throws ParserConfigurationException, SAXException, IOException {
        ParseSAX handler = new ParseSAX();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse("src/jmh/resources/1000.xml", handler);
    }
}
```

````
Benchmark            Mode  Cnt   Score    Error  Units
withSAXParse100      avgt   10    847 ±  0.001  ms/op
withSAXParse1000     avgt   10   3,925 ±  0.001  ms/op
````

- 10배 많아져도 시간이 10배 늘어나지 않음

## DOM 파서는 어떻게 사용할까?

- `DocumentBuilderFactory` : DOM 파서 객체를 생성하는 추상 클래스
- `DocumentBuilder` : 여러 종류의 `parse()` 메소드를 제공
- `Document` : XML 문서를 메모리에 로딩한 후 트리 구조로 관리
- `Node` : `Document`의 최상위 노드

```java
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

```

````
@Benchmark
public void withDOMParse100() throws ParserConfigurationException, SAXException, IOException {
    ParseSAX handler = new ParseSAX();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();
    saxParser.parse("src/jmh/resources/100.xml", handler);
}

@Benchmark
public void withDOMParse1000() throws ParserConfigurationException, SAXException, IOException {
    ParseSAX handler = new ParseSAX();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();
    saxParser.parse("src/jmh/resources/1000.xml", handler);
}
````

````
Benchmark            Mode  Cnt   Score    Error  Units
withDOMParse100      avgt   10    1,395 ±  0.001  ms/op
withDOMParse1000     avgt   10   7,129 ±  0.001  ms/op
````

- 10배 많아져도 시간이 10배 늘어남
- CPU 시간이 DOM이 더 많이 사용함
- XML 파서에서 CPU 성능은 매우 중요함
- DOM 파서를 사용하면, 메모리를 많이 사용하므로 OutOfMemoryError가 발생할 수 있음

## XML 파서가 문제가 된 사례

- OutOfMemoryError
- XML 처리를 위해 많은 메모리 소모
- 특정 WAS의 파서는 메모리 누수가 발생할 수 있음 (WAS 벤더사에 따라 다름)
- Apache SAX parser 권장

## JSON과 파서들

- JSON : JavaScript Object Notation
- 자주 쓰이는 JSON parser : Jackson JSON, Google gson
- Serialize, Deserialize 성능이 XML 대비 별로임 , 파싱 시간은 비슷
- 데이터 전송을 위한 전용 라이브러리 추천

## 데이터 전송을 빠르게 하는 라이브러리 소개

- https://github.com/eishay/jvm-serializers/wiki
- Google protobuf, Apache Thrift, Apache Avro

## 정리

- XML, JSON으로 데이터를 주고받으면 파싱하는 시간이 오래걸림
- 파싱하는 시간 떄문에 CPU, Memory 사용량이 많아짐
