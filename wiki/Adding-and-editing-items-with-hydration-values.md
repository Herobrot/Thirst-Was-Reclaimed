Inside your minecraft folder, in the `config` folder, and inside the `thirst` folder, you'll find the `item-settings.toml` file. By editing this file, you can manipulate which items can quench the player's thirst.

### Adding drinks / foods
You can add or modify an existing thirst-quenching item by adding (or modifying if already present) to the lists of items `drinks = [...` (for drinks) and `foods = [...` (for foods). The difference is that drinks only restore hydration, while foods also restore hunger. The format is `["item-id", hydration-amount, quenching-amount]`, where:
- `item-id` is the item's id, so for example `minecraft:apple` for the Apple
- `hydration-amount` is how many half-droplets to restore for item drank (e.g 3 means 1.5 droplets)
- `quenching-amount` the item's "quenchness", which is discussed more in detail in the [Hydration System](https://github.com/mlus-asuka/Thirst-Was-Reclaimed/wiki/Hydration-System) page

### Removing items
You can remove any items you don't want to interfere with a player's thirst both by deleting the item's entry in the `drinks` or `foods` list, or, in case the item is hard-coded in, by adding an entry to the `itemsBlacklist` list. The format for this list is `"item-id"`, so for example `"minecraft:apple"` for the Apple.