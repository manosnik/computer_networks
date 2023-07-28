import java.io.IOException;
import java.net.*;

public class audioApplication {


    public static void main(String[] args) throws Exception {
       // echoing();
        DatagramSocket ds = new DatagramSocket();
        String str = "A2610 AQ F777";
        InetAddress ip = InetAddress.getByName("155.207.18.208");
        DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 38038);
        ds.send(dp);

        RxThread a=new RxThread();
        a.run();
    }
}
