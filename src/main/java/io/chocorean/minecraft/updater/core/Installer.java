package io.chocorean.minecraft.updater.core;

/**
 * Interface for all installers
 * @param <T>  Type of return of the installer
 * @author mcdostone
 */
public interface Installer<T> {

    T install();

}
