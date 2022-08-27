# PreBot

PreBot is a Discord Bot Software made in java, PreBot alone does nothing,
but you add your features by using plugins! plugins design are similar to bukkit, with most things simplified to you!

# NOTE:
This project is still in alpha, if you have any suggestions feel free to open a feature request on this github!

# Installation

1. Download the latest dev build from the [Jenkins](https://ci.bluetree242.ml/job/PreBot),
   Download the `PreBot-VERSION.jar`. put it on your server root (you can use a java pterodactyl container egg).
2. Set up a start script that starts the bot
3. Start the bot, the configuration will be generated. Configure your token in `prebot.yml`
4. Put your plugins in the plugins folder
5. Start the bot again, it should go online.

# Building From Source

Use the following command to build PreBot from the source:

```bash
./gradlew build
```

Then you should find the standalone launcher in `launcher\build\libs`.

# Making a PreBot Plugin
See the Instructions for making a PreBot Plugin [here](MAKING_PLUGIN.md)