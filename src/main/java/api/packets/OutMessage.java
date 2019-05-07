package api.packets;

public class OutMessage extends Packet {
    public String message;

    public OutMessage(String message) {
        this.message = message;
    }
}