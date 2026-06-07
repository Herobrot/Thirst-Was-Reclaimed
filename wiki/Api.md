Here we are, good luck looking through my code. Here is some helpful info on some of the methods you can use

### Adding thirst to an item via code
You can add hydration and "quenchness" to a drink or an item by subscribing to the `RegisterThirstValueEvent`. This event is fired during mod setup, and provides the following methods:

```java
@SubscribeEvent
public void onRegisterThirstValue(RegisterThirstValueEvent event)
{
    event.addDrink(MyModItems.MY_DRINK.get(), 6, 8);   // treats it as a drink (no hunger restored)
    event.addFood(MyModItems.MY_FOOD.get(), 2, 3);     // treats it as food (also restores hunger)
}
```

| Method | Description |
|--------|-------------|
| `addDrink(Item item, int thirst, int quenched)` | Adds a drink. Only restores hydration, not hunger. |
| `addFood(Item item, int thirst, int quenched)` | Adds a food. Restores both hydration and hunger. |
| `addContainer(ContainerWithPurity container)` | Registers a custom water container with empty/filled pair. |
| `addContainer(Item item)` | Simple version: registers an item as a water container. |

The player will still be able to overwrite your changes by editing the config.

> **Note:** The old static methods `ThirstHelper.addDrink()` and `ThirstHelper.addFood()` are deprecated. Please use the event-based API above.

### The WaterPurity class
This class contains a lot of methods relative to purity.

```java
ItemStack getFilledContainer(ItemStack container, boolean fromFilled)
```
> Given an `ItemStack`, if the item is a `ContainerWithPurity`, returns the filled equivalent of the water container given in input. The second parameter specifies if the container inputted is the empty or filled version.

```java
boolean isWaterFilledContainer(ItemStack item)
```
> Returns if the item is a filled water container.

```java
boolean isEmptyWaterContainer(ItemStack item)
```
> Returns if the item is an empty water container.

```java
int getPurity(ItemStack item) & int getPurity(FluidStack fluid)
```
> Returns the purity level [0-3] of an `ItemStack` or a `FluidStack`

```java
boolean hasPurity(ItemStack item) & boolean hasPurity(FluidStack fluid)
```
> Returns whether the item or fluid has a purity value in its tag.

```java
ItemStack addPurity(ItemStack item, int purity) and FluidStack addPurity(FluidStack fluid, int purity)
```
> Sets the specified purity of the item or fluid by adding it to its tag.

```java
String getPurityText(int purity)
```
> Returns the text string corresponding to the inputted purity (already translated in the language selected by the player).

```java
int getPurityColor(int purity)
```
> Returns the decimal color value corresponding to the inputted purity.
