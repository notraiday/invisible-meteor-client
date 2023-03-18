/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.commands.commands.*;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.server.command.CommandManager;

import java.util.*;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Commands extends System<Commands> {
    public static final DelegateCRM REGISTRY_ACCESS = new DelegateCRM();

    public static final CommandDispatcher<FabricClientCommandSource> DISPATCHER = new CommandDispatcher<>();
    public static final FabricClientCommandSource COMMAND_SOURCE = (FabricClientCommandSource) new ClientCommandSource(null, mc);

    private final List<Command> commands = new ArrayList<>();

    public Commands() {
        super(null);
    }

    public static Commands get() {
        return Systems.get(Commands.class);
    }

    @Override
    public void init() {
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
        add(new EnderChestCommand());
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
        add(new MacroCommand());
        add(new ModulesCommand());
        add(new BindsCommand());
        add(new GiveCommand());
        add(new BindCommand());
        add(new FovCommand());
        add(new RotationCommand());
        add(new WaypointCommand());
        add(new InputCommand());

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

    public void add(Command command) {
        commands.removeIf(existing -> existing.getName().equals(command.getName()));
        command.registerTo((CommandDispatcher<CommandSource>) (Object) DISPATCHER);
        commands.add(command);
    }

    public void dispatch(String message) throws CommandSyntaxException {
        ParseResults<FabricClientCommandSource> results = DISPATCHER.parse(message, COMMAND_SOURCE);
        DISPATCHER.execute(results);
    }

    public List<Command> getAll() {
        return commands;
    }

    public Command get(String name) {
        for (Command command : commands) {
            if (command.getName().equals(name)) {
                return command;
            }
        }

        return null;
    }
}
