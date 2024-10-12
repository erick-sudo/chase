To combine both the daemon service and the main application into a single `.deb` package using Gradle's `packageDeb` task, you can configure the Debian packaging process to include both components and ensure they work seamlessly together. Here's a high-level approach:

### 1. **Include the Daemon and Main Application in One Package**
You will need to ensure that both the daemon service and the main application are part of the same package structure. This involves packaging both the executable for the daemon and the main application together within the `.deb` file.

### 2. **Set Up Daemon Service in the Debian Package**
To configure the daemon service as part of the `.deb` package, you will need:
- A `systemd` service file to define how the daemon runs.
- The `systemd` service file should be placed in `/lib/systemd/system/` or `/etc/systemd/system/` within the Debian package.
- Ensure that the daemon executable is placed in an appropriate location (e.g., `/usr/bin/` or `/opt/yourapp/`).

### 3. **Package the Main Application**
Package the main application as part of the `.deb` file by placing its executable in the appropriate location (e.g., `/usr/bin/` or `/opt/yourapp/`).

### 4. **Gradle `packageDeb` Configuration**
In your `build.gradle.kts`, modify the `packageDeb` task to include both the daemon service and the main application.

You can do this using the `deb` block from the `os-package` plugin:

```kotlin
plugins {
    id("nebula.ospackage") version "9.3.0"
}

ospackage {
    packageName = "your-app"
    version = project.version.toString()
    release = "1"
    
    from(file("build/libs/your-app.jar")) {
        into("/opt/yourapp/")
    }

    // Add your systemd service
    from(file("src/main/resources/yourapp-daemon.service")) {
        into("/lib/systemd/system/")
        fileMode = 644
    }

    // Symlink the executable in /usr/bin
    link("/usr/bin/yourapp", "/opt/yourapp/your-app")
    
    postInstall("systemctl enable yourapp-daemon")
    postInstall("systemctl start yourapp-daemon")
    
    // Ensure daemon is removed properly
    postRemove("systemctl stop yourapp-daemon")
    postRemove("systemctl disable yourapp-daemon")
}
```

### 5. **Handle IPC Between the Daemon and Application**
You can continue using file-based IPC in the `sharedDirectory`. The daemon will handle the IPC communication, trigger the ProcessBuilder to open the main application or related windows based on IPC signals, and both can be managed by the user using systemd commands.

### 6. **Testing the Combined Package**
Once the `.deb` package is created, you can install it using:

```bash
sudo dpkg -i yourapp_version.deb
```

Then, verify that:
- The daemon is running using `systemctl status yourapp-daemon`.
- The main application launches when triggered by the daemon.

This setup will bundle the daemon and the main application together into a `.deb` file, with systemd handling the lifecycle of the daemon.