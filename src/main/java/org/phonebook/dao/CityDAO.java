package org.phonebook.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.phonebook.entity.CityAreaCode;

public class CityDAO {

	public static CityAreaCode findCityCodesInDb(CityAreaCode reqCity, Session s) {
		Integer cac = reqCity.getCityAreaCode();

		if (cac == null) {
			return null;
		}

		List<CityAreaCode> result = new ArrayList<>();
		Integer clac = reqCity.getCityLocalAreaCode();
		try {
			String query = "";
			if (clac == null) {
				query += "SELECT * FROM cityareacode " + "WHERE cityareacode = " + cac + ";";

			} else {
				query += "SELECT * FROM cityareacode " + "WHERE cityareacode = " + cac + " "
						+ "AND citylocalareacode = " + reqCity.getCityLocalAreaCode() + ";";

			}

			result = s.createNativeQuery(query, org.phonebook.entity.CityAreaCode.class).list();

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		if (result.isEmpty()) {
			result.add(null);
		}
		return result.get(0);

	}

}
