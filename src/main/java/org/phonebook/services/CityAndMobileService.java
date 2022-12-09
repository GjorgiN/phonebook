package org.phonebook.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.phonebook.dao.PhonebookDAO;
import org.phonebook.entity.Phonebook;
import org.phonebook.reqAndRes.CityMobileCodesReq;

@Path("/cityAndMobile")
public class CityAndMobileService {

	@GET
	@Path("/getAllContactsByCodes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Phonebook> getAllContactsByCityOrOperator(CityMobileCodesReq req) throws Exception {
		List<Phonebook> result = new ArrayList<>();

		if (req.getCityCode() != null) {
			result.addAll(PhonebookDAO.getAllContactsByCityCodes(req));
		} else if (req.getMobilePrefix() != null) {
			result.addAll(PhonebookDAO.getAllContactsByMobileOperator(req));
		}

		return result;
	}

}
