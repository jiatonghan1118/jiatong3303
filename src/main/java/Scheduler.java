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

    private RequestConsumer requestConsumer;


    String ELEVATOR_IP_ADDRESS = "";

    /**
     * Constructor for class Scheduler
     */
    public Scheduler() {
        //initial elevator status hard code need to change
        for (int i = 0; i < 4; i++) {
            elevatorStatus.add(new ElevatorInfo(0,1,"STAY"));
        }


        try {
            //sendReceiveSocket = new DatagramSocket();
            elevatorSendSocket = new DatagramSocket();
            requestConsumer = new RequestConsumer(requestQueue,elevatorStatus,elevatorSendSocket,this);
            requestConsumer.start();
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

//    public RequestMsg getFirstRequest() {
//        RequestMsg firstQuest;
//        synchronized (requestQueue){
//            while((firstQuest = requestQueue.peek())==null){
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return firstQuest;
//        }
//    }


    public void receiveFromFloor(){
        try {
            floorReceiveSocket.receive(floorReceivedPacket);
            System.out.println("Received from floor");
            System.out.println(Arrays.toString(floorReceivedPacket.getData()));
            RequestMsg floorMsg = RequestMsg.decode(floorReceivedPacket.getData());
//            requestQueue.offer(floorMsg);
            requestConsumer.schedule(floorMsg);
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
  