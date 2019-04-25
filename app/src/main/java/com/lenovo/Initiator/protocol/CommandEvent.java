package com.lenovo.Initiator.protocol;

public class CommandEvent {
    private String command;

    public CommandEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
