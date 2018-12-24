package org.isa.takeoff.dto;

import java.time.LocalDateTime;

import org.isa.takeoff.model.User;

public class UserDTO 
{
	private Long id;
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private LocalDateTime dateOfBirth;
	private String aboutMe;
	private String imagePath;
	private boolean enabled;
	
	public UserDTO() { }
	
	public UserDTO(User user)
	{
		this(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(),
			 user.getPhoneNumber(), user.getAddress(), user.getDateOfBirth(), user.getAboutMe(), user.getImagePath(), user.isEnabled());
	}
	
	public UserDTO(Long id, String username, String password, String email, String firstName, String lastName,
			String phoneNumber, String address, LocalDateTime dateOfBirth, String aboutMe, String imagePath,
			boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.dateOfBirth = dateOfBirth;
		this.aboutMe = aboutMe;
		this.imagePath = imagePath;
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}