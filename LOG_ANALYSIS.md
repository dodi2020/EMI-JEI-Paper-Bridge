# Log Analysis Notes

## Unable to Access Provided Logs

The user provided two mclo.gs log URLs for analysis:
- https://mclo.gs/BSeuv28 (latest.log)
- https://mclo.gs/IbHMGbP (crash report)

Unfortunately, these URLs are blocked in the current development environment due to network restrictions. Both direct access via browser and API calls to `api.mclo.gs` are blocked.

## Alternative: GitHub Action Workflow

A GitHub Action workflow has been created (`fetch-logs.yml`) that can be run manually to fetch logs from mclo.gs when needed. This workflow runs in GitHub's infrastructure which has different network access.

To use it:
1. Go to Actions tab in GitHub
2. Select "Fetch mclo.gs Logs"
3. Click "Run workflow"
4. Enter the log ID (BSeuv28 or IbHMGbP)
5. View the log output in the workflow run

## What the Logs Likely Contain

Based on the context (EMI/JEI Paper bridge plugin), the logs likely show:

### Possible Issues in latest.log (BSeuv28)
- Plugin loading errors
- Missing dependencies
- Configuration errors
- Plugin messaging channel issues
- Permission errors
- Item giving failures

### Possible Issues in crash report (IbHMGbP)
- Client-side crash (EMI/JEI mod issue)
- Mod conflict
- Version incompatibility
- Protocol mismatch between client and server

## What We've Implemented to Address Potential Issues

1. **Robust Error Handling**
   - Try-catch blocks around plugin message handling
   - Graceful fallbacks when items can't be parsed
   - Detailed error logging with configurable verbosity

2. **Configuration Validation**
   - Safe defaults in config.yml
   - Null checks for all configuration values
   - Reload functionality to fix config without restart

3. **Permission System**
   - Fine-grained permissions for different actions
   - Item-specific restrictions
   - Blacklist system for dangerous items

4. **Debug Mode**
   - Extensive logging when debug: true
   - Protocol message logging
   - Item give action logging

5. **Events API**
   - Extensibility for other plugins
   - Ability to cancel/modify item giving
   - Source tracking (EMI, JEI, command)

## For the User

To help debug your issue:

1. **Enable debug mode** in config.yml:
   ```yaml
   debug: true
   logging:
     log-item-gives: true
     log-protocol-messages: true
   ```

2. **Reload the plugin**: `/emireload`

3. **Try the action that fails**

4. **Upload new log** to mclo.gs and share

5. **Check for specific errors**:
   - "Could not resolve" - Missing dependency
   - "Permission denied" - Permission issue
   - "Unknown plugin message" - Protocol mismatch
   - "Failed to parse" - Data format issue

## Common Solutions

### If plugin doesn't load:
- Verify Paper 1.21.1+
- Check Java 21 installed
- Look for dependency conflicts

### If item giving doesn't work:
- Grant `emijeipb.cheat` permission
- Check `allow-cheat-mode: true` in config
- Verify item isn't blacklisted
- Enable debug logging

### If client crashes:
- Update EMI/JEI to latest version
- Check mod compatibility
- Try without other mods to isolate issue
- This is likely a client-side mod issue, not server plugin

## Next Steps

Once logs are accessible:
1. Analyze specific error messages
2. Identify root cause
3. Implement targeted fixes
4. Add tests to prevent regression
5. Update documentation with solution
