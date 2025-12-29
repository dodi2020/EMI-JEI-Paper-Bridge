# EMI-JEI-Paper-Bridge

A Paper plugin for Minecraft 1.21.1 that bridges EMI (Enhanced Material Information) and JEI (Just Enough Items) client-side mods with server functionality. This plugin enables full client mod features including item giving, cheat mode, and custom item support on Paper servers.

## Features

- **Item Giving/Cheat Mode Support**: Allows players with proper permissions to use EMI/JEI cheat mode to spawn items
- **Plugin Messaging Channels**: Implements custom plugin channels for communication with EMI and JEI mods
- **Custom Items Support**: Handles modded items when using hybrid server setups
- **Permission System**: Fine-grained permissions for item giving, cheat mode, and specific items
- **Item Restrictions**: Blacklist or restrict specific items from being given via cheat mode
- **Configurable**: Extensive configuration options for customizing behavior
- **Command Support**: Manual commands for giving items and reloading configuration

## Requirements

- Minecraft 1.21.1
- Paper server or compatible fork
- Java 21 or higher
- EMI and/or JEI installed on client

## Installation

1. Download the latest release from the [Releases](https://github.com/dodi2020/EMI-JEI-Paper-Bridge/releases) page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/EMI-JEI-Paper-Bridge/config.yml`
5. Grant permissions to players as needed

## Configuration

The plugin creates a `config.yml` file in `plugins/EMI-JEI-Paper-Bridge/` with the following options:

- `allow-cheat-mode`: Enable/disable cheat mode functionality
- `max-cheat-stack-size`: Maximum stack size for items given via cheat mode
- `allow-custom-items`: Enable support for modded custom items
- `protocol.enable-plugin-channels`: Enable plugin messaging channels
- `restrictions.blacklisted-items`: List of items that cannot be given
- `restrictions.restricted-items`: Items requiring specific permissions

See the default `config.yml` for more options and documentation.

## Permissions

- `emijeipb.*` - Grants all permissions
- `emijeipb.cheat` - Allows using cheat mode features from EMI/JEI
- `emijeipb.give` - Allows giving items to yourself
- `emijeipb.give.others` - Allows giving items to other players
- `emijeipb.reload` - Allows reloading the plugin configuration

### Item-Specific Permissions

Configure item-specific permissions in `config.yml` under `restrictions.restricted-items`:

```yaml
restrictions:
  restricted-items:
    'minecraft:bedrock': 'emijeipb.give.bedrock'
    'minecraft:command_block': 'emijeipb.give.commandblock'
```

## Commands

- `/emiitem <player> <item> [amount] [nbt]` - Give items to a player
  - Aliases: `/jeiitem`, `/giveitem`
  - Examples:
    - `/emiitem diamond 64` - Give yourself 64 diamonds
    - `/emiitem PlayerName iron_ingot 32` - Give PlayerName 32 iron ingots
- `/emireload` - Reload the plugin configuration
  - Alias: `/jeireload`

## How It Works

### EMI Support

EMI primarily uses vanilla `/give` commands for cheat mode. This plugin:
1. Listens for EMI plugin messages on the `emi:main` channel
2. Provides fallback support for item giving via plugin channels
3. Ensures proper permissions are checked before allowing item spawning

### JEI Support

JEI has a more established plugin messaging protocol. This plugin:
1. Listens for JEI plugin messages on the `jei:main` channel
2. Handles `GiveItem`, `RequestCheatPermission`, and `SetHotbarItem` messages
3. Sends cheat permission status to clients when they join
4. Processes item giving requests with proper validation

### Both Mods (Layered Runtime)

When both EMI and JEI are installed simultaneously:
- The plugin registers both plugin channels
- Messages are handled based on which mod sent them
- Permissions apply to both mods equally
- Configuration affects both mods

## Building from Source

```bash
git clone https://github.com/dodi2020/EMI-JEI-Paper-Bridge.git
cd EMI-JEI-Paper-Bridge
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## Compatibility

- **Server**: Paper 1.21.1 (may work on compatible forks like Purpur, Pufferfish)
- **Client Mods**:
  - EMI (all versions for 1.21.1)
  - JEI (all versions for 1.21.1)
  - Both together (layered runtime)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [EMI](https://github.com/emilyploszaj/emi) by emilyploszaj
- [JEI](https://github.com/mezz/JustEnoughItems) by mezz
- [JEIBridge](https://github.com/drunderscore/jeibridge) by drunderscore for protocol reference
- PaperMC team for the Paper server software

## Support

If you encounter any issues or have questions:
1. Check the [Wiki](https://github.com/dodi2020/EMI-JEI-Paper-Bridge/wiki)
2. Search [existing issues](https://github.com/dodi2020/EMI-JEI-Paper-Bridge/issues)
3. Create a new issue if needed

## Disclaimer

This plugin is not officially affiliated with EMI, JEI, or their developers. It is an independent bridge implementation for server-side support.