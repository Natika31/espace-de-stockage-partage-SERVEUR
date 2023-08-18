package com.projet.edp.DTO;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.projet.edp.fileTree.domain.Directory;
import com.projet.edp.fileTree.domain.FileContent;
import com.projet.edp.fileTree.domain.TreeItem;
import com.projet.edp.fileTree.domain.MyFile;
import com.projet.edp.fileTree.dto.DirectoryDTO;
import com.projet.edp.fileTree.dto.DirectoryDTOConversion;
import com.projet.edp.fileTree.dto.FileDTO;
import com.projet.edp.fileTree.dto.FileDTOConversion;
import com.projet.edp.fileTree.dto.TreeItemDTO;
import com.projet.edp.fileTree.dto.TreeDTOConversion;
import com.projet.edp.fileTree.service.FileContentService;
import com.projet.edp.fileTree.service.FileContentServiceImpl;

class TreeDTOConversionTest {

	private static TreeDTOConversion treeItemDTOConversion ;

	@BeforeAll
	static void setup() {
		treeItemDTOConversion = new TreeDTOConversion();
	}

	private Directory dItem1;

	@Test
	void test_GivenFileItem_WhenConvertEntityToDTO_ThenReturnFileItemDTO() throws FileNotFoundException, IOException  {
		//Given a file item
		FileContent fileContent = new FileContent();
		byte[] binaryArray = fileContent.convertInputFileToBinaryArray("C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf");
		fileContent.setBinary_content(binaryArray);
		TreeItem fItem = new MyFile("file", "/home/file.pdf","pdf","C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf", fileContent ); 
		fItem.setItem_id(1L);

		//When convert file item to file item DTO
		TreeItemDTO itemDTO = treeItemDTOConversion.convertEntityToDTO(fItem);

		//Then return item DTO
		assertNotNull(itemDTO.getItem_id());
		assertEquals(fItem.getItem_id(), Long.valueOf(itemDTO.getItem_id()));
		assertNotNull(itemDTO.getItem_name());
		assertEquals(fItem.getItem_name(), itemDTO.getItem_name());
		assertEquals(fItem.getItem_local_path(), itemDTO.getItem_local_path());
		assertEquals("file",itemDTO.getItem_type());
	}

	@Test
	void test_GivenEmptyDirectoryItem_WhenConvertEntityToDTO_ThenReturnDirItemDTOWithZeroChildren() throws FileNotFoundException, IOException  {
		//Given a directory item
		TreeItem dItem = new Directory("/home/dir/", "dir");
		dItem.setItem_id(1L);

		//When convert directory item to directory item  DTO
		TreeItemDTO dItemDTO = treeItemDTOConversion.convertEntityToDTO(dItem);		

		//Then return file DTO
		assertNotNull(dItemDTO.getItem_id());
		assertEquals(dItem.getItem_id(), Long.valueOf(dItemDTO.getItem_id()));
		assertNotNull(dItemDTO.getItem_name());
		assertEquals(dItem.getItem_name(), dItemDTO.getItem_name());
		assertEquals(dItem.getItem_local_path(), dItemDTO.getItem_local_path());
		assertEquals("folder", dItemDTO.getItem_type());
		assertEquals(((Directory) dItem).getChildren().size(), dItemDTO.getChildren().size());

	}

	@Test
	void test_GivenDirectoryContainsOneFileItem_WhenConvertEntityToDTO_ThenReturnDirectoryDTOContainsOneFileDTO() throws FileNotFoundException, IOException {
		//Given a directory item
		TreeItem dItem = new Directory("/home/dir/", "dir");
		dItem.setItem_id(1L);
		//Given a file item
		FileContent fileContent = new FileContent();
		byte[] binaryArray = fileContent.convertInputFileToBinaryArray("C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf");
		fileContent.setBinary_content(binaryArray);
		TreeItem childItem = new MyFile("file", "/home/dir/file.pdf","pdf","C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf", fileContent ); 
		childItem.setItem_id(2L);
		//add a child to parent directory's children list
		((Directory) dItem).addChildren(childItem);

		//When convert directory to directory item DTO
		TreeItemDTO itemDTO = treeItemDTOConversion.convertEntityToDTO(dItem);

		//Then return parent and child item DTO
		assertNotNull(itemDTO.getItem_id());
		assertEquals(dItem.getItem_id(), Long.valueOf(itemDTO.getItem_id()));
		assertNotNull(itemDTO.getItem_name());
		assertEquals(dItem.getItem_name(), itemDTO.getItem_name());
		assertEquals(dItem.getItem_local_path(), itemDTO.getItem_local_path());
		assertEquals("folder", itemDTO.getItem_type());
		assertEquals(((Directory) dItem).getChildren().size(), itemDTO.getChildren().size());
		assertNotNull(itemDTO.getChildren().get(0).getItem_id());
		assertEquals(((Directory) dItem).getChildren().get(0).getItem_id(), Long.valueOf(itemDTO.getChildren().get(0).getItem_id()));
		assertNotNull(itemDTO.getChildren().get(0).getItem_name());
		assertEquals(((Directory) dItem).getChildren().get(0).getItem_name(), itemDTO.getChildren().get(0).getItem_name());
		assertEquals(((Directory) dItem).getChildren().get(0).getClass().toString() , itemDTO.getChildren().get(0).getItem_type());


	}

	@Test
	void test_GivenDirectoryContainsOneDirectory_WhenConvertEntityToDTO_ThenReturnDirectoryDTOContainsOneDirectoryDTO() throws FileNotFoundException, IOException {
		//Given two directory items
		Directory parentDirectory = new Directory("/home/dir1/", "dir1");
		parentDirectory.setItem_id(1L);
		//Given a directory item
		Directory childDirectory = new Directory("/home/dir1/dir2/", "dir2");
		childDirectory.setItem_id(2L);
		//add a child to parent directory's children list
		TreeItem childItem = childDirectory;
		parentDirectory.addChildren(childItem);

		//When convert directory to directory item DTO
		TreeItemDTO itemDTO = treeItemDTOConversion.convertEntityToDTO(parentDirectory);

		//Then return parent and child item DTO
		assertNotNull(itemDTO.getItem_id());
		assertEquals(parentDirectory.getItem_id(), Long.valueOf(itemDTO.getItem_id()));
		assertNotNull(itemDTO.getItem_name());
		assertEquals(parentDirectory.getItem_name(), itemDTO.getItem_name());
		assertEquals(parentDirectory.getItem_local_path(), itemDTO.getItem_local_path());
		assertNotNull(childItem.getItem_name());
		assertEquals(childItem.getItem_id(),Long.valueOf(itemDTO.getChildren().get(0).getItem_id()));
		assertNotNull(childItem.getItem_name());
		assertEquals(childItem.getItem_name(),itemDTO.getChildren().get(0).getItem_name());
		assertNotNull(childDirectory.getChildren());
		assertEquals(childDirectory.getChildren().size(), itemDTO.getChildren().get(0).getChildren().size());
	}

	@Test
	void test_GivenDirectoryContainsOneDirectoryContainsOneFile_WhenConvertEntityToDTO_ThenReturnDirectoryDTOContainsOneDirectoryDTOContainsOneFileDTO() throws FileNotFoundException, IOException {
		//Given two directory items
		TreeItem parentItem = new Directory("/home/dir1/", "dir1");
		parentItem.setItem_id(1L);
		TreeItem childDItem = new Directory("/home/dir1/dir2/", "dir2");
		childDItem.setItem_id(2L);
		//Given a file item
		FileContent fileContent = new FileContent();
		byte[] binaryArray = fileContent.convertInputFileToBinaryArray("C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf");
		fileContent.setBinary_content(binaryArray);
		TreeItem childFileItem = new MyFile("file", "/home/dir/file.pdf","pdf","C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf", fileContent ); 
		childFileItem.setItem_id(3L);
		//add a child to child directory's children list
		Directory childDir = (Directory) childDItem;
		childDir.addChildren(childFileItem);
		//add a child to parent directory's children list
		Directory parentDir = (Directory) parentItem;
		parentDir.addChildren(childDItem);

		//When convert directory to directory item DTO
		TreeItemDTO itemDTO = treeItemDTOConversion.convertEntityToDTO(parentItem);

		assertNotNull(parentItem.getItem_id());
		assertNotNull(parentItem.getItem_name());
		assertEquals(parentItem.getItem_id(), Long.valueOf(itemDTO.getItem_id()));
		assertNotNull(parentItem.getItem_name());
		assertEquals(parentItem.getItem_name(), itemDTO.getItem_name());
		assertEquals(parentItem.getItem_local_path(), itemDTO.getItem_local_path());
		assertNotNull(itemDTO.getChildren().get(0).getItem_id());
		assertEquals(parentDir.getChildren().get(0).getItem_id(),Long.valueOf(itemDTO.getChildren().get(0).getItem_id()));
		assertNotNull(parentDir.getChildren().get(0).getItem_name());
		assertEquals(parentDir.getChildren().get(0).getItem_name(),itemDTO.getChildren().get(0).getItem_name());
		assertNotNull(((Directory) parentDir.getChildren().get(0)).getChildren());
		assertEquals(((Directory) parentDir.getChildren().get(0)).getChildren().size(), itemDTO.getChildren().get(0).getChildren().size());
		assertNotNull(itemDTO.getChildren().get(0).getChildren().get(0));
		assertNotNull(((Directory) parentDir.getChildren().get(0)).getChildren().get(0).getItem_id());
		assertEquals(((Directory) parentDir.getChildren().get(0)).getChildren().get(0).getItem_id(),Long.valueOf(itemDTO.getChildren().get(0).getChildren().get(0).getItem_id()));
		assertNotNull(((Directory) parentDir.getChildren().get(0)).getChildren().get(0).getItem_name());
		assertEquals(((Directory) parentDir.getChildren().get(0)).getChildren().get(0).getItem_name(),itemDTO.getChildren().get(0).getChildren().get(0).getItem_name());
		assertNotNull(((MyFile) ((Directory) parentDir.getChildren().get(0)).getChildren().get(0)).getFile_format());

	}

	@Test
	void test_GivenDirectoryContainsOneDirectoryAndOneFile_WhenConvertEntityToDTO_ThenReturnDirectoryDTOContainsOneDirectoryDTOAndOneFileDTO() throws FileNotFoundException, IOException {
		//Given two directory items
		TreeItem dItem1 = new Directory("/home/dir1/", "dir1");
		dItem1.setItem_id(1L);
		TreeItem dItem2 = new Directory("/home/dir1/dir2/", "dir2");
		dItem2.setItem_id(2L);
		//Given a file item
		FileContent fileContent = new FileContent();
		byte[] binaryArray = fileContent.convertInputFileToBinaryArray("C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf");
		fileContent.setBinary_content(binaryArray);
		TreeItem fItem = new MyFile("file", "/home/dir/file.pdf","pdf","C:/Users/Natacha/Documents/cnam/GLG204 - 2023/DANS MON ILE.pdf", fileContent ); 
		fItem.setItem_id(3L);
		//add a child to parent directory's children list
		Directory parentDir = (Directory) dItem1;
		parentDir.addChildren(dItem2);
		parentDir.addChildren(fItem);

		//When convert directory to directory item DTO
		TreeItemDTO itemDTO = treeItemDTOConversion.convertEntityToDTO(parentDir);

		//parent
		assertNotNull(parentDir.getItem_id());
		assertNotNull(parentDir.getItem_name());
		assertEquals(parentDir.getItem_id(), Long.valueOf(itemDTO.getItem_id()));
		assertNotNull(parentDir.getItem_name());
		assertEquals(parentDir.getItem_name(), itemDTO.getItem_name());
		assertEquals(parentDir.getItem_local_path(), itemDTO.getItem_local_path());
		assertEquals("folder", itemDTO.getItem_type());

		//directory child 1
		assertNotNull(itemDTO.getChildren().get(0).getItem_id());
		assertEquals(parentDir.getChildren().get(0).getItem_id(),Long.valueOf(itemDTO.getChildren().get(0).getItem_id()));
		assertNotNull(parentDir.getChildren().get(0).getItem_name());
		assertEquals(parentDir.getChildren().get(0).getItem_name(),itemDTO.getChildren().get(0).getItem_name());
		assertEquals(parentDir.getChildren().get(0).getClass().toString(), itemDTO.getChildren().get(0).getItem_type());
		assertNotNull(((Directory) parentDir.getChildren().get(0)).getChildren());
		assertEquals(((Directory) parentDir.getChildren().get(0)).getChildren().size(), itemDTO.getChildren().get(0).getChildren().size());

		//file child 2
		assertNotNull(itemDTO.getChildren().get(1).getItem_id());
		assertEquals(parentDir.getChildren().get(1).getItem_id(),Long.valueOf(itemDTO.getChildren().get(1).getItem_id()));
		assertNotNull(parentDir.getChildren().get(1).getItem_name());
		assertEquals(parentDir.getChildren().get(1).getItem_name(),itemDTO.getChildren().get(1).getItem_name());
		assertEquals(parentDir.getChildren().get(0).getClass().toString(), itemDTO.getChildren().get(0).getItem_type());
		
	}

	//fileTreeItemDTO  => fileTreeItem
	//create 

	@Test
	void test_GivenFileItemDTO_WhenConvertDTOToEntity_ThenReturnFileItem()  {
		//Given an item DTO
		TreeItemDTO fItemDTO = new TreeItemDTO("1", "/home/file.pdf", "file", MyFile.class.toString());

		//WhenConvertDTOToEntity
		TreeItem fItem = treeItemDTOConversion.convertFileItemDTOtoFileItem(fItemDTO);

		//ThenReturnFileItem
		assertNotNull(fItem.getItem_id());
		assertEquals(Long.valueOf(fItemDTO.getItem_id()), fItem.getItem_id());
		assertNotNull(fItem.getItem_name());
		assertEquals(fItemDTO.getItem_name(), fItem.getItem_name());
		assertNotNull(fItem.getItem_local_path());
		assertEquals(fItemDTO.getItem_local_path(), fItem.getItem_local_path());
		assertEquals(fItemDTO.getItem_type(), fItem.getClass().toString());

	}

	@Test
	void test_GivenEmptyDirectoryItemDTO_WhenConvertDTOtoEntity_ThenReturnDirEmptyDirectoryItem()  {
		//Given an item DTO
		TreeItemDTO dItemDTO = new TreeItemDTO("1", "/home/dir", "dir", Directory.class.toString());

		//WhenConvertDTOToEntity
		TreeItem dItem = treeItemDTOConversion.convertDirectoryItemDTOtoDirectoryItem(dItemDTO);

		//Then Return Directory Item
		assertNotNull(dItem.getItem_id());
		assertEquals(Long.valueOf(dItemDTO.getItem_id()), dItem.getItem_id());
		assertNotNull(dItem.getItem_name());
		assertEquals(dItemDTO.getItem_name(), dItem.getItem_name());
		assertNotNull(dItem.getItem_local_path());
		assertEquals(dItemDTO.getItem_local_path(), dItem.getItem_local_path());
		assertEquals(dItemDTO.getItem_type(), dItem.getClass().toString());

	}

	@Test void test_GivenDirectoryDTOContainsOneFileItemDTO_WhenConvertEntityToDTO_ThenReturnDirectoryContainsOneFileItem() {
		//Given a directory item DTO
		TreeItemDTO dItemDTO = new TreeItemDTO("1","/home/dir/", "dir", Directory.class.toString());
		//Given a file item DTO
		TreeItemDTO fItemDTO = new TreeItemDTO("2", "/home/dir/file.pdf", "file", MyFile.class.toString());

		//add a child to parent directory's children list
		List<TreeItemDTO> children = new ArrayList<>();
		children.add(fItemDTO);
		dItemDTO.setChildren(children);

		//When convert directory item DTO to directory item
		TreeItem dItem = treeItemDTOConversion.convertDirectoryItemDTOtoDirectoryItem(dItemDTO);

		//Then return directory parent and child file item
		assertNotNull(dItem.getItem_id());
		assertEquals(Long.valueOf(dItemDTO.getItem_id()), dItem.getItem_id());
		assertNotNull(dItem.getItem_name());
		assertEquals(dItemDTO.getItem_name(), dItem.getItem_name());
		assertEquals(dItemDTO.getItem_local_path(), dItem.getItem_local_path());
		assertEquals(dItemDTO.getItem_type(), dItem.getClass().toString());
		assertEquals(dItemDTO.getChildren().size(), ((Directory) dItem).getChildren().size());
		assertNotNull(dItemDTO.getChildren().get(0).getItem_id());
		assertEquals(Long.valueOf(dItemDTO.getChildren().get(0).getItem_id()), ((Directory) dItem).getChildren().get(0).getItem_id());
		assertNotNull(dItemDTO.getChildren().get(0).getItem_name());
		assertEquals(dItemDTO.getChildren().get(0).getItem_name(), ((Directory) dItem).getChildren().get(0).getItem_name());
		assertEquals(dItemDTO.getChildren().get(0).getItem_type(), ((Directory) dItem).getChildren().get(0).getClass().toString());
	}
	
	@Test
	void test_GivenDirectoryDTOContainsOneDirectoryDTO_WhenConvertDTOToEntity_ThenReturnDirectoryContainsOneDirectory() {
		//Given a directory item DTO
		TreeItemDTO dItemDTO1 = new TreeItemDTO("1","/home/dir1/", "dir1", Directory.class.toString());
		//Given a directory item DTO
		TreeItemDTO dItemDTO2 = new TreeItemDTO("2","/home/dir1/dir2/", "dir2", Directory.class.toString());
		
		//add a child to parent directory's children list
		List<TreeItemDTO> children = new ArrayList<>();
		children.add(dItemDTO2);
		dItemDTO1.setChildren(children);

		//When convert directory item DTO to directory item
		TreeItem dItem = treeItemDTOConversion.convertDirectoryItemDTOtoDirectoryItem(dItemDTO1);

		//Then return parent and child item DTO
		assertNotNull(dItem.getItem_id());
		assertEquals(Long.valueOf(dItemDTO1.getItem_id()), dItem.getItem_id());
		assertNotNull(dItem.getItem_name());
		assertEquals(dItemDTO1.getItem_name(), dItem.getItem_name());
		assertEquals(dItemDTO1.getItem_local_path(), dItem.getItem_local_path());
		assertNotNull(dItemDTO1.getChildren().get(0).getItem_name());
		assertEquals(Long.valueOf(dItemDTO1.getChildren().get(0).getItem_id()),((Directory) dItem).getChildren().get(0).getItem_id());
		assertNotNull(dItemDTO1.getChildren().get(0).getItem_name());
		assertEquals(dItemDTO1.getChildren().get(0).getItem_name(),((Directory) dItem).getChildren().get(0).getItem_name());
		assertNotNull(dItemDTO1.getChildren().get(0).getChildren());
		assertEquals(dItemDTO1.getChildren().get(0).getChildren().size(), ((Directory) ((Directory) dItem).getChildren().get(0)).getChildren().size());
	}

	@Test
	void test_GivenDirectoryDTOContainsOneDirectoryDTOContainsOneFileDTO_WhenConvertEntityToDTO_ThenReturnDirectoryContainsOneDirectoryContainsOneFile() {
		//Given two items DTO
		TreeItemDTO parentItemDTO = new TreeItemDTO("1", "/home/dir1/", "dir1", Directory.class.toString());
		TreeItemDTO childDItemDTO = new TreeItemDTO("2","/home/dir1/dir2/", "dir2", Directory.class.toString());
		//Given a file item DTO
		TreeItemDTO fItemDTO = new TreeItemDTO("3", "/home/dir1/dir2/file.pdf", "file", MyFile.class.toString());
		//add a child to child directory's children list
		List<TreeItemDTO> children2 = new ArrayList<>();
		children2.add(fItemDTO);
		childDItemDTO.setChildren(children2);
		//add a child to parent directory's children list
		List<TreeItemDTO> children1 = new ArrayList<>();
		children1.add(childDItemDTO);
		parentItemDTO.setChildren(children1);

		//When convert directory item DTO to directory item
		TreeItem parentItem = treeItemDTOConversion.convertDirectoryItemDTOtoDirectoryItem(parentItemDTO);

		assertNotNull(parentItem.getItem_id());
		assertNotNull(parentItem.getItem_name());
		assertEquals(parentItem.getItem_id(), Long.valueOf(parentItemDTO.getItem_id()));
		assertNotNull(parentItem.getItem_name());
		assertEquals(parentItem.getItem_name(), parentItemDTO.getItem_name());
		assertEquals(parentItem.getItem_local_path(), parentItemDTO.getItem_local_path());
		assertEquals(parentItem.getClass().toString(), parentItemDTO.getItem_type());
		assertNotNull(((Directory) parentItem).getChildren().get(0).getItem_id());
		assertEquals(((Directory) parentItem).getChildren().get(0).getItem_id(),Long.valueOf(parentItemDTO.getChildren().get(0).getItem_id()));
		assertNotNull(((Directory) parentItem).getChildren().get(0).getItem_name());
		assertEquals(((Directory) parentItem).getChildren().get(0).getItem_name(),parentItemDTO.getChildren().get(0).getItem_name());
		assertEquals(((Directory) parentItem).getChildren().get(0).getClass().toString(), parentItemDTO.getChildren().get(0).getItem_type());
		assertNotNull(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren());
		assertEquals(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().size(), parentItemDTO.getChildren().get(0).getChildren().size());
		assertNotNull(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().get(0));
		assertNotNull(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().get(0).getItem_id());
		assertEquals(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().get(0).getItem_id(),Long.valueOf(parentItemDTO.getChildren().get(0).getChildren().get(0).getItem_id()));
		assertNotNull(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().get(0).getItem_name());
		assertEquals(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().get(0).getItem_name(),parentItemDTO.getChildren().get(0).getChildren().get(0).getItem_name());
		assertEquals(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().get(0).getClass().toString(), parentItemDTO.getChildren().get(0).getChildren().get(0).getItem_type());
	}
	
	@Test
	void test_GivenDirectoryDTOContainsOneDirectoryDTOAndOneFileDTO_WhenConvertDTOToEntity_ThenReturnDirectoryContainsOneDirectoryAndOneFile()  {
		//Given two items DTO
		TreeItemDTO parentItemDTO = new TreeItemDTO("1", "/home/dir1/", "dir1", Directory.class.toString());
		TreeItemDTO childDItemDTO = new TreeItemDTO("2","/home/dir1/dir2/", "dir2", Directory.class.toString());
		//Given a file item DTO
		TreeItemDTO fItemDTO = new TreeItemDTO("3", "/home/dir1/file.pdf", "file", MyFile.class.toString());
		//add a child to parent directory's children list
		List<TreeItemDTO> children = new ArrayList<>();
		children.add(childDItemDTO);
		children.add(fItemDTO);
		parentItemDTO.setChildren(children);
		
		//When convert directory item DTO to directory item
		TreeItem parentItem = treeItemDTOConversion.convertDirectoryItemDTOtoDirectoryItem(parentItemDTO);

		//parent
		assertNotNull(parentItem.getItem_id());
		assertNotNull(parentItem.getItem_name());
		assertEquals(parentItem.getItem_id(), Long.valueOf(parentItemDTO.getItem_id()));
		assertNotNull(parentItem.getItem_name());
		assertEquals(parentItem.getItem_name(), parentItemDTO.getItem_name());
		assertEquals(parentItem.getItem_local_path(), parentItemDTO.getItem_local_path());
		assertEquals(parentItem.getClass().toString(), parentItemDTO.getItem_type());


		//directory child 1
		assertNotNull(((Directory) parentItem).getChildren().get(0).getItem_id());
		assertEquals(((Directory) parentItem).getChildren().get(0).getItem_id(),Long.valueOf(parentItemDTO.getChildren().get(0).getItem_id()));
		assertNotNull(((Directory) parentItem).getChildren().get(0).getItem_name());
		assertEquals(((Directory) parentItem).getChildren().get(0).getItem_name(),parentItemDTO.getChildren().get(0).getItem_name());
		assertEquals(((Directory) parentItem).getChildren().get(0).getClass().toString(), parentItemDTO.getChildren().get(0).getItem_type());
		assertNotNull(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren());
		assertEquals(((Directory) ((Directory) parentItem).getChildren().get(0)).getChildren().size(), parentItemDTO.getChildren().get(0).getChildren().size());

		//file child 2
		assertNotNull(((Directory) parentItem).getChildren().get(1).getItem_id());
		assertEquals(((Directory) parentItem).getChildren().get(1).getItem_id(),Long.valueOf(parentItemDTO.getChildren().get(1).getItem_id()));
		assertNotNull(((Directory) parentItem).getChildren().get(1).getItem_name());
		assertEquals(((Directory) parentItem).getChildren().get(1).getItem_name(),parentItemDTO.getChildren().get(1).getItem_name());
		assertEquals(((Directory) parentItem).getChildren().get(1).getClass().toString(), parentItemDTO.getChildren().get(1).getItem_type());

	}



}
