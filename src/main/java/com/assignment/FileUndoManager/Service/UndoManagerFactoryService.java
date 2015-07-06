package com.assignment.FileUndoManager.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assignment.FileUndoManager.Service.UndoManagerService;
import com.assignment.FileUndoManager.impl.Document;
import com.assignment.FileUndoManager.impl.UndoManager;
import com.assignment.FileUndoManager.impl.UndoManagerFactory;

@Component
public class UndoManagerFactoryService implements UndoManagerFactory{
	
	@Autowired
	private UndoManagerService unDoManagerSer; 
	
	@Autowired
	private ChangeService changeSer;
	
			
	@Override
	public UndoManager createUndoManager(Document doc, int bufferSize) {
		unDoManagerSer.registerChange(changeSer);
		unDoManagerSer.undo();
		return new UndoManagerService();
	}


}
