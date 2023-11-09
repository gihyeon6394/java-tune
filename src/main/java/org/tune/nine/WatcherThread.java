package org.tune.nine;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatcherThread extends Thread {
    String dirName;

    public WatcherThread(String dirName) {
        this.dirName = dirName;
    }

    public void run() {
        System.out.println("WatcherThread started");
        watchFile();
        System.out.println("WatcherThread ended");
    }

    private void watchFile() {

        Path dir = Paths.get(dirName);
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

            dir.register(watchService, java.nio.file.StandardWatchEventKinds.ENTRY_CREATE,
                    java.nio.file.StandardWatchEventKinds.ENTRY_DELETE,
                    java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;

            for (int i = 0; i < 4; i++) {
                key = watchService.take();
                String watchedTime = new Date().toString();
                List<WatchEvent<?>> events = key.pollEvents();

                events.stream().forEach(event -> {
                    Path name = (Path) event.context();
                    if (event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("Created: " + name + " " + watchedTime);
                    } else if (event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("Delete: " + name + " " + watchedTime);
                    } else if (event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("Modify: " + name + " " + watchedTime);
                    }
                });

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
