package org.egov.domain.model;

import java.util.ArrayList;
import java.util.List;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties 
@EnableConfigurationProperties(ReportYamlMetaData.class)
public class ReportYamlMetaData {
    private String reportName;
    public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<sourceColumns> getSourceColumns() {
		return sourceColumns;
	}
	public void setSourceColumns(List<sourceColumns> sourceColumns) {
		this.sourceColumns = sourceColumns;
	}
	public List<searchParams> getSearchParams() {
		return searchParams;
	}
	public void setSearchParams(List<searchParams> searchParams) {
		this.searchParams = searchParams;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	private String summary;
    private String version;
    private List<sourceColumns> sourceColumns = new ArrayList<>();
    private List<searchParams> searchParams = new ArrayList<>();
    public static class sourceColumns {
    	private String label;
    	public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		private String name;
    	private String type;
    	private String source;
    	@Override
        public String toString() {
            return "sourceColumns{" +
                    "lable='" + label + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", source='" + source + '\'' +
                    '}';
        }
    }
    public static class searchParams {
    	private String label;
    	public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		private String name;
    	private String type;
    	private String source;
    	@Override
        public String toString() {
            return "searchParams{" +
                    "lable='" + label + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", source='" + source + '\'' +
                    '}';
        }
    }
    private String query;
    @Override
    public String toString() {
        return "ReportDefinition{" +
                "reportName='" + reportName + '\'' +
                ", summary=" + summary +
                ", version=" + version +
                ", sourceColumns=" + sourceColumns +
                ", searchParams=" + searchParams +
                ", query=" + query +
                '}';
    }
    
}

