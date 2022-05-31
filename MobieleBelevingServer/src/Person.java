import java.io.Serializable;

public class Person implements Serializable {

    private String userName;
    private String barcode;

    public Person(String userName, String barcode){
        this.userName = userName;
        this.barcode = barcode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getBarcode() {
        return this.barcode;
    }
}
