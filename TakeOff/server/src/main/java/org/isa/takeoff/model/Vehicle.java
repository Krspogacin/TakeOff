package org.isa.takeoff.model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.isa.takeoff.dto.VehicleDTO;

@Entity
public class Vehicle 
{	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="brand", nullable = false)
	private String brand;
	
	@Column(name="model", nullable = false)
	private String model;
	
	@Column(name="year", nullable = false)
	private Integer year;
	
	@Enumerated(EnumType.STRING)
	@Column(name="fuel", nullable = false)
	private FuelType fuel;
	
	@Column(name="numOfSeats", nullable = false)
	private Integer numOfSeats;
	
	@Enumerated(EnumType.STRING)
	@Column(name="transmission", nullable = false)
	private TransmissionType transmission;
	
	@Column(name="reserved", nullable = false)
	private boolean reserved;
	
	@OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<VehicleReservation> vehicleReservations = new HashSet<>();
	
	@OneToMany(mappedBy = "id.vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<VehicleRating> vehicleRatings = new HashSet<>();
	
	@OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<VehiclePrice> vehiclePrices = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private RentACar rentACar;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="image", nullable=true)
	private byte[] image;

	@Version
	private Long version;

	public Vehicle() { }

	public Vehicle(String brand, String model, Integer year, FuelType fuel, Integer numOfSeats, 
				   TransmissionType transmission, boolean reserved, byte[] image) {
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.fuel = fuel;
		this.numOfSeats = numOfSeats;
		this.transmission = transmission;
		this.reserved = reserved;
		this.image = image;
	}
	
	public Vehicle(VehicleDTO vehicleDTO)
	{
		this(vehicleDTO.getBrand(), vehicleDTO.getModel(), vehicleDTO.getYear(), vehicleDTO.getFuel(), vehicleDTO.getNumOfSeats(),
			 vehicleDTO.getTransmission(), vehicleDTO.isReserved(), (vehicleDTO.getImage() == null) ? null : vehicleDTO.getImage().getBytes(StandardCharsets.UTF_8));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	public FuelType getFuel() {
		return fuel;
	}

	public void setFuel(FuelType fuel) {
		this.fuel = fuel;
	}

	public Integer getNumOfSeats() {
		return numOfSeats;
	}

	public void setNumOfSeats(Integer numOfSeats) {
		this.numOfSeats = numOfSeats;
	}

	public TransmissionType getTransmission() {
		return transmission;
	}

	public void setTransmission(TransmissionType transmission) {
		this.transmission = transmission;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public List<VehicleReservation> getVehicleReservations() {
		return new ArrayList<>(vehicleReservations);
	}

	public void setVehicleReservations(List<VehicleReservation> vehicleReservations) {
		this.vehicleReservations = new HashSet<>(vehicleReservations);
	}
	
	public List<VehicleRating> getVehicleRatings() {
		return new ArrayList<>(vehicleRatings);
	}

	public void setVehicleRatings(List<VehicleRating> vehicleRatings) {
		this.vehicleRatings = new HashSet<>(vehicleRatings);
	}

	public List<VehiclePrice> getVehiclePrices() {
		return new ArrayList<>(vehiclePrices);
	}

	public void setVehiclePrices(List<VehiclePrice> vehiclePrices) {
		this.vehiclePrices = new HashSet<>(vehiclePrices);
	}
	
	public RentACar getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACar rentACar) {
		this.rentACar = rentACar;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vehicle that = (Vehicle) o;
		if (that.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}