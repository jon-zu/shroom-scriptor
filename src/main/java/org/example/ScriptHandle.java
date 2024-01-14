package org.example;

import java.util.Objects;

public class ScriptHandle {
    /// Virtual thread handle
    private final Thread thread;

    private final ScriptRunnable script;

    private String message;

    private int messageIndex;

    public ScriptHandle(ScriptRunnable scriptRunnable) {
        this.messageIndex = -1;
        // Set the runnable
        this.script = scriptRunnable;
        // Spawn the thread
        this.thread = Thread.ofVirtual()
                .name("Script")
                .start(scriptRunnable);
    }

    /// Stop the script
    public void stop() {
        this.thread.interrupt();
    }

    public boolean isRunning() {
        return this.thread.isAlive();
    }

    public void start() throws InterruptedException {
        this.script.sendAction(NpcActions.START);
    }

    /// Do a singular step for the script
    public void step(NpcActions action) throws InterruptedException {
        if(action == NpcActions.PREV) {
            if(Objects.equals(this.message, "long-message")) {
                if(this.messageIndex == 0 || this.messageIndex == -1) {
                    throw new RuntimeException("Invalid operation");
                }

                this.messageIndex += 1;
            }
        }

        this.script.sendAction(action);
        this.message = this.script.waitMessage();
    }

    public String getMessage() {
        return message;
    }
}
