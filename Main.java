import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    static String fullDirName = "F:"+File.separator+"JAVA"+File.separator+"Games"+ File.separator;

    public static void main(String[] args) {

        initGameDir();   //первая задача
        createSaves();   //вторая задача

        //третья задача
        File directory = new File(fullDirName + "savegames");

        for (File f : directory.listFiles()) {
            if (f.getName().endsWith(".zip")) {
                openZip(f.getAbsolutePath(),fullDirName + "savegames");
            }
        }

        for (File f : directory.listFiles()) {
            if (f.getName().endsWith(".dat")) {
                System.out.println(openProgress(f.getAbsolutePath()).toString());;
            }
        }


    }

    private static GameProgress openProgress(String pathToFile) {

        GameProgress gameProgress = null;
        // откроем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(pathToFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // десериализуем объект и скастим его в класс
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return gameProgress;

    }

    private static void openZip(String pathToFile, String pathToDirectory) {

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathToFile))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                // получим название файла
                // распаковка
                FileOutputStream fout = new FileOutputStream(pathToDirectory + "\\"+ name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (
                Exception ex) {

        }
    }

    public static void createSaves() {
        GameProgress gameProgress_0 = new GameProgress(100,300,2,33.21);
        GameProgress gameProgress_1 = new GameProgress(80,100,6,335.22);
        GameProgress gameProgress_2 = new GameProgress(40,50,99,121.23);

        saveGame(fullDirName + "savegames"+ File.separator +"gp_0.dat",gameProgress_0);
        saveGame(fullDirName + "savegames"+ File.separator +"gp_1.dat",gameProgress_1);
        saveGame(fullDirName + "savegames" +File.separator +"gp_2.dat",gameProgress_2);

        zipFiles(fullDirName + "savegames"+ File.separator +"gp_0.dat","gp_0.dat");
        zipFiles(fullDirName + "savegames"+ File.separator +"gp_1.dat","gp_1.dat");
        zipFiles(fullDirName + "savegames"+ File.separator +"gp_2.dat","gp_2.dat");

        deleteUnzippedFiles();
    }

    private static void deleteUnzippedFiles() {

        File directory = new File(fullDirName + "savegames");

        for (File f : directory.listFiles()) {
            if (f.getName().endsWith(".dat")) {
                f.delete();
            }
        }

    }

    public static void saveGame(String fileName, GameProgress gameProgress) {

        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void zipFiles(String fileName, String archiveName) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(fileName + ".zip"));
             FileInputStream fis = new FileInputStream(fileName)) {
            ZipEntry entry = new ZipEntry(archiveName);
            zout.putNextEntry(entry);
            // считываем содержимое файла в массив byte
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // добавляем содержимое к архиву
            zout.write(buffer);// закрываем текущую запись для новой записи
            zout.closeEntry();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void initGameDir() {
        //ЛОГИ
        initCatalog(fullDirName + "temp");
        createFileByName(fullDirName + "temp"+ File.separator + "temp.txt");

        StringBuffer sbLogs = new StringBuffer();

        sbLogs.append(initCatalog(fullDirName + "res")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "savegames")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "src")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "src"+ File.separator +"main")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "src"+ File.separator +"test")).append("\n");
        sbLogs.append(createFileByName(fullDirName + "src"+ File.separator +"main"+ File.separator + "Main.java")).append("\n");
        sbLogs.append(createFileByName(fullDirName + "src"+ File.separator +"main"+ File.separator + "Utils.java")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "res"+ File.separator +"drawables")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "res"+ File.separator +"vectors")).append("\n");
        sbLogs.append(initCatalog(fullDirName + "res"+ File.separator +"icons")).append("\n");

        writeFile(fullDirName + "temp"+ File.separator + "temp.txt",sbLogs);
    }

    public static String initCatalog(String dirName) {
        File f = new File(dirName);
        if (f.mkdir()) {
            return "Directory " + dirName + " is created";
        } else {
            return "Directory " + dirName + " is not created";
        }
    }

    public static String createFileByName(String fileName) {

        File myFile = new File(fileName);// создадим новый файл
        try {
            if (myFile.createNewFile())
                return "Файл " + fileName + " был создан";
        } catch (IOException ex) {
            return "Файл " + fileName + " был создан по ошибке - " + ex.getMessage();
        }
        return "Файл " + fileName + "не был создан";

    }

    public static void writeFile(String fileName, StringBuffer sb) {

        try (FileWriter writer = new FileWriter(fileName, false)) {
            // запись всей строки
            writer.write(sb.toString());
            // запись по символам
            writer.append('\n');
            writer.append('!');
            // дозаписываем и очищаем буфер
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

}



