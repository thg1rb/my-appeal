package ku.cs.services.exceptions;

public class EmptyInputException extends Exception {
    public EmptyInputException() {
        super("กรุณากรอกข้อมูลให้ครบถ้วน");
    }
}
