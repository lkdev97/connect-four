package de.battleship.server.packets.game;

import java.util.HashMap;

public abstract class GamePacket {
    public static HashMap<Integer, Class<? extends GamePacket>> registeredInPackets;


    static {
        registeredInPackets = new HashMap<>();
        registeredInPackets.put(0, InPingPacket.class);
        registeredInPackets.put(1, InConnectRequest.class);
    }


    public static Class<? extends GamePacket> getInPacketById(int id) {
        return registeredInPackets.getOrDefault(id, null);
    }
}
