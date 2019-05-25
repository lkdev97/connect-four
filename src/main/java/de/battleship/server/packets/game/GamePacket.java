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

import org.jetbrains.annotations.NotNull;

import de.battleship.Lobby;
import de.battleship.Player;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public abstract class GamePacket {
    public static HashMap<Integer, Class<? extends GamePacket>> registeredInPackets;

    protected static ObjectMapper jsonConverter;

    static {
        jsonConverter = new ObjectMapper();

        registeredInPackets = new HashMap<>();
        registeredInPackets.put(0, InPingPacket.class);
        registeredInPackets.put(1, InConnectRequest.class);
        registeredInPackets.put(16, InPlayerMove.class);
    }

    public static GamePacket fromString(String data) throws JsonParseException, JsonMappingException, IOException {
        return jsonConverter.readValue(data, PacketContainer.class).data;
    }

    @Override
    public String toString() {
        try {
            return jsonConverter.writeValueAsString(new PacketContainer(0, this));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, Player player) {
        System.err.println("Tried to handle packet " + this.getClass().getCanonicalName() + "!");
    }


    @JsonDeserialize(using = PacketContainerDeserializer.class)
    private static class PacketContainer {
        @NotNull // damit es hier keine Warnung in VS Code gibt (Annotation eigtl. nicht nötig)
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
            Class<? extends GamePacket> inPacketType = registeredInPackets.get(packetId);

            if (inPacketType != null)
                return new PacketContainer(packetId, jsonConverter.treeToValue(dataNode, inPacketType));

            return null;
        }
    }
}
