package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Product;
import com.prs.db.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepo;

	// list all products
	@GetMapping("/")
	public JsonResponse listProduct() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepo.findAll());

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();

		}
		return jr;
	}

	// return 1 product
	@GetMapping("/{id}")
	public JsonResponse getProduct(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepo.findById(id));

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();

		}
		return jr;
	}

	// - adds a new product
	@PostMapping("/")
	public JsonResponse addProduct(@RequestBody Product p) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepo.save(p));
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}

	// update a Product
	@PutMapping("/")
	public JsonResponse updateProduct(@RequestBody Product p) {
		JsonResponse jr = null;
		try {
			if (productRepo.existsById(p.getId())) {
				jr = JsonResponse.getInstance(productRepo.save(p));
			} else {
				jr = JsonResponse.getInstance("Error updating Product! Product: " + p.getId() + " does not exist");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}

	// Delete a Product
	@DeleteMapping("/{id}")
	public JsonResponse deleteProduct(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			if (productRepo.existsById(id)) {
				productRepo.deleteById(id);
				jr = JsonResponse.getInstance("Product " + id + " successfully deleted");
			} else
				jr = JsonResponse.getInstance("Error deleting Product! Product" + id + " does not exist");
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




