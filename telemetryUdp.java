import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class telemetryUdp {
    public static void main(String[] args)throws Exception {

        File motor=new File("motor.txt");
        FileOutputStream in1=new FileOutputStream(motor,true);
        FileWriter fw1=new FileWriter(motor);
        PrintWriter pw1=new PrintWriter(fw1);

        File altitude=new File("altitude.txt");
        FileOutputStream in2=new FileOutputStream(altitude,true);
        FileWriter fw2=new FileWriter(altitude);
        PrintWriter pw2=new PrintWriter(fw2);

        File temperature=new File("temperature.txt");
        FileOutputStream in3=new FileOutputStream(temperature,true);
        FileWriter fw3=new FileWriter(temperature);
        PrintWriter pw3=new PrintWriter(fw3);

        File pressure=new File("pressure.txt");
        FileOutputStream in4=new FileOutputStream(pressure,true);
        FileWriter fw4=new FileWriter(pressure);
        PrintWriter pw4=new PrintWriter(fw4);

       long duration=100000;
       long startTime;
       byte[] buffer=new byte[1000];
       String str;
       String x1,x2,x3,x4;

        try {
           DatagramSocket ds = new DatagramSocket(48078);
           ds.setSoTimeout(2000);
           DatagramPacket dp = new DatagramPacket(buffer,buffer.length);


        startTime=System.currentTimeMillis();

        while (System.currentTimeMillis()-startTime<duration){
            try{
                ds.receive(dp);
                str=new String(buffer,0, dp.getLength());
                System.out.println(str);

                x1=str.substring(51,54);
                x2=str.substring(64,67);
                x3=str.substring(80,86);
                x4=str.substring(96,103);

                pw1.println(x1);
                pw2.println(x2);
                pw3.println(x3);
                pw4.println(x4);

            }catch(IOException a){
                System.out.println("timeOut");
            }
        }
    }catch (SocketException se){
        System.out.println(se);
    }
        fw1.close();
        fw2.close();
        fw3.close();
        fw4.close();
    }

}
