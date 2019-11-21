package undo.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import undo.Document;
import undo.UndoManager;
import undo.UndoManagerFactory;

/**
 * Default implementation of {@link UndoManagerFactory}.
 */
public class UndoManagerFactoryImpl implements UndoManagerFactory {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(UndoManagerFactoryImpl.class);

    @Override
    public UndoManager createUndoManager(Document doc, int bufferSize) {
        Objects.requireNonNull(doc, "The document is mandatory");
        if (bufferSize < 0) {
            throw new IllegalArgumentException("The buffer size must be greater than 0");
        }

        LOG.debug("Creating a instance of UndoManager with a buffer size of '{}'", bufferSize);
        return new UndoManagerImpl(doc, bufferSize);
    }
}
