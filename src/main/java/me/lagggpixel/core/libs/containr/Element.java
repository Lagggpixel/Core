package me.lagggpixel.core.libs.containr;

import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class Element implements Component {

    private final String id;

    public Element() {
        this.id = RandomStringUtils.randomAlphabetic(8);
    }

    public void click(ContextClickInfo info) {}

    @Nullable
    public abstract ItemStack item(Player player);

}
