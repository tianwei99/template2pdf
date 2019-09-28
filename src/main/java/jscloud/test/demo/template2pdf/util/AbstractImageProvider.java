package jscloud.test.demo.template2pdf.util;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;

import java.io.IOException;

public class AbstractImageProvider implements ImageProvider {
     @Override
     public Image retrieve(String src) {
         int pos = src.indexOf("base64,");
         try {
             if (src.startsWith("data") && pos > 0) {
                 byte[] img = Base64.decode(src.substring(pos + 7));
                 return Image.getInstance(img);
             } else if (src.startsWith("http")) {
                 return Image.getInstance(src);
             }
         } catch (BadElementException ex) {
             return null;
         } catch (IOException ex) {
             return null;
         }
         return null;
     }
     @Override
     public String getImageRootPath() {
         return null;
     }

             @Override
     public void store(String s, Image image) {

     }

             @Override
     public void reset() {

     }
 }
