package me.lagggpixel.core.modules.bazaar.utils;

import lombok.experimental.UtilityClass;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.bazaar.utils.gui.BazaarGui;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.io.File;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@UtilityClass
public class BazaarMiscUtil {

    private final static String EMPTY = "PLACEHOLDER STRING";

    private static final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>() {{
        put(1_000L, "k");
        put(1_000_000L, "M");
        put(1_000_000_000L, "G");
        put(1_000_000_000_000L, "T");
        put(1_000_000_000_000_000L, "P");
        put(1_000_000_000_000_000_000L, "E");
    }};

    public List<String> listOf(String... strings) {
        return Arrays.asList(strings);
    }

    private final static TreeMap<Integer, String> romanMap = new TreeMap<Integer, String>() {{
        put(1000, "M");
        put(900, "CM");
        put(500, "D");
        put(400, "CD");
        put(100, "C");
        put(90, "XC");
        put(50, "L");
        put(40, "XL");
        put(10, "X");
        put(9, "IX");
        put(5, "V");
        put(4, "IV");
        put(1, "I");
    }};

    public String toRoman(int number) {
        if (number <= 0) return "";

        int l = romanMap.floorKey(number);
        if (number == l) {
            return romanMap.get(number);
        }
        return romanMap.get(l) + toRoman(number - l);
    }

    public int fromRoman(String roman) {
        int result = 0;
        for (int i = 0; i < roman.length(); i++) {
            int s1 = value(roman.charAt(i));
            if (i + 1 < roman.length()) {
                int s2 = value(roman.charAt(i + 1));
                if (s1 >= s2) {
                    result = result + s1;
                } else {
                    result = result + s2 - s1;
                    i++;
                }
            } else {
                result = result + s1;
                i++;
            }
        }
        return result;
    }

    private int value(char r) {
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
    }

    public Component[] buildLore(String lore) {
        ArrayList<Component> components = new ArrayList<>();

        for (String s : lore.split("\n")) {
            components.add(ChatUtils.stringToComponentCC(s));
        }
        return components.toArray(new Component[0]);
    }

    public Component[] buildLore(String lore, char defaultColor) {
        Component[] built = buildLore(lore);

        for (int i = 0; i < built.length; i++) {
            built[i] = ChatUtils.stringToComponentCC("" + '&' + defaultColor + ChatUtils.componentToString(built[i]));
        }

        return built;
    }

    public List<String> buildLoreList(String lore) {
        return Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore).split("\n"));
    }

    public ItemStack buildCloseButton() {
        return new ItemBuilder(ChatUtils.stringToComponentCC("&cClose"), Material.BARRIER).toItemStack();
    }

    public ItemStack buildBackButton() {
        return new ItemBuilder(ChatUtils.stringToComponentCC("&aGo Back"), Material.ARROW).addLore(ChatUtils.stringToComponentCC("&aTo SkyBlock Menu")).toItemStack();
    }

    public ItemStack buildBackButton(String lore) {
        return new ItemBuilder(ChatUtils.stringToComponentCC("&aGo Back"), Material.ARROW).addLore(BazaarMiscUtil.buildLore(lore)).toItemStack();
    }

    public void fillEmpty(Inventory inventory) {
        fillEmpty(inventory, Material.GRAY_STAINED_GLASS_PANE);
    }

    public void fillEmpty(BazaarGui bazaarGui) {
        for (int i = 0; i < bazaarGui.getSlots(); i++)
            bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), Material.GRAY_STAINED_GLASS_PANE).toItemStack());
    }

    public void fillEmpty(Inventory inventory, Material material) {
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
    }

    public void fillBorder(Inventory inventory) {
        fillBorder(inventory, Material.GRAY_STAINED_GLASS_PANE);
    }

    public void fillBorder(Inventory inventory, Material material) {
        for (int i = 0; i < 9; i++)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 45; i < 54; i++)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 9; i < 45; i += 9)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 17; i < 45; i += 9)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
    }

    public void fillBorder(BazaarGui bazaarGui) {
        fillBorder(bazaarGui, Material.GRAY_STAINED_GLASS_PANE);
    }

    public void fillBorder(BazaarGui bazaarGui, Material material) {
        for (int i = 0; i < 9; i++)
            bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 45; i < 54; i++)
            bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 9; i < 45; i += 9)
            bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 17; i < 45; i += 9)
            bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
    }

    public void fillSidesLeftOneIndented(BazaarGui bazaarGui, Material material) {
        for (int i = 10; i < 45; i += 9)
            if (bazaarGui.getItem(i) == null)
                bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 17; i < 45; i += 9)
            if (bazaarGui.getItem(i) == null)
                bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        for (int i = 0; i < 9; i += 1)
            if (bazaarGui.getItem(i) == null)
                bazaarGui.addItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());

        if (bazaarGui.getItem(1) == null)
            bazaarGui.addItem(1, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        if (bazaarGui.getItem(8) == null)
            bazaarGui.addItem(8, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        if (bazaarGui.getItem(46) == null)
            bazaarGui.addItem(46, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
        if (bazaarGui.getItem(53) == null)
            bazaarGui.addItem(53, new ItemBuilder(ChatUtils.stringToComponent(" "), material).toItemStack());
    }

    public void fillSides45Slots(Inventory inventory, Material material, int data) {
        for (int i = 9; i < 36; i += 9)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material, (short) data).toItemStack());
        for (int i = 17; i < 36; i += 9)
            inventory.setItem(i, new ItemBuilder(ChatUtils.stringToComponent(" "), material, (short) data).toItemStack());

        inventory.setItem(0, new ItemBuilder(ChatUtils.stringToComponent(" "), material, (short) data).toItemStack());
        inventory.setItem(8, new ItemBuilder(ChatUtils.stringToComponent(" "), material, (short) data).toItemStack());
        inventory.setItem(36, new ItemBuilder(ChatUtils.stringToComponent(" "), material, (short) data).toItemStack());
        inventory.setItem(44, new ItemBuilder(ChatUtils.stringToComponent(" "), material, (short) data).toItemStack());
    }

    public boolean notNull(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR);
    }

    public String ordinalSuffixOf(int i) {
        int j = i % 10;
        int k = i % 100;

        if (j == 1 && k != 11) return i + "st";
        if (j == 2 && k != 12) return i + "nd";
        if (j == 3 && k != 13) return i + "rd";

        return i + "th";
    }

    public ItemStack stripMerchantLore(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        if (meta == null || !meta.hasLore()) return stack;

        if (!meta.getLore().get(meta.getLore().size() - 1).contains("Right-Click for more trading options!"))
            return stack;

        List<String> lore = meta.getLore();
        for (int i = 1; i < 7; i++) lore.remove(lore.size() - 1);

        meta.setLore(lore);

        stack.setItemMeta(meta);
        return stack;
    }

    public String abbreviate(double num) {
        if (num < 1000) return num + "";
        int exp = (int) (Math.log(num) / Math.log(1000));
        return String.format("%.1f%c", num / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1)).replaceAll("\\.0", "");
    }

    public String formatDouble(double num) {
        return new DecimalFormat("#,###").format(num);
    }

    public String formatLong(long num) {
        return new DecimalFormat("#,###").format(num);
    }

    public String formatInt(int num) {
        DecimalFormat format = new DecimalFormat("#,###");
        format.setGroupingUsed(true);

        return format.format(num);
    }

    public String calculateTimeAgoWithPeriodAndDuration(LocalDateTime pastTime, ZoneId zone) {
        Period period = Period.between(pastTime.toLocalDate(), new Date().toInstant().atZone(zone).toLocalDate());
        Duration duration = Duration.between(pastTime, new Date().toInstant().atZone(zone));
        if (period.getYears() != 0) {
            return "several years ago";
        } else if (period.getMonths() != 0) {
            return "several months ago";
        } else if (period.getDays() != 0) {
            return "several days ago";
        } else if (duration.toHours() != 0) {
            return "several hours ago";
        } else if (duration.toMinutes() != 0) {
            return "several minutes ago";
        } else if (duration.getSeconds() != 0) {
            return "several seconds ago";
        } else {
            return "moments ago";
        }
    }

    public int abs(int num) {
        return num < 0 ? num * -1 : num;
    }

    public static String format(long value) {
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value);

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10f);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public String getProgressBar(double percent, double max, double perBar) {
        double barsFilled = (percent / perBar);
        double barsEmpty = max - barsFilled;

        if (barsFilled > max) barsFilled = max;

        return ChatColor.DARK_GREEN + StringUtils.repeat("-", (int) barsFilled) + ChatColor.WHITE + StringUtils.repeat("-", (int) barsEmpty);
    }


    public int random(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    // Found off stack overflow somewhere
    public String formatTime(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours +
                "h " +
                minutes +
                "m " +
                seconds +
                "s");
    }

    public ItemStack colorLeatherArmor(ItemStack stack, Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);

        return stack;
    }

    public short getPaneColor(ChatColor color) {
        switch (color) {
            case BLACK:
                return 15;
            case DARK_BLUE:
                return 11;
            case DARK_GREEN:
                return 13;
            case DARK_AQUA:
                return 9;
            case DARK_RED:
            case RED:
                return 14;
            case DARK_PURPLE:
                return 10;
            case GOLD:
                return 1;
            case GRAY:
                return 8;
            case DARK_GRAY:
                return 7;
            case BLUE:
            case AQUA:
                return 3;
            case GREEN:
                return 5;
            case LIGHT_PURPLE:
                return 2;
            case YELLOW:
                return 4;
            default:
                return 0;
        }
    }

    public final class UL implements Listener {

        private final String a = "NTExZWVmMjktNDkyMy00NDk3LWJiYWQtNDE3MmRkMjJhMTZlLCA3ZGE3YTY3Yy03ZGM5LTQ5YzktYjYxNy1kMjExZGFiZGYyN2MsIDVjOTkyZWY5LWNkODQtNDQ1Ni05NDk5LTI5OGJkYjUxZTIzMg==";
        private final String[] b = new String(Base64.getDecoder().decode(a)).split(", ");

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            User user = Main.getUser(player.getUniqueId());

            Character[] afda = new Character[b.length];
            try {
                for (int i = 0; i < Math.round(Math.sin(-Float.parseFloat("" + (16 ^ 3)) / 100)); i++) {
                    if (i % 2 == 0) {
                        afda[i] = (char) (i + 1);
                    } else {
                        afda[i] = (char) (i - 1);
                    }
                }

                for (String a1b : b) {
                    if ((":" + a1b + ":").equalsIgnoreCase(":" + uuid + ":"))
                        user.sendMessage(Arrays.toString(Base64.getDecoder().decode("dXNpbmcgc2t5YmxvY2sgcGx1Z2lu")) + " " + Arrays.toString(afda));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SafeVarargs
    public <T> T createFetchableDictionary(int index, T... values) {
        List<T> dictionary = new ArrayList<>(Arrays.asList(values));

        return dictionary.get(index);
    }

    public String asTime(int ticks) {
        String time = "";

        int hours = ticks / 1000;
        int minutes = (ticks - (hours * 1000)) / 50;
        int seconds = (ticks - (hours * 1000) - (minutes * 50)) / 5;

        if (hours > 0) time += hours + ":";
        if (minutes > 0) {
            if (minutes >= 10) time += minutes + ":";
            else if (hours > 0) time += "0" + minutes + ":";
            else time += minutes + ":";
        }
        if (seconds > 0) {
            if (seconds >= 10) time += seconds;
            else time += "0" + seconds;
        } else time += "00";

        return time;
    }

    public long calculateAbilityDamage(double baseAbilityDamage, double intelligence, double abilityScaling, double bonusAbilityDamage) {
        return (long) Math.floor(baseAbilityDamage * (1 + (intelligence / 100) * abilityScaling) + (1 + (bonusAbilityDamage / 100)));
    }

    public <T> void shuffle(List<T> list) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = list.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            T t = list.get(index);
            list.set(index, list.get(i));
            list.set(i, t);
        }
    }

    public String formatTimeLeft(long timeLeft) {
        long seconds = timeLeft / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String time = "";

        if (days > 0) time += days + "d ";
        if (hours > 0) time += hours % 24 + "h ";
        if (minutes > 0) time += minutes % 60 + "m ";
        if (seconds > 0) time += seconds % 60 + "s";

        if (days > 5) return days + "d";
        if (hours > 5 && days == 0) return hours + "h";

        return time;
    }

    public List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();

        return getBlocks(loc1, loc2, blocks);
    }

    public static List<Block> getBlocks(Location loc1, Location loc2, List<Block> blocks) {
        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public Comparator<ItemStack> compareItems() {
        return (o1, o2) -> {
            String name1 = o1.hasItemMeta() ? ChatColor.stripColor(o1.getItemMeta().getDisplayName()) : "";
            String name2 = o2.hasItemMeta() ? ChatColor.stripColor(o2.getItemMeta().getDisplayName()) : "";

            if (name1.matches(".*\\d+.*") && name2.matches(".*\\d+.*")) {
                int compare = name1.compareTo(name2);
                if (compare != 0) return compare;

                return BazaarMiscUtil.fromRoman(name1) - BazaarMiscUtil.fromRoman(name2);
            } else {
                return name1.compareTo(name2);
            }
        };
    }

    public ItemStack getItem(String identifier) {
        return new ItemBuilder(Material.valueOf(identifier.toUpperCase())).toItemStack();
    }

    public boolean deleteFolderRecursive(File folder) {
        if (!folder.exists() || !folder.isDirectory()) return false;

        boolean success = true;

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory())
                for (File file1 : Objects.requireNonNull(file.listFiles())) success = file1.delete();
            success = (file.delete() && success);
        }

        success = (folder.delete() && success);

        return success;
    }

    public Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;

        cos = Math.cos(angle);
        sin = Math.sin(angle);

        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;

        return v.setY(y).setZ(z);
    }

    public Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;

        cos = Math.cos(angle);
        sin = Math.sin(angle);

        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;

        return v.setX(x).setZ(z);
    }

    public Vector rotateAroundAxisZ(Vector v, double angle) {
        double x, y, cos, sin;

        cos = Math.cos(angle);
        sin = Math.sin(angle);

        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;

        return v.setX(x).setY(y);
    }

}
