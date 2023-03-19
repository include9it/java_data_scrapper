package io.saltpay.utils;

import io.saltpay.models.SsnData;
import io.saltpay.models.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtil {

    public static <T> List<Chunk<T>> prepareChunks(int numberOfChunks, List<T> listOfData) {
        List<Chunk<T>> listOfChunks = new ArrayList<>();

        ListUtil.splitList(listOfData, numberOfChunks).forEach(chunk -> listOfChunks.add(new Chunk<>(chunk)));

        return listOfChunks;
    }

    public static List<List<SsnData>> splitToChunks(int numberOfChunks, List<SsnData> listOfSsnData) {
        return ListUtil.splitList(listOfSsnData, numberOfChunks);
    }
}
