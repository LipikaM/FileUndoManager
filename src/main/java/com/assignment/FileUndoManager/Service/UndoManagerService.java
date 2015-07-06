package com.assignment.FileUndoManager.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assignment.FileUndoManager.impl.Change;
import com.assignment.FileUndoManager.impl.UndoManager;

@Component
public class UndoManagerService implements UndoManager{
	
	@Autowired
	private DocumentService doc;
	
	@Autowired
	private ChangeService changeSer;
	
	private List<String> registeredChnageType = new ArrayList<String>();
	
		
	public void registerChange(Change change) {
		registeredChnageType.add(change.getType());
	}
	
	public boolean canUndo(){
		if(!(changeSer.getType().equals("invalid"))){
			return true;
		}else{
			return false;
		}
	}
	
	
	public void undo(){
		if(canUndo()){
			modify();
		}else{
			throw new IllegalStateException("Undo operation can't be performed.");
		}
		
	}
	
	public boolean canRedo(){
		if(!(changeSer.getType().equals("invalid"))){
			return true;
		}else{
			return false;
		}
	}
	
	public void redo(){
		if(canRedo()){
			modify();
		}else{
			throw new IllegalStateException("Undo operation can't be performed.");
		}
	}
	
	public void modify(){
		File sourceFile = new File(doc.getFileName());
		File bkupFile = new File(doc.getBackupFileName());
		File deleteMe = new File(doc.getTempFile());
		
		sourceFile.renameTo(deleteMe);
		bkupFile.renameTo(sourceFile);
		deleteMe.renameTo(bkupFile);
	}

	public List<String> getRegisteredChnageType() {
		return registeredChnageType;
	}

	public void setRegisteredChnageType(List<String> registeredChnageType) {
		this.registeredChnageType = registeredChnageType;
	}

		
	
}
