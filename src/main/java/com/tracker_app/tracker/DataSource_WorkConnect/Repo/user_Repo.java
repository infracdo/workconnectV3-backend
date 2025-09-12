package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface user_Repo extends CrudRepository<user, Long>{

    @Query("SELECT d FROM user d WHERE d.username=?1")
    user get_user(String username);

    @Query(value = "SELECT * FROM user WHERE username = :username ;", nativeQuery = true)
    public List<user> find_user_by_username(@Param("username") String username);

    @Query(value = "SELECT * FROM user u "
        + "WHERE "  
        + "(:dateFrom IS NULL OR u.dateCreated >= :dateFrom) "
        + "AND (:dateTo IS NULL OR u.dateCreated <= :dateTo) "
        + "AND (:username IS NULL OR u.username LIKE :username%) "
        + "AND (:mobileNumber IS NULL OR u.mobileNumber LIKE :mobileNumber%) "
        + "AND (:firstName IS NULL OR u.firstName LIKE :firstName%) "
        + "AND (:lastName IS NULL OR u.lastName LIKE :lastName%) "
        + "AND (:tenant IS NULL OR u.tenant LIKE :tenant%) "
        , nativeQuery = true)
    Page<user> searchUser(
        @Param("dateFrom") Date dateFrom,
        @Param("dateTo") Date dateTo, 
        @Param("username") String username, 
        @Param("mobileNumber") String mobileNumber, 
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("tenant") String tenant,
        Pageable pageable
        );
}
    