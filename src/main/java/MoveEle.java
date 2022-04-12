import java.util.Arrays;

public class MoveEle implements ElevatorState {
    Elevator elevator;
    public MoveEle(Elevator newElevator){
        elevator=newElevator;
    }

    @Override
    public void openDoor() {

    }

    @Override
    public void closeDoor() {

    }

    @Override
    public void timeIsUp() {
        elevator.setCurrentFloor(elevator.getQueue().first());
        int id = elevator.getId();
        System.out.println("elevator#" + id + " reached floor " + elevator.getCurrentFloor());
        elevator.setCurrentState(elevator.getOpening());
        System.out.println("Elevator is opening the door");
        System.out.println("Element in Queue(old)");
        System.out.println(Arrays.toString(elevator.getQueue().toArray()));
        elevator.getQueue().pollFirst();
        System.out.println("Element in Queue");
        System.out.println(Arrays.toString(elevator.getQueue().toArray()));
        elevator.reportStatus();
        elevator.timerStart(2000);
    }

    @Override
    public void receiveRequest() {

    }

    @Override
    public void reachFloor(int floor) {

    }
}