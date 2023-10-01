package io.saltpay.models.chunk;

import io.saltpay.models.SsnData;

import java.util.List;

public record SsnDataChunk(List<SsnData> listOfSsnData) {
}
