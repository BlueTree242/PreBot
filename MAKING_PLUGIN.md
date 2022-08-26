# Requirements
1. An IDE
2. Java 8+
3. Experience with how java works

# Step 1: Init Project

1. Create a new Maven/Gradle Project
2. Add the repo and dependency to your build.gradle/pom.xml

## For Maven:
```xml
  <repositories>
        <repository>
            <id>bluetree242-repo</id>
            <url>https://repo.bluetree242.ml/repository/maven-public/</url>
        </repository>     
  </repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>me.bluetree242.prebot</groupId>
        <artifactId>core</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

Replace VERSION with the prebot version you use.

## For Gradle
```groovy
repositories {
    mavenCentral()
    maven {
        url "https://repo.bluetree242.ml/repository/maven-public/"
    }
}
```
```groovy
dependencies {
    compileOnly 'me.bluetree242.prebot:core:VERSION'
}
```

Replace VERSION with the prebot version you use.

# Step 2: Create your prebot.yml

Before writing any code, you need to add the description file of your plugin.
add a resource `prebot.yml` in your project resources, and here is an example:

```yaml
name: TestPlugin
version: 1.0
authors: [BlueTree242] #Must have atleast 1 author
dependencies: [] #other plugins that must be installed for this plugin to work
softdependencies: [] # Other plugins that your plugin might use if it finds it installed
main: package.to.main.MainClass #the main class of the plugin
required-intents: [] #Intents this plugin requries, they will be enabled when the bot starts
required-cache-flags: [] #Cache flags this plugin requires, they will be enabled when the bot starts
```

After creating your prebot.yml, you need to create your main class. **Your Main class must extend JarPlugin**

Example:

```java
public class MainClass extends JarPlugin {
    public void onEnable() {
        //this is called when this plugin is enabling
    }
    public void onDisable() {
        //this is called when this plugin is disabling
    }
    
    public void onShardReady(JDA shard) {
        //this is called when a jda shard is ready
    }
    public void onShardReconnect(JDA shard) {
        //this is called when a jda shard has reconnected
    }
}
```

# Logging
If you want to log something using your plugin, please use `getLogger()` method available in your main class instance.

# Configuration
Configuration in PreBot uses [DazzleConf](https://github.com/A248/DazzleConf). This library makes life with configuration easier

To create a config.yml, create a config interface (more details on dazzleconf's github).
Extend `PluginConfiguration` in your interface. here is an example

```java
public interface Config extends PluginConfig {

    @AnnotationBasedSorter.Order(10) //order this option to be first (10 and not 1 to ease if you want to add others above it later)
    @ConfDefault.DefaultString("It's Working!")
    @ConfComments("This is just a test!")
    String test();
}
```

After that, you must reload your config on enable, this is how:

```java
 public class MainClass extends JarPlugin {
    public void onEnable() {
        reloadConfig(Config.class);
        getLogger().info("Value: {}", getConfig().test()); //prints "It's Working!"
    }
    
    public Config getConfig() {
        return (Config) super.getConfig(); //return your Config and not PluginConfig
    }
}
```

## Multiple Configurations
Here is an example if you want your config to be named `otherconfig.yml`

```java
 public class MainClass extends JarPlugin {
    public void onEnable() {
        reloadConfig("otherconfig", Config.class);
        getLogger().info("Value: {}", getOtherConfig().test()); //prints "It's Working!"
    }
    
    public Config getOtherConfig() {
        return (Config) super.getConfig("otherconfig"); //return your Config and not PluginConfig
    }
}
```

# Listening to events

Event System is managed by [JDAEventer](https://github.com/BlueTree242/JDAEventer). To register a listener as a plugin:

```java
 public class MainClass extends JarPlugin {
    public void onEnable() {
        registerListeners(new ListenerClass()); 
    }
}
public class ListenerClass implements DiscordListener {
    @HandleEvent
    public void onMessage(MessageReceivedEvent event) {
        //do whatever
    }
    
    @HandleEvent
    public void onShardManagerBuild(ShardManagerPreBuildEvent event) {
        //a custom event we have, fired when we are about to build our shard manager.
    }
}
```

**NOTE: Listeners are removed when plugin is disabled, which means you will not receive events if the plugin is disabled**

# Custom Events

Custom events are simple in PreBot (and JDAEventer generally). All you have to do is to make
your event class, and annotate it with `CustomEvent`. you can fire it using `JDAEventer#fireEvent`.
You can get JDAEventer instance using `PreBot#getJDAEventer`. And you can get PreBot instance using `PreBot#getInstance` static method or `JarPlugin#getPreBot.
