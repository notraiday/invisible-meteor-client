/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.*;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.commands.commands.*;
import meteordevelopment.meteorclient.systems.commands.commands.EnchantCommand;
import meteordevelopment.meteorclient.systems.commands.commands.GiveCommand;
import meteordevelopment.meteorclient.systems.commands.commands.LocateCommand;
import meteordevelopment.meteorclient.systems.commands.commands.ReloadCommand;
import meteordevelopment.meteorclient.systems.commands.commands.SayCommand;
import meteordevelopment.meteorclient.systems.commands.commands.SpectateCommand;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.*;

import java.util.*;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Commands extends System<Commands> {
    public static final DelegateCRM REGISTRY_ACCESS = new DelegateCRM();
    private final CommandDispatcher<FabricClientCommandSource> DISPATCHER = new CommandDispatcher<>();
    private final List<Command> commands = new ArrayList<>();
    private final Map<Class<? extends Command>, Command> commandInstances = new HashMap<>();

    public Commands() {
        super(null);
    }

    public static Commands get() {
        return Systems.get(Commands.class);
    }

    @Override
    public void init() {
        add(new BaritoneCommand());
        add(new VClipCommand());
        add(new HClipCommand());
        add(new DismountCommand());
        add(new DamageCommand());
        add(new DropCommand());
        add(new EnchantCommand());
        add(new FakePlayerCommand());
        add(new FriendsCommand());
        add(new CommandsCommand());
        add(new InventoryCommand());
        add(new LocateCommand());
        add(new NbtCommand());
        add(new NotebotCommand());
        add(new PeekCommand());
        add(new ProfilesCommand());
        add(new ReloadCommand());
        add(new ResetCommand());
        add(new SayCommand());
        add(new ServerCommand());
        add(new SwarmCommand());
        add(new ToggleCommand());
        add(new SettingCommand());
        add(new SpectateCommand());
        add(new GamemodeCommand());
        add(new SaveMapCommand());
        add(new ModulesCommand());
        add(new BindsCommand());
        add(new GiveCommand());
        add(new BindCommand());
        add(new FOVCommand());
        add(new RotationCommand());
        add(new WaypointCommand());

        commands.sort(Comparator.comparing(Command::getName));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            REGISTRY_ACCESS.setDelegate(registryAccess);
            Map<CommandNode<FabricClientCommandSource>, CommandNode<FabricClientCommandSource>> originalToCopy = new HashMap<>();
            originalToCopy.put(DISPATCHER.getRoot(), dispatcher.getRoot());
            copyChildren(DISPATCHER.getRoot(), dispatcher.getRoot(), originalToCopy);
        });
    }

    private static void copyChildren(CommandNode<FabricClientCommandSource> origin,
                                     CommandNode<FabricClientCommandSource> target,
                                     Map<CommandNode<FabricClientCommandSource>, CommandNode<FabricClientCommandSource>> originalToCopy) {
        for (CommandNode<FabricClientCommandSource> child : origin.getChildren()) {
            CommandNode<FabricClientCommandSource> result = child.createBuilder().build();
            originalToCopy.put(child, result);
            target.addChild(result);

            if (!child.getChildren().isEmpty()) {
                copyChildren(child, result, originalToCopy);
            }
        }
    }

    public void dispatch(String message) throws CommandSyntaxException {
        dispatch(message, new ChatCommandSource(mc));
    }

    public void dispatch(String message, CommandSource source) throws CommandSyntaxException {
        ParseResults<CommandSource> results = getDispatcher().parse(message, source);
        getDispatcher().execute(results);
    }

    public CommandDispatcher<CommandSource> getDispatcher() {
        return (CommandDispatcher<CommandSource>) (Object) DISPATCHER;
    }

    private final static class ChatCommandSource extends ClientCommandSource {
        public ChatCommandSource(MinecraftClient client) {
            super(null, client);
        }
    }

    public void add(Command command) {
        commands.removeIf(command1 -> command1.getName().equals(command.getName()));
        commandInstances.values().removeIf(command1 -> command1.getName().equals(command.getName()));

        command.registerTo(getDispatcher());
        commands.add(command);
        commandInstances.put(command.getClass(), command);
    }

    public int getCount() {
        return commands.size();
    }

    public List<Command> getAll() {
        return commands;
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T get(Class<T> klass) {
        return (T) commandInstances.get(klass);
    }
}
