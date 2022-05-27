import java.io.Serializable;

public class Person implements Serializable {

    private String userName;
    private String barcode;

    public Person(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
