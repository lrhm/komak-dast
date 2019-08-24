package ir.treeco.aftabe2.Object;

public class Level {

    private int id;
    private boolean resolved;
    private String type;

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String pics;
    private String answer;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
