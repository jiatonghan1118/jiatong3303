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
        System.out.println("door is opened");
        if(elevator.getQueue().isEmpty()){
            System.out.println("finished one direction");
            elevator.switchDirection();
            System.out.println("now direction is "+elevator.getCurrentDirection());
            if(elevator.getCurrentDirection()==Elevator.STILL){
                elevator.setCurrentState(elevator.getIdle());
                System.out.println("Elevator back to idle state");
            }else{
                elevator.setCurrentState(elevator.getClosing());
                System.out.println("Elevator back to closing state");
                //close door after 2s
                elevator.timerStart(2000);
                System.out.println("door will close after 2 s");
            }
        }else {
            elevator.setCurrentState(elevator.getClosing());
            System.out.println("Elevator back to closing state");
            //close door after 2s
            elevator.timerStart(2000);
            System.out.println("door will close after 2 s");
        }


    }

    @Override
    public void receiveRequest() {

    }

    @Override
    public void reachFloor(int floor) {

    }
}