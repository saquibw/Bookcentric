package com.bookcentric.readingchallenge;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ReadingChallengeService {
	public void save(ReadingChallenge readingChallenge);
	public void saveImage(MultipartFile file, Integer id) throws IOException;
	public byte[] getImageBy(Integer id);
	public List<ReadingChallenge> getAll();
	public ReadingChallenge getBy(Integer id);
}
