/**
 * This class handles the closing operation and closing state of the elevator of the elevator doors
 * @author Jiatong Han
 *
 */
public class Closing implements ElevatorState {
    Elevator elevator;
    
    /**
     * Constructor for Closing class
     * @param newElevator
     */
    public Closing(Elevator newElevator){
        elevator=newElevator;
    }


    @Override
    public void openDoor() {

    }

    @Override
    public void closeDoor() {

    }
    
/**
 * This method tells the elevator when the 
 * time is up and has to move to next floor
 */
    @Override
    public void timeIsUp() {
        System.out.println("door closed");
        elevator.setCurrentState(elevator.getEleMove());
        System.out.println("Elevator begin to move to destination");
        int arrivalTime = Math.abs(elevator.getQueue().first()-elevator.getCurrentFloor());
        System.out.println("will arrive after: "+ arrivalTime + "s");

        elevator.timerStart((arrivalTime*100));
    }

    @Override
    public void receiveRequest()  {

    }

    @Override
    public void reachFloor(int floor) {

    }
}
