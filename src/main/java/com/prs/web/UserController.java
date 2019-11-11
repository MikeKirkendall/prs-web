package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.web.JsonResponse;
import com.prs.business.User;
import com.prs.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	
		@Autowired
		private UserRepository userRepo;
		
		
		//list all users
		@GetMapping("/")
		public JsonResponse listUser() {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(userRepo.findAll());

			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();

			}
			return jr;
		}
		@PostMapping("/login")
		public JsonResponse addLogin(@RequestBody User u) {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(userRepo.findByUserNameAndPassWord(u.getUserName(),u.getPassWord()));
				
			}catch (DataIntegrityViolationException dive) {
				jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			}
			
			catch (Exception e) {
				jr =JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			return jr;
		}
		
			//return 1 user
			@GetMapping("/{id}")
			public JsonResponse getUser(@PathVariable int id) {
				JsonResponse jr = null;
				try {
					jr = JsonResponse.getInstance(userRepo.findById(id));

				} catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();

				}
				return jr;
			}
				// - adds a new user
				@PostMapping("/")
				public JsonResponse addUser(@RequestBody User u) {
					JsonResponse jr = null;
					try {
						jr = JsonResponse.getInstance(userRepo.save(u));
					} catch (DataIntegrityViolationException dive) {
						jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
						dive.printStackTrace();
					} catch (Exception e) {
						jr = JsonResponse.getInstance(e);
						e.printStackTrace();
					}
					return jr;

				}
				
				// update a User
				@PutMapping("/")
				public JsonResponse updateUser(@RequestBody User u) {
					JsonResponse jr = null;
					try {
						if (userRepo.existsById(u.getId())) {
							jr = JsonResponse.getInstance(userRepo.save(u));
						} else {
							jr = JsonResponse.getInstance("Error updating User! User: " + u.getId() + " does not exist");
						}
					} catch (Exception e) {
						jr = JsonResponse.getInstance(e);
						e.printStackTrace();
					}
					return jr;

				}
				// Delete a User
				@DeleteMapping("/{id}")
				public JsonResponse deleteUser(@PathVariable int id) {
					JsonResponse jr = null;
					try {
						if (userRepo.existsById(id)) {
							userRepo.deleteById(id);
							jr = JsonResponse.getInstance("User " + id + " successfully deleted");
						} 
						else 
							jr = JsonResponse.getInstance("Error deleting User! User" + id + " does not exist");
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

