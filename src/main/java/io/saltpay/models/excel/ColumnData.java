package io.saltpay.models.excel;

import java.util.List;

public record ColumnData(String headerName, List<String> values) {
}
