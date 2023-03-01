package io.saltpay.utils;

import io.saltpay.model.SsnChunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtil {

    public static List<SsnChunk> prepareChunks(int numberOfChunks, List<String> listOfSsn) {
        List<SsnChunk> listOfChunks = new ArrayList<>();

        ListUtil.splitList(listOfSsn, numberOfChunks).forEach(chunk -> listOfChunks.add(new SsnChunk(chunk)));

        return listOfChunks;
    }
}
