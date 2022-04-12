/**This class is a representation of an elevator
 * @author Jiatong Han
 * @anthor Boshen Zhang
 */
import data_structure.*;

import java.io.IOException;

import java.util.*;

public class Elevator {
    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int STILL = 0;
    private ElevatorSubSystem elevatorSubSystem;
    private int id;
    private int currentFloor= 0;
    private Scheduler scheduler;
    private int currentDirection;
    private Timer timer;
    private TimerTask ring;

    private TreeSet<Integer> upQueue=new TreeSet<>(Comparator.naturalOrder());
    private TreeSet<Integer> downQueue=new TreeSet<>(Comparator.reverseOrder());

    ElevatorState idle;
    ElevatorState opening;
    ElevatorState moveEle;
    ElevatorState closing;
    ElevatorState reachFloor;

    ElevatorState currentState;


    /**
     * Constructor for Elevator class
     * @param id
     */
    public Elevator(int id) {
        idle=new Idle(this);
        closing=new Closing(this);
        opening =new Opening(this);
        moveEle=new MoveEle(this);

        currentState =idle;
        this.id=id;
    }

    public Elevator(Scheduler scheduler) {
        idle=new Idle(this);
        closing=new Closing(this);
        opening =new Opening(this);
        moveEle=new MoveEle(this);

        currentState =idle;

        this.scheduler = scheduler;
        scheduler.addElevator(this);
    }
    /**
     * Default constructor for Elevator class
     */
    public Elevator(){
        idle=new Idle(this);
        closing=new Closing(this);
        opening =new Opening(this);
        moveEle=new MoveEle(this);

        currentState =idle;

    }

    /**
     * Overload constructor for Elevator class
     */
    public Elevator(int id, ElevatorSubSystem elevatorSubSystem) {
        this();
        this.id=id;
        this.elevatorSubSystem=elevatorSubSystem;
    }

    /**
     * return task list based on direction
     * @return queue
     */
    public TreeSet<Integer> getQueue(){
        if(currentDirection==UP){
            return upQueue;
        }else if(currentDirection==DOWN){
            return downQueue;
        }else{
            System.out.println("ERROR");
            return null;
        }
    }



    /**
     * setCurrentState
     * @param newElevatorState
     */
    public void setCurrentState(ElevatorState newElevatorState){
        currentState =newElevatorState;
    }

    /**
     * timerStart
     */
    public void timerStart(int delay) {
        timer=new Timer();
        ring =new TimerTask() {
            @Override
            public void run() {
                currentState.timeIsUp();
            }
        };
        timer.schedule(ring,delay);
    }

    /**
     * receiveRequest
     */
    public void receiveRequest(RequestMsg requestMsg){
        int direction=RequestMsg.directionToInt(requestMsg.getDirection());
        int from=requestMsg.getStartFloor();
        int destination=requestMsg.getDestination();
        if(currentDirection==UP){
            if(direction==UP){
                upQueue.add(from);
                upQueue.add(destination);
            }else if(direction==DOWN){
                upQueue.add(from);
                downQueue.add(destination);
            }else{
                System.out.println("ERROR");
            }
        }else if(currentDirection==DOWN){
            if(direction==DOWN){
                downQueue.add(from);
                downQueue.add(destination);
            }else if(direction==UP){
                downQueue.add(from);
                upQueue.add(destination);
            }else{
                System.out.println("ERROR");
            }
        }else{//elevator idle
            if(from>(getCurrentFloor())){
                currentDirection=UP;
                receiveRequest(requestMsg);
            }else if(from < getCurrentFloor()){
                currentDirection=DOWN;
                receiveRequest(requestMsg);
            }else{
                if(direction==UP){
                    upQueue.add(destination);
                }else if(direction==DOWN){
                    downQueue.add(destination);
                }else{
                    System.out.println("ERROR");
                }
            }
        }

        currentState.receiveRequest();
    }



    /**
     * getClosing returns closing state
     * @return closing
     */
    public ElevatorState getClosing() {
        return closing;
    }
    /**
     * getOpenig returns opening state
     * @return opening
     */
    public ElevatorState getOpening() {
        return opening;
    }

    /**
     * getEleMove returns moveEle state
     * @return moveEle
     */
    public ElevatorState getEleMove(){
        return moveEle;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getId() {
        return id;
    }

    /**
     * GetIdle returns idle state
     * @return idle
     */
    public ElevatorState getIdle() {
        return idle;
    }

    /**
     * Get Current Direction
     * @return
     */
    public int getCurrentDirection() {
        return currentDirection;
    }



    /**
     *convert height to floor
     */
    public int getCurrentFloor(){
        return currentFloor;
    }


    /**
     * Repot Elevator Status to System
     */
    public void reportStatus(){
        try {
            elevatorSubSystem.updateEleStatusToSch(new ElevatorInfo(id,currentFloor,RequestMsg.intToDirection(currentDirection)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When elevator finish all task in one direction, check the other direction
     */
    public void switchDirection() {
        if(currentDirection==UP){
            if(downQueue.isEmpty()){
                currentDirection=STILL;
            }else{
                currentDirection=DOWN;
            }
        }else{
            if(upQueue.isEmpty()){
                currentDirection=STILL;
            }else{
                currentDirection=UP;
            }
        }
    }
}