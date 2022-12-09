package org.phonebook.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.phonebook.dao.PhonebookDAO;
import org.phonebook.entity.Phonebook;

@Path("/country")
public class CountryCodeService {

	@GET
	@Path("/getContacts/{countryCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Phonebook> getAllContactsByCountryCode(@PathParam("countryCode") Integer cc) {
		List<Phonebook> result = new ArrayList<>();
		
		result = PhonebookDAO.getAllContactsByCountry(cc);
		
		
		return result;
		
	}
	
	
	
}
