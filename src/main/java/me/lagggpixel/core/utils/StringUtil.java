/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtil {
  
  public interface Case {
    Case LOWER_CAMEL = new LowerCamelCase();
    Case SNAKE = new SnakeCase();
    
    String toLowerCamel(String s);
    String toSnake(String s);
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class LowerCamelCase implements Case {
    
    @Override
    public String toLowerCamel(String s) {
      return s;
    }
    
    @Override
    public String toSnake(String s) {
      final StringBuilder builder = new StringBuilder();
      
      for (char c : s.toCharArray()) {
        if (Character.isUpperCase(c)) builder.append("_");
        
        builder.append(Character.toLowerCase(c));
      }
      
      return builder.toString();
    }
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SnakeCase implements Case {
    
    @Override
    public String toLowerCamel(String s) {
      final String[] split = s.split("_");
      
      if (split.length == 1) return s;
      
      return split[0] + Arrays.stream(Arrays.copyOfRange(split, 1, split.length))
          .map(StringUtil::capitalize)
          .collect(Collectors.joining());
    }
    
    @Override
    public String toSnake(String s) {
      return s;
    }
  }
  
  public String capitalize(String s) {
    return StringUtils.capitalize(s);
  }
}
