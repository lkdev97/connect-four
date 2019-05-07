package api.packets;

public class OutError extends Packet {
    public String error;

    public OutError(String error) {
        this.error = error;
    }
}