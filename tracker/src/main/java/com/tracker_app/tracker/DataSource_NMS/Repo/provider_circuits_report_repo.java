package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuit_report;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface provider_circuits_report_repo
                extends CrudRepository<provider_circuit_report, Long> {

        @Query(value = "SELECT pc.id, pc. atis, pc.provider, pc.active, pc.not_found_w_mac, pc.not_found_wout_mac, pc.not_in_list "
                        +
                        "FROM provider_circuits_report pc " +
                        "JOIN (SELECT provider, MAX(timestamp) AS latest_timestamp FROM provider_circuits_report GROUP BY provider) AS latest "
                        +
                        "ON pc.provider = latest.provider AND pc.timestamp = latest.latest_timestamp", nativeQuery = true)
        List<provider_circuit_report> getLatestProviderCircuitsReports();

}
