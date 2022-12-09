package org.phonebook.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.phonebook.entity.CountryCode;

public class CountryDAO {

	public static CountryCode findCountryInDb(Integer countryCode, Session s) {
		List<CountryCode> result = new ArrayList<>();
		try {
			result = s.createNativeQuery("SELECT * FROM countrycode WHERE countrycode = " + countryCode + ";",
					org.phonebook.entity.CountryCode.class).list();

			if (result.isEmpty()) {
				return null;
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return result.get(0);
	}

	public static CountryCode saveCountryCode(CountryCode cc) {
		Session s = new SessionDAO().getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();

			s.save(cc);

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
			return null;
		}

		return cc;

	}

}
