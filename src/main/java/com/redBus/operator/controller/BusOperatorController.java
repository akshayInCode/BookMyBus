package com.redBus.operator.controller;


import com.redBus.operator.payload.BusOperatorDto;
import com.redBus.operator.service.BusOperatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/bus-operator")
public class BusOperatorController {

    private BusOperatorService busOperatorService;

    public BusOperatorController(BusOperatorService busOperatorService) {
        this.busOperatorService = busOperatorService;
    }
    //http://localhost:8080/api/bus-operator

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> scheduleBus(@Valid @RequestBody BusOperatorDto busOperatorDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BusOperatorDto dto = busOperatorService.scheduleBus(busOperatorDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/bus-operator/{busId}
    @DeleteMapping("/{busId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBusOperator(@PathVariable String busId) {
        busOperatorService.deleteBusOperator(busId);
        return new ResponseEntity<>("BusOperator is deleted", HttpStatus.OK);

    }

    @PutMapping("/{busId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusOperatorDto> updateBusOperator(
            @PathVariable String busId, @RequestBody BusOperatorDto busOperatorDto) {
        BusOperatorDto dto = busOperatorService.updateBusOperator(busId, busOperatorDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    //http://localhost:8080/api/bus-operator?busOperatorCompanyName=Indian travels
    @GetMapping
    public ResponseEntity<List<BusOperatorDto>> getBusDetailsByCompanyName
    (@RequestParam(value = "busOperatorCompanyName") String busOperatorCompanyName,
     @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
     @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
     @RequestParam(value = "sortBy", defaultValue = "driverName", required = false) String sortBy,
     @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        List<BusOperatorDto> busOperatorDtoList = busOperatorService.getBusDetailsByCompanyName(busOperatorCompanyName, pageNo, pageSize, sortBy, sortDir);
        if (busOperatorDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(busOperatorDtoList, HttpStatus.OK);
    }
}




