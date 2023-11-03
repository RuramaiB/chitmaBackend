package me.ruramaibotso.umc.repository;


import me.ruramaibotso.umc.model.PayNowDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PayNowDetails, Integer> {

}
