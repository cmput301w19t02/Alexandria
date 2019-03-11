package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

/**
 * A model class for user details, which holds its username, full name
 * email address, phone number, and picture if any, used to create
 * an instance of a user and to pass information to activity classes
 */
public class UserProfile {

    private String name;
    private String email;
    private String phone;
    private String picture;
    private String username;

    /**
     * No args constructor to maintain compatibility
     * with Firebase deserializer
     * DO NOT USE
     */
    @Deprecated
    public UserProfile(){}

    /**
     * User profile constructor
     *
     * @param name Full name of the user
     * @param email Email address of the user
     * @param phone Phone number of the user
     * @param picture Avatar of the user, which might be empty
     * @param username Username of the user
     */
    public UserProfile(String name, String email, String phone, String picture, String username) {
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(email == null || email.isEmpty()){
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if(phone == null || phone.isEmpty()){
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
        this.username = username;
    }

    /**
     * getter for the full name of the use
     *
     * @return String Full name
     */
    public String getName() {
        return name;
    }

    /**
     * sets full name of the user to the string passed
     * if cannot be null or empty
     *
     * @throws IllegalArgumentException Name cannot be null or empty
     * @param name full name of the user
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.name = name;
    }

    /**
     * getter for the email address of the user
     *
     * @return String Email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the email address of the user
     * it cannot be null or empty
     *
     * @throws IllegalArgumentException Email cannot be null or empty
     * @param email Email address
     */
    public void setEmail(String email) {
        if(email == null || email.isEmpty()){
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        this.email = email;
    }

    /**
     * getter for the phone number of the user
     *
     * @return String Phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * sets the phone number of the user
     * it cannot be null or empty
     *
     * @throws IllegalArgumentException Phone number cannot be null or empty
     * @param phone Phone number
     */
    public void setPhone(String phone) {
        if(phone == null || phone.isEmpty()){
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }

        this.phone = phone;
    }

    /**
     * gets an id of the user avatar as on the database
     *
     * @return String picture id
     */
    public String getPicture() {
        return picture;
    }

    /**
     * sets user avatar to the string id passed
     * can be empty as the avatar is not required
     *
     * @param picture picture id
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * gets username of the user
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets username of the user to the string passed
     * cannot be null or empty
     *
     * @throws IllegalArgumentException Username cannot be null or empty
     * @param username username
     */
    public void setUsername(String username) {
        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }
}
