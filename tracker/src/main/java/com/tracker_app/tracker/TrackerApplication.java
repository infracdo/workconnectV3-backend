package com.tracker_app.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@SpringBootApplication
@EnableKafka
// @EnableCaching
@EnableAsync
@EnableScheduling
public class TrackerApplication {

  public static void main(String[] args) throws Exception {
    try {
      String sshUser = "ubuntu";
      String sshHost = "202.60.10.142";
      int sshPort = 22; // Default SSH port
      int localPort = 3307; // Local port to use for tunnel
      String remoteHost = "192.168.18.142";
      int remotePort = 3306; // Port of the database on the remote host

      JSch jsch = new JSch();
      Session session = jsch.getSession(sshUser, sshHost, sshPort);
      session.setPassword("ap0ll0");
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      int assignedPort = session.setPortForwardingL(localPort, remoteHost, remotePort);

      System.out.println("SSH Tunnel established on localhost:" + assignedPort);

          // Once you're done, remember to disconnect the session:
          // session.disconnect();
    } catch (Exception e) {
      // TODO: handle exception
    }
    
    
    SpringApplication.run(TrackerApplication.class, args);
  }
}
