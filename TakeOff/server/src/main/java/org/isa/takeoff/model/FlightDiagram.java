package org.isa.takeoff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class FlightDiagram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "_rows", nullable = false)
	private Integer rows;

	@Column(name = "_cols", nullable = false)
	private Integer cols;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "flight_disabled_rows")
	@Column(name = "_row", nullable = false)
	private Set<Integer> disabledRows = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "flight_disabled_cols")
	@Column(name = "_column", nullable = false)
	private Set<Integer> disabledCols = new HashSet<>();

	@Version
	private Long version;

	public FlightDiagram() {

	}

	public FlightDiagram(Integer rows, Integer cols, List<Integer> disabledRows, List<Integer> disabledCols) {
		this.rows = rows;
		this.cols = cols;
		this.disabledRows = new HashSet<>(disabledRows);
		this.disabledCols = new HashSet<>(disabledCols);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getCols() {
		return cols;
	}

	public void setCols(Integer cols) {
		this.cols = cols;
	}

	public List<Integer> getDisabledRows() {
		return new ArrayList<Integer>(disabledRows);
	}

	public void setDisabledRows(List<Integer> disabledRows) {
		this.disabledRows = new HashSet<>(disabledRows);
	}

	public List<Integer> getDisabledCols() {
		return new ArrayList<>(disabledCols);
	}

	public void setDisabledCols(List<Integer> disabledCols) {
		this.disabledCols = new HashSet<>(disabledCols);
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
		FlightDiagram that = (FlightDiagram) o;
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
