# Contributing to EMI-JEI-Paper-Bridge

Thank you for your interest in contributing to EMI-JEI-Paper-Bridge! This document provides guidelines for contributing to the project.

## How to Contribute

### Reporting Bugs

When reporting bugs, please include:

1. **Server Environment**
   - Paper version (e.g., Paper 1.21.1)
   - Java version (run `java -version`)
   - Plugin version
   - Other relevant plugins

2. **Client Environment**
   - Minecraft version
   - Mod loader (Forge, Fabric, Quilt, NeoForge)
   - EMI/JEI version
   - Other client mods

3. **Steps to Reproduce**
   - Detailed steps to reproduce the issue
   - Expected behavior vs. actual behavior

4. **Logs**
   - Upload `latest.log` to https://mclo.gs
   - Include crash reports if applicable
   - Enable debug mode if possible

### Suggesting Features

Feature requests should include:

1. **Use Case** - Why is this feature needed?
2. **Implementation Ideas** - How might this work?
3. **Compatibility** - Which versions should this support?
4. **Alternative Solutions** - What workarounds exist today?

### Pull Requests

We welcome pull requests! Please:

1. **Fork the repository** and create a branch for your feature
2. **Follow code style** - Match the existing code conventions
3. **Test your changes** - Ensure the plugin builds and works
4. **Update documentation** - Add/update relevant docs
5. **Write clear commit messages** - Describe what and why

## Development Setup

### Prerequisites

- Java 21 JDK
- Gradle 8.5+ (or use the wrapper)
- Git
- IDE (IntelliJ IDEA recommended)

### Clone and Build

```bash
git clone https://github.com/dodi2020/EMI-JEI-Paper-Bridge.git
cd EMI-JEI-Paper-Bridge
./gradlew build
```

The compiled JAR will be in `build/libs/`.

### Project Structure

```
EMI-JEI-Paper-Bridge/
├── src/main/java/io/github/dodi2020/emijeipb/
│   ├── EMIJEIPaperBridge.java          # Main plugin class
│   ├── commands/                        # Command implementations
│   ├── events/                          # Custom events
│   ├── listeners/                       # Event listeners
│   └── managers/                        # Core functionality managers
├── src/main/resources/
│   ├── plugin.yml                       # Plugin metadata
│   └── config.yml                       # Default configuration
└── build.gradle                         # Build configuration
```

### Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Max 120 characters
- **Naming**: 
  - Classes: PascalCase
  - Methods/Variables: camelCase
  - Constants: UPPER_SNAKE_CASE
- **Comments**: Javadoc for public methods
- **Imports**: No wildcard imports

### Testing

While we don't have automated tests yet (contributions welcome!), please manually test:

1. **Build Success** - `./gradlew build` should succeed
2. **Plugin Loading** - Plugin loads on Paper 1.21.1
3. **Commands Work** - `/emiitem` and `/emireload` function correctly
4. **Permissions** - Permission checks work as expected
5. **Configuration** - Config changes take effect after reload

## Areas Needing Help

We especially welcome contributions in these areas:

1. **Testing Framework** - Add JUnit/Mockito tests
2. **NBT Parsing** - Improve NBT data handling
3. **Protocol Documentation** - Document EMI/JEI message formats
4. **Internationalization** - Add multi-language support
5. **Performance** - Optimize plugin message handling
6. **Documentation** - Improve/expand docs and examples

## Code of Conduct

### Our Standards

- **Be respectful** - Treat everyone with respect
- **Be constructive** - Provide helpful feedback
- **Be collaborative** - Work together towards solutions
- **Be patient** - Remember we're all volunteers

### Unacceptable Behavior

- Harassment or discrimination
- Trolling or insulting comments
- Publishing others' private information
- Other unprofessional conduct

## Questions?

- GitHub Discussions: https://github.com/dodi2020/EMI-JEI-Paper-Bridge/discussions
- Issues: https://github.com/dodi2020/EMI-JEI-Paper-Bridge/issues

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

Thank you for contributing to EMI-JEI-Paper-Bridge! 🎉
