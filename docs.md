# Table of Contents

- [Items](#items)
  - [Symbols](#symbols)
- [Block Entities](#block-entities)
  - [Display Panel](#display-panel)
- [Tags](#tags)
  - [Block Tags](#block-tags)
  - [Item Tags](#item-tags)

<br><br>

# Items

## Symbols:
Symbol items are plain items that don't do anything on their own. Their only purpose is to be used for decoration or as a meaningful placeholder.  
This can come in handy when labeling machines or storage, to have more meaningful frequencies when setting up Create mod Redstone Links, or as decoration around your base.  
  
Symbols come in 6 material variants (Coal, Iron, Gold, Lapis, Redstone, Emerald), which can be placed into a stonecutter to obtain any symbol variant.  
Symbols can also be placed into a stonecutter to transmute them into different symbols of the same material.  
Some categories of symbols have special shapes, like a triangular base for the "Warning" category.

<br><br>

# Block Entities

## Display Panel:
Reminiscent of an Item Frame, the Display Panel is a block entity that can display a single item on its front face.  
16 dye color variants are available, crafted by combining the panel with the corresponding dye in a crafting grid.  
Use it for displaying blocks or items like [symbols](#symbols), as a 2px wide "vertical slab" for decoration, as a single-item buffer for contraptions, and much more!  
  
Interactions:
- **Right-clicking an empty panel** with an item in hand will place the item on the panel.
- **Shift-right-clicking a filled panel** with an empty hand will pick up the item from the panel.
- **Powering the panel with redstone** will lock it, preventing items from being inserted or extracted until the power is removed.
- **Using a comparator**, the panel emits a redstone signal strength relative to the max stack size of the contained item (see table below).
- **Using hoppers**, items can be inserted into and extracted from the panel.

<br>
  
Comparison with Item Frames:  
| Pro/Con | Description |
| :-- | :-- |
| Pro | Doesn't need a supporting block. |
| Pro | Hoppers can insert and extract items. |
| Pro | Can be dyed in 16 colors. |
| Pro | Items are rendered at around twice the scale. |
| Pro | Emits a comparator signal that is proportional to the item's max stack size (see table below). |
| Neutral | Is a block entity instead of an entity. This means it also can't be moved by pistons. |
| Neutral | Has a 2x16x16 px hitbox, allowing entities to collide with it. |
| Neutral | The full panel size is 16x16 instead of 12x12, obscuring the entire block it's placed on. |
| Con | Contained items can't be rotated. |
| Con | Can't place multiple panels on different faces in the same block space. |
| Con | Can't place panels on top or bottom block faces. |
| Con | Panels always render dynamic items like compasses, clocks or maps in their default state. |
  
<br>

Comparator signal strengths:
| Max Stack Size | Signal Strength |
| :-- | :-- |
| (empty panel) | 0 |
| 1 | 15 |
| 2-8 | 11 |
| 9-16 | 7 |
| 17-32 | 3 |
| 33-64 | 1 |

<br>

<br><br>

# Tags

## Block Tags:
- `factory_symbols:displays` - All blocks that can display items.

<br>

## Item Tags:
- `factory_symbols:symbols` - All symbol items.
- `factory_symbols:symbols/<material>` - Symbol items of a specific material.  
  e.g.: `factory_symbols:symbols/iron` for symbol items made of iron.
