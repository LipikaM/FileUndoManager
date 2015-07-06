package com.assignment.FileUndoManager.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assignment.FileUndoManager.Service.DocumentService;
import com.assignment.FileUndoManager.impl.Change;
import com.assignment.FileUndoManager.impl.Document;


@Component
public class ChangeService implements Change{

	@Autowired
	private DocumentService docService;
	
	
	@Override
	public String getType() {
		String loggedType = docService.getTypeLogged();
		
		if(loggedType.equals("delete")){
			return "delete";
		}else if(loggedType.equals("insert")){
			return "insert";
		}else if(loggedType.equals("dotset")){
			return "dotset";
		}else{
			return "invalid";
		}
	
	}

	@Override
	public void apply(Document doc) {
		if(getType().equals("delete")){
			System.out.println("change has been applied");
		}else if(getType().equals("insert")){
			System.out.println("change has been applied");
		}else{
			throw new IllegalStateException();
		}
		
	}

	@Override
	public void revert(Document doc) {
		if(getType().equals("delete")){
			System.out.println("change has been reverted");
		}else if(getType().equals("insert")){
			System.out.println("change has been reverted");
		}else{
			throw new IllegalStateException();
		}
	}

}
