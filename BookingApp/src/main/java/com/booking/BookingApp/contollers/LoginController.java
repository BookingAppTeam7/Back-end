package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.dtos.users.JwtAuthenticationRequest;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IUserRepository;
import com.booking.BookingApp.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private IUserRepository userRepository;
	@PostMapping()
	@CrossOrigin(origins = "http://localhost:4200")
	public User login(@RequestBody JwtAuthenticationRequest user) {
		System.out.println("USEEER ----> "+ user.getUsername());
		System.out.println("PASSWORD ----> "+ user.getPassword());

		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getUsername(),
				user.getPassword());

		System.out.println("AUTH REQ---> "+ authReq.toString());

		System.out.println("28 LINIJA LOGIN CONTROLLER");
		Authentication auth = authenticationManager.authenticate(authReq);
		System.out.println("33333 LINIJA LOGIN CONTROLLER");
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(auth);

		System.out.println("33 LINIJA LOGIN CONTROLLER username "+user.getUsername());
		String token = jwtTokenUtil.generateToken(user.getUsername());
		System.out.println("35 LINIJA LOGIN CONTROLLER");
		Optional<User> logedInUser=userRepository.findById(user.getUsername());
		logedInUser.get().setJwt(token);
		System.out.println("SETTTT ATRIBUTA "+ token);
		return logedInUser.get();
	}

}
