package com.neobridge.auth.dto;

import com.neobridge.auth.entity.User;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for user responses containing non-sensitive user information.
 */
public class UserResponse {

    private String id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;
    private String countryOfResidence;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private User.KycStatus kycStatus;
    private User.UserStatus userStatus;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private Boolean twoFactorEnabled;
    private Set<String> roles;

    public UserResponse() {}

    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setNationality(user.getNationality());
        response.setCountryOfResidence(user.getCountryOfResidence());
        response.setAddressLine1(user.getAddressLine1());
        response.setAddressLine2(user.getAddressLine2());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setPostalCode(user.getPostalCode());
        response.setKycStatus(user.getKycStatus());
        response.setUserStatus(user.getUserStatus());
        response.setEmailVerified(user.getEmailVerified());
        response.setPhoneVerified(user.getPhoneVerified());
        response.setTwoFactorEnabled(user.getTwoFactorEnabled());
        
        if (user.getRoles() != null) {
            response.setRoles(user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet()));
        }
        
        return response;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public User.KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(User.KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public User.UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(User.UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
