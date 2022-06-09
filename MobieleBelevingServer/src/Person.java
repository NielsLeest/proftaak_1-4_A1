import java.io.Serializable;

public class Person implements Serializable {

    private String userName;
    private String barcode;
    private String age;

    public Person(String userName, String age, String barcode){
        this.userName = userName;
        this.age = age;
        this.barcode = barcode;
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

    @Override
    public String toString() {
        return "Person{" +
                "userName='" + userName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
