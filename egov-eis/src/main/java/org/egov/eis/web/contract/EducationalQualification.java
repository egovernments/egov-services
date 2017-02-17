package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EducationalQualification {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("qualification")
	private String qualification = null;

	@JsonProperty("majorSubject")
	private String majorSubject = null;

	@JsonProperty("yearOfPassing")
	private String yearOfPassing = null;

	@JsonProperty("university")
	private String university = null;

	public EducationalQualification id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EducationalQualification qualification(String qualification) {
		this.qualification = qualification;
		return this;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public EducationalQualification majorSubject(String majorSubject) {
		this.majorSubject = majorSubject;
		return this;
	}

	public String getMajorSubject() {
		return majorSubject;
	}

	public void setMajorSubject(String majorSubject) {
		this.majorSubject = majorSubject;
	}

	public EducationalQualification yearOfPassing(String yearOfPassing) {
		this.yearOfPassing = yearOfPassing;
		return this;
	}

	public String getYearOfPassing() {
		return yearOfPassing;
	}

	public void setYearOfPassing(String yearOfPassing) {
		this.yearOfPassing = yearOfPassing;
	}

	public EducationalQualification university(String university) {
		this.university = university;
		return this;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EducationalQualification educationalQualification = (EducationalQualification) o;
		return Objects.equals(this.id, educationalQualification.id)
				&& Objects.equals(this.qualification, educationalQualification.qualification)
				&& Objects.equals(this.majorSubject, educationalQualification.majorSubject)
				&& Objects.equals(this.yearOfPassing, educationalQualification.yearOfPassing)
				&& Objects.equals(this.university, educationalQualification.university);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, qualification, majorSubject, yearOfPassing, university);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EducationalQualification {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    qualification: ").append(toIndentedString(qualification)).append("\n");
		sb.append("    majorSubject: ").append(toIndentedString(majorSubject)).append("\n");
		sb.append("    yearOfPassing: ").append(toIndentedString(yearOfPassing)).append("\n");
		sb.append("    university: ").append(toIndentedString(university)).append("\n");
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
