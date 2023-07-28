import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class vehicleDiagnostics {
    private static void sender(OutputStream out, String strIn) {

        byte[] b  = strIn.getBytes();

        try {
            out.write(b);
        }
        catch ( IOException ioe ) {

            System.out.println(ioe);
        }
    }

    private static String receiver ( InputStream in ) {

        int i;
        String strOut= "";
        for ( ;; ) {
            try {
                i = in.read();
                if ( ( i ==-1 ) || ( i == 13 ) )
                    return strOut;
                strOut+= (char) i;
            }
            catch ( IOException ioe ) {
                System.out.println(ioe);
                return "ioe";
            }
        }
    }
    public static void main(String[] args) throws Exception {

            String code1 ="01 1F\r";
            String code2 ="01 0F\r";
            String code3 ="01 11\r";
            String code4 ="01 0C\r";
            String code5 ="01 0D\r";
            String code6 ="01 05\r";
            String serverResponse;
            int f;
            String xx,yy;

        File engineRunTime=new File("engineRunTime.txt");
        FileOutputStream in1=new FileOutputStream(engineRunTime,true);
        FileWriter fw1=new FileWriter(engineRunTime);
        PrintWriter pw1=new PrintWriter(fw1);

        File inTakeAirTemperature=new File("inTakeAirTemperature.txt");
        FileOutputStream in2=new FileOutputStream(inTakeAirTemperature,true);
        FileWriter fw2=new FileWriter(inTakeAirTemperature);
        PrintWriter pw2=new PrintWriter(fw2);

        File throttlePosition=new File("throttlePosition.txt");
        FileOutputStream in3=new FileOutputStream(throttlePosition,true);
        FileWriter fw3=new FileWriter(throttlePosition);
        PrintWriter pw3=new PrintWriter(fw3);

        File engineRPM=new File("engineRPM.txt");
        FileOutputStream in4=new FileOutputStream(engineRPM,true);
        FileWriter fw4=new FileWriter(engineRPM);
        PrintWriter pw4=new PrintWriter(fw4);

        File vehicleSpeed=new File("vehicleSpeed.txt");
        FileOutputStream in5=new FileOutputStream(vehicleSpeed,true);
        FileWriter fw5=new FileWriter(vehicleSpeed);
        PrintWriter pw5=new PrintWriter(fw5);

        File coolantTemperature=new File("coolantTemperature.txt");
        FileOutputStream in6=new FileOutputStream(coolantTemperature,true);
        FileWriter fw6=new FileWriter(coolantTemperature);
        PrintWriter pw6=new PrintWriter(fw6);

            Socket s = new Socket("155.207.18.208", 29078);
            s.setSoTimeout(8000);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            long p_time = System.currentTimeMillis();

        while ((System.currentTimeMillis() - p_time)<240000) {
            sender(s.getOutputStream(),code1);
            serverResponse=receiver(s.getInputStream());
            System.out.println(serverResponse);
            xx=serverResponse.substring(6,8);
            System.out.println(xx);
            yy=serverResponse.substring(9,11);
            System.out.println(yy);
            f=256*Integer.parseInt(xx,16)+Integer.parseInt(yy,16);
            System.out.println(f);
            pw1.println(f);

            sender(s.getOutputStream(),code2);
            serverResponse=receiver(s.getInputStream());
            xx=serverResponse.substring(6,8);
            f=Integer.parseInt(xx,16)-40;
            pw2.println(f+"Celsius");

            sender(s.getOutputStream(),code3);
            serverResponse=receiver(s.getInputStream());
            xx=serverResponse.substring(6,8);
            f=Integer.parseInt(xx,16)*100/255;
            pw3.println(f+"%");

            sender(s.getOutputStream(),code4);
            serverResponse=receiver(s.getInputStream());
            xx=serverResponse.substring(6,8);
            yy=serverResponse.substring(9,11);
            f=((Integer.parseInt(xx,16)*256)+Integer.parseInt(yy,16))/4;
            pw4.println(f+"RPM");

            sender(s.getOutputStream(),code5);
            serverResponse=receiver(s.getInputStream());
            xx=serverResponse.substring(6,8);
            f=Integer.parseInt(xx,16);
            pw5.println(f+"km/h");

            sender(s.getOutputStream(),code6);
            serverResponse=receiver(s.getInputStream());
            xx=serverResponse.substring(6,8);
            f=Integer.parseInt(xx,16)-40;
            pw6.println(f+"Celsius");


        }
        fw1.close();
        fw2.close();
        fw3.close();
        fw4.close();
        fw5.close();
        fw6.close();

           out.close();
           s.close();

    }

}