package io.saltpay.utils;

import io.saltpay.models.SsnData;
import io.saltpay.models.chunk.SsnChunk;
import io.saltpay.models.chunk.SsnDataChunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtil {

    public static List<SsnChunk> prepareChunks(int numberOfChunks, List<String> listOfSsn) {
        List<SsnChunk> listOfChunks = new ArrayList<>();

        ListUtil.splitList(listOfSsn, numberOfChunks).forEach(chunk -> listOfChunks.add(new SsnChunk(chunk)));

        return listOfChunks;
    }

    public static List<SsnDataChunk> prepareSsnDataChunks(int numberOfChunks, List<SsnData> listOfSsnData) {
        List<SsnDataChunk> listOfChunks = new ArrayList<>();

        ListUtil.splitList(listOfSsnData, numberOfChunks).forEach(chunk -> listOfChunks.add(new SsnDataChunk(chunk)));

        return listOfChunks;
    }

    public static List<List<SsnData>> splitToChunks(int numberOfChunks, List<SsnData> listOfSsnData) {
        return ListUtil.splitList(listOfSsnData, numberOfChunks);
    }
}
