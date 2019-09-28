package jscloud.test.demo.template2pdf;

import jscloud.common.base.JscloudApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"jscloud.test.demo.template2pdf","jscloud.demo"})
public class Template2PDFApplication {

    public static void main(String[] args) throws IOException {
        JscloudApplication.run(Template2PDFApplication.class, args);

//        RandomAccessFile aFile = new RandomAccessFile("C:\\Users\\think\\Desktop\\ant_1.2.5\\readme.txt", "rw");
//        FileChannel inChannel = aFile.getChannel();
//
//        ByteBuffer buf = ByteBuffer.allocate(48);
//
//        int bytesRead = inChannel.read(buf);
//        while (bytesRead != -1) {
//
//            System.out.println("Read " + bytesRead);
//            buf.flip();
//
//            while(buf.hasRemaining()){
//                System.out.print((char) buf.get());
//            }
//
//            buf.clear();
//            bytesRead = inChannel.read(buf);
//        }
//        aFile.close();
    }

}
