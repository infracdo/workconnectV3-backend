package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.attendance;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface attendance_Repo extends CrudRepository<attendance, Long>{
    @Query("SELECT d FROM attendance d WHERE d.user=?1")
    List<attendance> findByuser(String user);
}
