package library.models;

public class Reader {
    private String TIN; // Tax Identification Number
    private String name;
    private int age;
    private String category;

    public Reader(String TIN, String name, int age, String category) {
        this.TIN = TIN;
        this.name = name;
        this.age = age;
        this.category = category;
    }

    public String getTIN() {
        return TIN;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCategory() {
        return category;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}