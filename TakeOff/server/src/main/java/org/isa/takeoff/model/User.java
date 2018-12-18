package org.isa.takeoff.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username", unique = true, nullable = false)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="email", unique = true, nullable=false)
	private String email;
	
	@Column(name="firstName", nullable=false)
	private String firstName;
	
	@Column(name="lastName", nullable=false)
	private String lastName;
	
	@Column(name="phoneNumber", nullable=false)
	private String phoneNumber;
	
	@Column(name="address", nullable=false)
	private String address;
	
	@Column(name="dateOfBirth", nullable=true)
	private LocalDateTime dateOfBirth;
	
	@Column(name="aboutMe", nullable=true)
	private String aboutMe;
	
	@Column(name="imagePath", nullable=true)
	private String imagePath;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<AirCompanyRating> companyRatings = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FlightRating> flightRatings = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HotelRating> hotelRatings = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RoomRating> roomRatings = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RentACarRating> rentACarRatings = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VehicleRating> vehicleRatings = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Authority authority;
	
	public User() { }
	
	public User(String username, String password, String email, String firstName, 
							String lastName, String phoneNumber, String address) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
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

	public void setCountry(String address) {
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
	
	public boolean addAirCompanyRating(AirCompanyRating e) {
		return companyRatings.add(e);
	}
	
	public boolean removeAirCompanyRating(Object o) {
		return companyRatings.remove(o);
	}

	public AirCompanyRating getAirCompanyRating(int index) {
		return companyRatings.get(index);
	}
	
	public boolean addFlightRating(FlightRating e) {
		return flightRatings.add(e);
	}
	
	public boolean removeFlightRating(Object o) {
		return flightRatings.remove(o);
	}

	public FlightRating getFlightRating(int index) {
		return flightRatings.get(index);
	}
	
	public boolean addHotelRating(HotelRating e) {
		return hotelRatings.add(e);
	}
	
	public boolean removeHotelRating(Object o) {
		return hotelRatings.remove(o);
	}

	public HotelRating getHotelRating(int index) {
		return hotelRatings.get(index);
	}
	
	public boolean addRoomRating(RoomRating e) {
		return roomRatings.add(e);
	}
	
	public boolean removeRoomRating(Object o) {
		return roomRatings.remove(o);
	}

	public RoomRating getRoomRating(int index) {
		return roomRatings.get(index);
	}
	
	public boolean addRentACarRating(RentACarRating e) {
		return rentACarRatings.add(e);
	}
	
	public boolean removeRentACarRating(Object o) {
		return rentACarRatings.remove(o);
	}

	public RentACarRating getRentACarRating(int index) {
		return rentACarRatings.get(index);
	}
	
	public boolean addVehicleRating(VehicleRating e) {
		return vehicleRatings.add(e);
	}
	
	public boolean removeVehicleRating(Object o) {
		return vehicleRatings.remove(o);
	}

	public VehicleRating getVehicleRating(int index) {
		return vehicleRatings.get(index);
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
		if (that.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>(Arrays.asList(this.authority));
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

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}