public interface ElevatorState {
    void openDoor();
    void closeDoor();
    void timeIsUp();
    void receiveRequest();

    void reachFloor(int floor);

}