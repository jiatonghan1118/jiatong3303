package data_structure;
import java.nio.ByteBuffer;

/**
 * ElevatorInfo encapsulate all necessary information to help scheduler to schedule
 * Provide encoding and decoding method for UDP communication
 */
public class ElevatorInfo {
    private int id;
    private int currentFloor;
    private String currentDirection;
    private int arrivalTime;

    /**
     * Constructor for ElevatorInfo
     * @param id Elevator ID
     * @param currentFloor Elevator currentFloor
     * @param currentDirection Elevator current Direction
     */
    public ElevatorInfo(int id, int currentFloor, String currentDirection) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.currentDirection = currentDirection;
    }

    /**
     * decode UDP byte array to ElevatorInfo
     * byte 0-3 ID
     * byte 4-7 direction
     * byte 8-11 current floor
     * byte 12+ reserved
     * @param message UDP message received
     * @return ElevatorInfo
     */
    public static ElevatorInfo decode(byte[] message) {
        ByteBuffer buffer = ByteBuffer.allocate(30);
        buffer.put(message);
        int id = buffer.getInt(0);
        String direction = RequestMsg.intToDirection(buffer.getInt(4));
        int currentFloor = buffer.getInt(8);
        return new ElevatorInfo(id,currentFloor,direction);
    }

    /**
     * encode ElevatorInfo to UDP byte array
     * byte 0-3 ID
     * byte 4-7 direction
     * byte 8-11 current floor
     * byte 12+ reserved
     * @return UDP byte array
     */
    public byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(30);
        buffer.putInt(0,id);
        buffer.putInt(4,RequestMsg.directionToInt(currentDirection));
        buffer.putInt(8,currentFloor);
        return buffer.array();
    }

    /**
     * overwrite this info by a new info
     * @param info the new info
     */
    public void update(ElevatorInfo info) {
        this.currentDirection = info.getCurrentDirection();
        this.currentFloor = info.getCurrentFloor();
        System.out.println(this);
    }

    //getters and setters
    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public String getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(String currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "ElevatorInfo{" +
                "id=" + id +
                ", currentFloor=" + currentFloor +
                ", currentDirection=" + currentDirection +
                ", arrivalTime=" + arrivalTime +
                '}';
    }


}