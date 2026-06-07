If you're a dev, and you'd like to get access to this mod's methods to add hydration to your items or make any other tweak, here's one way to do it.

### Modifying the `build.gradle`
I still have no clue what goes on inside that file, but by using **CurseMaven** you can get this mod as one of the libs.

Add CurseMaven's repository to your repositories
```gradle
repositories {
     maven {
        url "https://cursemaven.com"
        content {
            includeGroup "curse.maven"
        }
    }
}
```

and add this mod to your dependencies
```gradle
dependencies {
    implementation fg.deobf("curse.maven:thirst-was-reclaimed-884742:file-id")
}
```

The first number there is CurseForge's mod id, and you need to change `file-id` to the number at the end of the url `https://www.curseforge.com/minecraft/mc-mods/thirst-was-reclaimed/files/...`, which corresponds to CurseForge's page of the file you want to use.

### Making the changes show up
You will need to re-generate your preferred IDE's runs, so for example run `gradlew genIntellijRuns` if you're using IntelliJ IDEA.