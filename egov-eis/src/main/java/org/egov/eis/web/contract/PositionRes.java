package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.egov.eis.persistence.entity.Position;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionRes {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Positions")
	private List<Position> positions = new ArrayList<Position>();

	public PositionRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public PositionRes position(List<Position> positions) {
		this.positions = positions;
		return this;
	}

	public PositionRes addPositionItem(Position positionItem) {
		this.positions.add(positionItem);
		return this;
	}

	public List<Position> getPosition() {
		return positions;
	}

	public void setPosition(List<Position> positions) {
		this.positions = positions;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PositionRes positionRes = (PositionRes) o;
		return Objects.equals(this.responseInfo, positionRes.responseInfo)
				&& Objects.equals(this.positions, positionRes.positions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, positions);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PositionRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    position: ").append(toIndentedString(positions)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
