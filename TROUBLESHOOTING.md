# Troubleshooting Guide

This guide helps diagnose and fix common issues with the EMI-JEI-Paper-Bridge plugin.

## Accessing Logs

If you encounter errors or crashes, you can share your logs using mclo.gs:

1. Navigate to your server's `logs` folder
2. Upload `latest.log` to https://mclo.gs
3. Share the generated link for support

### Using the GitHub Action

We provide a GitHub Action workflow to fetch logs from mclo.gs:

1. Go to the Actions tab in the repository
2. Select "Fetch mclo.gs Logs" workflow
3. Click "Run workflow"
4. Enter the log ID (e.g., `BSeuv28` from `https://mclo.gs/BSeuv28`)
5. The log will be downloaded and displayed in the workflow output

## Common Issues

### Plugin Not Loading

**Symptoms:**
- Plugin doesn't appear in `/plugins` list
- No messages in console from EMI-JEI-Paper-Bridge

**Solutions:**
1. Verify you're using Paper 1.21.1 or compatible fork
2. Check that Java 21 is installed: `java -version`
3. Look for errors in `logs/latest.log`
4. Ensure the JAR file is in the `plugins` folder

### Items Not Being Given

**Symptoms:**
- Clicking items in EMI/JEI doesn't give them
- Permission errors when trying to use cheat mode

**Solutions:**
1. Check player has `emijeipb.cheat` permission
2. Verify `allow-cheat-mode: true` in `config.yml`
3. Check if item is blacklisted in configuration
4. Enable debug mode to see detailed logs

### Plugin Messaging Not Working

**Symptoms:**
- Client mods don't communicate with server
- No logs about plugin messages

**Solutions:**
1. Verify `protocol.enable-plugin-channels: true` in `config.yml`
2. Ensure client has EMI or JEI installed
3. Check that plugin channels are registered (look for "Registered EMI plugin channel" in logs)
4. Enable `logging.log-protocol-messages: true` to debug

### Permission Errors

**Symptoms:**
- "You don't have permission" messages
- Items can't be given despite having permissions

**Solutions:**
1. Grant `emijeipb.cheat` or `emijeipb.give` permission
2. For restricted items, grant the specific permission (e.g., `emijeipb.give.bedrock`)
3. Reload permissions with your permission plugin
4. Try `/emireload` to reload plugin configuration

## Debug Mode

Enable debug logging for detailed information:

```yaml
# config.yml
debug: true
logging:
  log-item-gives: true
  log-protocol-messages: true
```

After enabling, reload with `/emireload` and check console output.

## Reporting Issues

When reporting issues:

1. Upload your `latest.log` to https://mclo.gs
2. Share your `config.yml` (remove sensitive info)
3. List installed plugins with `/plugins`
4. Specify your Paper version and Java version
5. Describe what you expected vs. what happened
6. Include any client-side crash reports if applicable

## Known Limitations

1. **NBT Support**: NBT parsing is simplified. Complex NBT data may not work perfectly.
2. **Custom Items**: Requires compatible mod loader on server for full custom item support.
3. **Network**: Plugin messaging requires client mods to send compatible protocol messages.

## Getting Help

- GitHub Issues: https://github.com/dodi2020/EMI-JEI-Paper-Bridge/issues
- Wiki: https://github.com/dodi2020/EMI-JEI-Paper-Bridge/wiki
- Discord: (Add your Discord server link)

## Crash Reports

If you have crash reports from mclo.gs:

### For Server Crashes
Share the log ID and we'll analyze common issues like:
- Plugin conflicts
- Paper version incompatibility
- Java version mismatch

### For Client Crashes
Client crashes are usually not related to this server plugin, but may indicate:
- EMI/JEI mod version incompatibility
- Mod conflicts on client
- Incompatible Forge/Fabric version
