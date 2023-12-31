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
        minutesWait *= 60000;

        minutesWait = 5000;

        System.out.println("Wait started");
        try {
            Thread.sleep(minutesWait);
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
