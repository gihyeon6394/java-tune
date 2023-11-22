package sample.thirteen;

import org.openjdk.jmh.annotations.*;
import org.tune.thirteen.ParseSAX;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

}
