import com.sun.org.apache.xpath.internal.objects.XNull;
import data_structure.ElevatorInfo;
import data_structure.RequestMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RequestConsumer extends Thread{

    public RequestConsumer(Queue<RequestMsg> requestQueue, ArrayList<ElevatorInfo> elevatorStatus, DatagramSocket elevatorSendSocket) {
        while(true){
            RequestMsg firstQuest;
            while((firstQuest = requestQueue.peek())==null){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int bestId = findBestElevatorID(elevatorStatus,firstQuest);

            RequestMsg elevatorMsg = new RequestMsg(firstQuest,bestId);
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
    }

    private int findBestElevatorID(ArrayList<ElevatorInfo> elevatorStatus, RequestMsg firstQuest) {
        ElevatorInfo best;
        PriorityQueue<ElevatorInfo> sameDirection = new PriorityQueue<>(
                (info1,info2) -> {

                    if (Math.abs(info1.getCurrentFloor() - firstQuest.getStartFloor())
                            < Math.abs(info2.getCurrentFloor() - firstQuest.getStartFloor())) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
        );


        for (ElevatorInfo info: elevatorStatus) {
            if(sameDirection(info,firstQuest)){
                sameDirection.add(info);
                //pai ren wu
            }
        }
        synchronized (elevatorStatus){
            best = elevatorStatus.remove(0);
        }

        int id = best.getId();

        return id;

    }




    private boolean sameDirection(ElevatorInfo info, RequestMsg firstQuest) {
        if(info.getCurrentDirection()== firstQuest.getDirection()){return  true;}
        return false;
    }
}
