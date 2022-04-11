/* @author : Boshen Zhang
 * @version 4.0
 * Class Floor represents a floor in a building
 */
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.*;
import data_structure.*;

public class Floor{

    private Scheduler scheduler;
    private RequestMsg requestMsg;
    private ArrivalMessage arrivalMessage;
    private ArrayList<String> info = new ArrayList<String>();
    private long startTime;
    //private static final int SCH_PORT = 23;
    private Timer t = new Timer();


    DatagramSocket sendSchedulerSocket;


    /**
     * Constructor for floor
     * @param
     */
    public Floor() {

//        //this.requestMsg = requestMsg;
//        //this.arrivalMessage = arrivalMessage;
//
        startTime = System.currentTimeMillis();

        try {
            sendSchedulerSocket = new DatagramSocket();

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * sendPacket sends messages to scheduler via UDP
     * @param array
     * @param len
     * @param destAddress
     * @param port
     * @param socket
     */
    public static void sendPacket(byte[]array, int len, InetAddress destAddress, int port, DatagramSocket socket){

        DatagramPacket packet = new DatagramPacket(array, len, destAddress, port);
        System.out.println("The Floor is sending a request:");
        System.out.println("From host: " + packet.getAddress());
        System.out.println("Destination host port: " + packet.getPort());

        try{
            socket.send(packet);
        } catch (IOException ie){
            ie.printStackTrace();
            System.exit(1);
        }
        System.out.println("floor reports to scheduler");
        System.out.print("Containing request: ");
        System.out.println(Arrays.toString(packet.getData()));
    }



    public void readFile2() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.getClass().getClassLoader().getResource("input").getFile()));
        String line;
        while ((line=reader.readLine())!=null){
            System.out.println("line is "+line);

            String[] message = line.split(" ");
            String time = message[0];
            String[] hms = time.split(":");
            //delay in second
            int delay = Integer.parseInt(hms[0])*3600+Integer.parseInt(hms[1])*60
                    +Integer.parseInt(hms[2]);
            TimerTask req = new TimerTask() {

                RequestMsg msg = new RequestMsg(Integer.parseInt(message[1]), message[2], Integer.parseInt(message[3]));

                @Override
                public void run() {
                    sendOutRequest(msg);
                }

                private void sendOutRequest(RequestMsg msg) {
                    System.out.println(msg.toString());
                    byte[] msgByte = msg.encode();
                    System.out.println(Arrays.toString(msgByte));
                    try {
                        sendPacket(msgByte, msgByte.length, InetAddress.getLocalHost(),Scheduler.RECEIVEFLOORPORT, sendSchedulerSocket);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            };

            t.schedule(req,delay*1000);
        }
    }


    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        Floor floor=new Floor();
        try {
            floor.readFile2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}