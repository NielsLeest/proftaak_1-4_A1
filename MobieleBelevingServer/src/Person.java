import java.io.Serializable;

public class Person implements Serializable {

    private String userName;
    private String barcode;
    private String age;
    private int image;

    public Person(String userName, String age, String barcode, int image){
        this.userName = userName;
        this.age = age;
        this.barcode = barcode;
        this.image = image;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getAge(){
        return this.age;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public int getImage() {
        return this.image;
    }

    @Override
    public String toString() {
        return "Person{" +
                "userName='" + userName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
