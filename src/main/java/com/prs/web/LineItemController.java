package com.prs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.LineItem;
import com.prs.business.Product;
import com.prs.business.Request;
import com.prs.db.LineItemRepository;
import com.prs.db.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/line-items")
public class LineItemController {

	@Autowired
	private LineItemRepository lineItemRepo;

	@Autowired
	private RequestRepository requestRepo;

	// list all lineItems
	@GetMapping("/")
	public JsonResponse listLineItem() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();

		}
		return jr;
	}

	// return 1 lineItem
	@GetMapping("/{id}")
	public JsonResponse getLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findById(id));

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();

		}
		return jr;
	}

	// - adds a new lineItem
	@PostMapping("/")
	public JsonResponse addLineItem(@RequestBody LineItem li) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.save(li));
			recalculateTotal(li.getRequest().getId());

		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}

	// any changes to Line item need: POST, PUT, DELETE
	// 2.recalc total = a-get list of line items for request(lines), b)loop through
	// lines c-sum new total. Void return type-means it will save total
	// 3. set request.Total = new Total.

	// update a LineItem
	@PutMapping("/")
	public JsonResponse updateLineItem(@RequestBody LineItem li) {
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(li.getId())) {
				jr = JsonResponse.getInstance(lineItemRepo.save(li));
				recalculateTotal(li.getRequest().getId());
			} else {
				jr = JsonResponse.getInstance("Error updating LineItem! LineItem: " + li.getId() + " does not exist");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}

	// get - line items for PR
	@GetMapping("/lines-for-pr/{Id}")
	public JsonResponse getAllLineItmes(@PathVariable int Id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findByRequestId(Id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}

	// Delete a LineItem
	@DeleteMapping("/{id}")
	public JsonResponse deleteLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(id)) {
				lineItemRepo.deleteById(id);
				LineItem li = lineItemRepo.findById(id).get();
				recalculateTotal(li.getRequest().getId());
				jr = JsonResponse.getInstance("LineItem " + id + " successfully deleted");
			} else
				jr = JsonResponse.getInstance("Error deleting LineItem! LineItem" + id + " does not exist");
		}

		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
		}

		catch (Exception e) {
			jr = JsonResponse.getInstance(e.getMessage());

		}
		return jr;

	}

	// save that total in Line Item instance
	private void recalculateTotal(int requestID) {
		// get a list of line items
		List<LineItem> lines = lineItemRepo.findByRequestId(requestID);
		// loop through list to get a sum of total
		double total = 0.0;
		for (LineItem line : lines) {
			Product p = line.getProduct();
			total += p.getPrice() * line.getQuantity();
		}
		// save that total in the instance of request
		Request r = requestRepo.findById(requestID).get();
		r.setTotal(total);
		requestRepo.save(r);
	}

}
