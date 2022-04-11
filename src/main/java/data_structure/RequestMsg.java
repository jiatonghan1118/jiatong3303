package data_structure;

/**
 * Request message format sent from Floor and Elevator
 *
 * @author Boshen Zhang
 */
import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.util.Arrays;

public class RequestMsg {
    private int startFloor;
    private String direction; // -1 if moving down, 1 if moving up, 0 if stay still
    private int destination;
    private int id;
    public static final int MSGSIZE = 30;

    /**
     * Default Constructor
     * @param startFloor
     * @param direction
     * @param destination
     */
    public RequestMsg(int startFloor, String direction, int destination){
        this.startFloor = startFloor;
        this.direction = direction;
        this.destination = destination;
    }

    public RequestMsg(RequestMsg old, int id) {
        this.startFloor=old.getStartFloor();
        this.direction=old.getDirection();
        this.destination=old.getDestination();
        this.id=id;
    }

    public RequestMsg(int currentFloor, String direction, int destination, int id) {
        startFloor = currentFloor;
        this.direction = direction;
        this.destination = destination;
        this.id = id;
    }

    /**
     * Convert direction to integer
     * @param direction
     * @return
     */
    public static int directionToInt(String direction){
        if(direction.equals("UP")){
            return 1;
        }else if(direction.equals("DOWN")){
            return -1;
        }else{
            return 0;
        }
    }


    public static String intToDirection(int direction){
        if(direction==1){
            return "UP";
        }else if(direction==-1){
            return "DOWN";
        }else{
            return "STAY";
        }
    }


    public int getStartFloor() {
        return this.startFloor;
    }
    public String getDirection(){return this.direction;}

    public int getDestination() { return destination; }


    /**
     * encode a request to byte array
     * byte 0-3, startFloor
     * byte 4-7, direction
     * byte 8-11, destination
     * byte 12-21, time
     * byte 22+, reserved
     * @return the encoded byte array
     * Request rq = new Request( ) ; rq
     *  byte [] a = rq.encode();
     *
     *
     */
    public byte[] encode() {
        ByteBuffer result = ByteBuffer.allocate(MSGSIZE);
        result.putInt(0,startFloor);
        System.out.println(direction);
        result.putInt(4,directionToInt(direction));
        result.putInt(8,destination);
        //add time here

        //wrap up
        return result.array();
    }

    /**
     * get id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * decode UDP message to Request
     * byte 0-3, currentFloor
     * byte 4-7, direction
     * byte 8-11, destination
     * byte 12-21, time
     * byte 22-25, Dispatched elevator ID
     * byte 25+ reserved
     * @param message UDP message
     * @return Request
     *
     *
     */
    public static RequestMsg decode(byte[] message){
        ByteBuffer buffer = ByteBuffer.allocate(MSGSIZE);
        System.out.println(Arrays.toString(message));
        buffer.put(message);
        int currentFloor = buffer.getInt(0);
        String direction = intToDirection(buffer.getInt(4));
        int destination = buffer.getInt(8);
        int id = buffer.getInt(22);
        return new RequestMsg(currentFloor,direction,destination,id);
    }

    @Override
    public String toString() {
        return "RequestMsg{" +
                "startFloor=" + startFloor +
                ", direction='" + direction + '\'' +
                ", destination=" + destination +
                ", id=" + id +
                '}';
    }
}