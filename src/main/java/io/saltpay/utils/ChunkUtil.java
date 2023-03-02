package io.saltpay.utils;

import io.saltpay.model.SsnData;
import io.saltpay.model.chunk.SsnChunk;
import io.saltpay.model.chunk.SsnDataChunk;

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
}
