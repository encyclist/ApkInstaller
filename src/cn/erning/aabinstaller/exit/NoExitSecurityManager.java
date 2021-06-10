package cn.erning.aabinstaller.exit;

import java.security.Permission;

/**
 * @author erning
 * @date 2021-06-09 9:54
 * des:
 */
public class NoExitSecurityManager extends SecurityManager {
    public boolean exitFilter = true;

    @Override
    public void checkPermission(Permission perm) {
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
    }

    @Override
    public void checkExit(int status) {
        super.checkExit(status);
        if (exitFilter) {
            throw new ExitException(status);
        }
    }
}
