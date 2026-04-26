package com.server.ticketmanagement.repositories;

import com.server.ticketmanagement.domain.entities.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {

    @Query("SELECT COUNT(v) FROM TicketValidation v JOIN v.ticket t JOIN t.ticketType tt JOIN tt.event e WHERE e.organizer.id = :organizerId")
    long countValidatedTicketsByOrganizer(@Param("organizerId") UUID organizerId);

}
