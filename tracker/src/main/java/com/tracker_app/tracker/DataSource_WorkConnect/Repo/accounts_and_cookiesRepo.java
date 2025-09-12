package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.accounts_and_cookies;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface accounts_and_cookiesRepo extends CrudRepository<accounts_and_cookies, Long>{
    @Query("SELECT d FROM accounts_and_cookies d WHERE d.name=?1")
    accounts_and_cookies findByAccountName(String name);

    @Query("SELECT d FROM accounts_and_cookies d WHERE d.cookie=?1")
    accounts_and_cookies findByAccountCooke(String cookie);
}
