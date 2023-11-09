package org.tune.nine;

public class WatcherSample {

    public static void main(String[] args) {
        WatcherThread watcherThread = new WatcherThread("/Users/gimgihyeon/Desktop/kghworks/dev/workspace_intellij/JavaTune/contents");
        watcherThread.start();
    }
}
