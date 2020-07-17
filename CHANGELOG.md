# Changelog

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