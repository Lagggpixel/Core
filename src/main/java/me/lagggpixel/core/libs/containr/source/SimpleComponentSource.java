package me.lagggpixel.core.libs.containr.source;

import lombok.Getter;
import me.lagggpixel.core.libs.containr.ComponentTunnel;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.ComponentSource;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleComponentSource implements ComponentSource {

    @Getter
    private final List<ComponentTunnel> tunnels = new CopyOnWriteArrayList<>();

    @Override
    public boolean enable(ComponentTunnel tunnel) {
        return tunnels.add(tunnel);
    }

    @Override
    public void disable(ComponentTunnel tunnel) {
        tunnels.remove(tunnel);
    }

    public int publish(Component component) {
        int count = 0;
        for (ComponentTunnel tunnel : tunnels) {
            tunnel.send(component);
            count++;
        }
        return count;
    }

    public void clear() {
        for (ComponentTunnel tunnel : tunnels) {
            tunnel.clear();
        }
    }
}
