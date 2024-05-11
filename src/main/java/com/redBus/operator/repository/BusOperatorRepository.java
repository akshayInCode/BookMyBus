package com.redBus.operator.repository;

import com.redBus.operator.entity.BusOperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface BusOperatorRepository extends JpaRepository<BusOperator, String> {

    Page<BusOperator> findByBusOperatorCompanyName(String busOperatorCompanyName, Pageable pageable);

    List<BusOperator> findByDepartureCityAndArrivalCityAndDepartureDate(String departureCity, String arrivalCity, Date departureDate);



//    Optional<BusOperator> findByBusId(String busId);

    @Query("SELECT bo FROM BusOperator bo WHERE bo.departureCity=:departureCity AND bo.arrivalCity=:arrivalCity AND bo.departureDate=:departureDate")
    List<BusOperator> searchByCitiesAndDate(@Param("departureCity") String departureCity, @Param("arrivalCity")String arrivalCity, @Param("departureDate")Date departureDate);

    // @Query("Select bo from BusOperator where bo.email=:email")
   // Optional<BusOperator> searchByEmail(@Param("email") String email);

}
