package me.lagggpixel.core.modules.bazaar.interfaces;

import java.util.UUID;

public interface BazaarOffer {
  
  UUID getOwner();
  
  int getAmount();
  
  double getPrice();
  
}
