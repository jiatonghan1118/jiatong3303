package data_structure;

public class ArrivalMessage {
    private int floor;
    private boolean arrived;
    public ArrivalMessage(int floor, boolean arrived){
        this.floor = 0;
        this.arrived = false;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public int getFloor() { return floor; }

    public boolean isArrived() { return arrived; }
}