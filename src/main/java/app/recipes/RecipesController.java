package app.recipes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipesController {

  @GetMapping
  public ResponseEntity<String> initReq() {
    return ResponseEntity.of(Optional.of("Hello"));
  }
}
