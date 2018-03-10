package io.chocorean.MinecraftUpdater.core;

import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 * Proxy class which enables to have the download progression.
 *
 * @author mcdostone
 */
public class CallbackByteChannel implements ReadableByteChannel {

    private final ProgressBar progression;
    private final long size;
    private final ReadableByteChannel rbc;
    private long sizeRead;

    public CallbackByteChannel(ReadableByteChannel rbc, long expectedSize, ProgressBar bar) {
        this.size = expectedSize;
        this.rbc = rbc;
        this.progression = bar;
    }

    public void close() throws IOException {
        rbc.close();
    }

    public boolean isOpen() {
        return rbc.isOpen();
    }

    @Override
    public int read(ByteBuffer bb) throws IOException {
        int n;
        double progress;
        if ((n = rbc.read(bb)) > 0) {
            sizeRead += n;
            progress = size > 0 ? (double) sizeRead / (double) size : -1.0;
            this.progression.setProgress(progress);
        }
        return n;
    }
}