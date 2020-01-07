package com.bookcentric.readingchallenge;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReadingChallengeServiceImpl implements ReadingChallengeService {
	
	@Autowired ReadingChallengeRepository repository;

	@Override
	public void save(ReadingChallenge readingChallenge) {
		repository.save(readingChallenge);
	}

	@Override
	public void saveImage(MultipartFile file, Integer id) throws IOException {
		byte[] image = file.getBytes();
		repository.updateImageById(image, id);

	}

	@Override
	public byte[] getImageBy(Integer id) {
		return repository.getImageById(id);
	}

	@Override
	public List<ReadingChallenge> getAll() {
		return repository.findAllByOrderByIdDesc();
	}

}
