package com.meama.security.user.storage.model;

import com.meama.security.role.storage.model.Role;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "systemuser")
public class User implements Serializable {

    private static final long serialVersionUID = -3009157732242241207L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SECURITY_KEY_SEQUENCE")
    @SequenceGenerator(name = "SECURITY_KEY_SEQUENCE", sequenceName = "SECURITY_KEY_SEQUENCE", initialValue = 10)
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String sapCode;
    private String personalId;
    private Date creationDate;
    private Date modificationDate;
    private boolean active;
    @Column(columnDefinition = "boolean default false")
    private boolean changePassword;
    private String imageUrl;
    @Column(columnDefinition = "bigint default 0")
    private int rating;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Role> roles;
    @Enumerated(value = EnumType.STRING)
    private UserType userType;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", sapCode='" + sapCode + '\'' +
                ", personalId='" + personalId + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", active=" + active +
                ", changePassword=" + changePassword +
                ", imageUrl='" + imageUrl + '\'' +
                ", rating=" + rating +
                ", roles=" + roles +
                ", userType=" + userType +
                '}';
    }
}
