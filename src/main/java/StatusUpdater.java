import data_structure.ElevatorInfo;
import data_structure.RequestMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class StatusUpdater extends Thread{

    private final ArrayList<ElevatorInfo> elevatorStatus;
    private final DatagramSocket elevatorReceiveSocket;
    private DatagramPacket elevatorReceivedPacket;

    public StatusUpdater(ArrayList<ElevatorInfo> elevatorStatus, DatagramSocket elevatorReceiveSocket) {
        this.elevatorStatus = elevatorStatus;
        this.elevatorReceiveSocket = elevatorReceiveSocket;
        byte[] receiveBuffer = new byte[RequestMsg.MSGSIZE];
        elevatorReceivedPacket = new DatagramPacket(receiveBuffer,receiveBuffer.length);
    }


    @Override
    public void run() {
        while (true){
            try {
                elevatorReceiveSocket.receive(elevatorReceivedPacket);
                ElevatorInfo info = ElevatorInfo.decode(elevatorReceivedPacket.getData());
                int id = info.getId();
                synchronized (elevatorStatus) {
                    ElevatorInfo old = elevatorStatus.get(id);
                    old = info;
                }
                System.out.println("elevatorInfo updated");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
