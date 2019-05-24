package de.battleship.server.packets.game;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class GamePacket {
    public static HashMap<Integer, Class<? extends GamePacket>> registeredInPackets;

    private static ObjectMapper jsonConverter;


    static {
        jsonConverter = new ObjectMapper();

        registeredInPackets = new HashMap<>();
        registeredInPackets.put(0, InPingPacket.class);
        registeredInPackets.put(1, InConnectRequest.class);
    }


    public static Class<? extends GamePacket> getInPacketById(int id) {
        return registeredInPackets.getOrDefault(id, null);
    }


    @Override
    public String toString() {
        try {
            return jsonConverter.writeValueAsString(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
