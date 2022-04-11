public class Idle implements ElevatorState{
    Elevator elevator;
    public Idle(Elevator newElevator){
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

    }

    @Override
    public void receiveRequest() {
        elevator.setCurrentState(elevator.getClosing());
        System.out.println("Elevator is closing the door");
        //Door will close after 2s
        elevator.timerStart(2000);
    }

    @Override
    public void reachFloor(int floor) {

    }

}