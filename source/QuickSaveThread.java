import java.nio.file.Files;
import java.nio.file.Path;

public class QuickSaveThread implements Runnable {
    int minutesWait = 5;
    Path filePath = null;

    QuickSaveThread(Path filePath) {
        this.filePath = filePath;
    }

    QuickSaveThread(int minutes, Path filePath) {
        this.filePath = filePath;
        minutesWait = minutes;
    }

    @Override
    public void run() {
        delayedDelete(minutesWait);
    }

    public void delayedDelete(int minutesWait) {

        System.out.println("Wait started");
        try {
            for (int i = 0; i < minutesWait; i++) {
                Thread.sleep(60000);
            }
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println(filePath.getFileName() + " has been deleted.");
            } else {
                System.out.println(filePath.getFileName() + " does not exist.");
            }
        } catch (Exception e) {
        }
    }
}
