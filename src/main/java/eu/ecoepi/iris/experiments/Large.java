package eu.ecoepi.iris.experiments;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

import eu.ecoepi.iris.Model;

class Large {
    private final ExecutorService executor;
    private final CompletionService<Void> tasks;
    
    private int todo = 0;
    private int done = 0;
    
    public Large() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        tasks = new ExecutorCompletionService<>(executor);
    }
    
    public void addTask(String name, Model.Options options) {
        tasks.submit(() -> {
            System.err.printf("Starting task %s...\n", name);

            try {
                Model.run(options);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, null);
        todo++;
    }
    
    public void waitForCompletion() throws ExecutionException, InterruptedException {
        while (done < todo) {
            tasks.take().get();
            done++;

            System.err.printf("%d out of %d tasks finished.\n", done, todo);
        }

        executor.shutdown();
    }
}
