package com.server.ticketmanagement.repositories;

import com.server.ticketmanagement.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("SELECT COUNT(t) FROM Ticket t JOIN t.ticketType tt JOIN tt.event e WHERE e.organizer.id = :organizerId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    long countTicketsSoldByOrganizerAndDate(@Param("organizerId") UUID organizerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(tt.price), 0) FROM Ticket t JOIN t.ticketType tt JOIN tt.event e WHERE e.organizer.id = :organizerId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    double sumTicketsSoldByOrganizerAndDate(@Param("organizerId") UUID organizerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT tt.name, tt.price, e.name AS event_name, COUNT(t.id) * tt.price AS total_sold " +
            "FROM Ticket t JOIN t.ticketType tt JOIN tt.event e WHERE e.organizer.id = :organizerId AND t.createdAt >= :startDate AND t.createdAt <= :endDate " +
            "GROUP BY tt.id, tt.name, e.name " +
            "ORDER BY e.name")
    List<Object[]> countTicketsByTicketTypeForEvent(@Param("organizerId") UUID organizerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    int countByTicketTypeId(UUID ticketTypeId);

    Page<Ticket> findByPurchaserId(UUID purchaserId, Pageable pageable);

    Optional<Ticket> findByIdAndPurchaserId(UUID ticketId, UUID purchaserId);
}
