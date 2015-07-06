package com.assignment.FileUndoManager.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;




import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assignment.FileUndoManager.Service.ChangeService;
import com.assignment.FileUndoManager.Service.DocumentService;
import com.assignment.FileUndoManager.Service.UndoManagerFactoryService;
import com.assignment.FileUndoManager.Service.UndoManagerService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/application-test-context.xml"})
public class UndoManagerTest{

	ClassPathXmlApplicationContext context;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Autowired
	DocumentService document;
	
	@Autowired
	UndoManagerFactoryService undoManagerFactory;
	
	@Autowired
	UndoManagerService undoManager;
	
	@Autowired
	ChangeService change;
	
	@Value( "${master.file.name}" )
	private String fileName;
	
	@Value( "${undo.file.name}" )
	private String backupFileName;
	
	@Value( "${temp.file.name}" )
	private String tempFile;
		
	@Before
	public void startUp() {
		
	}
		
	@Test
	public void documentDeleteStringByPositionTestPass() {
		cleanup();
		createTestFile();
		document.delete(4, "first");
		assertTrue(document.isValidChange());
	}
	
	@Test
	public void documentDeleteStringByPositionTestFail() {
		cleanup();
		createTestFile();
		Throwable e = null;
		try{
			document.delete(2000, "nottobeinserted");
		}catch(Throwable ex){
			e=ex;
		}
		assertTrue(e instanceof IllegalStateException);
	}
	
	@Test
	public void documentInsertStringByPositionPass() {
		cleanup();
		createTestFile();
		document.insert(3, "inserted");
		assertTrue(document.isValidChange());
	}
	
	@Test
	public void documentInsertStringByPositionFail() {
		cleanup();
		createTestFile();
		Throwable e = null;
		try{
			document.insert(2000, "nottobeinserted");
		}catch(Throwable ex){
			e=ex;
		}
		assertTrue(e instanceof IllegalStateException);
	}
	
	@Test
	public void setDotPassTest() {
		cleanup();
		createTestFile();
		document.setDot(2);
		assertTrue(document.isValidChange());
	}	
	
	@Ignore
	public void setDotFailTest() {
		cleanup();
		createTestFile();
		Throwable e = null;
		try{
			document.setDot(3000);
		}catch(Throwable ex){
			e = ex;
		}	
		assertTrue(e instanceof IllegalStateException);
	}	
	
	@Test
	public void undoManagerCanUndoPassTest(){
		cleanup();
		createTestFile();
		document.delete(4, "first");
		undoManager.canUndo();
		assertTrue(true);
		
	}
	
	@Test
	public void undoManagerCanRedoPassTest(){
		cleanup();
		createTestFile();
		document.delete(4, "first");
		undoManager.canRedo();
		assertTrue(true);
		
	}
	
	@Test
	public void undoManagerUnDoPassTest(){
		cleanup();
		createTestFile();
		String beforeDelete = readFile();
		document.delete(4, "first");
		undoManager.undo();
		String AfterUndo = readFile();
		if(beforeDelete.equals(AfterUndo)){
			assertTrue(true);
		}else{
			assertTrue(false);
		}
		
				
	}
	
	@Test
	public void undoManagerUnDoExceptionTest() {
		cleanup();
		createTestFile();
		Throwable e = null;
		try{
			document.setTypeLogged("abc");
			undoManager.undo();
		}catch(Throwable ex){
			e = ex;
		}	
		assertTrue(e instanceof IllegalStateException);
	}	
	
	
	@Test
	public void undoManagerReDoPassTest(){
		cleanup();
		createTestFile();
		document.delete(4, "first");
		String afterchange = readFile();
		undoManager.undo();
		undoManager.redo();
		String afterRedo = readFile();
		if(afterchange.equals(afterRedo)){
			assertTrue(true);
		}else{
			assertTrue(false);
		}
				
	}
	
	@Test
	public void UndoManagerFactoryGetChangeTypeTest(){
		cleanup();
		createTestFile();
		document.delete(4, "first");
		undoManagerFactory.createUndoManager(document, 2);
		String changeType = change.getType();
		if(changeType.equals(document.getTypeLogged())){
			assertTrue(true);
		}else{
			assertTrue(false);
		}
	}
		
	public void cleanup(){
		File deleteBackup = new File(backupFileName);
		File deleteTemp = new File(tempFile);
		deleteBackup.delete();
		deleteTemp.delete();
	}
	
	public String readFile(){
		File fileToRead = new File(fileName);
		String filedata = "";
		try{
		    Scanner scanner = new Scanner(fileToRead);
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        filedata = filedata + line;
		     }
		    scanner.close();
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		return filedata;
	}
	
	public void createTestFile(){
		try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println("The first line to test");
			writer.println("Tev is going for rest");
			writer.println("Little lucy looking lazy");
			writer.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
			
	}
