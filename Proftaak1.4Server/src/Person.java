public class Person {
    private String username;
    private String barcode;

    public Person(String username, String barcode){
        this.username = username;
        this.barcode = barcode;
    }

    public String getUsername(){
        return this.username;
    }

    public String getBarcode(){
        return this.barcode;
    }

}
