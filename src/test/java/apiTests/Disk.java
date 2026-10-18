package apiTests;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Disk {
    private String operation_id;
    private String href;
    private String method;
    private boolean templated;

    public Disk(String operation_id, String href, String method, boolean templated) {
        this.operation_id = operation_id;
        this.href = href;
        this.method = method;
        this.templated = templated;
    }

    public Disk() {
    }

    public String getOperation_id() {
        return operation_id;
    }

    public String getHref() {
        return href;
    }

    public String getMethod() {
        return method;
    }

    public boolean isTemplated() {
        return templated;
    }
}