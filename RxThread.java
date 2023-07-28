import javax.sound.sampled.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RxThread extends Thread {

    AudioFormat audioFormat;
    SourceDataLine sourceDataLine;
    int bNumber=132;

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

    public  byte[] dpcmDeconder(byte[][] audioData2d,int p){

       try {
           File differencesDPCM = new File("differencesDPCM.txt");
           FileOutputStream in1 = new FileOutputStream(differencesDPCM, true);
           FileWriter fw1 = new FileWriter(differencesDPCM);
           PrintWriter pw1 = new PrintWriter(fw1);

           File samplesDPCM = new File("samplesDPCM.txt");
           FileOutputStream in2 = new FileOutputStream(samplesDPCM, true);
           FileWriter fw2 = new FileWriter(samplesDPCM);
           PrintWriter pw2 = new PrintWriter(fw2);


        byte cellInDecode;
        int nibble1;
        int nibble2;
        int x0=0;
        int[] nibbles=new int[audioData2d[0].length*2*audioData2d.length];
        int[] diafores=new int[nibbles.length];
        byte [] audioDataDecoder=new byte[diafores.length];

        for(int d=0;d<audioData2d[0].length;d++){
            for (int g = 0; g < 128; g++) {
                System.out.print(audioData2d[d][g]);
            }
            System.out.println("");

        }


        for (int i=0;i<p;i++){
            for(int j=0;j<audioData2d[0].length;j++){
                cellInDecode= audioData2d[i][j];
                nibble2=cellInDecode&15;
                nibble1=(cellInDecode>>>4)&15;
                nibbles[2*j+(i*2*audioData2d[0].length)]=nibble1;
                nibbles[2*j+1+(i*2*audioData2d[0].length)]=nibble2;
            }

        }
        System.out.println("NIBBLES");
        for (int g = 0; g < nibbles.length; g++) {
            System.out.print(nibbles[g]);
        }

        for(int k=0;k<diafores.length;k++){
            diafores[k]=nibbles[k]-8;
            pw1.println(diafores[k]);
        }
        audioDataDecoder[0]=(byte)x0;

        for(int r=1;r<audioDataDecoder.length;r++){
            audioDataDecoder[r]=(byte)((diafores[r-1])+audioDataDecoder[r-1]);
            if(audioDataDecoder[r]<-120){
                audioDataDecoder[r]=-116;
            }
            if(audioDataDecoder[r]>120){
                audioDataDecoder[r]=116;
            }
            pw2.println(audioDataDecoder[r]);
        }

        fw1.close();
        fw2.close();

        return audioDataDecoder;

       }catch(IOException ioException){
        System.out.println(ioException);
    }
       byte[] error=new byte[3];
       return error;

}

    public byte[] aqDpcmDecoder(byte[][] audioData2d,int p){

        try{

            File differencesAQDPCM = new File("differencesAQDPCM.txt");
            FileOutputStream in1 = new FileOutputStream(differencesAQDPCM, true);
            FileWriter fw1 = new FileWriter(differencesAQDPCM);
            PrintWriter pw1 = new PrintWriter(fw1);

            File samplesAQDPCM = new File("samplesAQDPCM.txt");
            FileOutputStream in2 = new FileOutputStream(samplesAQDPCM, true);
            FileWriter fw2 = new FileWriter(samplesAQDPCM);
            PrintWriter pw2 = new PrintWriter(fw2);

            File mean=new File("mean.txt");
            FileOutputStream in3=new FileOutputStream(mean,true);
            FileWriter fw3=new FileWriter(mean);
            PrintWriter pw3=new PrintWriter(fw3);

            File bita=new File("bita.txt");
            FileOutputStream in4=new FileOutputStream(bita,true);
            FileWriter fw4=new FileWriter(bita);
            PrintWriter pw4=new PrintWriter(fw4);


        p++;
        System.out.println(p);
        int[] m=new int[p];
        int[] b=new int[p];


        int cellInDecode;
        int nibble1;
        int nibble2;

        int[][] nibbles=new int[p][(audioData2d[0].length-4)*2];
        byte[] audioDataDecoder=new byte[2*128*2*p];


        for (int i=0;i<p;i++) {
            m[i]=(int)((audioData2d[i][1]<<8)|(audioData2d[i][0]&0xFF));
            b[i]=(int)((audioData2d[i][3]<<8)|(audioData2d[i][2]&0xFF));

            pw3.println(m[i]);
            pw4.println(b[i]);
            //m[i] = 256 * audioData2d[i][1] + audioData2d[i][0];
            //b[i] = 256 * audioData2d[i][3] + audioData2d[i][2];
            System.out.println(m[i] + "__" + b[i]);

            for (int j = 4; j < audioData2d[0].length; j++) {
                cellInDecode = (int) audioData2d[i][j];
                nibble2 = cellInDecode & 15;
                nibble1 = (cellInDecode >>> 4) & 15;
                nibbles[i][2 * (j - 4) ] = nibble1;
                nibbles[i][2 * (j - 4) + 1] = nibble2;
            }
        } for(int i=0;i<1;i++) {
            for (int j = 0; j < nibbles[0].length; j++) {
               System.out.print(nibbles[i][j]);
                System.out.print("___");

            }
            System.out.println();
        }

       for(int i=0;i<nibbles.length;i++) {
           for (int j = 0; j < nibbles[0].length; j++) {
               nibbles[i][j] = (nibbles[i][j] - 8) * b[i];
               pw1.println(nibbles[i][j]);


           }
       }for(int i=0;i<1;i++) {
            for (int j = 0; j < nibbles[0].length; j++) {
                System.out.print(nibbles[i][j]);
                System.out.print("___");


            }
            System.out.println("");
        }



       for(int i=0;i<nibbles.length;i++) {
            for (int j = 0; j < nibbles[0].length; j++) {
                if(j==0&i>0){
                    nibbles[i][j]+=nibbles[i-1][nibbles[0].length-1];
                }
                if (j > 0) {
                    nibbles[i][j] += nibbles[i][j - 1];
                }
                if(nibbles[i][j]<-32700){
                    nibbles[i][j]=-32600;
                }
                if(nibbles[i][j]>32700){
                    nibbles[i][j]=32600;
                }
                pw2.println(nibbles[i][j]);
            }

            }for(int i=0;i<1;i++) {
            for (int j = 0; j < nibbles[0].length; j++) {
                System.out.print(nibbles[i][j]);
                System.out.print("___");


            }
            System.out.println("");
        }

        for(int i=0;i<nibbles.length;i++) {
            for (int j = 0; j < nibbles[0].length; j++) {
                nibbles[i][j] += m[i];
            }
        }for(int i=0;i<1;i++) {
            for (int j = 0; j < nibbles[0].length; j++) {
                System.out.print(nibbles[i][j]);
                System.out.print("___");


            }
            System.out.println("");
        }



       int count=0;
       for(int i=0;i<nibbles.length;i++){
           for(int j=0;j<nibbles[0].length;j++){
               audioDataDecoder[count]=(byte)nibbles[i][j];
               count++;
               audioDataDecoder[count]=(byte)(nibbles[i][j]>>>8);
               count++;
           }
       }


            fw1.close();
            fw2.close();
            fw3.close();
            fw4.close();

     return audioDataDecoder;

        }catch(IOException ioException){
            System.out.println(ioException);
        }
        byte[] error=new byte[3];
        return error;

    }

    @Override
    public void run() {

            byte[] audioData;
            int p=-1;
            byte[] b = new byte[bNumber];
            byte[] test;
            byte[][] buf = new byte[1000][bNumber];

           try {
               DatagramSocket rr = new DatagramSocket(48038);
               rr.setSoTimeout(8000);

               while (true) {

                   DatagramPacket rec = new DatagramPacket(b, b.length);
                   rr.receive(rec);
                   p++;

                   audioData = rec.getData();


                   for (int i = 0; i < audioData.length; i++) {
                       buf[p][i] = audioData[i];
                   }



               }
           }catch (SocketException sE){
               System.out.println(sE);
           }catch (IOException ioE){
               System.out.println(ioE);
           }

                test=aqDpcmDecoder(buf,p);

                try{


                    InputStream byteInputStream=new ByteArrayInputStream(test);

                    audioFormat=getAudioFormat();
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                    sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                    sourceDataLine.open(audioFormat);
                    sourceDataLine.start();


                    sourceDataLine.write(test,0,test.length);
                    sourceDataLine.close();

                }catch(LineUnavailableException lue){
                    System.out.println(lue);
                }
            }


        }
