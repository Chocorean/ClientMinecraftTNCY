package io.chocorean.minecraft.updater.exceptions;

/**
 * Exception when there is a problem during an installation.
 *
 * @author mcdostone
 */
public class InstallException extends Exception {

    public InstallException() {
        super("Problem during the installation");
    }

    public InstallException(String msg) {
        super(msg);
    }
}
