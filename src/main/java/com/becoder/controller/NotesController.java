package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.CollectionUtils;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;

	@PostMapping("/save-notes")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes Not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {

		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);
	}

	@GetMapping("/notes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> allNotes = notesService.getAllNotes();
		if (CollectionUtils.isEmpty(allNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
	}

	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		NotesResponse allNotes = notesService.getAllNotesByUser(pageSize,pageNo);
//		if(CollectionUtils.isEmpty(allNotes)) {
//			return ResponseEntity.noContent().build();
//		}
		return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception{
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
	}
	
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception{
		notesService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Not Restore Successfully", HttpStatus.OK);
	}

	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception{
		List<NotesDto> notes = notesService.getUserRecycleBinNotes();
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Notes is not available in Recycle Bin", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception{
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception{
		notesService.emptyRecycleBin();
		return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
	}
	
	@GetMapping("/fav/{noteId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception{
		notesService.favouritrNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Notes Added Favourite", HttpStatus.CREATED);
	}
	
	@DeleteMapping("/un-fav/{favNotId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNotId) throws Exception{
		notesService.unfavouriteNotes(favNotId);
		return CommonUtil.createBuildResponseMessage("Remove Favourite", HttpStatus.OK);
	}
	
	@GetMapping("/fav-note")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getUserfavouriteNote() throws Exception{
		List<FavouriteNoteDto> userFavouritrNotes = notesService.getUserFavouritrNotes();
		if(CollectionUtils.isEmpty(userFavouritrNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavouritrNotes, HttpStatus.OK);
	}
	
	@GetMapping("/copy/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception{
		Boolean copyNotes = notesService.copyNotes(id);
		if(copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied Successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Copy failed! Try again", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
