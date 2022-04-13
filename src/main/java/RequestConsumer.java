import data_structure.ElevatorInfo;
import data_structure.RequestMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RequestConsumer{
    private Queue<RequestMsg> requestQueue;
    private ArrayList<ElevatorInfo> elevatorStatus;
    private DatagramSocket elevatorSendSocket;
    private Scheduler scheduler;

    public RequestConsumer(Queue<RequestMsg> requestQueue,
                           ArrayList<ElevatorInfo> elevatorStatus, DatagramSocket elevatorSendSocket, Scheduler scheduler) {
        this.requestQueue = requestQueue;
        this.elevatorStatus = elevatorStatus;
        this.elevatorSendSocket = elevatorSendSocket;

        this.scheduler = scheduler;
    }

    public void schedule(RequestMsg floorMsg) {
        int bestId = findBestElevatorID(elevatorStatus,floorMsg);
        requestQueue.poll();
        RequestMsg elevatorMsg = new RequestMsg(floorMsg,bestId);
        byte[] buffer= elevatorMsg.encode();
        try {
            DatagramPacket elevatorInstruction = new DatagramPacket(
                    buffer,buffer.length, InetAddress.getLocalHost(),
                    ElevatorSubSystem.RECEIVESCHEDULER);
            elevatorSendSocket.send(elevatorInstruction);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    private int findBestElevatorID(ArrayList<ElevatorInfo> elevatorStatus, RequestMsg firstQuest) {
        System.out.println("elevator status");
        for (ElevatorInfo info : elevatorStatus){
            System.out.println(info.toString());
        }
        System.out.println("firstQuest");
        System.out.println(firstQuest.toString());
        PriorityQueue<ElevatorInfo> candidate = new PriorityQueue<>(
                (info1,info2) -> {

                    if (Math.abs(info1.getCurrentFloor() - firstQuest.getStartFloor())
                            < Math.abs(info2.getCurrentFloor() - firstQuest.getStartFloor())) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
        );

        //rule 1: find elevator in the same direction
        for (ElevatorInfo info: elevatorStatus) {
            if(sameDirection(info,firstQuest)){
                candidate.add(info);
            }
        }
        //rule 2: if no elevator in the same direction, find idle one
        if(candidate.isEmpty()){
            System.out.println("apply  rule 2");
            for (ElevatorInfo info: elevatorStatus) {
                if(info.getCurrentDirection().equals("STAY")){
                    candidate.add(info);
                }
            }
        }
        //rule 3: if no idle either, give it to first elevator
        if(candidate.isEmpty()){
            System.out.println("apply  rule 3");
            candidate.add(elevatorStatus.get(0));
        }


        int id = candidate.peek().getId();
        System.out.println("cadidates");
        System.out.println(candidate);
        System.out.println("best elevator is #" + id);

        return id;

    }

    private boolean sameDirection(ElevatorInfo info, RequestMsg firstQuest) {
        if(info.getCurrentDirection()== firstQuest.getDirection()){return  true;}
        return false;
    }
}
