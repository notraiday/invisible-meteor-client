package anticope.rejects.utils.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServerListPinger {
    private final ArrayList<IServerFinderDisconnectListener> disconnectListeners = new ArrayList<>();
    private boolean notifiedDisconnectListeners = false;
    private boolean failedToConnect = true;

    public void addServerFinderDisconnectListener(IServerFinderDisconnectListener listener) {
        disconnectListeners.add(listener);
    }

    private void notifyDisconnectListeners() {
        synchronized (this) {
            if (!notifiedDisconnectListeners) {
                notifiedDisconnectListeners = true;
                for (IServerFinderDisconnectListener l : disconnectListeners) {
                    if (l != null) {
                        if (failedToConnect) l.onServerFailed();
                        else l.onServerDisconnect();
                    }
                }
            }
        }
    }

    public void add(final MServerInfo entry, final Runnable runnable) throws UnknownHostException {
        String[] parts = entry.address.split(":");
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 25565;

        long start = System.currentTimeMillis();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1500);
            failedToConnect = false;
            entry.ping = System.currentTimeMillis() - start;
            entry.label = "Online";
            entry.playerCountLabel = "?/?";
            entry.playerCount = 0;
            entry.playercountMax = 0;
            runnable.run();
        } catch (IOException ignored) {
            failedToConnect = true;
            entry.label = "multiplayer.status.cannot_connect";
            entry.playerCountLabel = "";
            entry.playerCount = 0;
            entry.playercountMax = 0;
        }

        notifyDisconnectListeners();
    }

    public void tick() {
        // no-op, this simplified implementation pings synchronously
    }

    public void cancel() {
        // no-op
    }
}