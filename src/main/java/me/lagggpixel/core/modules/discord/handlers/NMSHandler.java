/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.utils.ExceptionUtils;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMSHandler {
  protected String versionPrefix = "";
  protected boolean failed = false;
  
  protected Class<?> class_CraftPlayer;
  protected Class<?> class_GameProfile;
  protected Class<?> class_GameProfileProperty;
  protected Class<?> class_EntityPlayer;
  protected Method method_CraftPlayer_getHandle;
  protected Method method_EntityPlayer_getGameProfile;
  public Method method_GameProfile_getProperties;
  protected Field field_PropertyMap_properties;
  public Field field_GameProfileProperty_value;
  
  {
    String className = Bukkit.getServer().getClass().getName();
    String[] packages = className.split("\\.");
    if (packages.length == 5) {
      versionPrefix = packages[3] + ".";
    }
    
    try {
      class_EntityPlayer = fixBukkitClass("net.minecraft.server.EntityPlayer", "net.minecraft.server.level.EntityPlayer");
      try {
        method_EntityPlayer_getGameProfile = class_EntityPlayer.getMethod("getProfile");
      } catch (NoSuchMethodException e) {
        try {
          method_EntityPlayer_getGameProfile = class_EntityPlayer.getMethod("getGameProfile");
        } catch (NoSuchMethodException e2) {
          method_EntityPlayer_getGameProfile = Arrays.stream(class_EntityPlayer.getMethods())
              .filter(method -> method.getReturnType().getSimpleName().equals("GameProfile"))
              .findFirst().orElseThrow(() -> new RuntimeException("Couldn't find the GameProfile method"));
        }
      }
      
      class_CraftPlayer = fixBukkitClass("org.bukkit.craftbukkit.entity.CraftPlayer");
      method_CraftPlayer_getHandle = class_CraftPlayer.getMethod("getHandle");
      
      class_GameProfile = getClass("com.mojang.authlib.GameProfile");
      class_GameProfileProperty = getClass("com.mojang.authlib.properties.Property");
      if (class_GameProfile == null) {
        class_GameProfile = getClass("net.minecraft.util.com.mojang.authlib.GameProfile");
        class_GameProfileProperty = getClass("net.minecraft.util.com.mojang.authlib.properties.Property");
      }
      method_GameProfile_getProperties = class_GameProfile.getMethod("getProperties");
      field_GameProfileProperty_value = class_GameProfileProperty.getDeclaredField("value");
      field_GameProfileProperty_value.setAccessible(true);
      field_PropertyMap_properties = method_GameProfile_getProperties.getReturnType().getDeclaredField("properties");
      field_PropertyMap_properties.setAccessible(true);
    } catch (Throwable e) {
      ExceptionUtils.handleException(e);
      failed = true;
    }
  }
  
  public Class<?> getClass(String className) {
    Class<?> result = null;
    try {
      result = NMSHandler.class.getClassLoader().loadClass(className);
    } catch (Exception ignored) {
    }
    return result;
  }
  
  public Class<?> fixBukkitClass(String className, String... alternateClassNames) throws ClassNotFoundException {
    List<String> classNames = new ArrayList<>();
    classNames.add(className);
    classNames.addAll(Arrays.asList(alternateClassNames));
    
    for (String name : classNames) {
      try {
        return NMSHandler.class.getClassLoader().loadClass(name);
      } catch (ClassNotFoundException ignored) {
      }
      
      if (!versionPrefix.isEmpty()) {
        name = name.replace("org.bukkit.craftbukkit.", "org.bukkit.craftbukkit." + versionPrefix);
        name = name.replace("net.minecraft.server.", "net.minecraft.server." + versionPrefix);
      }
      
      try {
        return NMSHandler.class.getClassLoader().loadClass(name);
      } catch (ClassNotFoundException ignored) {
      }
    }
    throw new ClassNotFoundException("Could not find " + className);
  }
  
  public Object getHandle(Player player) {
    if (failed) return null;
    
    try {
      return method_CraftPlayer_getHandle.invoke(player);
    } catch (Throwable e) {
      ExceptionUtils.handleException(e);
    }
    return null;
  }
  
  public Object getGameProfile(Player player) {
    if (failed) return null;
    
    Object handle = getHandle(player);
    if (handle != null) {
      try {
        return method_EntityPlayer_getGameProfile.invoke(handle);
      } catch (Throwable e) {
        ExceptionUtils.handleException(e);
      }
    }
    return null;
  }
  
  public Object getTextureProperty(Object propertyMap) {
    if (failed) return null;
    
    try {
      Object multi = field_PropertyMap_properties.get(propertyMap);
      //noinspection rawtypes
      Iterator it = ((Iterable) multi.getClass()
          .getMethod("get", Object.class)
          .invoke(multi, "textures")).iterator();
      if (it.hasNext()) {
        return it.next();
      }
    } catch (Throwable e) {
      ExceptionUtils.handleException(e);
    }
    return null;
  }
  
  public String getTexture(Player player) {
    if (failed) return null;
    
    try {
      Object profile = getGameProfile(player);
      if (profile == null) return null;
      Object propertyMap = method_GameProfile_getProperties.invoke(profile);
      Object textureProperty = getTextureProperty(propertyMap);
      if (textureProperty != null) {
        String textureB64 = (String) field_GameProfileProperty_value.get(textureProperty);
        String textureData = new String(Base64.decodeBase64(textureB64));
        Matcher matcher = Pattern.compile("https?://.+(?<texture>\\w{64})\"").matcher(textureData);
        if (matcher.find()) return matcher.group("texture");
      }
    } catch (Throwable e) {
      ExceptionUtils.handleException(e);
    }
    return null;
  }
  
}
