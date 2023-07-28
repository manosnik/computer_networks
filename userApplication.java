import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.*;

public class userApplication {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket();
        String str = "E9801";
        InetAddress ip = InetAddress.getByName("155.207.18.208");
        String message = "";
        DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 38038);
        ds.send(dp);

        File delay=new File("delay.txt");
        FileOutputStream in1=new FileOutputStream(delay,true);
        FileWriter fw1=new FileWriter(delay);
        PrintWriter pw1=new PrintWriter(fw1);

        File throughput_8=new File("throughput8.txt");
        FileOutputStream in2=new FileOutputStream(throughput_8,true);
        FileWriter fw2=new FileWriter(throughput_8);
        PrintWriter pw2=new PrintWriter(fw2);

        File throughput_16=new File("throughput16.txt");
        FileOutputStream in3=new FileOutputStream(throughput_16,true);
        FileWriter fw3=new FileWriter(throughput_16);
        PrintWriter pw3=new PrintWriter(fw3);

        File throughput_32=new File("throughput32.txt");
        FileOutputStream in4=new FileOutputStream(throughput_32,true);
        FileWriter fw4=new FileWriter(throughput_32);
        PrintWriter pw4=new PrintWriter(fw4);

        long[][] dataForThroughput=new long[10000][3];
        int i=0;

        DatagramSocket rr = new DatagramSocket(48038);
        rr.setSoTimeout(8000);
        byte[] b = new byte[2048];
        DatagramPacket rec = new DatagramPacket(b, b.length);

        long startTime, sentingTime, receivingTime;
        startTime = System.currentTimeMillis();


        while ((System.currentTimeMillis() - startTime) < 240000) {
            ds.send(dp);
            sentingTime = System.currentTimeMillis();


                try {
                    rr.receive(rec);
                    receivingTime = System.currentTimeMillis();
                    message = new String(b, 0, rec.getLength());
                    System.out.println(message);
                    // System.out.println(receivingTime - sentingTime);
                    pw1.println(receivingTime-sentingTime);

                    dataForThroughput[i][0]=sentingTime-startTime;
                    System.out.print(dataForThroughput[i][0]+"___");
                    dataForThroughput[i][1]=receivingTime-startTime;
                    System.out.println(dataForThroughput[i][1]);
                    dataForThroughput[i][2]=receivingTime-sentingTime;
                    i++;


                } catch (Exception x) {
                    System.out.println(x);
                }
        }

        int n=0;
        for (long sec=0;sec<=240000-8000;sec=sec+1000){
            for (int j=0;j< dataForThroughput.length;j++){
                if(sec<dataForThroughput[j][0] & dataForThroughput[j][1]<sec+8000){
                    n++;
                }
            }
            System.out.println((float)n/8);
            pw2.println((float)n/8);
            n=0;
        }

        for (long sec=0;sec<=240000-16000;sec=sec+1000){
            for (int j=0;j< dataForThroughput.length;j++){
                if(sec<dataForThroughput[j][0] & dataForThroughput[j][1]<sec+16000){
                    n++;
                }
            }
            System.out.println((float)n/16);
            pw3.println((float)n/16);
            n=0;
        }

        for (long sec=0;sec<=240000-32000;sec=sec+1000){
            for (int j=0;j< dataForThroughput.length;j++){
                if(sec<dataForThroughput[j][0] & dataForThroughput[j][1]<sec+32000){
                    n++;
                }
            }
            System.out.println((float)n/32);
            pw4.println((float)n/32);
            n=0;
        }

    fw1.close();
    fw2.close();
    fw3.close();
    fw4.close();

    double a=0.875;
    double bb=0.75;
    double c=2;
    double[] SRTT=new double[i];
    double[] s=new double[i];
    double[] RTO=new double[i];

        File srtt=new File("srtt.txt");
        FileOutputStream in5=new FileOutputStream(srtt,true);
        FileWriter fw5=new FileWriter(srtt);
        PrintWriter pw5=new PrintWriter(fw5);

        File sigma=new File("sigma.txt");
        FileOutputStream in6=new FileOutputStream(sigma,true);
        FileWriter fw6=new FileWriter(sigma);
        PrintWriter pw6=new PrintWriter(fw6);

        File rto=new File("rto.txt");
        FileOutputStream in7=new FileOutputStream(rto,true);
        FileWriter fw7=new FileWriter(rto);
        PrintWriter pw7=new PrintWriter(fw7);


    SRTT[0]=(1-a)*dataForThroughput[0][2];
    s[0]=(1-bb)*Math.abs(SRTT[0]-dataForThroughput[0][2]);
    RTO[0]=SRTT[0]+c*s[0];

    pw5.println(SRTT[0]);
    pw6.println(s[0]);
    pw7.println(RTO[0]);

    for(int k=1;k<i;k++){
        SRTT[k]=a*SRTT[k-1]+(1-a)*dataForThroughput[k][2];
        s[k]=bb*s[k-1]+ (1-bb)*Math.abs(SRTT[k]-dataForThroughput[k][2]);
        RTO[k]=SRTT[k]+c*s[k];

        pw5.println(SRTT[k]);
        pw6.println(s[k]);
        pw7.println(RTO[k]);
    }

        fw5.close();
        fw6.close();
        fw7.close();

    }


}
