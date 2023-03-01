package io.saltpay.threads;

import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfo;

import java.util.concurrent.Callable;

public class DataCollectorCallable implements Callable<SsnData> {

    private final CreditInfo creditInfoScrapper;

    public DataCollectorCallable(CreditInfo creditInfoScrapper) {
        this.creditInfoScrapper = creditInfoScrapper;
    }

    @Override
    public SsnData call() throws Exception {
        creditInfoScrapper.start();

        return null;
    }
}


//public class WebsiteDataCollector {
//
//    private static final int THREAD_POOL_SIZE = 5;
//
//    public List<Model> collectDataFromWebsite(List<String> urls) throws Exception {
//        List<Model> models = new ArrayList<>();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
//
//        List<Future<Model>> futures = new ArrayList<>();
//        for (String url : urls) {
//            Callable<Model> callable = new DataCollectorCallable(url);
//            Future<Model> future = executorService.submit(callable);
//            futures.add(future);
//        }
//
//        for (Future<Model> future : futures) {
//            models.add(future.get());
//        }
//
//        executorService.shutdown();
//        return models;
//    }
//
//    private static class DataCollectorCallable implements Callable<Model> {
//
//        private final String url;
//
//        public DataCollectorCallable(String url) {
//            this.url = url;
//        }
//
//        @Override
//        public Model call() throws Exception {
//            // Use Selenium to collect data from the website and save it into a model
//            Model model = new Model();
//            // ...
//            return model;
//        }
//    }
//}
