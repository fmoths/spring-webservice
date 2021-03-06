package com.moth.webservice.web.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moth.webservice.config.auth.Role;
import com.moth.webservice.web.member.repository.MemberEntity;
import com.moth.webservice.web.member.repository.MemberRepository;
import com.moth.webservice.web.member.repository.MemberRequestDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
	
	private MemberRepository memberRepository;
	
	public boolean isAlreadyJoin(MemberRequestDTO dto) {
		 return memberRepository.findByEmail(dto.getEmail()).isPresent();
	}
	
	@Transactional
	public Long joinUser(MemberRequestDTO dto) {
		
		//비밀번호 암호화
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		return memberRepository.save(dto.toEntity()).getId();
	}
	
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		
		Optional <MemberEntity> userEntityWrapper = memberRepository.findByEmail(userEmail);
		MemberEntity memberEntity = userEntityWrapper.get();
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if(("admin@example.com").equals(userEmail)) {
			authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
		}else {
			authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
		}
		
		return new User(memberEntity.getEmail(), memberEntity.getPassword(), authorities);
	}
}
