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
- Right-clicking an empty panel with an item in hand will place the item on the panel
- Shift-right-clicking a panel with an empty hand will pick up the item from the panel
  
Comparison with Item Frames:  
| Pro/Con | Description |
| :-- | :-- |
| Pro | Doesn't need a supporting block to be placed. |
| Pro | Is a block instead of an entity |
| Pro | Has a hitbox, allowing it to be walked on. |
| Pro | Can be dyed in 16 colors. |
| Neutral | Also emits a redstone signal when containing an item, but at a constant strength of 15. |
| Con | Can't rotate contained items. |
| Con | Can't place multiple on different faces in the same block space. |
| Con | Can't place on top or bottom block faces. |
| Con | Always renders dynamic items like compasses, clocks or maps in their default state. |

<br>

# Tags

## Block Tags:
- `factory_symbols:displays` - All blocks that can display items.

## Item Tags:
- `factory_symbols:symbols` - All symbol items.
- `factory_symbols:materials/<material>` - Symbol items of a specific material.
    - e.g.: `factory_symbols:materials/coal` - Coal symbol items.
- `factory_symbols:categories/<category>` - Symbol items of a specific category.
    - e.g. `factory_symbols:categories/arrow` - Arrow symbol items.
    - Find all categories in `common/src/main/resources/assets/factory_symbols/textures/item/symbol/` (and all the symbol variants within each subfolder).
