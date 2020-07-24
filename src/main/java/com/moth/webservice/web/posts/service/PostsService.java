package com.moth.webservice.web.posts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moth.webservice.web.posts.dto.PostsMainResponseDto;
import com.moth.webservice.web.posts.dto.PostsSaveRequestDto;
import com.moth.webservice.web.posts.model.PostsRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostsService {
	
	private PostsRepository postsRepository;
	
	@Transactional
	public Long save(PostsSaveRequestDto dto) {
		return postsRepository.save(dto.toEntity()).getId();
	}
	
	@Transactional(readOnly = true)
	public List <PostsMainResponseDto> findAllDesc(){
		return postsRepository.findAllDesc().map(PostsMainResponseDto::new).collect(Collectors.toList());
	}
	
	//TODO:: Exception 만들자..!!
	@Transactional(readOnly = true)
	public PostsMainResponseDto findById(Long postId) {
		return new PostsMainResponseDto(postsRepository.findById(postId).get());
	}
	
	@Transactional
	public Long update (Long postId, PostsSaveRequestDto dto) {
		
		postsRepository.findById(postId).ifPresent((entity ->{
			entity.updatePostsEntity(dto);
			postsRepository.save(entity);
		}));
		
		return postId;
	}
	
	@Transactional
	public void deleteById (Long postId) {
		postsRepository.deleteById(postId);
	}
}