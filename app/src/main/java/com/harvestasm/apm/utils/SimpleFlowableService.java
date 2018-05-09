package com.harvestasm.apm.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SimpleFlowableService {
    private static final int DEFAULT_THREADS = 2;

    // RxJava异步执行Callable的Service对象
    private final ExecutorService executor;
    public <T> Disposable runWithFlowable(Callable<T> callable, Consumer<T> consumer) {
        // Flowable.fromFuture() 在非主线程执行Callable对象
        // Flowable.fromCallable(callable).subscribe(onNext);
        Future<T> future = executor.submit(callable);
        Disposable disposable = Flowable.fromFuture(future).subscribe(consumer);
        return disposable;
    }

    public SimpleFlowableService() {
        this(DEFAULT_THREADS);
    }
    public SimpleFlowableService(int nThreads) {
        executor = Executors.newFixedThreadPool(2);
    }
}
