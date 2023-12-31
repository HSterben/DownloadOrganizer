# DownloadOrganizer
A Java-based FileWatcher utility that monitors a specific directory (Downloads) for new files. Once a new file is detected, the utility checks for its readiness, and depending on the file type or name, moves it to a designated subdirectory. It handles temporary download files (.tmp, .opdownload, .crdownload) and can rename files based on specific criteria.

# Java FileWatcher Utility

## Description
Java FileWatcher is a utility designed to monitor a specified directory (typically the Downloads folder) for new files. Upon detecting a new file, the utility assesses whether the file is completely written to disk and then moves it to an appropriate subdirectory based on its type or name. The utility is particularly adept at handling various stages of downloaded files, including temporary files like `.tmp` or `.opdownload`. It also features functionality to rename files under certain conditions.

## Features
- **Directory Monitoring**: Watches a specified directory for new files.
- **File Handling**: Handles different types of files, including incomplete/temporary download files.
- **File Sorting**: Moves files to specific subdirectories based on type or name.
- **File Renaming**: Renames files based on predefined criteria.
- **Robust File Readiness Check**: Ensures files are fully written before moving.

## How to Use
1. **Install**: Install the project as ZIP and extract the files.
2. **Setup**: Run the `DownloadOrganizerSetup` to set up the program as a Startup App.
3. **File Operations**: Add, download, or modify files in the monitored directory and observe the utility moving and renaming them as per the logic defined.
4. **Quick Save Function**: To use the Quick Save folder; when downloading, simply add the letters `qsa` (*Non case-sensitive*) before your file name.

## Requirements
- Java Runtime Environment (JRE) or Java Development Kit (JDK) installed.

## License
GNU General Public License (GPL-3.0)

## Author
HSterben
