package com.tracker_app.tracker.Helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Value("${Keycloak.BaseUrl}")
  private String keycloakUrl;

  @Value("${Keycloak.Key}")
  private String keycloakKey;

  @Value("${Keycloak.Url}")
  private String keycloakBaseUrl;

  @Value("${Keycloak.Secret}")
  private String keycloakSecret;

  @Value("${Keycloak.Id}")
  private String keycloakId;

  @Value("${Keycloak.Realm}")
  private String keycloakRealm;

  @Value("${Keycloak.RealmMaster}")
  private String keycloakMasterRealm;

  @Value("${Keycloak.Username}")
  private String keycloakUsername;

  @Value("${Keycloak.Pass}")
  private String keycloakPass;

  /*
    @Autowired
    private UserRepository userRepository;
    */
  //returns status = [SUCCESS:FAILED], string message
  public Map<String, String> isTokenValid(String authToken) {
    Map<String, String> results = new HashMap<String, String>();
    if (authToken == null || authToken.isEmpty()) {
      //log.info("AuthService:isTokenValid - Authorization token not found");
      results.put("status", "FAILED");
      results.put("message", "Authorization token not found");
      results.put("expired", "false");
    } else {
      try {
        String[] tokens = authToken.split("\\.");
        String token = tokens[1];
        if (token != null && !token.isEmpty()) {
          byte[] decoded = Base64.getDecoder().decode(token);
          String decodedString = new String(decoded);
          //log.info("AuthService:isTokenValid - decodedString:" + decodedString);
          JsonParser springParser = JsonParserFactory.getJsonParser();
          Map<String, Object> tokenMap = springParser.parseMap(decodedString);
          if (
            tokenMap.containsKey("iss") &&
            tokenMap.containsKey("azp") &&
            tokenMap.containsKey("exp")
          ) {
            String iss = tokenMap.get("iss").toString();
            String azp = tokenMap.get("azp").toString();
            long exp = Long.parseLong(tokenMap.get("exp").toString());
            Date expDate = new Date(exp * 1000);
            //log.info("exp {} expDate {}",exp, expDate);
            Date now = new Date();
            int dateResult = now.compareTo(expDate);
            //check if token is expired
            if (dateResult < 0) {
              //check if token is valid
              if (iss.equals(keycloakUrl) && azp.equals(keycloakKey)) {
                //log.info("AuthService:isTokenValid - Authorization token is valid");
                results.put("status", "SUCCESS");
                results.put("message", "Authorization token is valid");
                results.put("expired", "false");
                results.put(
                  "username",
                  tokenMap.containsKey("preferred_username")
                    ? tokenMap.get("preferred_username").toString()
                    : null
                );
              } else {
                //log.error("AuthService:isTokenValid - Invalid token");
                System.out.println(keycloakUrl + " AND " + iss);
                System.out.println(keycloakKey + " AND " + azp);
                results.put("status", "FAILED");
                results.put("message", "Invalid token");
                results.put("expired", "false");
              }
            } else {
              //log.error("AuthService:isTokenValid - Expired token");
              results.put("status", "FAILED");
              results.put("message", "Expired token");
              results.put("expired", "true");
            }
          } else {
            //log.error("AuthService:isTokenValid - Authorization token key not found");
            results.put("status", "FAILED");
            results.put("message", "Authorization token key not found");
            results.put("expired", "false");
          }
        } else {
          //log.error("AuthService:isTokenValid - Authorization token is null");
          results.put("status", "FAILED");
          results.put("message", "Authorization token is null");
          results.put("expired", "false");
        }
      } catch (Exception e) {
        //log.error("AuthService:isTokenValid - Exception:{}", e.getMessage());
        e.printStackTrace();
        results.put("status", "FAILED");
        results.put("message", "Invalid token");
        results.put("expired", e.getMessage());
      }
    }

    return results;
  }

  public String getUser(String authToken) {
    String[] tokens = authToken.split("\\.");
    String token = tokens[1];
    byte[] decoded = Base64.getDecoder().decode(token);
    String decodedString = new String(decoded);
    System.out.println(decodedString);
    // log.info("AuthService:isTokenValid - decodedString:" + decodedString);
    JsonParser springParser = JsonParserFactory.getJsonParser();
    Map<String, Object> tokenMap = springParser.parseMap(decodedString);
    String user = tokenMap.get("preferred_username").toString();
    return user;
  }

  public String getName(String authToken) {
    String[] tokens = authToken.split("\\.");
    String token = tokens[1];
    byte[] decoded = Base64.getDecoder().decode(token);
    String decodedString = new String(decoded);
    System.out.println(decodedString);
    // log.info("AuthService:isTokenValid - decodedString:" + decodedString);
    JsonParser springParser = JsonParserFactory.getJsonParser();
    Map<String, Object> tokenMap = springParser.parseMap(decodedString);
    String user = tokenMap.get("name").toString();
    return user;
  }

  /*
    public User getUserObject(String authToken) {
        String[] tokens = authToken.split("\\.");
        String token = tokens[1];
        byte[] decoded = Base64.getDecoder().decode(token);
        String decodedString = new String(decoded);
        // log.info("AuthService:isTokenValid - decodedString:" + decodedString);
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> tokenMap = springParser.parseMap(decodedString);
        String userString = tokenMap.get("preferred_username").toString();
        User user = null;
        user = userRepository.findByUsername(userString);
        return user;
    }
    */
  /*
    public String encodePassword(String password) {
        BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();
        String hash = bEncoder.encode(password);
        return hash;
    }

    public boolean validateCredentials(String hash, String password) {
        BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();
        return hash !=null && bEncoder.matches(password, hash);
    }
    */
  public String getId(String authToken) {
    String[] tokens = authToken.split("\\.");
    String token = tokens[1];
    byte[] decoded = Base64.getDecoder().decode(token);
    String decodedString = new String(decoded);
    // log.info("AuthService:isTokenValid - decodedString:" + decodedString);
    JsonParser springParser = JsonParserFactory.getJsonParser();
    Map<String, Object> tokenMap = springParser.parseMap(decodedString);
    String id = tokenMap.get("sub").toString();
    return id;
  }

  //check roles from token
  public List<String> getRoles(String authToken) {
    String[] tokens = authToken.split("\\.");
    String token = tokens[1];
    byte[] decoded = Base64.getDecoder().decode(token);
    String decodedString = new String(decoded);
    JsonParser springParser = JsonParserFactory.getJsonParser();
    Map<String, Object> tokenMap = springParser.parseMap(decodedString);

    // fetch roles
    ObjectMapper omap = new ObjectMapper();
    Map<String, Object> realmAccess = omap.convertValue(
      tokenMap.get("realm_access"),
      Map.class
    );
    List<String> roles = omap.convertValue(
      realmAccess.get("roles"),
      List.class
    );

    return roles;
  }

  //check roles from keycloak server
  public List<String> getUserRoles(String auth) {
    String userId = getId(auth);
    //log.info("User ID: {}", userId);
    String url = keycloakBaseUrl + "auth";

    Keycloak keycloak = KeycloakBuilder
      .builder() //
      .serverUrl(keycloakBaseUrl.replace("\r\n", "") + "auth") //
      .realm(keycloakMasterRealm.replace("\r\n", "")) //
      .grantType(OAuth2Constants.PASSWORD) //
      .clientId(keycloakId.replace("\r\n", "")) //
      .clientSecret(keycloakSecret.replace("\r\n", "")) //
      .username(keycloakUsername.replace("\r\n", "")) //
      .password(keycloakPass.replace("\r\n", "")) //
      .build();

    // Get realm
    RealmResource realmResource = keycloak.realm(keycloakRealm);
    UsersResource usersResource = realmResource.users();
    UserResource userResource = usersResource.get(userId);
    //log.info("Logged in user roles: {}",userResource.roles().realmLevel().listEffective());
    List<RoleRepresentation> roles = userResource
      .roles()
      .realmLevel()
      .listEffective();
    List<String> userRoles = new ArrayList<String>();
    for (RoleRepresentation roleRepresentation : roles) {
      userRoles.add(roleRepresentation.getName());
    }
    return userRoles;
  }

  public Map<String, List<String>> getUserAttributes(String auth) {
    String userId = getId(auth);

    Keycloak keycloak = KeycloakBuilder
      .builder() //
      .serverUrl(keycloakBaseUrl.replace("\r\n", "") + "auth") //
      .realm(keycloakMasterRealm.replace("\r\n", "")) //
      .grantType(OAuth2Constants.PASSWORD) //
      .clientId(keycloakId.replace("\r\n", "")) //
      .clientSecret(keycloakSecret.replace("\r\n", "")) //
      .username(keycloakUsername.replace("\r\n", "")) //
      .password(keycloakPass.replace("\r\n", "")) //
      .build();

    // Get realm
    RealmResource realmResource = keycloak.realm(keycloakRealm);
    UsersResource usersResource = realmResource.users();
    UserResource userResource = usersResource.get(userId);
    UserRepresentation user = userResource.toRepresentation();
    Map<String, List<String>> attribs = new HashMap<String, List<String>>();
    try {
      attribs = user.getAttributes();
    } catch (Exception e) {
      //TODO: handle exception
    }
    return attribs;
  }

  public Map<String, Object> getUserUsername(String auth) throws JSONException {
    String userId = getId(auth);
    System.out.println(userId);
    Keycloak keycloak = KeycloakBuilder
      .builder() //
      .serverUrl(keycloakBaseUrl.replace("\r\n", "") + "auth") //
      .realm(keycloakMasterRealm.replace("\r\n", "")) //
      .grantType(OAuth2Constants.PASSWORD) //
      .clientId(keycloakId.replace("\r\n", "")) //
      .clientSecret(keycloakSecret.replace("\r\n", "")) //
      .username(keycloakUsername.replace("\r\n", "")) //
      .password(keycloakPass.replace("\r\n", "")) //
      .build();

    // Get realm
    RealmResource realmResource = keycloak.realm(keycloakRealm);
    UsersResource usersResource = realmResource.users();
    UserResource userResource = usersResource.get(userId);
    UserRepresentation user = userResource.toRepresentation();
    Map<String, Object> jsonObject = new HashMap<String, Object>();
    jsonObject.put("username", user.getUsername());
    jsonObject.put("first_name", user.getFirstName());
    jsonObject.put("last_name", user.getLastName());
    jsonObject.put("email", user.getEmail());

    return jsonObject;
  }
  /*
    public Map<String, String> authenticateUser(String authenticationToken) {
        Map<String, String> result = isTokenValid(authenticationToken);
        String username = result.get("username");
        if (result.get("status").equals("SUCCESS")) {
            return result;
        } else {
            throw new InvalidAuthenticationTokenException(username, result);
        }
    }

    public Map<String, String> authenticateUser(String authenticationToken, String... expectedRoleId) {
        Map<String, String> result = authenticateUser(authenticationToken);
        String username = result.get("username");
        List<String> roles = getUserRoles(authenticationToken);
        for (String roleId : expectedRoleId) {
            if  (roles.contains(roleId)) {
                return result;
            }
        }
        throw new InvalidAuthenticationRoleException(username, result, expectedRoleId);
    }
    */
}
