package Model;

import java.math.BigDecimal;
import java.util.Objects;

public class Student {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String category; 
    private BigDecimal hscPercentage;
    private BigDecimal entranceScore;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getHscPercentage() { return hscPercentage; }
    public void setHscPercentage(BigDecimal hscPercentage) { this.hscPercentage = hscPercentage; }
    public BigDecimal getEntranceScore() { return entranceScore; }
    public void setEntranceScore(BigDecimal entranceScore) { this.entranceScore = entranceScore; }
}