package com.shopping.item.repository;

import com.shopping.item.entity.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT i
           FROM Item i
           WHERE i.id = :itemId
             AND i.deleted = false
    """)
    Optional<Item> findByIdForUpdate(@Param("itemId") Long itemId);

    Optional<Item> findByIdAndDeletedFalse(@Param("id") Long itemId);
}
