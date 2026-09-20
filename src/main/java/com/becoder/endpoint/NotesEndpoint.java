package com.becoder.endpoint;

import org.springframework.http.ResponseEntity;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.becoder.util.Constants.ROLE_ADMIN;
import static com.becoder.util.Constants.ROLE_USER;
import static com.becoder.util.Constants.ROLE_ADMIN_USER;

@Tag(name = "Notes",description = "All the Notes Operation APIs")
@RequestMapping("/api/v1/notes")
public interface NotesEndpoint {
	
	@Operation(summary = "Save Notes",tags = {"Notes","User"},description = "user save notes")
	@PostMapping(value ="/save-notes",consumes = "multipart/form-data")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveNotes(@RequestParam 
			@Parameter(description = "JSON string notes",required = true,
			content = @Content(schema=@Schema(implementation = NotesRequest.class))) String notes, @RequestParam(required = false) MultipartFile file) throws Exception ;
	
	@Operation(summary = "Download upload file",tags = {"Notes","User"},description = "download file")
	@GetMapping("/download/{id}")
	@PreAuthorize(ROLE_ADMIN_USER)
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get All Notes",tags = {"Notes"},description = "Get All Notes Admin")
	@GetMapping("/notes")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes() ;
	
	@Operation(summary = "Get All Notes for User",tags = {"Notes","User"},description = "Get All Notes for User")
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);
	
	@Operation(summary = "Search Notes",tags = {"Notes","User"},description = "User search notes")
	@GetMapping("/search-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key",defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);
	
	@Operation(summary = "Delete Notes",tags = {"Notes","User"},description = "delete notes by user")
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Restore Deleted Notes",tags = {"Notes","User"},description = "Restore Deleted Notes from recycle bin")
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get Notes from recycle bin",tags = {"Notes","User"},description = "Get Notes from recycle bin")
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@Operation(summary = "Hard delete notes",tags = {"Notes","User"},description = "hard delete notes")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Empty user recycle bin",tags = {"Notes","User"},description = "empty user recycle bin")
	@DeleteMapping("/delete")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception;
	
	@Operation(summary = "Faourite Notes",tags = {"Notes","User"},description = "user favourite notes")
	@GetMapping("/fav/{noteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception;
	
	@Operation(summary = "Unfavourite Note",tags = {"Notes","User"},description = "user unfavourite notess")
	@DeleteMapping("/un-fav/{favNotId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNotId) throws Exception;
	
	@Operation(summary = "Get User Faourite Notes",tags = {"Notes","User"},description = "user favourite notes")
	@GetMapping("/fav-note")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserfavouriteNote() throws Exception;
	
	@Operation(summary = "Copy Notes",tags = {"Notes","User"},description = "copy notes")
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
	
}
