package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Vendor;
import com.prs.db.VendorRepository;

@CrossOrigin
@RestController
@RequestMapping("/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vendorRepo;
	
	
	//list all vendors
	@GetMapping("/")
	public JsonResponse listVendor() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(vendorRepo.findAll());

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();

		}
		return jr;
	}
		//return 1 vendor
		@GetMapping("/{id}")
		public JsonResponse getVendor(@PathVariable int id) {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(vendorRepo.findById(id));

			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();

			}
			return jr;
		}
			// - adds a new vendor
			@PostMapping("/")
			public JsonResponse addVendor(@RequestBody Vendor v) {
				JsonResponse jr = null;
				try {
					jr = JsonResponse.getInstance(vendorRepo.save(v));
				} catch (DataIntegrityViolationException dive) {
					jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
					dive.printStackTrace();
				} catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
				return jr;

			}
			
			// update a Vendor
			@PutMapping("/")
			public JsonResponse updateVendor(@RequestBody Vendor v) {
				JsonResponse jr = null;
				try {
					if (vendorRepo.existsById(v.getId())) {
						jr = JsonResponse.getInstance(vendorRepo.save(v));
					} else {
						jr = JsonResponse.getInstance("Error updating Vendor! Vendor: " + v.getId() + " does not exist");
					}
				} catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
				return jr;

			}
			// Delete a Vendor
			@DeleteMapping("/{id}")
			public JsonResponse deleteVendor(@PathVariable int id) {
				JsonResponse jr = null;
				try {
					if (vendorRepo.existsById(id)) {
						vendorRepo.deleteById(id);
						jr = JsonResponse.getInstance("Vendor " + id + " successfully deleted");
					} 
					else 
						jr = JsonResponse.getInstance("Error deleting Vendor! Vendor" + id + " does not exist");
					}
					
					
					 catch (DataIntegrityViolationException dive) {
						jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
						}

				 
					 catch (Exception e) {
					jr = JsonResponse.getInstance(e.getMessage());

				}
				return jr;

					 }

}





