import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

public class Transaction {
    private int id;
    private String amount;
    private String type;
    private String note;
    private String date;
    private byte[] imageBytes;
    private String transactionType;
    
    public Transaction(int id, String amount, String type, String note, String date, byte[] imageBytes) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = date;
        this.imageBytes = imageBytes;
    }

    public Transaction(String amount, String type, String note, String date, byte[] imageBytes) {
        this.id = getLastId(transactionType) + 1; // Get the last ID and increment it
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = date;
        this.imageBytes = imageBytes;
    }

    public String toTSVString() {
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        return id + "\t" + amount + "\t" + type + "\t" + note + "\t" + date + "\t" + encodedImage;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount='" + amount + '\'' +
                ", type='" + type + '\'' +
                ", note='" + note + '\'' +
                ", date='" + date + '\'' +
                ", imageBytes='" + imageBytes + '\'' +
                '}';
    }

    public static int getLastId(String transactionType) {
        int lastId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(transactionType + ".tsv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                int id = Integer.parseInt(values[0]);
                if (id > lastId) {
                    lastId = id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastId;

}

}
