package cn.erning.aabinstaller.exit;

/**
 * @author erning
 * @date 2021-06-09 9:54
 * des:
 */
public class ExitException extends SecurityException {
    private static final long serialVersionUID = 1L;
    public final int status;

    public ExitException(int status) {
        super("成功拦截System.ext(0)!");
        this.status = status;
    }
}
