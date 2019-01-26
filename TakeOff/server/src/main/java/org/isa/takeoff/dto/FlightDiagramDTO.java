package org.isa.takeoff.dto;

import java.util.List;

import org.isa.takeoff.model.FlightDiagram;

public class FlightDiagramDTO {

	private Long id;
	private Integer rows;
	private Integer cols;
	private List<Integer> disabledRows;
	private List<Integer> disabledCols;
	private Long version;

	public FlightDiagramDTO() {

	}

	public FlightDiagramDTO(FlightDiagram diagram) {
		this(diagram.getId(), diagram.getRows(), diagram.getCols(), diagram.getDisabledRows(),
				diagram.getDisabledCols(), diagram.getVersion());
	}

	public FlightDiagramDTO(Long id, Integer rows, Integer cols, List<Integer> disabledRows,
			List<Integer> disabledCols, Long version) {
		super();
		this.id = id;
		this.rows = rows;
		this.cols = cols;
		this.disabledRows = disabledRows;
		this.disabledCols = disabledCols;
		this.version = version;
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
		return disabledRows;
	}

	public void setDisabledRows(List<Integer> disabledRows) {
		this.disabledRows = disabledRows;
	}

	public List<Integer> getDisabledCols() {
		return disabledCols;
	}

	public void setDisabledCols(List<Integer> disabledCols) {
		this.disabledCols = disabledCols;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
