package com.becoder.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.becoder.entity.Notes;
import com.becoder.repository.NotesRepository;

public class NotesScheduler {	
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNotesSchedular() {
		LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
		List<Notes> deleteNotes = notesRepository.findAllByIsDeletedAndDeletedOnBefore(true,cutOffDate);
		notesRepository.deleteAll(deleteNotes);
	}
}
