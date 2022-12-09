package org.phonebook.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
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
import org.phonebook.reqAndRes.CityMobileCodesReq;
import org.phonebook.reqAndRes.PhoneNumber;
import org.phonebook.reqAndRes.PhoneNumberUpdates;
import org.phonebook.reqAndRes.RecordRequest;

public class PhonebookDAO {

	public static String createRecord(RecordRequest req) {
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
//					address = p.getAddress();
//					s.save(address);
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

			CityAreaCode cac = CityDAO.findCityCodesInDb(reqPhoneNumber.getCityAreaCode(), s);
			if (cac == null) {
				cac = reqPhoneNumber.getCityAreaCode();
				s.save(cac);
			}

			Landline landline = new Landline(cc, reqPhoneNumber.getPhoneNumber(), cac);
//			PhoneNumberId compositeKey = new PhoneNumberId(reqPhoneNumber.getPhoneNumber(), cc, cac);
//			Phonebook record = new Phonebook(landline, p);
			LandlinesPhonebook record = new LandlinesPhonebook(p, landline);

			s.save(record);

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
			return "Something went wrong, try again later";
		}

		return "New record saved successfully";
	}

	public static Phonebook findPhoneNumber(PhoneNumber req) {

		Integer reqCityCode = null;
		Integer reqLocalCode = null;
		Integer reqMobileCode = null;

		Integer reqCountryCode = req.getCountryCode().getCountryCode();

		if (req.getCityAreaCode() != null) {
			reqCityCode = req.getCityAreaCode().getCityAreaCode();
			reqLocalCode = req.getCityAreaCode().getCityLocalAreaCode();
		} else {
			reqMobileCode = req.getMobileCode().getPrefix();
		}

		Integer reqPhoneNumber = req.getPhoneNumber();

		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = s.beginTransaction();
		List<Phonebook> result = new ArrayList<>();

		try {
			if (reqCityCode != null) {
				String query = "SELECT * " + "FROM landlines_phonebook "
						+ "JOIN countrycode ON countrycode.country_id=landlines_phonebook.countrycode "
						+ "JOIN cityareacode ON cityareacode.city_id=landlines_phonebook.cityareacode " + "WHERE "
						+ "landlines_phonebook.phonenumber = " + reqPhoneNumber + " " + "AND countrycode.countrycode = "
						+ reqCountryCode + " " + "AND cityareacode.cityareacode = " + reqCityCode + " ";
				if (reqLocalCode == null) {
					query += "AND cityareacode.citylocalareacode IS NULL;";
				} else {
					query += "AND cityareacode.citylocalareacode = " + reqLocalCode + ";";
				}
				result.addAll(s.createNativeQuery(query, org.phonebook.entity.LandlinesPhonebook.class).list());

			}

			if (reqMobileCode != null) {
				String query = "SELECT * " + "FROM mobile_phonebook "
						+ "JOIN countrycode ON countrycode.country_id=mobile_phonebook.countrycode "
						+ "JOIN mobilecode ON mobilecode.operator_id=mobile_phonebook.mobilecode " + "WHERE "
						+ "mobile_phonebook.phonenumber = " + reqPhoneNumber + " " + "AND countrycode.countrycode = "
						+ reqCountryCode + " " + "AND mobilecode.prefix = " + reqMobileCode + ";";

				result.addAll(s.createNativeQuery(query, org.phonebook.entity.MobilePhonebook.class).list());

			}

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}
		if (result.isEmpty()) {
			result.add(null);
		}
		return result.get(0);

	}

	public static List<Phonebook> getAllContacts() {
		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = null;
		List<Phonebook> wholePhonebook = new ArrayList<>();
		try {
			tx = s.beginTransaction();

			wholePhonebook.addAll(s.createNativeQuery("SELECT * FROM landlines_phonebook;",
					org.phonebook.entity.LandlinesPhonebook.class).list());

			wholePhonebook.addAll(
					s.createNativeQuery("SELECT * FROM mobile_phonebook;", org.phonebook.entity.MobilePhonebook.class)
							.list());
//			for (Phonebook phonebook : wholePhonebook) {
//				s.get(CountryCode.class, phonebook.getPhoneNumber().getCountryCode().getCountryId());
//				s.get(CityAreaCode.class, phonebook.getPhoneNumber().getCityAreaCode().getCityId());
//				s.get(Phonebook.class, phonebook.getPhoneNumber().getPhoneNumber());
//				s.get(Person.class, phonebook.getPerson().getEmbg());
//				s.get(Address.class, phonebook.getPerson().getAddress().getUniqueId());
//			}

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return wholePhonebook;
	}

	public static List<Phonebook> getAllContactsByCountry(Integer cc) {
		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = null;
		List<Phonebook> result = new ArrayList<>();
		try {
			tx = s.beginTransaction();

			String phonebook = "landlines_";

			String query = "SELECT * FROM public." + phonebook + "phonebook " + "JOIN public.countrycode ON public."
					+ phonebook + "phonebook.countrycode = public.countrycode.country_id "
					+ "WHERE public.countrycode.countrycode = " + cc + ";";

			result.addAll(s.createNativeQuery(query, org.phonebook.entity.LandlinesPhonebook.class).list());

			phonebook = "mobile_";
			query = "SELECT * FROM public." + phonebook + "phonebook " + "JOIN public.countrycode ON public."
					+ phonebook + "phonebook.countrycode = public.countrycode.country_id "
					+ "WHERE public.countrycode.countrycode = " + cc + ";";

			result.addAll(s.createNativeQuery(query, org.phonebook.entity.MobilePhonebook.class).list());

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return result;
	}

	public static List<Phonebook> getAllContactsByCityCodes(CityMobileCodesReq cityCodesReq) {
		List<Phonebook> result = new ArrayList<>();
		Integer cityCode = cityCodesReq.getCityCode();
		Integer cityLocalCode = cityCodesReq.getCityLocalCode();

		if (cityCode == null) {
			return null;
		}

		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = null;

		try {
			tx = s.beginTransaction();

			String query = "SELECT * FROM public.landlines_phonebook"
					+ " JOIN public.cityareacode ON public.landlines_phonebook.cityareacode = public.cityareacode.city_id"
					+ " WHERE public.cityareacode.cityareacode = " + cityCode;
			if (cityLocalCode == null) {
				query += ";";
			} else {
				query += " AND public.cityareacode.citylocalareacode = " + cityLocalCode + ";";
			}

			result.addAll(s.createNativeQuery(query, org.phonebook.entity.LandlinesPhonebook.class).list());

			tx.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return result;
	}

	public static List<Phonebook> getAllContactsByMobileOperator(CityMobileCodesReq req) {
		List<Phonebook> result = new ArrayList<>();
		Integer mobilePrefix = req.getMobilePrefix();

		Session s = new SessionDAO().getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();

			String query = "SELECT * FROM public.mobile_phonebook"
					+ " JOIN public.mobilecode ON public.mobile_phonebook.mobilecode = public.mobilecode.operator_id"
					+ " WHERE public.mobilecode.prefix = " + mobilePrefix;

			result.addAll(s.createNativeQuery(query, org.phonebook.entity.MobilePhonebook.class).list());

			tx.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return result;
	}

	public static List<Phonebook> getAllContactsByEmbg(String embg) {
		List<Phonebook> result = new ArrayList<>();

		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = null;

		try {
			tx = s.beginTransaction();

			result.addAll(s.createNativeQuery("SELECT * FROM landlines_phonebook WHERE person_id = '" + embg + "';",
					org.phonebook.entity.LandlinesPhonebook.class).list());

			result.addAll(s.createNativeQuery("SELECT * FROM mobile_phonebook WHERE person_id = '" + embg + "';",
					org.phonebook.entity.MobilePhonebook.class).list());

			tx.commit();
			s.close();

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return result;
	}

	public static List<Phonebook> getAllContactsByFirstName(String firstName) {
		List<Phonebook> result = new ArrayList<>();

		SessionDAO session = new SessionDAO();
		Session s = session.getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			String queryLandlines = "SELECT * FROM public.landlines_phonebook JOIN public.person ON "
					+ "public.landlines_phonebook.person_id = public.person.embg WHERE public.person.firstname = '"
					+ firstName + "';";

			result.addAll(s.createNativeQuery(queryLandlines, org.phonebook.entity.LandlinesPhonebook.class).list());

			String queryMobile = "SELECT * FROM public.mobile_phonebook JOIN public.person ON "
					+ "public.mobile_phonebook.person_id = public.person.embg WHERE public.person.firstname = '"
					+ firstName + "';";

			result.addAll(s.createNativeQuery(queryMobile, org.phonebook.entity.MobilePhonebook.class).list());

			tx.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return result;
	}

	public static String updatePhoneNumber(PhoneNumberUpdates phoneNumUpdates) {
		Session s = new SessionDAO().getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();

			PhoneNumber currentPhone = phoneNumUpdates.getCurrentPhoneNumber();
			PhoneNumber newPhone = phoneNumUpdates.getNewPhoneNumber();
			Phonebook currentRecord = findPhoneNumber(currentPhone);

			if (findPhoneNumber(newPhone) != null) {
				tx.rollback();
				s.close();
				return "Phone number already in use.";
			}

			CountryCode updateCountry = newPhone.getCountryCode();
			CountryCode newCountry = CountryDAO.findCountryInDb(updateCountry.getCountryCode(), s);
			if (newCountry == null) {
				newCountry = updateCountry;
				s.save(newCountry);
			}

			CityAreaCode updateCity = newPhone.getCityAreaCode();
			if (updateCity != null) {
				CityAreaCode newCity = CityDAO.findCityCodesInDb(updateCity, s);
				if (newCity == null) {
					newCity = updateCity;
					s.save(newCity);
				}

//				PhoneNumberId newRecordId = new PhoneNumberId(newPhone.getPhoneNumber(), newCountry, newCity);
				Landline landline = new Landline(newCountry, newPhone.getPhoneNumber(), newCity);
//				Phonebook updatedRecord = new Phonebook(landline, currentRecord.getPerson());
				LandlinesPhonebook updatedRecord = new LandlinesPhonebook(currentRecord.getPerson(), landline);

				s.save(updatedRecord);
				s.delete(currentRecord);
			}

			MobileCode updateMobile = newPhone.getMobileCode();
			if (updateMobile != null) {
				MobileCode newMobile = MobileDAO.findMobileCodesInDB(updateMobile.getPrefix(),
						updateMobile.getOperator());
				if (newMobile == null) {
					newMobile = updateMobile;
					s.save(newMobile);
				}
				Mobile mobile = new Mobile(newCountry, newPhone.getPhoneNumber(), newMobile);
				MobilePhonebook updatedRecord = new MobilePhonebook(currentRecord.getPerson(), mobile);

				s.save(updatedRecord);
				s.delete(currentRecord);
			}

			tx.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
		}

		return "Phone number updated successfully";
	}

	public static String updateQueryPhoneNumber(PhoneNumberUpdates phoneNumUpdates) {
		Session s = new SessionDAO().getSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();

			PhoneNumber currentPhone = phoneNumUpdates.getCurrentPhoneNumber();
			PhoneNumber newPhone = phoneNumUpdates.getNewPhoneNumber();

			Phonebook newRecord = findPhoneNumber(newPhone);
			if (newRecord != null) {
				tx.rollback();
				s.close();
				return "Phone number is already used.";
			}

			Phonebook currentRecord = findPhoneNumber(currentPhone);
			if (currentRecord.getClass() == LandlinesPhonebook.class) {
				LandlinesPhonebook currentLandline = (LandlinesPhonebook) currentRecord;

				CountryCode newCountry = CountryDAO.findCountryInDb(newPhone.getCountryCode().getCountryCode(), s);
				CountryCode currentCountry = CountryDAO.findCountryInDb(currentPhone.getCountryCode().getCountryCode(),
						s);
				Boolean countryChanged = false;
				if (newCountry != currentCountry) {
					countryChanged = !countryChanged;
					if (newCountry == null) {
						newCountry = newPhone.getCountryCode();
						s.save(newCountry);
						s.flush();
						// if flush is not used, must save the country in separate session,
						// country must be persisted in DB (tx.commmit) before you can update
//					newCountry = CountryDAO.saveCountryCode(newPhone.getCountryCode());
					}

					countryUpdateQuery(newCountry, currentLandline, s);

					// cannot update by changing via setters - throws error that obj is changed
//				currentLandline
//						.setPhoneNumber(new Landline(newCountry, currentLandline.getPhoneNumber().getPhoneNumber(),
//								currentLandline.getPhoneNumber().getCityAreaCode()));
//				s.update(currentLandline);
				}

				CityAreaCode currentCity = CityDAO.findCityCodesInDb(currentPhone.getCityAreaCode(), s);
				CityAreaCode newCity = CityDAO.findCityCodesInDb(newPhone.getCityAreaCode(), s);
				Boolean cityChanged = false;
				if (currentCity != newCity) {
					cityChanged = !cityChanged;
					if (newCity == null) {
						newCity = newPhone.getCityAreaCode();
						s.save(newCity);
						s.flush();
					}
					UUID countryId = countryChanged ? newCountry.getCountryId() : currentCountry.getCountryId();
					Integer currentPhoneNumber = currentLandline.getPhoneNumber().getPhoneNumber();

					cityUpdateQuery(newCity.getCityId(), currentCity.getCityId(), countryId, currentPhoneNumber, s);

				}

				Integer currentPhoneDigits = currentLandline.getPhoneNumber().getPhoneNumber();
				Integer newPhoneDigits = newPhone.getPhoneNumber();
				if (currentPhoneDigits != newPhoneDigits) {
					UUID countryId = countryChanged ? newCountry.getCountryId() : currentCountry.getCountryId();
					UUID cityId = cityChanged ? newCity.getCityId() : currentCity.getCityId();
					
					phoneDigitsUpdateQuery(countryId, cityId, currentPhoneDigits, newPhoneDigits, s);
				}

			}
			tx.commit();
			s.close();
			return "Phone number was successfully updated";

		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			s.close();
			return "Update of phone number failed.";
		}

	}

	private static void phoneDigitsUpdateQuery(UUID countryId, UUID cityId, Integer currentPhoneDigits,
			Integer newPhoneDigits, Session s) {

		NativeQuery<LandlinesPhonebook> q = s.createNativeQuery("UPDATE landlines_phonebook SET phonenumber = '"
				+ newPhoneDigits + "' WHERE countrycode = '" + countryId + "' AND " + "cityareacode = '" + cityId
				+ "' AND " + "phonenumber = " + currentPhoneDigits + ";",
				org.phonebook.entity.LandlinesPhonebook.class);
		q.executeUpdate();

	}

	private static void cityUpdateQuery(UUID newCityId, UUID currentCityId, UUID countryId, Integer phoneNumber,
			Session s) {

		NativeQuery<LandlinesPhonebook> q = s.createNativeQuery("UPDATE landlines_phonebook SET cityareacode = '"
				+ newCityId + "' WHERE countrycode = '" + countryId + "' AND " + "cityareacode = '" + currentCityId
				+ "' AND " + "phonenumber = " + phoneNumber + ";", org.phonebook.entity.LandlinesPhonebook.class);
		q.executeUpdate();

	}

//	private static void cityUpdateQuery(CityAreaCode newCity, LandlinesPhonebook currentLandline, Session s) {
//		UUID currentCountryId = currentLandline.getPhoneNumber().getCountryCode().getCountryId();
//		UUID currentCityId = currentLandline.getPhoneNumber().getCityAreaCode().getCityId();
//		Integer currentPhoneDigits = currentLandline.getPhoneNumber().getPhoneNumber();
//
//	}

	private static void countryUpdateQuery(CountryCode newCountry, LandlinesPhonebook currentLandline, Session s) {
		UUID currentCountryId = currentLandline.getPhoneNumber().getCountryCode().getCountryId();
		UUID currentCityId = currentLandline.getPhoneNumber().getCityAreaCode().getCityId();
		Integer currentPhoneDigits = currentLandline.getPhoneNumber().getPhoneNumber();
		NativeQuery<LandlinesPhonebook> q = s
				.createNativeQuery(
						"UPDATE landlines_phonebook SET countrycode = '" + newCountry.getCountryId()
								+ "' WHERE countrycode = '" + currentCountryId + "' AND " + "cityareacode = '"
								+ currentCityId + "' AND " + "phonenumber = " + currentPhoneDigits + ";",
						org.phonebook.entity.LandlinesPhonebook.class);
		q.executeUpdate();

	}
}
