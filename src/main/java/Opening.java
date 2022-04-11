public class Opening implements ElevatorState {
    Elevator elevator;
    public Opening(Elevator newElevator){
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
        if(elevator.getQueue().isEmpty()){
            elevator.switchDirection();
            if(elevator.getCurrentDirection()==Elevator.STILL){
                elevator.setCurrentState(elevator.getIdle());
                System.out.println("Elevator back to idle state");
            }else{
                elevator.setCurrentState(elevator.getClosing());
                System.out.println("Elevator back to closing state");
                //close door after 2s
                elevator.timerStart(2000);
            }
        }


    }

    @Override
    public void receiveRequest() {

    }

    @Override
    public void reachFloor(int floor) {

    }
}