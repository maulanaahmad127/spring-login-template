package com.spring.login.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponse {
	
	private Long id;
	private String username;
	private String nama;
	private String no_handphone;
	private String jenis_kelamin;
	private String email;
	private List<String> roles;

	public UserInfoResponse(Long id, String username, String nama, String no_handphone, String jenis_kelamin,
			String email, List<String> roles) {
		this.id = id;
		this.username = username;
		this.nama = nama;
		this.no_handphone = no_handphone;
		this.jenis_kelamin = jenis_kelamin;
		this.email = email;
		this.roles = roles;
	}

}
