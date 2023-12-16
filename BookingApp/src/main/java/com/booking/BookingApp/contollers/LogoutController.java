package com.booking.BookingApp.contollers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/logOut")
@CrossOrigin(origins = "http://localhost:4200")
public class LogoutController {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET) // Dodaj ovu liniju
    public ResponseEntity<String> logout() {
        try {
            // Pribavi trenutnog autentifikovanog korisnika
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Ako je korisnik autentifikovan, odjavi ga
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }

            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during logout");
        }
    }
}