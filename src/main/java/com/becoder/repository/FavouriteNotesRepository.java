package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.FavouriteNotes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes, Integer> {

	List<FavouriteNotes>findByUserId(int userId);
	
	boolean existsByUserIdAndNote_Id(Integer userId, Integer noteId); 
	
	Optional<FavouriteNotes> findByIdAndUserId( Integer id, Integer userId);
	
}
