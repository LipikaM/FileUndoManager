package com.assignment.FileUndoManager.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignment.FileUndoManager.impl.Change;
import com.assignment.FileUndoManager.impl.ChangeFactory;


public class ChangeFactoryService implements ChangeFactory {
	
	@Autowired
	private ChangeService change;
		
	@Override
	public Change createDeletion(int pos, String s, int oldDot, int newDot) {
		return change;
	}

	@Override
	public Change createInsertion(int pos, String s, int oldDot, int newDot) {
		return change;
	}

}
