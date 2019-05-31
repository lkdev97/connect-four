package de.battleship.server.packets.game;

public class OutChatMessage extends GamePacket {
    public String sender;
    public String content;
    public String type;


    public OutChatMessage(String sender, String content) {
        this(sender, content, OutChatMessage.Type.NORMAL);
    }

    public OutChatMessage(String content, OutChatMessage.Type type) {
        this(null, content, type);
    }
    public OutChatMessage(String sender, String content, OutChatMessage.Type type) {
        this.sender = sender;
        this.content = content;
        this.type = type.toString();
    }



    public static enum Type {
        NORMAL("normal"),
        SUCCESS("success"),
        WARNING("warning");


        private String cssClass;

        Type(String cssClass) {
            this.cssClass = cssClass;
        }

        @Override
        public String toString() {
            return this.cssClass;
        }
    }
}
