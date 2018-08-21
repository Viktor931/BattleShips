package application.battleships.Exceptions;

public class WrongPlayerNameException extends RuntimeException {
    private String email;
    public WrongPlayerNameException(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
