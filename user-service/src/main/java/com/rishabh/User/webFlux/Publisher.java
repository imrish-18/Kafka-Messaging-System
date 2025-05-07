package com.rishabh.User.webFlux;


import reactor.core.publisher.Mono;

public interface Publisher<T> {

    public default void subscribe(Subscriber T) {
        Mono.just("");
    }
}


 interface Subscriber<T> {
    void onSubscribe(Subscription var1);

    void onNext(T var1);

    void onError(Throwable var1);

    void onComplete();
}

 interface Subscription {
    void request(long var1);

    void cancel();
}

interface Processor<T, R> extends org.reactivestreams.Subscriber<T>, org.reactivestreams.Publisher<R> {
}