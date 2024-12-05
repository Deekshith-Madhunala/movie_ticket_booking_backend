package movie.ticket.movie_ticket_booking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;

import movie.ticket.movie_ticket_booking.entity.User;
import movie.ticket.movie_ticket_booking.modelDTO.UserDTO;
import movie.ticket.movie_ticket_booking.service.UserService;
import movie.ticket.movie_ticket_booking.util.ReferencedException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "userId") final Integer userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<User> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final User createdUser = userService.create(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<Integer> updateUser(@PathVariable(name = "userId") final Integer userId,
            @RequestBody @Valid final UserDTO userDTO) {
        userService.update(userId, userDTO);
        return ResponseEntity.ok(userId);
    }

    @DeleteMapping("/{userId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") final Integer userId) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(userId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password){
        return ResponseEntity.ok(userService.findByEmailAndPassword(email, password));
    }

}
