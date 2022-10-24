package dat3.cars.api;

import dat3.cars.dto.CarRequest;
import dat3.cars.dto.CarResponse;
import dat3.cars.service.CarService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/cars")
@CrossOrigin
public class CarController {
  CarService carService;


  public CarController(CarService carService) {
    this.carService = carService;
  }

  //Security --> ADMIN ONLY
  @PostMapping
  public CarResponse addCar(@RequestBody CarRequest body) {
    return carService.addCar(body, true);
  }

  //Admin
  @PutMapping("/{carId}")
  public String editCar(@RequestBody CarRequest body, @PathVariable int carId) {
    return carService.editCar(body, carId);
  }

  @DeleteMapping("/{carId}")
  public String deleteCar( @PathVariable int carId) {
    return carService.deleteCar(carId);
  }

  //Security ADMIN
  @PatchMapping("/rentalprice/{carId}/{newPrice}")
  public void setNewPrice(@PathVariable int carId, @PathVariable double newPrice) {
    carService.setPricePrDay(carId, newPrice);
  }

  //Security ALL (Not Authenticated)
  @GetMapping
  public List<CarResponse> getCars() {
    return carService.getCars(false);
  }

  //Security ADMIN
  @GetMapping("/admin")
  public List<CarResponse> getCarsWithAllInfo() {
    return carService.getCars(true);
  }

  //Security USER
  //@PreAuthorize("hasAuthority('USER')")
  @GetMapping(path = "/{carId}")
  public CarResponse getCarById(@PathVariable int carId, Principal principal) throws Exception {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean includeAll = (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))?true:false;
    CarResponse response = carService.findCarById(carId, includeAll);
    return response;
  }
  //Security ADMIN
  @GetMapping(path = "/admin/{carId}")
  public CarResponse getCarByIdWithAllInfo(@PathVariable int carId) throws Exception {
    CarResponse response = carService.findCarById(carId, true);
    return response;
  }
}