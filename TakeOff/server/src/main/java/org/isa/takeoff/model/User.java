package org.isa.takeoff.model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.isa.takeoff.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "firstName", nullable = false)
	private String firstName;

	@Column(name = "lastName", nullable = false)
	private String lastName;

	@Column(name = "phoneNumber", nullable = false)
	private String phoneNumber;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "dateOfBirth", nullable = true)
	private LocalDate dateOfBirth;

	@Column(name = "aboutMe", nullable = true)
	private String aboutMe;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "image", nullable = true)
	private byte[] image;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@OneToMany(mappedBy = "user1", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Friend> friends = new HashSet<>();
	
	@OneToMany(mappedBy = "user2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Friend> friendsOf = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<AirCompanyRating> companyRatings = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<FlightReservation> flightRatings = new HashSet<>();

	@OneToMany(mappedBy = "id.user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<HotelRating> hotelRatings = new HashSet<>();

	@OneToMany(mappedBy = "id.user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomRating> roomRatings = new HashSet<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomReservation> roomReservations = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RentACarRating> rentACarRatings = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<VehicleReservation> vehicleRatings = new HashSet<>();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Authority authority;

	@Version
	private Long version;

	public User() {
	}

	public User(String username, String password, String email, String firstName, String lastName, String phoneNumber,
			String address, LocalDate dateofBirth, String aboutMe, byte[] image) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.dateOfBirth = dateofBirth;
		this.aboutMe = aboutMe;
		this.image = image;
	}

	public User(UserDTO userDTO) {
		this(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getFirstName(),
				userDTO.getLastName(), userDTO.getPhoneNumber(), userDTO.getAddress(), userDTO.getDateOfBirth(),
				userDTO.getAboutMe(),
				(userDTO.getImage() == null) ? null : userDTO.getImage().getBytes(StandardCharsets.UTF_8));
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public List<Friend> getFriends() {
		return new ArrayList<>(friends);
	}

	public void setFriends(List<Friend> friends) {
		this.friends = new HashSet<>(friends);
	}

	public List<Friend> getFriendsOf() {
		return new ArrayList<>(friendsOf);
	}

	public void setFriendsOf(List<Friend> friendsOf) {
		this.friendsOf = new HashSet<>(friendsOf);
	}

	public List<AirCompanyRating> getCompanyRatings() {
		return new ArrayList<>(companyRatings);
	}

	public void setCompanyRatings(List<AirCompanyRating> companyRatings) {
		this.companyRatings = new HashSet<>(companyRatings);
	}

	public List<FlightReservation> getFlightRatings() {
		return new ArrayList<>(flightRatings);
	}

	public void setFlightRatings(List<FlightReservation> flightRatings) {
		this.flightRatings = new HashSet<>(flightRatings);
	}

	public List<HotelRating> getHotelRatings() {
		return new ArrayList<>(hotelRatings);
	}

	public void setHotelRatings(List<HotelRating> hotelRatings) {
		this.hotelRatings = new HashSet<>(hotelRatings);
	}

	public List<RoomRating> getRoomRatings() {
		return new ArrayList<>(roomRatings);
	}

	public void setRoomRatings(List<RoomRating> roomRatings) {
		this.roomRatings = new HashSet<>(roomRatings);
	}

	public List<RoomReservation> getRoomReservations() {
		return new ArrayList<>(roomReservations);
	}

	public void setRoomReservations(List<RoomReservation> roomReservations) {
		this.roomReservations = new HashSet<>(roomReservations);
	}

	public List<RentACarRating> getRentACarRatings() {
		return new ArrayList<>(rentACarRatings);
	}

	public void setRentACarRatings(List<RentACarRating> rentACarRatings) {
		this.rentACarRatings = new HashSet<>(rentACarRatings);
	}

	public List<VehicleReservation> getVehicleRatings() {
		return new ArrayList<>(vehicleRatings);
	}

	public void setVehicleRatings(List<VehicleReservation> vehicleRatings) {
		this.vehicleRatings = new HashSet<>(vehicleRatings);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User that = (User) o;
		if (that.username == null || username == null) {
			return false;
		}
		return Objects.equals(username, that.username);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(username);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>(Arrays.asList(this.authority));
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}