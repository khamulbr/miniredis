package com.alessandrodias.miniredis.model;

import java.util.Arrays;
import java.util.List;

public class MiniRedisInstruction {
    private MiniRedisCommand miniRedisCommand;
    private List<String> arguments;

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public MiniRedisInstruction parse(String instructionReceived) {
        List<String> instruction = Arrays.asList(instructionReceived.split(" "));
        if (instruction.size() > 1) {
            miniRedisCommand.setCommand(instruction.get(0));
            arguments = instruction.subList(1, instruction.size() - 1);
        } else if (instruction.size() > 0) {
            miniRedisCommand.setCommand(instruction.get(0));
        }
        return this;
    }

    public MiniRedisCommand getMiniRedisCommand() {
        return miniRedisCommand;
    }

    public void setMiniRedisCommand(MiniRedisCommand miniRedisCommand) {
        this.miniRedisCommand = miniRedisCommand;
    }
}
