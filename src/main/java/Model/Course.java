package Model;

import java.math.BigDecimal;

public class Course {
    private int id;
    private String name;
    private int seats;
    private BigDecimal cutoff;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    public BigDecimal getCutoff() { return cutoff; }
    public void setCutoff(BigDecimal cutoff) { this.cutoff = cutoff; }
}