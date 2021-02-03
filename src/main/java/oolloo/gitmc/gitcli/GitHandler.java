package oolloo.gitmc.gitcli;

public class GitHandler {
    private static boolean busy = false;

    public GitHandler() {

    }

    public static int run(TextBuiltin runner) {
        if (busy) {
            return 0;
        } else {
            try {
                busy = true;
                runner.run();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            } finally {
                busy = false;
            }
        }
        return 1;
    }

}
