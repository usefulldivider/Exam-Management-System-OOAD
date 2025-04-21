package com.exam.exam_management_system.commands;

public class PaperCommandInvoker {
    public void executeCommand(PaperCommand command) {
        command.execute();
    }
} 