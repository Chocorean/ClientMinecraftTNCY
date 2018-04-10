package io.chocorean.minecraft.updater.core;

import io.chocorean.minecraft.updater.exceptions.InstallException;

/**
 * Interface for all installers
 * @param <T>  Type of return of the installer
 * @author mcdostone
 */
public interface Installer<T> {

    T install() throws InstallException;

}
