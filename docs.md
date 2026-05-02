# Items

## Symbols:
Symbol items are plain items that don't do anything on their own. Their only purpose is to be used for decoration or as a meaningful placeholder.  
This can come in handy when labeling machines or storage, to have more meaningful frequencies when setting up Create mod Redstone Links, or as decoration around your base.  
  
Symbols come in 6 material variants (Coal, Iron, Gold, Lapis, Redstone, Emerald), which can be placed into a stonecutter to obtain any symbol variant.  
Symbols can also be placed into a stonecutter to transmute them into different symbols of the same material.  
Some categories of symbols have special shapes, like a triangular base for the "Warning" category.

<br>

# Block Entities

## Display Panel:
Reminiscent of an Item Frame, the Display Panel is a block entity that can display a single item on its front face.  
16 dye color variants are available, crafted by combining the panel with the corresponding dye in a crafting grid.  
  
Interactions:
- Right-clicking an empty panel with an item in hand will place the item on the panel.
- Shift-right-clicking a panel with an empty hand will pick up the item from the panel.
- Powering the panel with redstone will lock it, preventing any interactions until the redstone signal is removed again.
- Using a comparator, the panel emits a redstone signal strength of 15 when containing a non-stackable item, 1 when containing a stackable item, and 0 when empty.
  
Comparison with Item Frames:  
| Pro/Con | Description |
| :-- | :-- |
| Pro | Doesn't need a supporting block. |
| Pro | Hoppers can insert and extract items. |
| Pro | Can be dyed in 16 colors. |
| Pro | Items are rendered at around twice the scale. |
| Pro | Emits a comparator signal that indicates whether the panel is emtpy, or contains stackable or non-stackable item. |
| Neutral | Is a block instead of an entity. |
| Neutral | Has a hitbox, allowing it to be walked on. |
| Neutral | The full panel size is 16x16 instead of 12x12, obscuring the entire block it's placed on. |
| Con | Contained items can't be rotated. |
| Con | Can't place multiple panels on different faces in the same block space. |
| Con | Can't place panels on top or bottom block faces. |
| Con | Panels always render dynamic items like compasses, clocks or maps in their default state. |

<br>

# Tags

## Block Tags:
- `factory_symbols:displays` - All blocks that can display items.

## Item Tags:
- `factory_symbols:symbols` - All symbol items.
- `factory_symbols:symbols/<material>` - Symbol items of a specific material.
    - e.g.: `factory_symbols:symbols/coal` - Coal symbol items.
