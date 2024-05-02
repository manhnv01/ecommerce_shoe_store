package com.nvm.shoestoreapi.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {
    private final BlockingQueue<Runnable> queue;

    public RequestQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public void addRequest(Runnable request) {
        try {
            queue.put(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void processRequests() {
        while (true) {
            try {
                Runnable request = queue.take();
                // Xử lý request
                request.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
