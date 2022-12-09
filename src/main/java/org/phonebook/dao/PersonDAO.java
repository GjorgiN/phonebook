package org.phonebook.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.phonebook.entity.Address;
import org.phonebook.entity.Person;
import org.phonebook.entity.Phonebook;
import org.phonebook.reqAndRes.PersonUpdates;

public class PersonDAO {

	public static Person findPersonByEmbg(String embg, Session s) {
		List<Person> result = new ArrayList<>();

		try {
			result = s.createNativeQuery("SELECT * FROM person WHERE embg = '" + embg + "';",
					org.phonebook.entity.Person.class).list();

			if (result.isEmpty())
				return null;

		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return result.get(0);
	}

	public static String updatePerson(PersonUpdates personUpdates) {
		String currentEmbg = personUpdates.getCurrentEmbg();
		String newEmbg = personUpdates.getNewEmbg();
		String newFirstName = personUpdates.getNewFirstName();
		String newLastName = personUpdates.getNewLastName();
		Address newAddressReq = personUpdates.getNewAddress();

		Session s = new SessionDAO().getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();

			// cannot return null since data sent from client side is received from backend
			Person p = findPersonByEmbg(currentEmbg, s);

			if (newFirstName != null) {
				p.setFirstName(newFirstName);
			}

			if (newLastName != null) {
				p.setLastName(newLastName);
			}

			if (newAddressReq != null) {
				Address newAddress = AddressDAO.findAddress(newAddressReq, s);
				if (newAddress == null) {
					newAddress = newAddressReq;
					s.save(newAddress);
				}
				p.setAddress(newAddress);
			}

			s.update(p);

			if (newEmbg != null && newEmbg != currentEmbg) {
				
				// alternative to save new and delete current one
				// remove constraint from phonebook tables, update embg, return contrainst to tables
//				s.createNativeQuery("UPDATE person SET embg = '" + newEmbg + "' WHERE embg = '" + currentEmbg + "';",
//						Person.class).executeUpdate();
				Person newPerson = new Person(newEmbg, p.getFirstName(), p.getLastName(), p.getAddress());
				s.save(newPerson);


				List<Phonebook> contacts = PhonebookDAO.getAllContactsByEmbg(currentEmbg);
				for (Phonebook contact : contacts) {
					contact.setPerson(newPerson);
					s.update(contact);
				}

				s.delete(p);
			}

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return "Contact updated successfully.";
	}

}

//	public static void main(String[] args) {
//
//		Person p = new Person();
//		System.out.println(p == null);
//
//	}
