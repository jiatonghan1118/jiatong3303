import data_structure.ElevatorInfo;
import data_structure.RequestMsg;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * ElevatorSubSystem communicate with scheduler to receive request from floorSubSystem and report elevator
 * information to the scheduler to help with scheduling
 */
public class ElevatorSubSystem{
    public static final int RECEIVESCHEDULER = 91;

    //map elevator id with memory location
    private Hashtable<Integer,Elevator> elevatorHashtable = new Hashtable<>();
    //send to scheduler
    private DatagramSocket sendSocket;
    //the number of elevator in this system
    private int elevatorNum;

    /**
     * read from config file and initial system and GUI
     */
    public ElevatorSubSystem(int elevatorNum,int floorNum) {
        //According to provide elevator number, add the elevators into this system
        try {

        	this.elevatorNum = elevatorNum;
            for (int i = 0; i < elevatorNum; i++) {
                Elevator e = new Elevator(i,this);
                elevatorHashtable.put(i, e);
            }
            sendSocket = new DatagramSocket();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * get Elevator by ID
     * @param id Elevator ID
     * @return the Elevator reference
     */
    public Elevator getElevatorById(int id){
        return elevatorHashtable.get(id);
    }

    /**
     * receive message from scheduler on port 80 using UDP
     */
    private void receive() {
        //Through message id, to find the responding elevator to receive request.
        try {
            DatagramSocket receiveSocket = new DatagramSocket(RECEIVESCHEDULER);
            byte[] buffer = new byte[RequestMsg.MSGSIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true){

                receiveSocket.receive(packet);
                byte[] message = packet.getData();
                RequestMsg request = RequestMsg.decode(message);
                System.out.println("receive from floor: "+request);
                getElevatorById(request.getId()).receiveRequest(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This system receive elevators' update and send report to the scheduler on port 91 using UDP
     * @param info The new status of an elevator
     * @throws IOException when sending cannot be accomplished
     */
    public void updateEleStatusToSch(ElevatorInfo info) throws IOException {
        byte [] message = info.encode();
        DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getLocalHost(),Scheduler.RECEIVEELEVATORPORT);
        sendSocket.send(packet);
        System.out.println("send to Scheduler: "+info);
    }


    //main function
    public static void main(String[] args) {
            ElevatorSubSystem system = new ElevatorSubSystem(4,10);

                system.receive();

    }

}
