# Changelog

## 1.14.4
- Fixed some of the new ores not being detected

## 1.14.3
- Fixed UpdateChecker showing the wrong version

## 1.14.2
- Fixed all the amethyst blocks not being broken by pickaxes

## 1.14.0
- Added support for many new 1.17 materials. If some are still missing, please let me know on my Discord or in the discussion.
- Updated ChestSortAPI

## 1.13.0
- Updated ChestSortAPI to version 3.0.0. If you have ChestSort installed, you must use at least ChestSort 10.0.0!

## 1.12.1
- Updated config version (to show the new hint from 1.12.0)

## 1.12.0
- You can set messages to an empty String ("") to avoid them from being shown to the player.

## 1.11.0
- Added global block blacklist

## 1.10.2
- Added Russian translation

## 1.10.1
- Fixed BestTools trying to choose a tool for AIR in rare cases

## 1.10.0
- Added option to always use the current slot as favorite slot (set favorite-slot to -1)

## 1.9.1
- Refill works with the offhand as well

## 1.9.0
- Removed console message when attempting to refill bonemeal after right-clicking a block that can not be fertilized by bone meal
- Added possibility to disable using axes as weapons

## 1.8.1
- Refill now works for many new items like bone meal, ender pearls or ender eyes. 

## 1.8.0
- Empty soup bowls and potion bottles will ne be moved to another free slot when refill is enabled

## 1.7.0
- Added possibility to disable hint messages (just set them to an empty string (""))
- Added option to automatically switch to best sword/axe when attacking monsters
- Added silk touch and pickaxe as best tool to  kinds of glass and sea lanterns

## 1.6.2
- Nether gold ore, quartz and monster spawners will now be preferably mined with silk touch
- Already prepared ability to use best swords/axes on mobs, according to the enchantments Sharpness, Smite and Ban of Anthropods

## 1.6.1
- Fixed broken config updater leading to corrupt config files and exceptions. If you get errors on start, please delete your config.yml once. In future versions, it gets updated automatically.
- Added support for PlaceholderAPI (see new config.yml)

## 1.6.0
- Added GUI (beta!) using /besttools gui
- Added new blacklist command, so that BestTools will not change tools when trying to break those blocks.

Usage:
- /besttools bl -- Show your blacklist
- /besttools bl add -- Adds your currently held item to your blacklist
- /besttools bl add inventory -- Adds all items from your inventory to your blacklist
- /besttools bl add hotbar -- Adds all items from your hotbar to your blacklist
- /besttools bl add <items...> -- Add specified items to your blacklist
- /besttools bl remove -- Removes your currently held item from your blacklist
- /besttools bl remove inventory -- Removes all items from your inventory from your blacklist
- /besttools bl remove hotbar -- Removes all items from your hotbar from your blacklist
- /besttools bl remove <items...> -- Remove items from your blacklist
- /besttools bl reset -- Removes all items from your blacklist

## 1.5.0
- Instant breakable blocks like torches, grass, flowers etc. are no broken with the current item if it's not a hoe (because the hoe would take damage)
- Does not empty a slot into the inventory to use the empty hand to break a block when another undamagable item or block from the hotbar can be used instead
- Further performance optimization
  - replaced LinkedLists with ArrayLists
  - pregenerates tools material list instead of comparing strings (which was done to avoid problems with versions before netherite tools)1
  - checks player cache before checking everything else like permissions to further speed things up
- Added performance test mode (/besttools performance, needs permission besttools.debug)


## 1.4.1
- Added measure-performance option to demonstrate how fast BestTools is

## 1.4.0
##### Huuuuuge performance boost by caching two simple values

BestTools will now cache the last material a player has interacted with, and a boolean whether something important in their inventory has changed.

If the player interacts with the same material again, and they neither picked up a tool, broke a tool, changed their currently held item, nor dropped a tool, the BestTools listener will immediately exit because the player still has the best tool in his hands. This should improve performance by a greeeeeaaaaaaat amount. Even hundreds of players stripmining at once with golden efficiency 5 pickaxes should not be a problem for BestTools! :)

Also cleaned up the code.

## 1.3.0
- Added config option "dont-switch-during-battle" (default: true) that prevents BestTools from switching to a tool if the player currently holds a weapon (Sword, Crossbow, Bow or Trident)
- Added Chinese and Chinese (Traditional) translations

## 1.2.1
- Fixed misspelled permission (again)

## 1.2.0
- BestTools now prefers items with silk touch when breaking glass, enderchests and glowstone

## 1.1.0
- Refill works on food, snacks, and anything else digestible
- /besttools and /refill will only work in survival mode or, if enabled in the config, in adventure mode
- Added German translation 

## 1.0.1
- Fixed typo in permission. /refill should work now
- Added Spanish translation
- Added download link to Update checker

# Todo
- Auto refill edibles