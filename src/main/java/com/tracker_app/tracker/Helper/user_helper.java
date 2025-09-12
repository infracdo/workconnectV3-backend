package com.tracker_app.tracker.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import com.tracker_app.tracker.CustomObject.keycloack_user;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.user;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.user_Repo;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class user_helper {

    @Value("${Keycloak.Url}")
    private String keycloak_url;

    @Value("${Keycloak.Realm}")
    private String keycloak_relm;

    @Value("${Keycloak.RealmMaster}")
    private String keycloak_relm_master;

    @Value("${Keycloak.Id}")
    private String keycloak_client_id;

    @Value("${Keycloak.Secret}")
    private String keycloak_secret;

    @Value("${Keycloak.Username}")
    private String keycloak_username;

    @Value("${Keycloak.Pass}")
    private String keycloak_password;

    @Autowired
    private user_Repo userRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private mail_service mailservice;

    //@Autowired
    //private kafka_helper kafka = new kafka_helper();

    @Autowired
    private tenant_lookup_repo tenant_repo;

    @Value("${kafka.logs.topic}")
    private String kafka_topic;

    public ResponseEntity<Object> create_user(String authToken, Map<String, String> keyclaockUser) throws JSONException{
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date date = new Date(System.currentTimeMillis());

        Map<String, Object> user_detail = (Map<String, Object>) authService.getUserUsername(authToken);
        //Map<String, Object> message = new HashMap<String, Object>();

        try {
            Map<String, String> result = authService.isTokenValid(authToken);
            if (result.get("status").equals("SUCCESS")) {
                if(authService.getRoles(authToken).contains("ROLE_USERADMIN_CREATE_USER")) {

                    String username = keyclaockUser.get("username").toString();
                    String firstName = keyclaockUser.get("firstName").toString();
                    String lastName = keyclaockUser.get("lastName").toString();
                    String isEnabled = keyclaockUser.get("isEnabled").toString();
                    String mobileNumber = keyclaockUser.get("mobileNumber").toString();
                    String email = keyclaockUser.get("email").toString();
                    String remarks = keyclaockUser.get("remarks").toString();
                    String tenant = "";
                    if(authService.getRoles(authToken).contains("ROLE_SUPERADMIN")){
                        String tenant_name = keyclaockUser.get("tenants").toString();
                        tenant = tenant_repo.get_tenantslug_by_name(tenant_name);
                    }

                    JSONArray roles =  new JSONArray(keyclaockUser.get("role").toString());

                    if(keyclaockUser.get("role").toString().contains("ROLE_TENANT_ADMIN")){
                        JSONArray tenant_roles = new JSONArray();
                        List<String> roles_to_add = new ArrayList<String>();
                        roles_to_add.add("ROLE_SITES_VIEW");
                        roles_to_add.add("ROLE_SITES_NETWORK_VIEW");
                        roles_to_add.add("ROLE_SITES_INTERFACES_VIEW");
                        roles_to_add.add("ROLE_SITES_DEVICES_VIEW");
                        roles_to_add.add("ROLE_SITES_ASSETS_VIEW");
                        roles_to_add.add("ROLE_NETBOX_VIEW_CIRCUITS");
                        roles_to_add.add("ROLE_USERADMIN_VIEW_USER");
                        roles_to_add.add("ROLE_USERADMIN_CREATE_USER");
                        roles_to_add.add("ROLE_USERADMIN_UPDATE_USER");
                        roles_to_add.add("ROLE_TENANT_ADMIN");
                        for (String string : roles_to_add) {
                            tenant_roles.put(new JSONObject().put("name", string));
                        }
                        roles = tenant_roles;
                    }
                    if(keyclaockUser.get("role").toString().contains("ROLE_TENANT_USER")){
                        JSONArray tenant_roles = new JSONArray();
                        List<String> roles_to_add = new ArrayList<String>();
                        roles_to_add.add("ROLE_SITES_VIEW");
                        roles_to_add.add("ROLE_SITES_NETWORK_VIEW");
                        roles_to_add.add("ROLE_SITES_INTERFACES_VIEW");
                        roles_to_add.add("ROLE_SITES_DEVICES_VIEW");
                        roles_to_add.add("ROLE_SITES_ASSETS_VIEW");
                        roles_to_add.add("ROLE_NETBOX_VIEW_CIRCUITS");
                        roles_to_add.add("ROLE_TENANT_USER");
                        for (String string : roles_to_add) {
                            tenant_roles.put(new JSONObject().put("name", string));
                        }
                        roles = tenant_roles;
                    }


                    if(!keyclaockUser.get("role").toString().contains("default-roles-"+keycloak_relm)){
                        JSONObject jo = new JSONObject("{\"name\":\"default-roles-"+keycloak_relm+"\"}");
                        roles.put(jo);
                    }
                    
                    System.out.println("TEST :" + userRepo.find_user_by_username(username));
                    if(!userRepo.find_user_by_username(username).isEmpty()){
                        return new ResponseEntity<Object>("ERROR User already exists",HttpStatus.CONFLICT);
                    }
            
                    Keycloak keycloak = KeycloakBuilder.builder() //
                        .serverUrl(keycloak_url.replace("\r\n","")+"auth") //
                        .realm(keycloak_relm_master.replace("\r\n","")) //
                        .grantType(OAuth2Constants.PASSWORD) //
                        .clientId(keycloak_client_id.replace("\r\n","")) //
                        .clientSecret(keycloak_secret.replace("\r\n","")) //
                        .username(keycloak_username.replace("\r\n","")) //
                        .password(keycloak_password.replace("\r\n","")) //
                        .build();
                    
                    UserRepresentation user = new UserRepresentation();
                    user.setEnabled(Boolean.parseBoolean(isEnabled));
                    user.setUsername(username);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
            
                    
                    Map<String, List<String>> attributes = new HashMap<String, List<String>>();

                    String slug = "";
                    try {
                        Map<String, List<String>> attrib = authService.getUserAttributes(authToken);
                        slug = attrib.get("tenant").get(0);
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
            
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    attributes.put("dateCreated", Arrays.asList(sdf.format(new Date())));
                    attributes.put("lastUpdated", Arrays.asList(sdf.format(new Date())));
                    String createdBy = authService.getUser(authToken);
                    attributes.put("createdBy", Arrays.asList(createdBy));
                    attributes.put("lastUpdatedBy", Arrays.asList(createdBy));
                    if(authService.getRoles(authToken).contains("ROLE_SUPERADMIN")){
                        attributes.put("tenant", Arrays.asList(tenant));
                    }
                    else if(authService.getRoles(authToken).contains("ROLE_TENANT_ADMIN")){
                        attributes.put("tenant", Arrays.asList(slug));
                    }else{
                        attributes.put("tenant", Arrays.asList(tenant));
                    }
                    
                    user.setAttributes(attributes);
                    
                    keycloak.tokenManager().getAccessToken();
                    RealmResource realmResource = keycloak.realm(keycloak_relm);
                    UsersResource usersResource = realmResource.users();
            
                    Response response = usersResource.create(user);
                    System.out.println(response);
                    System.out.println(response.getStatus());
                    if (response.getStatus()==409){
                        
                        List<UserRepresentation>  keyclaock_user= realmResource.users().search(username, 0, 10, true);
                        System.out.println(keyclaock_user.size());
                        UserResource userResource = usersResource.get(keyclaock_user.get(0).getId());
                        keyclaock_user.get(0).setAttributes(attributes);
                        userResource.update(keyclaock_user.get(0));

                        try {
                            String compositeRoles = "";
                            if(roles.length() > 0) {
                                List<RoleRepresentation> assignedRoles = new ArrayList<RoleRepresentation>();
                    
                                for(int i=0; i<roles.length(); i++) {
                                    JSONObject group = roles.getJSONObject(i);
                                    compositeRoles += String.format("%s,", group.get("name").toString());
                                    try {
                                        RoleRepresentation currentRole = realmResource.roles().get(group.get("name").toString()).toRepresentation();
                                        assignedRoles.add(currentRole);
                                    } catch (Exception e) {
                                        return new ResponseEntity<Object>("Role does not exist: "+group.get("name").toString(),HttpStatus.NOT_FOUND);
                                    }
                                }
                                userResource.roles().realmLevel().add(assignedRoles);
                            }
                        } catch (Exception e) {
                            //TODO: handle exception
                        }
            
                        user newuser = new user();
                        newuser.setKeycloakId(keyclaock_user.get(0).getId());
                        newuser.setusername(username);
                        newuser.setfirstName(firstName);
                        newuser.setlastName(lastName);
                        newuser.setemail(email);
                        newuser.setmobileNumber(mobileNumber);
                        newuser.setremarks(remarks);
                        newuser.setisEnabled(isEnabled);
                        newuser.setdateCreated(new Date());
                        newuser.setlastUpdated(new Date());
                        newuser.setcreatedBy(createdBy);
                        newuser.setlastUpdatedBy(createdBy);
                        newuser.setisEnabled(isEnabled);
                        newuser.setremarks(remarks);
                        newuser.setroles(keyclaockUser.get("role").toString());
                        if(authService.getRoles(authToken).contains("ROLE_SUPERADMIN")){
                            if(tenant==null){
                                newuser.settenant("");    
                            }else{
                                String tenant_name = tenant_repo.get_tenant_by_slug(tenant);
                                newuser.settenant(tenant_name);
                            }
                        }
                        else if(authService.getRoles(authToken).contains("ROLE_TENANT_ADMIN")){
                            String tenant_name = tenant_repo.get_tenant_by_slug(slug);
                            newuser.settenant(tenant_name);
                        }
                        else{
                            newuser.settenant("");
                        }
                        userRepo.save(newuser);
                        /* 
                        message.put("user_details",user_detail);
                        message.put("log_type","create_user");
                        message.put("status","success");
                        message.put("user_created",newuser);
                        message.put("time",formatter.format(date));
                        kafka.sendMessage(message.toString(), kafka_topic);
                        */
                        return new ResponseEntity<Object>("User Enabled",HttpStatus.OK);
                    }
            
                    String userId = CreatedResponseUtil.getCreatedId(response);
                    
                    /*
                    user newuser = new user();
                    newuser.setusername(keyclaockUser.getusername());
                    newuser.setfirstName(keyclaockUser.getfirstName());
                    newuser.setlastName(keyclaockUser.getlastName());
                    newuser.setemail(keyclaockUser.getemail());
                    newuser.setKeycloakId(userId);
                    userRepo.save(newuser);
                    */
            
                    //password generation
                    CharacterRule digits1 = new CharacterRule(EnglishCharacterData.Alphabetical);
                    PasswordGenerator passwordGenerator = new PasswordGenerator();
                    String password1 = passwordGenerator.generatePassword(7, digits1);
            
                    CharacterRule digits2 = new CharacterRule(EnglishCharacterData.Special);
                    String password2 = passwordGenerator.generatePassword(7, digits2);
                    String pass = password1;

                    System.out.println("Password"+password1);
            
                    CredentialRepresentation passwordCred = new CredentialRepresentation();
                    passwordCred.setTemporary(true);
                    passwordCred.setType(CredentialRepresentation.PASSWORD);
                    passwordCred.setValue(pass);
            
                    UserResource userResource = usersResource.get(userId);
                    userResource.resetPassword(passwordCred);
            
                    
                    System.out.println(roles.get(0));
                    // Assign roles
                    String compositeRoles = "";
                    if(roles.length() > 0) {
                        List<RoleRepresentation> assignedRoles = new ArrayList<RoleRepresentation>();
            
                        for(int i=0; i<roles.length(); i++) {
                            JSONObject group = roles.getJSONObject(i);
                            compositeRoles += String.format("%s,", group.get("name").toString());
                            try {
                                RoleRepresentation currentRole = realmResource.roles().get(group.get("name").toString()).toRepresentation();
                                assignedRoles.add(currentRole);
                            } catch (Exception e) {
                                return new ResponseEntity<Object>("Role does not exist: "+group.get("name").toString(),HttpStatus.NOT_FOUND);
                            }
                        }
                        userResource.roles().realmLevel().add(assignedRoles);
                        /*
                        // log.info("composite roles: {}", compositeRoles);
            
                        // assign client level role for managing groups and roles
                        ClientRepresentation realmManagement = realmResource.clients().findByClientId("realm-management").get(0);
                       // log.info("UserService:createUser - Fetched {} client", realmManagement.getName());
                        List<RoleRepresentation> existingRoles = userResource.roles().getAll().getRealmMappings();
                        Boolean isManage = false, isView = false;
                        RoleRepresentation manageRealm = realmResource.clients().get(realmManagement.getId()).roles().get("manage-realm").toRepresentation();
                        RoleRepresentation queryUsers = realmResource.clients().get(realmManagement.getId()).roles().get("query-users").toRepresentation();
                        RoleRepresentation viewRealm = realmResource.clients().get(realmManagement.getId()).roles().get("view-realm").toRepresentation();
                        List<RoleRepresentation> clientLevelRoles = new ArrayList<RoleRepresentation>();
                        for(RoleRepresentation role : existingRoles) {
                            if(role.isComposite()) {
                                String realmRoleComposites = realmResource.roles().get(role.getName()).getRealmRoleComposites().toString();
                                if ((realmRoleComposites.contains("READ_CREATE_ROLE") || realmRoleComposites.contains("ROLE_CREATE_GROUP") ||
                                    realmRoleComposites.contains("READ_UPDATE_ROLE") || realmRoleComposites.contains("ROLE_UPDATE_GROUP")) && !isManage) {
                                   // log.info("UserService:createUser - Attempting to assign manage-realm and query-users");
                                    clientLevelRoles.clear(); // clear to prevent duplicates on saving client roles on keycloak
                                    clientLevelRoles.add(manageRealm);
                                    clientLevelRoles.add(queryUsers);
                                    userResource.roles().clientLevel(realmManagement.getId()).add(clientLevelRoles);
                                   // log.info("UserService:createUser - Successfully assigned manage-realm and query-users");
                                    isManage = true;
                                }
                                if((realmRoleComposites.contains("ROLE_READ_ROLE") || realmRoleComposites.contains("ROLE_READ_GROUP")) && !isView) {
                                   // log.info("UserService:createUser - Attempting to assign view-realm");
                                    clientLevelRoles.clear(); // clear to prevent duplicates on saving client roles on keycloak
                                    clientLevelRoles.add(viewRealm);
                                    userResource.roles().clientLevel(realmManagement.getId()).add(clientLevelRoles);
                                   // log.info("UserService:createUser - Successfully assigned view-realm");
                                    isView = true;
                                }
                                
                                if(isManage && isView) {
                                    // stop assigning client roles if already satisfied
                                   // log.info("UserService:createUser - Manage and view satisfied");
                                    break;
                                }
                            }
                        }
                        */
                    }
                    user newuser = new user();
                    newuser.setKeycloakId(userId);
                    newuser.setusername(username);
                    newuser.setfirstName(firstName);
                    newuser.setlastName(lastName);
                    newuser.setemail(email);
                    newuser.setmobileNumber(mobileNumber);
                    newuser.setremarks(remarks);
                    newuser.setisEnabled(isEnabled);
                    newuser.setdateCreated(new Date());
                    newuser.setlastUpdated(new Date());
                    newuser.setcreatedBy(createdBy);
                    newuser.setlastUpdatedBy(createdBy);
                    newuser.setisEnabled(isEnabled);
                    newuser.setremarks(remarks);
                    newuser.setroles(keyclaockUser.get("role").toString());
                    if(authService.getRoles(authToken).contains("ROLE_SUPERADMIN")){
                        if(tenant==null){
                            newuser.settenant("");    
                        }else{
                            String tenant_name = tenant_repo.get_tenant_by_slug(tenant);
                            newuser.settenant(tenant_name);
                        }
                    }
                    else if(authService.getRoles(authToken).contains("ROLE_TENANT_ADMIN")){
                        String tenant_name = tenant_repo.get_tenant_by_slug(slug);
                        newuser.settenant(tenant_name);
                    }
                    else{
                        newuser.settenant("");
                    }
                    userRepo.save(newuser);
                    /*
                    message.put("user_details",user_detail);
                    message.put("log_type","create_user");
                    message.put("status","success");
                    message.put("user_created",newuser);
                    message.put("time",formatter.format(date));
                    kafka.sendMessage(message.toString(), kafka_topic);
                    */

                    //Send email
                    String emailSubject = "Workconnect Account";
                    String emailBody = "Welcome " + username + "!" + "\r\n" +
                    "Your initial password is " + pass + "\r\n" +
                    "Upon log-in, you will be prompted to change your password.";
                    JSONObject request = new JSONObject();
                    request.put("to", email);
                    request.put("text", emailBody );
                    request.put("subject", emailSubject);
                    mailservice.processNotification(request);

                    return new ResponseEntity<Object>("Your Password is: "+pass,HttpStatus.OK);
                }
                else {
                    /* 
                    message.put("user_details",user_detail);
                    message.put("log_type","create_user");
                    message.put("status","fail");
                    message.put("user_created",keyclaockUser);
                    message.put("time",formatter.format(date));
                    kafka.sendMessage(message.toString(), kafka_topic);
                    */
                    return new ResponseEntity<Object>("Invalid User Access.",HttpStatus.UNAUTHORIZED);
                }
            }
            else {
                /* 
                message.put("user_details",user_detail);
                message.put("log_type","create_user");
                message.put("status","fail");
                message.put("user_created",keyclaockUser);
                message.put("time",formatter.format(date));
                kafka.sendMessage(message.toString(), kafka_topic);
                */
                return new ResponseEntity<Object>(result.get("message"),HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            /* 
            message.put("user_details",user_detail);
            message.put("log_type","create_user");
            message.put("status","fail");
            message.put("user_created",keyclaockUser);
            message.put("time",formatter.format(date));
            kafka.sendMessage(message.toString(), kafka_topic);
            */
            return new ResponseEntity<Object>("Exception in Create User:" + e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   public ResponseEntity<Object> updateUser(String auth,Map<String, String> keyclaockUser) throws JSONException{
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date date = new Date(System.currentTimeMillis());

        Map<String, Object> user_detail = (Map<String, Object>) authService.getUserUsername(auth);
        //Map<String, Object> message = new HashMap<String, Object>();

        try {
            Map<String, String> result = authService.isTokenValid(auth);
            if (result.get("status").equals("SUCCESS")) {
                System.out.println(authService.getRoles(auth).contains("ROLE_USERADMIN_UPDATE_USER"));
                if(authService.getRoles(auth).contains("ROLE_USERADMIN_UPDATE_USER")) {

                    String username = keyclaockUser.get("username").toString();
                    String firstName = keyclaockUser.get("firstName").toString();
                    String lastName = keyclaockUser.get("lastName").toString();
                    String isEnabled = keyclaockUser.get("isEnabled").toString();
                    String mobileNumber = keyclaockUser.get("mobileNumber").toString();
                    String email = keyclaockUser.get("email").toString();
                    String remarks = keyclaockUser.get("remarks").toString();
                    
                    String tenant = "";
                    if(authService.getRoles(auth).contains("ROLE_SUPERADMIN")){
                        String tenant_name = keyclaockUser.get("tenants").toString();
                        tenant = tenant_repo.get_tenantslug_by_name(tenant_name);
                    }

                    JSONArray roles =  new JSONArray(keyclaockUser.get("role").toString());

                    if(authService.getRoles(auth).contains("ROLE_TENANT_ADMIN"))

                    if(keyclaockUser.get("role").toString().contains("ROLE_TENANT_ADMIN")){
                        if(authService.getRoles(auth).contains("ROLE_TENANT_ADMIN")){
                            return new ResponseEntity<Object>("Can only update tenant users.",HttpStatus.UNAUTHORIZED);
                        }
                        JSONArray tenant_roles = new JSONArray();
                        List<String> roles_to_add = new ArrayList<String>();
                        roles_to_add.add("ROLE_SITES_VIEW");
                        roles_to_add.add("ROLE_SITES_NETWORK_VIEW");
                        roles_to_add.add("ROLE_SITES_INTERFACES_VIEW");
                        roles_to_add.add("ROLE_SITES_DEVICES_VIEW");
                        roles_to_add.add("ROLE_SITES_ASSETS_VIEW");
                        roles_to_add.add("ROLE_NETBOX_VIEW_CIRCUITS");
                        roles_to_add.add("ROLE_USERADMIN_VIEW_USER");
                        roles_to_add.add("ROLE_USERADMIN_CREATE_USER");
                        roles_to_add.add("ROLE_USERADMIN_UPDATE_USER");
                        roles_to_add.add("ROLE_TENANT_ADMIN");
                        for (String string : roles_to_add) {
                            tenant_roles.put(new JSONObject().put("name", string));
                        }
                        roles = tenant_roles;
                    }
                    if(keyclaockUser.get("role").toString().contains("ROLE_TENANT_USER")){
                        JSONArray tenant_roles = new JSONArray();
                        List<String> roles_to_add = new ArrayList<String>();
                        roles_to_add.add("ROLE_SITES_VIEW");
                        roles_to_add.add("ROLE_SITES_NETWORK_VIEW");
                        roles_to_add.add("ROLE_SITES_INTERFACES_VIEW");
                        roles_to_add.add("ROLE_SITES_DEVICES_VIEW");
                        roles_to_add.add("ROLE_SITES_ASSETS_VIEW");
                        roles_to_add.add("ROLE_NETBOX_VIEW_CIRCUITS");
                        roles_to_add.add("ROLE_TENANT_USER");
                        for (String string : roles_to_add) {
                            tenant_roles.put(new JSONObject().put("name", string));
                        }
                        roles = tenant_roles;
                    }

                    if(!keyclaockUser.get("role").toString().contains("default-roles-"+keycloak_relm)){
                        JSONObject jo = new JSONObject("{\"name\":\"default-roles-"+keycloak_relm+"\"}");
                        roles.put(jo);
                    }
                    
                    if(userRepo.find_user_by_username(username).isEmpty()){
                        return new ResponseEntity<Object>("ERROR does not exist or not yet Added",HttpStatus.CONFLICT);
                    }
                    
                    System.out.println(roles.toString());

                    Keycloak keycloak = KeycloakBuilder.builder() //
                        .serverUrl(keycloak_url.replace("\r\n","")+"auth") //
                        .realm(keycloak_relm_master.replace("\r\n","")) //
                        .grantType(OAuth2Constants.PASSWORD) //
                        .clientId(keycloak_client_id.replace("\r\n","")) //
                        .clientSecret(keycloak_secret.replace("\r\n","")) //
                        .username(keycloak_username.replace("\r\n","")) //
                        .password(keycloak_password.replace("\r\n","")) //
                        .build();
            
                    // Get realm
                    RealmResource realmResource = keycloak.realm(keycloak_relm);
                    UsersResource usersResource = realmResource.users();
                    
                    user olduser = userRepo.get_user(username);
                    user kafkaolduser = olduser;
                    UserResource userResource = usersResource.get(olduser.getKeycloakId());
                    
            
                    // Update assignment of roles
                    String compositeRoles = "";
                    List<RoleRepresentation> assignedRoles = new ArrayList<RoleRepresentation>();
                    List<RoleRepresentation> existingRoles = userResource.roles().getAll().getRealmMappings();
                    List<String> passedRoles = new ArrayList<String>();
                    List<RoleRepresentation> removedRoles = new ArrayList<RoleRepresentation>();
                    //ClientRepresentation realmManagement = realmResource.clients().findByClientId("realm-management").get(0);
                    if(roles.length() > 0) {
                        for(int i=0; i<roles.length(); i++) {
                            JSONObject group = roles.getJSONObject(i);
                            compositeRoles += String.format("%s,", group.get("name").toString());
                            passedRoles.add(group.get("name").toString());
                            RoleRepresentation currentRole = realmResource.roles().get(group.get("name").toString()).toRepresentation();
                            if(existingRoles == null || !existingRoles.contains(currentRole)) {
                                assignedRoles.add(currentRole);
                            }
                        }
                        userResource.roles().realmLevel().add(assignedRoles);
            
                        // remove previously existing roles (if there are existing roles) from passed roles
                        if(existingRoles != null) {
                            existingRoles.forEach(role -> {
                                if(!passedRoles.contains(role.getName())) {
                                    // ignore default roles else remove them
                                    if(!(role.getName().equals("offline_access") || role.getName().equals("uma_authorization"))) {
                                        removedRoles.add(role);
                                    }
                                }
                            });
                            userResource.roles().realmLevel().remove(removedRoles);
                        }
                        
                    } 
                    else{
                        // excempt remove default roles
                        System.out.println("UserService:updateUser - No roles found. Attempting to clear all roles.");
                        existingRoles.forEach(role -> {
                            if(role.getName().equals("offline_access") || role.getName().equals("uma_authorization")) {
                                removedRoles.add(role);
                            }
                        });
                        existingRoles.removeAll(removedRoles);
            
                        userResource.roles().realmLevel().remove(existingRoles);
                        System.out.println("UserService:updateUser - Successfully removed all roles.");
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    Date today = new Date();
                    String updatedBy = authService.getUser(auth);

                    String slug = "";
                    try {
                        Map<String, List<String>> attrib = authService.getUserAttributes(auth);
                        slug = attrib.get("tenant").get(0);
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                    try {
                        try {
                            UserRepresentation userRepresentation = userResource.toRepresentation();
                            userRepresentation.setEnabled(Boolean.parseBoolean(isEnabled));
                            userRepresentation.setFirstName(firstName);
                            userRepresentation.setLastName(lastName);
                            userRepresentation.setEmail(email);
                            userRepresentation.singleAttribute("lastUpdated", sdf.format(today));
                            userRepresentation.singleAttribute("lastUpdatedBy", updatedBy);
                            if(authService.getRoles(auth).contains("ROLE_SUPERADMIN")){
                                userRepresentation.singleAttribute("tenant",tenant);
                            }
                            if(authService.getRoles(auth).contains("ROLE_TENANT_ADMIN")){
                                userRepresentation.singleAttribute("tenant",slug);
                            }
                            userResource.update(userRepresentation);
                        } catch (Exception e) {
                            List<UserRepresentation>  keyclaock_user= realmResource.users().search(username, 0, 10, true);
                            UserResource userResource2 = usersResource.get(keyclaock_user.get(0).getId());
                            UserRepresentation userRepresentation = userResource2.toRepresentation();
                            userRepresentation.setEnabled(Boolean.parseBoolean(isEnabled));
                            userRepresentation.singleAttribute("lastUpdated", sdf.format(today));
                            userRepresentation.singleAttribute("lastUpdatedBy", updatedBy);
                            if(authService.getRoles(auth).contains("ROLE_SUPERADMIN")){
                                userRepresentation.singleAttribute("tenant",tenant);
                            }
                            if(authService.getRoles(auth).contains("ROLE_TENANT_ADMIN")){
                                userRepresentation.singleAttribute("tenant",slug);
                            }
                            userResource2.update(userRepresentation);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    
                    //Log Event
                    //logService.logUserState(user, "Update User - Before");
            
                    //log.info("UserService:updateUser - Update user in database");
                    
                    try {
                        olduser.setusername(username);
                        olduser.setfirstName(firstName);
                        olduser.setlastName(lastName);
                        olduser.setemail(email);
                        olduser.setmobileNumber(mobileNumber);
                        olduser.setremarks(remarks);
                        olduser.setisEnabled(isEnabled);
                        olduser.setlastUpdated(new Date());
                        olduser.setlastUpdatedBy(updatedBy);
                        olduser.setisEnabled(isEnabled);
                        olduser.setremarks(remarks);
                        olduser.setroles(keyclaockUser.get("role").toString());
                        if(authService.getRoles(auth).contains("ROLE_SUPERADMIN")){
                            if(tenant==null){
                                olduser.settenant("");    
                            }else{
                                String tenant_name = tenant_repo.get_tenant_by_slug(tenant);
                                olduser.settenant(tenant_name);
                            }
                        }
                        else if(authService.getRoles(auth).contains("ROLE_TENANT_ADMIN")){
                            String tenant_name = tenant_repo.get_tenant_by_slug(slug);
                            olduser.settenant(tenant_name);
                        }
                        else{
                            olduser.settenant("");
                        }
                        userRepo.save(olduser);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    
                    /* 
                    message.put("user_details",user_detail);
                    message.put("log_type","update_user");
                    message.put("status","success");
                    message.put("old_values",kafkaolduser);                            
                    message.put("new_values",olduser);
                    message.put("time",formatter.format(date));
                    kafka.sendMessage(message.toString(), kafka_topic);
                    */
                    System.out.println("UserService:updateUser - User successfully updated");
                    return new ResponseEntity<Object>("User successfully updated", HttpStatus.OK);
                }
                else {
                    /* 
                    message.put("user_details",user_detail);
                    message.put("log_type","update_user");
                    message.put("status","fail");
                    message.put("old_values",keyclaockUser);
                    message.put("time",formatter.format(date));
                    kafka.sendMessage(message.toString(), kafka_topic);
                    */
                    return new ResponseEntity<Object>("Invalid User Access.",HttpStatus.UNAUTHORIZED);
                }
            }
            else {
                /*
                message.put("user_details",user_detail);
                message.put("log_type","update_user");
                message.put("status","fail");
                message.put("old_values",keyclaockUser);
                message.put("time",formatter.format(date));
                kafka.sendMessage(message.toString(), kafka_topic);
                */
                return new ResponseEntity<Object>(result.get("message"),HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            /* 
            message.put("user_details",user_detail);
            message.put("log_type","update_user");
            message.put("status","fail");
            message.put("old_values",keyclaockUser);
            message.put("time",formatter.format(date));
            kafka.sendMessage(message.toString(), kafka_topic);
            */
            return new ResponseEntity<Object>("Exception in Create User:" + e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getRealmRoles(String auth) {
        
        Map<String, String> result = authService.isTokenValid(auth);
        if (result.get("status").equals("SUCCESS")) {
            if(authService.getRoles(auth).contains("ROLE_TENANT_ADMIN")) {
                Keycloak keycloak = KeycloakBuilder.builder() //
                    .serverUrl(keycloak_url.replace("\r\n","")+"auth") //
                    .realm(keycloak_relm_master.replace("\r\n","")) //
                    .grantType(OAuth2Constants.PASSWORD) //
                    .clientId(keycloak_client_id.replace("\r\n","")) //
                    .clientSecret(keycloak_secret.replace("\r\n","")) //
                    .username(keycloak_username.replace("\r\n","")) //
                    .password(keycloak_password.replace("\r\n","")) //
                    .build();

                // Get realm
                RealmResource realmResource = keycloak.realm(keycloak_relm);
                System.out.println("RESULT------------ "+realmResource.roles().get("ROLE_TENANT_USER").toRepresentation().getName());
                
                RoleRepresentation role_user = realmResource.roles().get("ROLE_TENANT_USER").toRepresentation();
                List<String> arrayRoles = new ArrayList<String>();
                arrayRoles.add(role_user.getName());
                return new ResponseEntity<Object>(arrayRoles, HttpStatus.OK);
            }else{
                Keycloak keycloak = KeycloakBuilder.builder() //
                    .serverUrl(keycloak_url.replace("\r\n","")+"auth") //
                    .realm(keycloak_relm_master.replace("\r\n","")) //
                    .grantType(OAuth2Constants.PASSWORD) //
                    .clientId(keycloak_client_id.replace("\r\n","")) //
                    .clientSecret(keycloak_secret.replace("\r\n","")) //
                    .username(keycloak_username.replace("\r\n","")) //
                    .password(keycloak_password.replace("\r\n","")) //
                    .build();
    
                // Get realm
                RealmResource realmResource = keycloak.realm(keycloak_relm);
                List<RoleRepresentation> roles = realmResource.roles().list();
                List<String> arrayRoles = new ArrayList<String>();

                for(RoleRepresentation roleRepresentation: roles) {
                    if(roleRepresentation.getName().contains("GROUP")){
                        arrayRoles.add(roleRepresentation.getName());
                    }
                    System.out.println("contatinate roles------------ "+arrayRoles);
                };

                System.out.println("RESULT------------ "+realmResource.roles().list());
                return new ResponseEntity<Object>(arrayRoles, HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<Object>("THERS AN ERROR! "+result.get("message"),HttpStatus.UNAUTHORIZED);
        }
    }
    public ResponseEntity<Object> getUsers(String auth,Map<String, String> params) throws ParseException {
        Map<String, String> result = authService.isTokenValid(auth);
        if (result.get("status").equals("SUCCESS")) {
            try {
                if(authService.getRoles(auth).contains("ROLE_USERADMIN_VIEW_USER")) {
                    String username = null;
                    String firstName = null;
                    String lastName = null;
                    String mobileNumber = null;
                    Date dateFrom = null;
                    Date dateTo = null;
                    int pageNo = 0;
                    int pageSize = 0;
            
            
                    System.out.println(params);
            
                    if(!params.get("username").isEmpty()) {
                        username = params.get("username").toString();
                    }
                    if(!params.get("firstName").isEmpty()) {
                        firstName = params.get("firstName").toString();
                    }
                    if(!params.get("lastName").isEmpty()) {
                        lastName = params.get("lastName").toString();
                    }
                    if(!params.get("mobileNumber").isEmpty()){
                        mobileNumber = params.get("mobileNumber").toString();
                    }
                    if(!params.get("pageNo").isEmpty()){
                        pageNo = Integer.parseInt(params.get("pageNo").toString());
                    }
                    if(!params.get("pageSize").isEmpty()){
                        pageSize = Integer.parseInt(params.get("pageSize").toString());
                    }
                    if(!params.get("dateFrom").isEmpty()){
                        dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("dateFrom").toString());
                    }
                    if(!params.get("dateTo").isEmpty()){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(params.get("dateTo").toString()));
                        c.add(Calendar.DATE, 1);
                        dateTo = c.getTime();
                    }

                    String tenant = "";
                    try {
                        Map<String, List<String>> attrib = authService.getUserAttributes(auth);
                        tenant = attrib.get("tenant").get(0);
                        if(authService.getRoles(auth).contains("ROLE_SUPERADMIN")){
                            tenant = "";
                        }
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
            
                    Pageable limit = PageRequest.of(pageNo, pageSize, Sort.by("dateCreated").descending());
                    Page<user> olduser = null;
                    try {
                        olduser = userRepo.searchUser(dateFrom, dateTo,username, mobileNumber, firstName, lastName, tenant, limit);     
                    } catch (Exception e) {
                        return new ResponseEntity<Object>(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
                        //TODO: handle exception
                    }
                                        
                    JSONArray userDetails = new JSONArray();
            
                    for (user user : olduser) {
                        JSONObject detail = setUserDetail(user);
                        userDetails.put(detail);
                    }
                    JSONObject response = new JSONObject();
                    response.put("statusCode", "200");
                    response.put("message", "Success");
                    response.put("users", userDetails);
                    response.put("pageSize", olduser.getTotalPages());
                    System.out.println(userDetails);
                    System.out.println(response);

                    return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<Object>("User access not allowed", HttpStatus.UNAUTHORIZED);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Object>("Error encountered in listing users:" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<Object>( result.get("message")+" "+result.get("expired"),HttpStatus.UNAUTHORIZED);
        }
    }

    public JSONObject setUserDetail(user user)throws JSONException{
        JSONObject detail = new JSONObject();
        detail.put("id", user.getid());
        detail.put("firstName", user.getfirstName());
        detail.put("lastName", user.getlastName());
        detail.put("username", user.getusername());
        detail.put("mobileNumber", user.getmobileNumber());
        detail.put("email", user.getemail());
        detail.put("remarks", user.getremarks());
        detail.put("dateCreated", user.getdateCreated());
        detail.put("lastUpdated", user.getlastUpdated());
        detail.put("isEnabled", user.getisEnabled());
        detail.put("createdBy", user.getcreatedBy());
        detail.put("lastUpdatedBy", user.getlastUpdatedBy());
        detail.put("tenant", user.gettenant());
        JSONArray roles = new JSONArray(user.getroles());
        JSONArray roleArray = new JSONArray();

        for(int i = 0; i < roles.length(); i++)
        {
              JSONObject objects = roles.getJSONObject(i);
              if(objects.get("name").toString().contains("GROUP")) roleArray.put(objects.get("name"));
              //Iterate through the elements of the array i.
              //Get thier value.
              //Get the value for the first element and the value for the last element.
        }
        // if(roles.length() > 0) {
        //     for(Object role : roles.toArray()) {
        //         JSONObject group = new JSONObject(role.toString());
        //         roleArray.add(String.format("%s,", group.get("name").toString()));
        //     }
        // }

        detail.put("roles", roleArray);
        return detail;
    }


    
    public ResponseEntity<Object> create_roles(String role_name,String role_description) {
        Keycloak keycloak = KeycloakBuilder.builder() //
            .serverUrl(keycloak_url.replace("\r\n","")+"auth") //
            .realm(keycloak_relm_master.replace("\r\n","")) //
            .grantType(OAuth2Constants.PASSWORD) //
            .clientId(keycloak_client_id.replace("\r\n","")) //
            .clientSecret(keycloak_secret.replace("\r\n","")) //
            .username(keycloak_username.replace("\r\n","")) //
            .password(keycloak_password.replace("\r\n","")) //
            .build();
        RealmResource realmResource = keycloak.realm(keycloak_relm);
        RoleRepresentation new_role = new RoleRepresentation();
        new_role.setName(role_name);
        new_role.setDescription(role_description);
        try {
            realmResource.roles().create(new_role);
            return new ResponseEntity<Object>("Created Role: "+role_name, HttpStatus.OK);
        } catch (Exception e) {
            //TODO: handle exception
            return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    public ResponseEntity<Object> get_user_roles(String auth,Map<String, String> params) throws JSONException{
        StringBuilder sb = new StringBuilder();   
        sb.append("[");
        List<String>  roles = authService.getUserRoles(auth);
        if(!roles.isEmpty()){
            for (int j=0;j<roles.size();j++) {                   
                if(j>0){
                    sb.append(",{\"name\":\""+roles.get(j)+"\"}");
                }else{
                    sb.append("{\"name\":\""+roles.get(j)+"\"}");
                }
            }
        }
        sb.append("]");
        return new ResponseEntity<Object>(sb.toString(), HttpStatus.OK);
    }

//##########################################################################################
    public void sync_users() throws JSONException{
        Keycloak keycloak = KeycloakBuilder.builder() //
            .serverUrl(keycloak_url.replace("\r\n","")+"auth") //
            .realm(keycloak_relm_master.replace("\r\n","")) //
            .grantType(OAuth2Constants.PASSWORD) //
            .clientId(keycloak_client_id.replace("\r\n","")) //
            .clientSecret(keycloak_secret.replace("\r\n","")) //
            .username(keycloak_username.replace("\r\n","")) //
            .password(keycloak_password.replace("\r\n","")) //
            .build();
        RealmResource realmResource = keycloak.realm(keycloak_relm);
               
        List<UserRepresentation> users = realmResource.users().list(0, 1000000);
        for(int i = 0; i < users.size(); i++) {
            StringBuilder sb = new StringBuilder();
            
            sb.append("[");
            
            List<RoleRepresentation>  roles = realmResource.users().get(users.get(i).getId()).roles().getAll().getRealmMappings();
            System.out.println(users.get(i).getEmail());
            if(!roles.isEmpty()){
                for (int j=0;j<roles.size();j++) {                   
                    if(j>0){
                        sb.append(",{\"name\":\""+roles.get(j).getName()+"\"}");
                    }else{
                        sb.append("{\"name\":\""+roles.get(j).getName()+"\"}");
                    }
                }
            }
            sb.append("]");
            System.out.println(sb.toString());
            try {
                user user = userRepo.get_user(users.get(i).getUsername());
                
                user.setKeycloakId(users.get(i).getId());
                user.setemail(users.get(i).getEmail());
                user.setfirstName(users.get(i).getFirstName());
                user.setlastName(users.get(i).getLastName());
                user.setusername(users.get(i).getUsername());
                user.setdateCreated(Date.from(Instant.ofEpochMilli(users.get(i).getCreatedTimestamp())));
                user.setmobileNumber(" ");
                user.setisEnabled(users.get(i).isEnabled().toString());
                user.setremarks(" ");
                if(user.getlastUpdatedBy()==null||user.getlastUpdatedBy().isEmpty()){
                    user.setlastUpdatedBy(" ");
                }
                if(user.getcreatedBy()==null || user.getcreatedBy().isEmpty()){
                    user.setcreatedBy(" ");
                }
                if(user.getlastUpdated()==null || user.getlastUpdated().toString().isEmpty()){
                    user.setlastUpdated(Date.from(Instant.ofEpochMilli(users.get(i).getCreatedTimestamp())));
                }
                Map<String,List<String>> attribs = new HashMap<String,List<String>>();
                attribs = users.get(i).getAttributes();
                
                try {
                    System.out.println(attribs.get("tenant").get(0));
                    String tenant_name = tenant_repo.get_tenant_by_slug(attribs.get("tenant").get(0));
                    user.settenant(tenant_name);
                } catch (Exception e) {
                    user.settenant("");
                }
                    
                user.setroles(sb.toString());
                userRepo.save(user);
            } catch (Exception e) {
                user user = new user();
                user.setKeycloakId(users.get(i).getId());
                user.setemail(users.get(i).getEmail());
                user.setfirstName(users.get(i).getFirstName());
                user.setlastName(users.get(i).getLastName());
                user.setusername(users.get(i).getUsername());
                user.setdateCreated(Date.from(Instant.ofEpochMilli(users.get(i).getCreatedTimestamp())));
                user.setmobileNumber(" ");
                user.setisEnabled(users.get(i).isEnabled().toString());
                user.setremarks(" ");
                user.setlastUpdatedBy(" ");
                user.setcreatedBy(" ");
                user.setlastUpdated(Date.from(Instant.ofEpochMilli(users.get(i).getCreatedTimestamp())));
                Map<String,List<String>> attribs = new HashMap<String,List<String>>();
                attribs = users.get(i).getAttributes();
                
                try {
                    System.out.println(attribs.get("tenant").get(0));
                    String tenant_name = tenant_repo.get_tenant_by_slug(attribs.get("tenant").get(0));
                    user.settenant(tenant_name);
                } catch (Exception z) {
                    user.settenant("");
                }
                user.setroles(sb.toString());
                userRepo.save(user);
                //TODO: handle exception
            }
        }   
    }
}
