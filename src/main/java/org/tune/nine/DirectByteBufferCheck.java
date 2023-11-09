package org.tune.nine;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.FileSystem;

/**
 * getDirectBuffer()를 지속적으로 호출하는 클래스
 */
public class DirectByteBufferCheck {

    public static void main(String[] args) {
        DirectByteBufferCheck check = new DirectByteBufferCheck();
        for (int i = 1; i < 1024000; i++) {

            check.getDirectBuffer();

            if (i % 100 == 0) {
                System.out.println("i = " + i);
            }
        }
    }

    public ByteBuffer getDirectBuffer() {
        ByteBuffer buffer;
        buffer = ByteBuffer.allocateDirect(1024 * 1024);
        return buffer;
    }
}
