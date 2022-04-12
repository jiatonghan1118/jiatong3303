/* @author  Boshen Zhang
 * @version 4.0
 *
 * Class Scheduler emulates/handles the scheduling of
 */

import java.net.InetAddress;
import java.util.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.*;

import data_structure.*;

public class Scheduler {
    public static final int RECEIVEFLOORPORT = 23;
    public static final int RECEIVEELEVATORPORT = 69;
    ArrayList<Integer> destiUp = new ArrayList<>();  // list of destinations above the elevator's current position.
    ArrayList<Integer> destiDown = new ArrayList<>(); // list of destinations below the elevator's current position.
    private Elevator elevator;
    private LinkedList<RequestMsg> request =new LinkedList<>();
    private byte receiveFloorBuffer[] = new byte[RequestMsg.MSGSIZE];
    private final Queue<RequestMsg> requestQueue=new LinkedList<>();
    private ArrayList<ElevatorInfo> elevatorStatus=new ArrayList<>();


    DatagramPacket floorReceivedPacket, elevatorReceivedPacket;
    DatagramSocket sendReceiveSocket, floorReceiveSocket, sendSocket;
    DatagramSocket elevatorSendSocket, elevatorReceiveSocket;


    String ELEVATOR_IP_ADDRESS = "";

    /**
     * Constructor for class Scheduler
     */
    public Scheduler() {
        RequestConsumer requestConsumer = new RequestConsumer(requestQueue,elevatorStatus,elevatorSendSocket,this);
        requestConsumer.start();

        try {
            //sendReceiveSocket = new DatagramSocket();
            elevatorSendSocket = new DatagramSocket();
            elevatorReceiveSocket = new DatagramSocket(RECEIVEELEVATORPORT);
            floorReceiveSocket = new DatagramSocket(RECEIVEFLOORPORT);

            floorReceivedPacket=new DatagramPacket(receiveFloorBuffer,receiveFloorBuffer.length);
            sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    //Not used for iteration 4
//    public synchronized void handleRequest(RequestMsg msg) {
//
//        while (!request.isEmpty()) {
//            try {
//                wait(); // wait when there is no destinations to go
//            } catch (InterruptedException e) {
//                return;
//            }
//        }
//
//        if(msg != null){
//            destiUp.add(msg.getDestination());
//        }
//        else if(msg.getMovement() == -1){
//            destiDown.add(msg.getDestination());
//        }
//        else;
//        request.add(msg);
//
//        notifyAll();
//    }

    /**
     * addElevator adds elevator to requests
     * @param elevator
     */
    public void addElevator(Elevator elevator) {
        this.elevator=elevator;
    }

    /**
     * getRequest gets request from the floors and elevators
     */

    public synchronized void getRequest(){ //not used in iteration 3
        while(request.isEmpty()){
            try{
                wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        RequestMsg a=request.pop();
        System.out.println(a);
       // elevator.receiveMsg(a);

        notifyAll();
    }

    /**
     * arrival
     */
    public synchronized boolean arrival(int upOrDown,ArrivalMessage arrMsg){
        if(arrMsg.isArrived()){
            if(upOrDown==-1){destiDown.remove(arrMsg.getFloor());}
            else if(upOrDown==1){destiUp.remove(arrMsg.getFloor());}
            return true;
        }
        return false;
    }

    public RequestMsg getFirstRequest() {
        RequestMsg firstQuest;
        synchronized (requestQueue){
            while((firstQuest = requestQueue.peek())==null){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return firstQuest;
        }
    }

    /**
     *MachineState handles the states of the scheduler
     */
    public enum MachineState {
        Waiting {
            @Override
            public MachineState nextState() {
                return Instructing;
            }

            @Override
            public String currentState() {
                return "Waiting";
            }
        },
        Instructing {
            @Override
            public MachineState nextState() {
                return Waiting;
            }

            @Override
            public String currentState() {
                return "Instructing";
            }
        };
        public abstract MachineState nextState();
        public abstract String currentState();
    }

//    /**
//     * receiveFromFloor receives request from floor
//     */
//    public void receiveFromFloor() {
//
//        if(destiUp.isEmpty()&&destiDown.isEmpty()) {
//            byte msg[] = new byte[40];
//        }
//
//        receivePacket = waitPacket(receiveSocket, "Scheduler");
//        byte[] msg = receivePacket.getData();
//        req = msg;
//        System.out.println("Scheduler receved floor request");
//        if (msg[0] > msg[3]) {
//            destiDown.add((int) msg[3]);
//        }
//
//        if(msg[0] < msg[3]) {
//            destiUp.add((int) msg[3]);
//        }
//
//        //DatagramPacket reply = waitPacket(receiveSocket, "Scheduler");
//        Floor.sendPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(),
//                receivePacket.getPort(), sendSocket);
//
//    }
    /**
     * Wait the packet from floor
     * @param s
     * @param source
     * @return
     */
    public DatagramPacket waitPacket(DatagramSocket s, String source){

        byte data[] = new byte[RequestMsg.MSGSIZE];

        DatagramPacket receivedPacket = new DatagramPacket(data, data.length);
        System.out.println(source + " is waiting");

        try{
            System.out.println("waiting...");
            s.receive(receivedPacket);
        }catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("From host: " + receivedPacket.getAddress());
        System.out.println("Destination host port: " + receivedPacket.getPort());
        return receivedPacket;
    }


    public void receiveFromFloor(){
        try {
            floorReceiveSocket.receive(floorReceivedPacket);
            System.out.println("Received from floor");
            System.out.println(Arrays.toString(floorReceivedPacket.getData()));
            RequestMsg floorMsg = RequestMsg.decode(floorReceivedPacket.getData());
            requestQueue.offer(floorMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        Scheduler s= new Scheduler();

        while(true){
            s.receiveFromFloor();
        }
    }

}
  