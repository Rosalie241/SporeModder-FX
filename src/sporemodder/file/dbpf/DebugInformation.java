/****************************************************************************
* Copyright (C) 2019 Eric Mor
*
* This file is part of SporeModder FX.
*
* SporeModder FX is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
****************************************************************************/
package sporemodder.file.dbpf;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sporemodder.file.filestructures.FileStream;
import sporemodder.file.ResourceKey;
import sporemodder.file.prop.PropertyKey;
import sporemodder.file.prop.PropertyList;
import sporemodder.file.prop.PropertyString16;

public class DebugInformation {

	public static final String FOLDER_NAME = "_SporeModder";

	private String projectName;
	private String inputPath;
	
	private final List<String> fileNames = new ArrayList<String>();
	private final List<ResourceKey> fileKeys = new ArrayList<ResourceKey>();
	
	public DebugInformation(String projectName, String inputPath) {
		this.projectName = projectName;
		this.inputPath = inputPath;
	}
	
	public void addFile(String folderName, String fileName, int groupID, int instanceID, int typeID) {
		addFile(folderName, fileName, new ResourceKey(groupID, instanceID, typeID));
	}
	
	public void addFile(String folderName, String fileName, ResourceKey name) {
		fileNames.add(folderName + File.separator + fileName);
		fileKeys.add(name);
	}
	
	public void addFile(String text, ResourceKey name) {
		fileNames.add(text);
		fileKeys.add(name);
	}
	
	public void saveInformation(DBPFPacker packer) throws IOException {
		
		ResourceKey name = new ResourceKey();
		name.setGroupID(FOLDER_NAME);
		name.setInstanceID(projectName);
		name.setTypeID("prop");
		
		PropertyList prop = new PropertyList();
		prop.add("modDebugPath", new PropertyString16(inputPath));
		
		prop.add("modFilePaths", new PropertyString16(fileNames.toArray(new String[0])));
		prop.add("modFileKeys", new PropertyKey(fileKeys.toArray(new ResourceKey[0])));
		
		packer.writeFile(name, stream -> prop.write(stream));
	}
	
	public static void main(String[] args) throws IOException {
		
		try (FileStream stream = new FileStream("C:\\Users\\Eric\\Desktop\\test.prop", "r")) {
			
			PropertyList prop = new PropertyList();
			prop.read(stream);
		}
	}
}
