package com.prs.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Request;
import com.prs.db.RequestRepository;
import com.prs.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/requests")
public class RequestController {

	@Autowired
	private RequestRepository requestRepo;
	@Autowired
	private UserRepository userRepo;

	// list all requests
	@GetMapping("/")
	public JsonResponse listRequest() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.findAll());

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();

		}
		return jr;
	}

	// return 1 request
	@GetMapping("/{id}")
	public JsonResponse getRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.findById(id));

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// - adds a new request
	@PostMapping("/")
	public JsonResponse addRequest(@RequestBody Request r) {
		JsonResponse jr = null;
		try {
			r.setStatus("New");
			r.setSubmittedDate(LocalDateTime.now());
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// update a Request
	@PutMapping("/")
	public JsonResponse updateRequest(@RequestBody Request r) {
		JsonResponse jr = null;
		try {
			if (requestRepo.existsById(r.getId())) {
				jr = JsonResponse.getInstance(requestRepo.save(r));
			} else {
				jr = JsonResponse.getInstance("Error updating Request! Request: " + r.getId() + " does not exist");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// Delete a Request
	@DeleteMapping("/{id}")
	public JsonResponse deleteRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			if (requestRepo.existsById(id)) {
				requestRepo.deleteById(id);
				jr = JsonResponse.getInstance("Request " + id + " successfully deleted");
			} else
				jr = JsonResponse.getInstance("Error deleting Request! Request" + id + " does not exist");
		}

		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
		}

		catch (Exception e) {
			jr = JsonResponse.getInstance(e.getMessage());

		}
		return jr;

	}

// submit for new review (json response <$50 = auto approve, $50<submit for// review)
	@PutMapping("/submit-review")
	public JsonResponse submitForReview(@RequestBody Request r) {
		JsonResponse jr = null;
		try {
			// status automatically approves for items <= 50
			if (r.getTotal() <= 50) {
				r.setStatus("Approved");
			} else {
				r.setStatus("Review");
			}
			// get and set date to local current time
			r.setSubmittedDate(LocalDateTime.now());

			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	// List all requests in review status, but not assigned to a specific user
	@GetMapping("/list-review/{id}")
	public JsonResponse reviewStatus(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.findByUserNotAndStatus(userRepo.findById(id).get(), "Review"));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}
	//set status 
	@PutMapping("/reject")
	public JsonResponse reject(@RequestBody Request r) {
		JsonResponse jr = null;
		try {
			r.setStatus("Rejected");
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	//set status to approve
	@PutMapping("/approve")
	public JsonResponse approve(@RequestBody Request r) {
		JsonResponse jr = null;
		try {
			r.setStatus("Approved");
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}
