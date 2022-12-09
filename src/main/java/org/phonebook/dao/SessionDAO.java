package org.phonebook.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionDAO {

	public Session getSession() {
		SessionFactory factory = null;
		Session session = null;
		try {
			Configuration cfg = new Configuration();

			cfg.addAnnotatedClass(org.phonebook.entity.Person.class);
			cfg.addAnnotatedClass(org.phonebook.entity.Address.class);
			cfg.addAnnotatedClass(org.phonebook.entity.CityAreaCode.class);
			cfg.addAnnotatedClass(org.phonebook.entity.MobileCode.class);
			cfg.addAnnotatedClass(org.phonebook.entity.CountryCode.class);
			cfg.addAnnotatedClass(org.phonebook.entity.LandlinesPhonebook.class);
			cfg.addAnnotatedClass(org.phonebook.entity.MobilePhonebook.class);

			factory = cfg.configure().buildSessionFactory();
			session = factory.openSession();
			return session;

		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			factory.close();
			return null;
		}
	}

	public SessionDAO() {
		super();
	}

}
