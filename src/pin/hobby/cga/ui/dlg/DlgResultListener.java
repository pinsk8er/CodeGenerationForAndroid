package pin.hobby.cga.ui.dlg;

/**
 * Created by pin-mint on 16. 4. 11.
 */
public interface DlgResultListener<T> {
    void Ok(T result);
    void Cancel();
}
