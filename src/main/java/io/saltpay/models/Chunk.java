package io.saltpay.models;

import java.util.List;

public record Chunk<T>(List<T> dataList) {
}
