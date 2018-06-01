package pro.savichev.db.log;

public interface Log {
    void i(String tag, String message);
    void i(String tag, String message, Throwable throwable);
    void e(String tag, String message);
    void e(String tag, String message, Throwable throwable);
}
