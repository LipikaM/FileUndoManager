# Implemented Class Description.

DocumentService(): It’s the main processing file which performs delete(), insert() and setDot() operations inside the file for the passed String and position.
 
ChangeService(): Get the type of the changes and register the changes inside the UndoManager for further undo and redo operations.
 
UndoManagerService(): Performs the operations of redo and undo the changes. Throws an exception in case the changes are not valid for the respective operations.
 
UndoManagerFactoryService: Returns the created undo Manager.
 
UndoManagerTest: It’s the Junit test class to trigger the processing.
 
application-config.properties : Configuration file for the file name. Tied up with Spring.
 
application-test-context.xml : Spring context file.
 
Pom.xml : Maven configuration file.
 
 
All the test cases can be verified by the UndoManagerTest class. It is creating the file performing the operation, tracking, undo, redo and verifying the result.
