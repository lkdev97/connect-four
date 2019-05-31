package de.battleship.server.packets.game;

public class OutChatMessage extends GamePacket {
    public String sender;
    public String content;
    public OutChatMessage.Type type;


    public OutChatMessage(String sender, String content) {
        this(sender, content, OutChatMessage.Type.NORMAL);
    }
    public OutChatMessage(String sender, String content, OutChatMessage.Type type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
    }



    public static enum Type {
        NORMAL("normal"),
        JOIN_GAME("join-game"),
        LEAVE_GAME("leave-game");


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
