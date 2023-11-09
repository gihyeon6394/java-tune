package sample.nine;

import org.openjdk.jmh.annotations.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BasicIOReadUtil {

    private String fileName  = "/Users/gimgihyeon/Desktop/kghworks/dev/workspace_intellij/JavaTune/contents/tmp02.txt";

    /**
     * 문자열을 글자 하나씩 읽음
     */
    @Benchmark
    public ArrayList readCharStream() {
        ArrayList<StringBuffer> list = new ArrayList<StringBuffer>();
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            int data = 0;

            // 한줄 씩 담을 StringBuffer
            StringBuffer sb = new StringBuffer();

            // fr.read() : Reads a single character.
            while ((data = fr.read()) != -1) {
                if (data == '\n') {
                    list.add(sb);
                    sb = new StringBuffer();
                } else {
                    sb.append((char) data);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return list;
    }

    /**
     * 문자열을 버퍼 크기만큼 읽음
     */
    @Benchmark
    public String readCharStreamWithBuffer() {
        StringBuffer retSB = new StringBuffer();
        FileReader fr = null;

        try {
            fr = new FileReader(fileName);
            int bufferSize = 1024 * 1024;
            char readBuffer[] = new char[bufferSize];
            int resultSize = 0;

            // fr.read(readBuffer) : Reads characters into an array.
            while ((resultSize = fr.read(readBuffer)) != -1) {
                if (resultSize == bufferSize) {
                    retSB.append(readBuffer);
                } else {
                    // 마지막 chunk 처리
                    for (int i = 0; i < resultSize; i++) {
                        retSB.append(readBuffer[i]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return retSB.toString();

    }


    /**
     * 라인 단위로 읽음
     */
    @Benchmark
    public List<String> readBufferReader() {
        List<String> list = new ArrayList<String>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String data;


            // br.readLine() : Reads a line of text.
            while ((data = br.readLine()) != null) {
                list.add(data);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return list;
    }

}
