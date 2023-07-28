import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;

public class PlayThread extends Thread{
    private byte[] elasticBuffer ;
    InputStream inputStream;
    SourceDataLine sourceDataLine;

    PlayThread( InputStream inputStream,SourceDataLine sourceDataLine){
        elasticBuffer=new byte[10000];
        this.inputStream=inputStream;
        this.sourceDataLine=sourceDataLine;
    }

    public byte[] getElasticBuffer() {
        return elasticBuffer;
    }
    public void setElasticBuffer(byte[] elasticBuffer) {
        this.elasticBuffer = elasticBuffer;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public SourceDataLine getSourceDataLine() {
        return sourceDataLine;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public void setSourceDataLine(SourceDataLine sourceDataLine) {
        this.sourceDataLine = sourceDataLine;
    }

    @Override
    public void run() {
        try {
            int cnt;
            while ((cnt = inputStream.read(elasticBuffer, 0, elasticBuffer.length)) != -1) {
                if (cnt > 0) {
                    sourceDataLine.write(elasticBuffer, 0, cnt);
                }
            }
            //  sourceLine.drain();
            // sourceLine.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    }

