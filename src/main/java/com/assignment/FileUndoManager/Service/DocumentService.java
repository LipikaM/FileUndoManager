package com.assignment.FileUndoManager.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.assignment.FileUndoManager.impl.Document;


@Component(value="documentOperation")
public class DocumentService implements Document{

	@Value( "${master.file.name}" )
	private String fileName;
	
	@Value( "${undo.file.name}" )
	private String backupFileName;
	
	@Value( "${temp.file.name}" )
	private String tempFile;
	
		
	@Autowired
	private ChangeService change;
	
	private String typeLogged;
	private boolean validChange=false;
	
		
	
	@Override
	public void delete(int pos, String s) {
		String line="";
		RandomAccessFile fileToRead = null;
		PrintWriter writer=null;
		String charset = "UTF-8";
					
		validChange = validChanges(pos,s);
		if(validChange){
		try{
			typeLogged="delete";
			fileToRead = new RandomAccessFile(fileName, "rw");
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(backupFileName), charset));
			while((line=fileToRead.readLine())!=null){
				writer.println(line);
			}
			fileToRead.seek(pos);
			byte[] bytes = new byte[s.length()];
			fileToRead.write(bytes);
			}catch (Exception e) {
		      e.printStackTrace();
		   }finally{
			   try{
				   fileToRead.close();
			   writer.close();
			   }catch(IOException ex){
				   ex.printStackTrace();
			   }
		   }
			
	}
}
	
	public boolean validChanges(int pos, String s){
		RandomAccessFile fileToRead=null;
		boolean found=false;
		try{
			fileToRead = new RandomAccessFile(fileName, "rw");
			double filelength = fileToRead.length();
			if(pos>filelength){
				found=false;
			}else{
				fileToRead.seek(pos);
				byte[] bytes = new byte[s.length()];
				fileToRead.readFully(bytes);
				String str = new String(bytes, "Windows-1252");
				if(str.equals(s)){
					found=true;
				}else{
					found=false;
				}
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			try{
			fileToRead.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		if(!found){
			throw new IllegalStateException("Sring not found");
		}else{
			return true;
		}
		
		
	}
	
	public boolean validToInsert(int pos, String s){
		RandomAccessFile fileToRead=null;
		PrintWriter writer = null;
		String line="";
		String charset = "UTF-8";
		
		boolean found=false;
		try{
			fileToRead = new RandomAccessFile(fileName, "rw");
			double filelength = fileToRead.length();
			if(pos>filelength){
				found=false;
			}else{
				found=true;
				writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(backupFileName), charset));
				while((line=fileToRead.readLine())!=null){
					writer.println(line);
				}
			}
			
			
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			try{
			fileToRead.close();
			if(found){
				writer.close();
			}
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		if(!found){
			throw new IllegalStateException("Position is illegal to insert");
		}else{
			return true;
		}
		
		
	}
	
	@Override
	public void insert(int pos, String s) {
		RandomAccessFile fileToRead=null;
		RandomAccessFile fileToWrite=null;
		FileChannel sourceChannel = null;
		FileChannel targetChannel = null;
		File temp = null;
		validChange = validToInsert(pos,s);
		if(validChange){
		try{
			typeLogged="insert";
			temp = new File(tempFile);
			fileToRead = new RandomAccessFile(new File(fileName), "rw");
			fileToWrite  = new RandomAccessFile(temp, "rw");
			long fileSize = fileToRead.length();
			sourceChannel = fileToRead.getChannel();
			targetChannel = fileToWrite.getChannel();
			sourceChannel.transferTo(pos, (fileSize - pos), targetChannel);
			sourceChannel.truncate(pos);
			fileToRead.seek(pos);
			fileToRead.writeBytes(s);
			long newOffset = fileToRead.getFilePointer();
			targetChannel.position(0L);
			sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - pos));
			
			}catch (Exception e) {
		      e.printStackTrace();
		   }finally{
			   try{
				   sourceChannel.close();
				   targetChannel.close();
				   fileToRead.close();
				   fileToWrite.close();
				   temp.delete();
			
			   }catch(IOException ex){
				   ex.printStackTrace();
			   }
		   }
			
	}
}

	@Override
	public void setDot(int pos) {
		RandomAccessFile fileToRead = null;
		try {
			fileToRead = new RandomAccessFile(fileName, "rw");
			double filelength = fileToRead.length();
			if(pos>filelength){
				throw new IllegalStateException("Illegal Position.");
			} else {
				fileToRead.seek(pos);
				validChange=true;
				typeLogged="dotset";
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fileToRead.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public String getBackupFileName() {
		return backupFileName;
	}

	public void setBackupFileName(String backupFileName) {
		this.backupFileName = backupFileName;
	}

	public String getTempFile() {
		return tempFile;
	}

	public void setTempFile(String tempFile) {
		this.tempFile = tempFile;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTypeLogged() {
		return typeLogged;
	}

	public void setTypeLogged(String typeLogged) {
		this.typeLogged = typeLogged;
	}

	public boolean isValidChange() {
		return validChange;
	}

	public void setValidChange(boolean validChange) {
		this.validChange = validChange;
	}


	
}
