package io.chocorean.MinecraftUpdater.core;

import io.chocorean.MinecraftUpdater.exceptions.InstallException;

/**
 * Interface for all installers
 * @param <T>  Type of return of the installer
 * @author mcdostone
 */
public interface Installer<T> {

    T install() throws InstallException;

}
