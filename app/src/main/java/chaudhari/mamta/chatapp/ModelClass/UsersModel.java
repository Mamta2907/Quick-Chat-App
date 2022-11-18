package chaudhari.mamta.chatapp.ModelClass;

public class UsersModel {

    String uid;
    String name;
    String Email;
    String imageUri;

    public UsersModel() {
    }

    public UsersModel(String uid, String name, String email, String imageUri) {
        this.uid = uid;
        this.name = name;
        Email = email;
        this.imageUri = imageUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

