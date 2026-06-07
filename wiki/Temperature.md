### Basic rules
The speed at which a player's hydration decreases is influenced by the biome the player is currently in:
- a biome's **temperature** matters, in that, **hot** biomes will make the player thirsty quicker than normal, and  **cold** ones will slow the process down. The standard temperature is the one found in **plains**.
- a biome's **humidity** matters, in that, **arid** biomes will make the player thirsty quicker than normal and  **humid** ones will slow the process down. The standard humidity is the one found in **plains**.
- **humidty** matters twice as much as **temperature**
- the **Nether**, no matter the biome, will **triple** the thirst depletion speed.

This system uses vanilla biomes' humidity and temperature values, meaning other modded biomes _should hopefully_ work as well.

### Cold Sweat integration
The mod [Cold Sweat](https://www.curseforge.com/minecraft/mc-mods/cold-sweat) adds a very accurate player temperature calculation, thus if installed, the player temperature determined by the mod will be used to calculate the hydration depletion speed.