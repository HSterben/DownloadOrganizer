import java.nio.file.*;
import java.util.HashMap;

public class DownloadOrganizer {
    static String userPath = System.getProperty("user.home") + "\\";
    static HashMap<String, Path> paths;
    static Path downloadPath = Paths.get(userPath + "Downloads\\");
    static Path filePath = null;
    static int count = 1;

    public static void main(String[] args) throws Exception {

        paths = getPaths();

        WatchService watcher = FileSystems.getDefault().newWatchService();
        downloadPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

        System.out.println("\nMonitoring directory: " + downloadPath);

        while (true) {

            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                // Skip/Discard the first 2 temporary file downloads (Unready)
                if (count++ < 3) {
                    continue;
                }

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();

                filePath = downloadPath.resolve(filename);

                // Discard / Ignore statements
                if ((filename.toString().toUpperCase().endsWith(".TMP"))
                        || (filename.toString().toUpperCase().endsWith("DOWNLOAD"))
                        || (filename.toString().toUpperCase().startsWith("IGN")) || (Files.isDirectory(filePath))) {
                    continue;
                }

                // Check if file stays existing or if its a placeholder
                if (!Files.exists(filePath)) {
                    System.out.println("File doesn't exist, waiting: " + filePath);
                    Thread.sleep(3000); // Wait for 3 seconds
                    if (!Files.exists(filePath)) {
                        System.out.println("File doesn't exist even after waiting: " + filePath);
                        continue;
                    }
                }

                if (!isFileReady(filePath)) {
                    continue;
                }

                // Getting FileName / FileType
                String fileName = filename.toString();
                String fileType = fileName.substring(fileName.indexOf(".") + 1);
                try {
                    fileName = fileName.substring(0, fileName.indexOf("."));
                } catch (Exception e) {
                }

                moveFile(fileName, fileType, filename);
                count = 1;
            }
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    private static void moveFile(String name, String type, Path fileName) {
        Path p = null;
        String prefix = "";
        if (name.toUpperCase().startsWith("QSA")) {
            try {
                p = paths.get("quick");
                Path destinationPath = p.resolve("QSA_" + fileCount(p) + "_" + fileName.toString().substring(3));
                Files.move(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(name + " --> " + " quicksave");
                QuickSaveThread deleteThread = new QuickSaveThread(destinationPath);
                Thread thread = new Thread(deleteThread);
                thread.start();
            } catch (Exception e) {
                System.out.println("Error moving file");
                e.printStackTrace();
            }
            return;
        }

        type = type.toUpperCase();
        System.out.println(type);
        if (type.contains("PNG") || type.contains("JPG") || type.contains("JPEG")) {
            p = paths.get("images");
            prefix = "IMAGE";
        } else if (type.contains("MP4") || type.contains("MOV") || type.contains("MPG") || type.contains("MPEG")
                || type.contains("3GP") || type.contains("AVI") || type.contains("MKV")) {
            p = paths.get("videos");
            prefix = "VIDEO";
        } else if (type.contains("WAV") || type.contains("MP3") || type.contains("M4A") || type.contains("MPC")) {
            p = paths.get("audios");
            prefix = "AUDIO";
        } else if (type.contains("GIF")) {
            p = paths.get("gifs");
            prefix = "GIF";
        } else if (type.contains("EXE")) {
            p = paths.get("app");
            prefix = "APP";
        } else if (type.contains("ZIP")) {
            p = paths.get("zip");
            prefix = "ZIP";
        } else if (type.contains("TTF")) {
            p = paths.get("font");
            prefix = "FONT";
        } else {
            p = paths.get("other");
            prefix = "OTHER";
        }

        try {
            Path destinationPath = p.resolve(prefix + "_" + fileCount(p) + "_" + fileName.toString());
            Files.move(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(name + " --> " + p.toString());
        } catch (Exception e) {
            System.out.println("Error moving file");
            e.printStackTrace();
        }
        return;
    }

    private static HashMap<String, Path> getPaths() {
        HashMap<String, Path> pathList = new HashMap<>();
        Path quickSave = Paths.get(userPath + "Downloads\\QuickSave\\");
        pathList.put("quick", quickSave);

        Path mediaFolder = Paths.get(userPath + "Downloads\\Media\\");
        pathList.put("media", mediaFolder);

        Path imageSave = Paths.get(userPath + "Downloads\\Media\\Images\\");
        pathList.put("images", imageSave);

        Path GIFSave = Paths.get(userPath + "Downloads\\Media\\GIFS\\");
        pathList.put("gifs", GIFSave);

        Path videoSave = Paths.get(userPath + "Downloads\\Media\\Videos\\");
        pathList.put("videos", videoSave);

        Path audioSave = Paths.get(userPath + "Downloads\\Media\\Audios\\");
        pathList.put("audios", audioSave);

        Path appSave = Paths.get(userPath + "Downloads\\Apps\\");
        pathList.put("app", appSave);

        Path fontSave = Paths.get(userPath + "Downloads\\Fonts\\");
        pathList.put("font", fontSave);

        Path zipSave = Paths.get(userPath + "Downloads\\Zips\\");
        pathList.put("zip", zipSave);

        Path otherSave = Paths.get(userPath + "Downloads\\Others\\");
        pathList.put("other", otherSave);

        for (Path p : pathList.values()) {
            if (!Files.exists(p)) {
                try {
                    Files.createDirectories(p);
                } catch (Exception e) {
                    System.out.println("Creation failed");
                }
            }
        }

        return pathList;
    }

    private static boolean isFileReady(Path path) {
        try {
            long size = Files.size(path);
            System.out.println("Size: " + size);
            while (true) {
                // Wait 1.5 seconds
                Thread.sleep(1500);

                // Compare size
                long currentSize = Files.size(path);
                if (size == currentSize) {
                    return true;
                }
                size = currentSize;
                System.out.println("Size: " + size);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static int fileCount(Path directory) {
        int fileCount = 0;
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
                for (Path p : directoryStream) {
                    fileCount++;
                }
            } catch (Exception e) {
                System.out.println("Error getting Directories");
            }
        }
        return fileCount;
    }
}