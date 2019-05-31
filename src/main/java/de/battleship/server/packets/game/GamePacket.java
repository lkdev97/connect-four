package de.battleship.server.packets.game;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.battleship.Lobby;
import de.battleship.OnlinePlayer;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public abstract class GamePacket {
    public static HashMap<Integer, Class<? extends GamePacket>> id2InPacket;
    public static HashMap<Class<? extends GamePacket>, Integer> inPacket2Id;

    public static HashMap<Integer, Class<? extends GamePacket>> id2OutPacket;
    public static HashMap<Class<? extends GamePacket>, Integer> outPacket2Id;

    protected static ObjectMapper jsonConverter;

    static {
        jsonConverter = new ObjectMapper();

        // In Packets
        id2InPacket = new HashMap<>();
        id2InPacket.put(0, InPingPacket.class);
        id2InPacket.put(1, InConnectRequest.class);
        id2InPacket.put(16, InPlayerMove.class);
        id2InPacket.put(17, InChatMessage.class);

        inPacket2Id = new HashMap<>();
        id2InPacket.forEach((k, v) -> inPacket2Id.put(v, k));

        // Out Packets
        id2OutPacket = new HashMap<>();
        id2OutPacket.put(0, OutError.class);
        id2OutPacket.put(1, OutConnectSuccess.class);
        id2OutPacket.put(16, OutGameField.class);
        id2OutPacket.put(17, OutChatMessage.class);

        outPacket2Id = new HashMap<>();
        id2OutPacket.forEach((k, v) -> outPacket2Id.put(v, k));
    }

    public static GamePacket fromString(String data) throws JsonParseException, JsonMappingException, IOException {
        return jsonConverter.readValue(data, PacketContainer.class).data;
    }

    @Override
    public String toString() {
        try {
            return jsonConverter.writeValueAsString(new PacketContainer(
                    outPacket2Id.getOrDefault(this.getClass(), inPacket2Id.getOrDefault(this.getClass(), 0)), this));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        System.err.println("Tried to handle packet " + this.getClass().getCanonicalName() + "!");
    }


    @JsonDeserialize(using = PacketContainerDeserializer.class)
    private static class PacketContainer {
        @SuppressWarnings("unused")
        public int packetId;
        public GamePacket data;

        public PacketContainer(int packetId, GamePacket packet) {
            this.packetId = packetId;
            this.data = packet;
        }
    }

    public static class PacketContainerDeserializer extends StdDeserializer<PacketContainer> {
        private static final long serialVersionUID = 1L;

        public PacketContainerDeserializer() {
            this(null);
        }
        public PacketContainerDeserializer(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public PacketContainer deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            JsonNode packetIdNode = node.get("packetId");
            JsonNode dataNode = node.get("data");

            int packetId = packetIdNode.asInt();
            Class<? extends GamePacket> inPacketType = id2InPacket.get(packetId);

            if (inPacketType != null)
                return new PacketContainer(packetId, jsonConverter.treeToValue(dataNode, inPacketType));

            return null;
        }
    }
}
