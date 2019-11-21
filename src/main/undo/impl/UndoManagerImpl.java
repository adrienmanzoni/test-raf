package undo.impl;

import java.util.Objects;
import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.Getter;
import undo.Change;
import undo.Document;
import undo.UndoManager;

/**
 * Default implementation of {@link UndoManager}.
 */
public class UndoManagerImpl implements UndoManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(UndoManagerImpl.class);

    /** Document. */
    @Getter(AccessLevel.PROTECTED)
    private Document doc;
    /** Number of changes stored. */
    @Getter(AccessLevel.PROTECTED)
    private int bufferSize;

    /** Changes queues. */
    @Getter(AccessLevel.PROTECTED)
    private Queue<Change> changes;
    /** Redd queues. */
    @Getter(AccessLevel.PROTECTED)
    private Queue<Change> redoes;

    public UndoManagerImpl(Document doc, int bufferSize) {
        this.doc = doc;
        this.bufferSize = bufferSize;

        this.changes = new CircularFifoQueue<>(bufferSize);
        this.redoes = new CircularFifoQueue<>(bufferSize);
    }

    @Override
    public void registerChange(Change change) {
        Objects.requireNonNull(change, "Cannot register a null change");

        LOG.debug("Adding change '{}' to the queue", change);
        this.changes.add(change);
    }

    @Override
    public boolean canUndo() {
        return !this.changes.isEmpty();
    }

    @Override
    public void undo() {
        if (this.canUndo()) {
            //Get the latest change & apply it
            Change lastChange = this.changes.poll();
            lastChange.apply(this.doc);

            //Add the change to the redoes list
            this.redoes.add(lastChange);
        } else {
            LOG.warn("The changes queue is empty");
        }
    }

    @Override
    public boolean canRedo() {
        return !this.redoes.isEmpty();
    }

    @Override
    public void redo() {
        if (this.canRedo()) {
            //Get the latest undo & revert it
            Change lastRedo = this.redoes.poll();
            lastRedo.revert(this.doc);

            //Add the redo to the changes list
            this.changes.add(lastRedo);
        } else {
            LOG.warn("The redoes queue is empty");
        }
    }
}