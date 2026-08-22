package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.FavouriteNotes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes, Integer> {

	List<FavouriteNotes>findByUserId(int userId);

}
