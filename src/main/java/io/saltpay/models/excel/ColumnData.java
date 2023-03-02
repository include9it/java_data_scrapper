package io.saltpay.models.excel;

import java.util.List;

public class ColumnData {
    private final String headerName;

    private final List<String> values;

    public ColumnData(String headerName, List<String> values) {
        this.headerName = headerName;
        this.values = values;
    }

    public String getHeaderName() {
        return headerName;
    }

    public List<String> getValues() {
        return values;
    }
}
