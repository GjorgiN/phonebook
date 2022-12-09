package org.phonebook.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.phonebook.dao.AddressDAO;
import org.phonebook.dao.CityDAO;
import org.phonebook.dao.CountryDAO;
import org.phonebook.dao.MobileDAO;
import org.phonebook.dao.PersonDAO;
import org.phonebook.dao.PhonebookDAO;
import org.phonebook.dao.SessionDAO;
import org.phonebook.entity.Address;
import org.phonebook.entity.CityAreaCode;
import org.phonebook.entity.CountryCode;
import org.phonebook.entity.Landline;
import org.phonebook.entity.LandlinesPhonebook;
import org.phonebook.entity.Mobile;
import org.phonebook.entity.MobileCode;
import org.phonebook.entity.MobilePhonebook;
import org.phonebook.entity.Person;
import org.phonebook.entity.Phonebook;
import org.phonebook.reqAndRes.ContactsByPerson;
import org.phonebook.reqAndRes.PersonUpdates;
import org.phonebook.reqAndRes.PhoneNumber;
import org.phonebook.reqAndRes.PhoneNumberUpdates;
import org.phonebook.reqAndRes.RecordRequest;
import org.phonebook.reqAndRes.UpdateRequest;
import org.phonebook.entity.PhoneNumberId;

@Path("/phonebook")
public class PhonebookService {

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public String createRecord(RecordRequest req) {
		Phonebook recordDb = PhonebookDAO.findPhoneNumber(req.getPhoneNumber());
		if (recordDb != null) {
			return "The phone number is already in use.";
		}

		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Person p = PersonDAO.findPersonByEmbg(req.getPerson().getEmbg(), s);
			if (p == null) {
				p = req.getPerson();
				Address address = AddressDAO.findAddress(p.getAddress(), s);
				if (address == null) {
					s.persist(p);
				} else {
					p.setAddress(address);
					s.save(p);
				}
			}

			PhoneNumber reqPhoneNumber = req.getPhoneNumber();
			CountryCode cc = CountryDAO.findCountryInDb(reqPhoneNumber.getCountryCode().getCountryCode(), s);
			if (cc == null) {
				cc = req.getPhoneNumber().getCountryCode();
				s.save(cc);
			}

			CityAreaCode cac = reqPhoneNumber.getCityAreaCode();
			if (cac != null) {
				cac = CityDAO.findCityCodesInDb(reqPhoneNumber.getCityAreaCode(), s);
				if (cac == null) {
					cac = reqPhoneNumber.getCityAreaCode();
					s.save(cac);

				}
				Landline landline = new Landline(cc, reqPhoneNumber.getPhoneNumber(), cac);
//				PhoneNumberId compositeKey = new PhoneNumberId(reqPhoneNumber.getPhoneNumber(), cc, cac);
//				Phonebook record = new Phonebook(landline, p);
				LandlinesPhonebook record = new LandlinesPhonebook(p, landline);
				s.save(record);
			}

			MobileCode mc = req.getPhoneNumber().getMobileCode();
			if (mc != null) {
				mc = MobileDAO.findMobileCodesInDB(mc.getPrefix(), mc.getOperator());
				if (mc == null) {
					mc = req.getPhoneNumber().getMobileCode();
					s.save(mc);
				}
				Mobile mobile = new Mobile(cc, reqPhoneNumber.getPhoneNumber(), mc);
				MobilePhonebook record = new MobilePhonebook(p, mobile);
				s.save(record);

			}

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return "New record saved successfully";
	}

	@DELETE
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteContact(PhoneNumber phoneNumberReq) {
		SessionDAO sessionGenerate = new SessionDAO();
		Session session = sessionGenerate.getSession();
		Transaction tx = null;
		String response = "Contact not found";
		try {
			tx = session.beginTransaction();

			Phonebook record = PhonebookDAO.findPhoneNumber(phoneNumberReq);
			if (record != null) {
				session.delete(record);
				response = "Contact deleted";
			}

			tx.commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			session.close();
		}

		return response;
	}

	@GET
	@Path("/getAllContacts")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Phonebook> getAllContacts() {
		List<Phonebook> allContacts = PhonebookDAO.getAllContacts();

		return allContacts;
	}

	@GET
	@Path("/getContactsByPhoneNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public Phonebook getContactsByPhoneNumber(PhoneNumber phoneNumber) {
		Phonebook record = PhonebookDAO.findPhoneNumber(phoneNumber);

		return record;
	}

	@GET
	@Path("/getContactsByEmbg")
	@Produces(MediaType.APPLICATION_JSON)
	public ContactsByPerson getAllContactsByPerson(@QueryParam("embg") String embg) {
		List<Phonebook> allContactsByPerson = PhonebookDAO.getAllContactsByEmbg(embg);

		ContactsByPerson contacts = new ContactsByPerson();

		if (allContactsByPerson.isEmpty()) {
			contacts.setPerson(null);
			contacts.setPhoneNumbers(null);

		} else {
			contacts.setPerson(allContactsByPerson.get(0).getPerson());
			contacts.setPhoneNumbers(new ArrayList<PhoneNumberId>());

			for (Phonebook phonebook : allContactsByPerson) {

				if (phonebook.getClass() == MobilePhonebook.class) {
					MobilePhonebook mobile = (MobilePhonebook) phonebook;
					contacts.getPhoneNumbers().add(mobile.getPhoneNumber());

				} else if (phonebook.getClass() == LandlinesPhonebook.class) {
					LandlinesPhonebook landline = (LandlinesPhonebook) phonebook;
					contacts.getPhoneNumbers().add(landline.getPhoneNumber());
				}
			}
		}
		return contacts;
	}

	@GET
	@Path("/getContactsByFirstName")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Phonebook> getContactsByFirstName(@QueryParam("firstName") String firstName) {
		List<Phonebook> allContactsByFirstName = PhonebookDAO.getAllContactsByFirstName(firstName);

		return allContactsByFirstName;
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateContact(UpdateRequest updateReq) {
		PersonUpdates personUpdates = updateReq.getPersonUpdates();
		String status = "";
		if (personUpdates != null) {
			status += PersonDAO.updatePerson(personUpdates);
		} else {
			status += "No person data was sent for update.";
		}

		status += " \n";

		PhoneNumberUpdates phoneNumUpdates = updateReq.getPhoneNumberUpdates();
		if (phoneNumUpdates.getNewPhoneNumber() == null) {
			status += "No phone number data was sent for update.";
		} else {
			// updateQuery works only on landlines
			status += PhonebookDAO.updateQueryPhoneNumber(phoneNumUpdates);

			// updates with save/delete
//			status += PhonebookDAO.updatePhoneNumber(phoneNumUpdates);
		}

		return status;

	}

}
