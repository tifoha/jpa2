package ua.tifoha;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Vitaliy Sereda on 05.06.17.
 */

//@Component
public class PhoneEntityListener {
	@Inject
	private ApplicationContext ctx;

	public PhoneEntityListener() {
		System.out.println("PhoneEntityListener.PhoneEntityListener");
	}

	@PrePersist
	public void prePersist(Phone phone) {
		System.out.println("PhoneEntityListener.prePersist " + phone);
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println("PhoneEntityListener.postConstruct");
	}
}
