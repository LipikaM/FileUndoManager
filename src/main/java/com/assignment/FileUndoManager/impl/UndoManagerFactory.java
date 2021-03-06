package com.assignment.FileUndoManager.impl;

/**
 * A factory for {@link UndoManager}
 *
 */
public interface UndoManagerFactory {

	/**
	 * Creates an undo manager for a {@link Document}.
	 * 
	 * @param doc The document to create the {@link UndoManager} for.
	 * @param bufferSize The number of {@link Change}es stored.
	 * @return The {@link UndoManager} created.
	 */
	public UndoManager createUndoManager(Document doc, int bufferSize);
	
}
