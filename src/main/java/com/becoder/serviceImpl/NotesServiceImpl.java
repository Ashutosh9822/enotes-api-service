package com.becoder.serviceImpl;

import java.io.File;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesDto.CategoryDto;
import com.becoder.dto.NotesDto.FilesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FavouriteNotes;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.FavouriteNotesRepository;
import com.becoder.repository.FileRepository;
import com.becoder.repository.NotesRepository;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtil;

import tools.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FavouriteNotesRepository favouriteNotesRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private ModelMapper mapper;

	@Value("${file.upload.path}")
	private String uploadpath;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {

		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);

		checkCategoryExist(notesDto.getCategory());

		if (!ObjectUtils.isEmpty(notesDto.getId())) {
			updateNotes(notesDto, file);
		}

		Notes notesMap = mapper.map(notesDto, Notes.class);
		FileDetails fileDetails = saveFileDetails(file);

		if (!ObjectUtils.isEmpty(fileDetails)) {
			notesMap.setFileDetails(fileDetails);
		} else {
			if (ObjectUtils.isEmpty(notesDto.getId())) {
				notesMap.setFileDetails(null);
			}
		}

		Notes saveNotes = notesRepository.save(notesMap);
		if (ObjectUtils.isEmpty(saveNotes)) {
			return false;
		}
		return true;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {
		Notes existNotes = notesRepository.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Notes ID"));

		if (ObjectUtils.isEmpty(file)) {
			notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FilesDto.class));
		}

	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

			String originalFilename = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);

			List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg");
			if (!extensionAllow.contains(extension)) {
				throw new IllegalArgumentException("Invalid file format! Upload only pdf,xlsx,jpg ");
			}

			String rndString = UUID.randomUUID().toString();
			String uploadfileName = rndString + "." + extension;

			File saveFile = new File(uploadpath);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}
			String storePath = uploadpath.concat(uploadfileName);

			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			if (upload != 0) {
				FileDetails fileDetails = new FileDetails();
				fileDetails.setOriginalFileName(originalFilename);
				fileDetails.setDisplayFileName(getDisplayName(originalFilename));
				fileDetails.setUploadFileName(uploadfileName);
				fileDetails.setFileSize(file.getSize());
				fileDetails.setPath(storePath);
				FileDetails saveFileDetails = fileRepository.save(fileDetails);
				return saveFileDetails;
			}
		}

		return null;
	}

	private String getDisplayName(String originalFilename) {
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);

		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extension;
		return fileName;
	}

	private void checkCategoryExist(CategoryDto category) throws Exception {
		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Category ID"));
	}

//	@Override
//	public List<NotesDto> getAllNotes() {
//
//		List<Notes> allNotes = notesRepository.findAll();
//		List<NotesDto> Noteslist = allNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();
//		return Noteslist;
//	}

	@Override
	public List<NotesDto> getAllNotes() {
		List<Notes> allNotes = notesRepository.findAll();
		List<NotesDto> Noteslist = allNotes.stream().map(note -> {
			NotesDto dto = mapper.map(note, NotesDto.class);

			if (note.getFileDetails() != null) {
				NotesDto.FilesDto fileDto = new NotesDto.FilesDto();
				fileDto.setId(note.getFileDetails().getId());
				fileDto.setOriginalFileName(note.getFileDetails().getOriginalFileName());
				fileDto.setDisplayFileName(note.getFileDetails().getDisplayFileName());
				dto.setFileDetails(fileDto);
			}

			return dto;
		}).toList();

		return Noteslist;
	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {
		InputStream io = new FileInputStream(fileDetails.getPath());
		return StreamUtils.copyToByteArray(io);
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDetails = fileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File is not available"));
		return fileDetails;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer pageSize, Integer pageNo) {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);
		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = new NotesResponse();
		notes.setNotes(notesDto);
		notes.setPageNo(pageNotes.getNumber());
		notes.setPageSize(pageNotes.getSize());
		notes.setTotalElements((int) pageNotes.getTotalElements());
		notes.setTotalPages(pageNotes.getTotalPages());
		notes.setIsFirst(pageNotes.isFirst());
		notes.setIsLast(pageNotes.isLast());

		return notes;
	}
	
	@Override
	public NotesResponse getNotesByUserSearch(Integer pageSize, Integer pageNo,String keyword) {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = notesRepository.searchNotes(keyword,userId, pageable);
		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = new NotesResponse();
		notes.setNotes(notesDto);
		notes.setPageNo(pageNotes.getNumber());
		notes.setPageSize(pageNotes.getSize());
		notes.setTotalElements((int) pageNotes.getTotalElements());
		notes.setTotalPages(pageNotes.getTotalPages());
		notes.setIsFirst(pageNotes.isFirst());
		notes.setIsLast(pageNotes.isLast());

		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes ID invalid! Not found"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes ID invalid! Not found"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDtoList = recycleNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();
		return notesDtoList;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes Not Found"));
		if (notes.getIsDeleted()) {
			notesRepository.delete(notes);
		} else {
			throw new IllegalArgumentException("You can not hard delete Directly");
		}
	}

	@Override
	public void emptyRecycleBin() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		if (!CollectionUtils.isEmpty(recycleNotes)) {
			notesRepository.deleteAll(recycleNotes);
		}
	}

	@Override
	public void favouritrNotes(Integer notesId) throws Exception {
		Integer userId = 1;
		Notes notes = notesRepository.findById(notesId).orElseThrow(() -> new ResourceNotFoundException("Notes not found! Invalid ID"));
		FavouriteNotes favouriteNotes = new FavouriteNotes();
		favouriteNotes.setNote(notes);
		favouriteNotes.setUserId(userId);
		favouriteNotesRepository.save(favouriteNotes);
	}

	@Override
	public void unfavouriteNotes(Integer favouriteNotesId) throws Exception {
		FavouriteNotes favNotes = favouriteNotesRepository.findById(favouriteNotesId).orElseThrow(() -> new ResourceNotFoundException("Favourite notes not found! Invalid ID"));
		favouriteNotesRepository.delete(favNotes);
	}

	@Override
	public List<FavouriteNoteDto> getUserFavouritrNotes() {

		int userId=1;
		List<FavouriteNotes> favouriteNotes = favouriteNotesRepository.findByUserId(userId);
		return favouriteNotes.stream().map(fn -> mapper.map(fn, FavouriteNoteDto.class)).toList();
	}

	@Override
	public Boolean copyNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes not found! Invalid ID"));
		Notes copyNotes = new Notes();
		copyNotes.setTitle(notes.getTitle());
		copyNotes.setDescription(notes.getDescription());
		copyNotes.setCategory(notes.getCategory());
		copyNotes.setIsDeleted(false);
		copyNotes.setFileDetails(null);

		Notes saveCopyNotes = notesRepository.save(copyNotes);
		
		if(ObjectUtils.isEmpty(saveCopyNotes)) {
			return false;
		}
		return true;
	}

	
	
}
