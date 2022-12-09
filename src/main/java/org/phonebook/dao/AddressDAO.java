package org.phonebook.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.phonebook.entity.Address;

public class AddressDAO {

	public static Address findAddress(Address address, Session s) {
		List<Address> result = new ArrayList<>();
		address.getCity();
		address.getStreet();
		address.getNumber();

		try {
			String query = "SELECT * FROM address WHERE city = '" + address.getCity() + "' AND street = '"
					+ address.getStreet() + "' AND number = '" + address.getNumber() + "';";

			result = s.createNativeQuery(query, org.phonebook.entity.Address.class).list();

			if (result.isEmpty()) {
				return null;
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return result.get(0);
	}

}
