Inside your minecraft folder, in the `config` folder, and inside the `thirst` folder, you'll find the `keyword.toml` file. This file allows for adding items faster but is less configurable.

### Keywords
Keywords allow for faster implementation of new thirst-quenching items. Every list of keywords is really just a **Regular Expression** (regex).

To not get too deep into regex, if an item's id (i.e. `farmersdelight:apple_cider`) contains a keyword (i.e. `apple`), the item will get selected. If you wanna learn more about regex, you can find a sick playground [here](https://regexr.com/).

### Drinks, Soups, and Fruits
To assign hydration and quenchness values faster, the config is split into 3 "lists" of keywords. Every item selected by a list is assigned the default hydration values for that list.

Each keyword is separated by a `|`. For example, here is the **Drinks** keyword list
```regex
(?:\\b|[^a-zA-Z])(drink|juice|tea|soda|coffee|wine|beer|cider|yogurt|milkshake|smoothie)(?:\\b|[^a-zA-Z])
```

### Blacklisting and Priorities
![Imgur](https://i.imgur.com/L0MngqW.png)

Since **any** item that contains a certain keyword will be given hydration values, and some mods add very peculiar items, to avoid bugs like this you can use the **Keyword Blacklist**. This is another list of keywords, but for items that must not be selected by other keyword lists.

Another important thing to mention is that this config has the **lowest priority**, meaning that any items that have hydration values added by code or other configs will also be ignored, so you dont have to worry about damaging existing ones.