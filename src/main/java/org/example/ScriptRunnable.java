package org.example;

import java.util.AbstractQueue;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class ScriptRunnable implements Runnable {
    protected IScriptApi api;

    private final BlockingQueue<NpcActions> actions;

    private final BlockingQueue<String> messages;

    public ScriptRunnable(IScriptApi api) {
        this.actions = new ArrayBlockingQueue<>(1);
        this.messages = new ArrayBlockingQueue<>(1);
        this.api = api;
    }

    public void sendMessage(String msg) throws InterruptedException {
        try {
            this.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(this.nextAction() != NpcActions.NEXT) {
            throw new RuntimeException("Expected NEXT action");
        }
    }

    public int sendInputNumber(String msg) throws InterruptedException {
        this.setMessage(msg);
        if(this.nextAction() != NpcActions.INPUT_NUMBER) {
            throw new RuntimeException("Expected NEXT action");
        }

        // TODO extract number from action
        return 1;
    }

    public int sendInputNumber(String msg, int min, int max) throws InterruptedException {
        this.setMessage(msg);
        if(this.nextAction() != NpcActions.INPUT_NUMBER) {
            throw new RuntimeException("Expected NEXT action");
        }

        var num = 1;
        if (num < min || num > max) {
            throw new RuntimeException("Invalid number");
        }

        // TODO extract number from action
        return num;
    }

    public int sendSelect(String msg) throws InterruptedException {
        this.setMessage(msg);
        if(this.nextAction() != NpcActions.SELECT) {
            throw new RuntimeException("Expected NEXT action");
        }

        // TODO extract number from action
        return 1;
    }

    public <T> T sendSelect(List<T> options) throws InterruptedException {
        var n = options.size();
        var selected = this.sendSelect("Select one of " + n + " options");
        if(selected < 0 || selected >= n) {
            throw new RuntimeException("Invalid option");
        }

        return options.get(selected);
    }

    public void sendEnd(String msg) throws InterruptedException {
        if(this.nextAction() != NpcActions.END) {
            throw new RuntimeException("Expected END action");
        }
    }

    public void waitStart() throws InterruptedException {
        if(this.nextAction() != NpcActions.START) {
            throw new RuntimeException("Expected START action");
        }
    }


    public String waitMessage() throws InterruptedException {
        return this.messages.take();
    }

    public void setMessage(String message) throws InterruptedException {
        this.messages.put(message);
    }

    protected NpcActions nextAction() throws InterruptedException {
        return this.actions.take();
    }

    public void sendAction(NpcActions action) throws InterruptedException {
        this.actions.put(action);
    }

    public abstract void exec(IScriptApi api) throws InterruptedException;


    @Override
    public void run() {
        try {
            this.exec(this.api);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
