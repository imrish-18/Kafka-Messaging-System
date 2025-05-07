package com.rishabh.User;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@SpringBootTest
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	public  void Test(){
		Mono<String> monoPublisher= Mono.just("Hello rishabh");
		monoPublisher.subscribe(new CoreSubscriber<String>() {
			@Override
			public void onSubscribe(Subscription subscription) {
				System.out.println("subscription is done ..");
				subscription.request(1);
			}

			@Override
			public void onNext(String data) {
				System.out.println("data is "+data);
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println("error {} "+throwable.getLocalizedMessage());
			}

			@Override
			public void onComplete() {
				System.out.println("subscription is completed ");
			}
		});
	}
}
