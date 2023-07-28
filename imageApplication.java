import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class imageApplication {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket();
        String str = "M5220 CAM=FIX";
        InetAddress ip = InetAddress.getByName("155.207.18.208");
       //String message = "";
        DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 38038);
        ds.send(dp);

        DatagramSocket rr = new DatagramSocket(48038);
        rr.setSoTimeout(80000);
        byte[] b = new byte[128];
        byte[] im=new byte[1000000];
        int j=0;
        DatagramPacket rec = new DatagramPacket(b, b.length);
        for (int k=0;k<5000;k++ ) {
            try {
                // ds.send(dp);
                rr.receive(rec);
                //im.=rec.getData();
                if(rec.getLength()>0) {
                    for (int i = 0; i < rec.getLength(); i++) {
                        im[j + i] = rec.getData()[i];
                    }
                    j += rec.getLength();
                    if (rec.getLength() < 128) {
                        break;
                    }
                }
            } catch (Exception x) {
                System.out.println(x);
                break;
            }


        }//https://www.tutorialspoint.com/How-to-convert-Byte-Array-to-Image-in-java
        try {
            InputStream in = new ByteArrayInputStream(im, 0, im.length);
            BufferedImage bi = ImageIO.read(in);
            File outputFile = new File("saved.png");
            ImageIO.write(bi, "png", outputFile);
            in.close();
        }catch (IOException e){
            System.out.println(e);
        }//https://docs.oracle.com/javase/tutorial/2d/images/saveimage.html

    }

}
