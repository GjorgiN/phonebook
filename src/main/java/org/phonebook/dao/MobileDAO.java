package org.phonebook.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.phonebook.entity.MobileCode;

public class MobileDAO {

	public static MobileCode findMobileCodesInDB(Integer prefix, String operator) {
		Session s = new SessionDAO().getSession();
		Transaction tx = null;
		List<MobileCode> result = new ArrayList<>();
		try {
			tx = s.beginTransaction();

			result = s.createNativeQuery(
					"SELECT * FROM mobilecode WHERE prefix = " + prefix + " AND operator = '" + operator + "';",
					org.phonebook.entity.MobileCode.class).list();

			tx.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

}
