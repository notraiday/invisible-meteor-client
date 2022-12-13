/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2022 Meteor Development.
 */

package meteordevelopment.meteorclient.systems.commands;

import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.command.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;

import java.util.*;
import java.util.stream.*;

public class DelegateCRM implements CommandRegistryAccess, CommandRegistryAccess.EntryListCreationPolicySettable {
    private CommandRegistryAccess delegate;
    private EntryListCreationPolicy entryListCreationPolicy;

    @Override
    public <T> RegistryWrapper<T> createWrapper(RegistryKey<? extends Registry<T>> registryRef) {
        return new RegistryWrapper<T>() {
            RegistryWrapper<T> delegateWrapper;

            private RegistryWrapper<T> getDelegate() {
                if (delegateWrapper != null) return delegateWrapper;
                if (delegate == null) throw new NullPointerException("Got null pointer delegate in command construction");
                return delegateWrapper = delegate.createWrapper(registryRef);
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
                return getDelegate().getOptional(key);
            }

            @Override
            public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
                return getDelegate().getOptional(tag);
            }

            @Override
            public Stream<RegistryEntry.Reference<T>> streamEntries() {
                return getDelegate().streamEntries();
            }

            @Override
            public Stream<RegistryKey<T>> streamKeys() {
                return getDelegate().streamKeys();
            }

            @Override
            public Stream<RegistryEntryList.Named<T>> streamTags() {
                return getDelegate().streamTags();
            }
        };
    }

    public void setDelegate(CommandRegistryAccess delegate) {
        this.delegate = delegate;
        if (entryListCreationPolicy != null) {
            if (delegate instanceof EntryListCreationPolicySettable elcps) {
                elcps.setEntryListCreationPolicy(entryListCreationPolicy);
            } else {
                MeteorClient.LOG.error("Could not set up delegate CRM: not an EntryListCreationPolicySettable");
            }
        }
    }

    @Override
    public void setEntryListCreationPolicy(EntryListCreationPolicy entryListCreationPolicy) {
        this.entryListCreationPolicy = entryListCreationPolicy;
    }
}
