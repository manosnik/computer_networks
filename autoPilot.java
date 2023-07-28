import java.io.*;
import java.net.*;
import java.util.Random;

public class autoPilot {
    public static void main(String[] args) throws Exception {
        String str1 = "AUTO FLIGHTLEVEL=065 LMOTOR=150 RMOTOR=150 PILOT\r\n";
        String str2 = "GET /netlab/hello.html HTTP/1.0\r\nHost: ithaki.eng.auth.gr:80\r\n\r\n";
        String level;
        String mtr;
        int desireLevel=276;
        String serverResponse;

        Socket s = new Socket("155.207.18.208", 38048);
        s.setSoTimeout(8000);
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        PrintWriter writer=new PrintWriter(s.getOutputStream(),true);

        long c_time,p_time,s_time;
        p_time=System.currentTimeMillis();
        s_time=System.currentTimeMillis();

        Random rMotor=new Random();
        Random rFlightLevel=new Random();
        int motor;
        int flightLevel;
        for(int i=0;i<15;i++) {
            writer.println(str1);
            serverResponse = reader.readLine();
            System.out.println(serverResponse);
        }
        while((System.currentTimeMillis()-p_time)<180000){
            writer.println(str1);
            serverResponse=reader.readLine();
            System.out.println(serverResponse);
            level=serverResponse.substring(44,47);
            System.out.println(level);
            flightLevel=Integer.parseInt(level);
            mtr=serverResponse.substring(20,23);
            motor=Integer.parseInt(mtr);
            System.out.println(motor);
            if(flightLevel<desireLevel){
                if(desireLevel-flightLevel>100) {
                    str1 = "AUTO FLIGHTLEVEL=" + String.valueOf(desireLevel) + " LMOTOR=" + String.valueOf(motor + 20) + " RMOTOR=" + String.valueOf(motor + 20) + " PILOT\r\n";
                }if(desireLevel-flightLevel<100&desireLevel-flightLevel>50) {
                    str1 = "AUTO FLIGHTLEVEL=" + String.valueOf(desireLevel) + " LMOTOR=" + String.valueOf(motor + 10) + " RMOTOR=" + String.valueOf(motor + 10) + " PILOT\r\n";
                }if(desireLevel-flightLevel<50) {
                    str1 = "AUTO FLIGHTLEVEL=" + String.valueOf(desireLevel) + " LMOTOR=" + String.valueOf(150) + " RMOTOR=" + String.valueOf(150) + " PILOT\r\n";
                }

            } else if(flightLevel>desireLevel){
                str1="AUTO FLIGHTLEVEL="+String.valueOf(desireLevel)+" LMOTOR="+String.valueOf(150)+" RMOTOR="+String.valueOf(150)+" PILOT\r\n";
            }
            /*if((System.currentTimeMillis()-s_time>20000)){
                s_time=System.currentTimeMillis();
                motor=rMotor.nextInt(50)+150;
                flightLevel=rFlightLevel.nextInt(350)+65;
                str1="AUTO FLIGHTLEVEL="+String.valueOf(motor)+" LMOTOR="+String.valueOf(motor)+" RMOTOR="+String.valueOf(flightLevel)+" PILOT\r\n";
            }*/
        }

        out.close();
        s.close();
    }
}