package com.becoder.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.CollectionUtils;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.endpoint.NotesEndpoint;
import com.becoder.entity.FileDetails;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtil;

@RestController
public class NotesController implements NotesEndpoint {

	@Autowired
	private NotesService notesService;

	@Override
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes Not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {

		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);
	}

	@Override
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> allNotes = notesService.getAllNotes();
		if (CollectionUtils.isEmpty(allNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		NotesResponse allNotes = notesService.getAllNotesByUser(pageSize,pageNo);
		
		return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key",defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		NotesResponse allNotes = notesService.getNotesByUserSearch(pageSize, pageNo, key);

		return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception{
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception{
		notesService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Not Restore Successfully", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception{
		List<NotesDto> notes = notesService.getUserRecycleBinNotes();
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Notes is not available in Recycle Bin", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception{
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception{
		notesService.emptyRecycleBin();
		return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception{
		notesService.favouritrNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Notes Added Favourite", HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNotId) throws Exception{
		notesService.unfavouriteNotes(favNotId);
		return CommonUtil.createBuildResponseMessage("Remove Favourite", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> getUserfavouriteNote() throws Exception{
		List<FavouriteNoteDto> userFavouritrNotes = notesService.getUserFavouritrNotes();
		if(CollectionUtils.isEmpty(userFavouritrNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavouritrNotes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception{
		Boolean copyNotes = notesService.copyNotes(id);
		if(copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied Successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Copy failed! Try again", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
