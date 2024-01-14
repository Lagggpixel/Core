package me.lagggpixel.core.utils;

import java.text.DecimalFormat;

public class NumberUtil {
  public static String formatInt(int num) {
    DecimalFormat format = new DecimalFormat("#,###");
    format.setGroupingUsed(true);
    
    return format.format(num);
  }
}
