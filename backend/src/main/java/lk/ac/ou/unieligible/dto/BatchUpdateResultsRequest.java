package lk.ac.ou.unieligible.dto;

import java.util.List;

public class BatchUpdateResultsRequest {

    public static class ResultItem {
        private String courseCode;
        private String resultStatus;
        private String academicYearTaken;

        public ResultItem() {}

        public ResultItem(String courseCode, String resultStatus, String academicYearTaken) {
            this.courseCode = courseCode;
            this.resultStatus = resultStatus;
            this.academicYearTaken = academicYearTaken;
        }

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

        public String getResultStatus() { return resultStatus; }
        public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

        public String getAcademicYearTaken() { return academicYearTaken; }
        public void setAcademicYearTaken(String academicYearTaken) { this.academicYearTaken = academicYearTaken; }
    }

    private List<ResultItem> results;

    public BatchUpdateResultsRequest() {}

    public BatchUpdateResultsRequest(List<ResultItem> results) {
        this.results = results;
    }

    public List<ResultItem> getResults() { return results; }
    public void setResults(List<ResultItem> results) { this.results = results; }
}

